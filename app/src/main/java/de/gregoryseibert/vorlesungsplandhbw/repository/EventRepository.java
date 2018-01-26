package de.gregoryseibert.vorlesungsplandhbw.repository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.database.EventDAO;
import de.gregoryseibert.vorlesungsplandhbw.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.model.Event.EventType;
import de.gregoryseibert.vorlesungsplandhbw.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.model.Week;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventRepository {
    private final int REFRESH_TRESHOLD;
    private final EventDAO EVENTDAO;
    private final OkHttpClient OKHTTPCLIENT;

    public EventRepository(EventDAO eventDAO, OkHttpClient okHttpClient, int refreshTreshold) {
        this.EVENTDAO = eventDAO;
        this.OKHTTPCLIENT = okHttpClient;
        this.REFRESH_TRESHOLD = refreshTreshold;
    }

    public List<Event> getAllEvents() {
        return EVENTDAO.getAll();
    }

    public Week getAllEventsOfWeek(String url, SimpleDate date) {
        SimpleDate rangeStart = date.getFirstDayOfWeek();
        SimpleDate rangeEnd = date.getLastDayOfWeek();
        rangeEnd.getCalendar().add(Calendar.HOUR_OF_DAY, 23);
        List<Event> events = EVENTDAO.getAllByRange(rangeStart.getMillis(), rangeEnd.getMillis());

        if(hasToRefresh(events)) {
            events.clear();
            events = refreshEvents(url, rangeStart);
        }

        Week week = new Week(rangeStart);

        if(events != null) {
            week.insertEvents(events);
        }

        return week;
    }

    public boolean hasToRefresh(List<Event> events) {
        SimpleDate dateTreshold = new SimpleDate();
        dateTreshold.addHours(-REFRESH_TRESHOLD);

        // TODO: check connectivity
        if(events.size() == 0) {
            return true;
        }

        for(Event event : events) {
            if(event.getStartDate().getMillis() > dateTreshold.getMillis()) {
                Timber.i("Event was too old [Loaded at: " + event.getLoadedAt().getFormatDateTime() + "]; [Treshold-Date: " + dateTreshold.getFormatDateTime() + "]; [Treshold: " + REFRESH_TRESHOLD + "]");
                return true;
            }
        }

        return false;
    }

    private List<Event> refreshEvents(String url, SimpleDate date) {
        String fullURL = generateURL(url, date.getDay(), date.getMonth(), date.getYear());

        Document doc = loadDocument(fullURL);

        if(doc != null) {
            EVENTDAO.deleteAllByRange(date.getMillis(), date.getLastDayOfWeek().getMillis());

            List<Event> events = parseDocumentForEvents(doc, date);

            EVENTDAO.insertAll(events);

            return events;
        }

        Timber.i("Document was null");

        return null;
    }

    public void emptyDatabase() {
        EVENTDAO.deleteAll();
    }

    private String generateURL(String url, int day, int month, int year) {
        url += "&day=" + day;
        url += "&month=" + (month + 1);
        url += "&year=" + year;

        Timber.i("URL: %s", url);

        return url;
    }

    private Document loadDocument(String url) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = OKHTTPCLIENT.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if(responseBody != null) {
                return Jsoup.parse(responseBody.string());
            } else {
                throw new IOException("Response null!");
            }
        } catch(IOException e) {
            Timber.e(e.getMessage());
        }

        return null;
    }

    private List<Event> parseDocumentForEvents(Document doc, SimpleDate firstDate) {
        List<Event> eventList = new ArrayList<>();

        Elements tableRows = doc.select("#calendar .week_table tbody tr");

        for(Element tableRow : tableRows) {
            Elements tableRowElements = tableRow.select(":root > td");

            Elements tableBlocks = tableRow.getElementsByClass("week_block");

            if(!tableBlocks.isEmpty()) {
                for(Element tableBlock : tableBlocks) {
                    int indexOfBlock = tableRowElements.indexOf(tableBlock) / 3;

                    Element link = tableBlock.getElementsByTag("a").first();
                    String linkText = link.text().replace("-", "");
                    String linkContent = linkText.substring(0, linkText.indexOf("erstellt"));

                    String[] linkSplit = linkContent.split(" ");
                    String[] startTime = linkSplit[0].split(":");
                    String[] endTime = linkSplit[1].split(":");

                    SimpleDate lStartDate = new SimpleDate(firstDate.getDay(), firstDate.getMonth(), firstDate.getYear(), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
                    lStartDate.addDays(indexOfBlock);

                    SimpleDate lEndDate = new SimpleDate(firstDate.getDay(), firstDate.getMonth(), firstDate.getYear(), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));
                    lEndDate.addDays(indexOfBlock);

                    StringBuilder titleBuilder = new StringBuilder();

                    for(int i = 2; i < linkSplit.length; i++) {
                        titleBuilder.append(linkSplit[i]);
                        titleBuilder.append(" ");
                    }

                    if (titleBuilder.length() > 0) {
                        titleBuilder.deleteCharAt(titleBuilder.length() - 1);
                    }

                    String lTitle = titleBuilder.toString();

                    Elements person = tableBlock.getElementsByClass("person");

                    String lLecturer = "";

                    if(person.size() > 0) {
                        lLecturer = person.first().text();

                        if(lLecturer.endsWith(",")) {
                            lLecturer = lLecturer.substring(0, lLecturer.length() - 1);
                        }
                    }

                    Elements resources = tableBlock.getElementsByClass("resource");

                    String lRoom = "";

                    for(int i = 0; i < resources.size(); i++) {
                        if(resources.get(i).text().contains(".")) {
                            lRoom = resources.get(i).text();
                        }
                    }

                    Event event;

                    if(linkContent.contains("Klausur")) {
                        event = new Event(new SimpleDate(), lStartDate, lEndDate, lTitle, lRoom, "", EventType.EXAM);
                    } else {
                        event = new Event(new SimpleDate(), lStartDate, lEndDate, lTitle, lRoom, lLecturer, EventType.LECTURE);
                    }

                    eventList.add(event);
                }
            }
        }

        return eventList;
    }
}
