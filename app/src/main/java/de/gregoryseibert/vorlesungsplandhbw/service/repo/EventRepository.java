package de.gregoryseibert.vorlesungsplandhbw.service.repo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.model.Event.EventType;
import de.gregoryseibert.vorlesungsplandhbw.model.EventHolder;
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
    private final EventDAO EVENTDAO;
    private final OkHttpClient OKHTTPCLIENT;

    private ArrayList<EventHolder> eventHolders;

    public EventRepository(EventDAO eventDAO, OkHttpClient okHttpClient) {
        this.EVENTDAO = eventDAO;
        this.OKHTTPCLIENT = okHttpClient;

        eventHolders = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            eventHolders.add(new EventHolder());
        }
    }

    public Week getAllEventsOfWeek(String url, SimpleDate date) {
        SimpleDate rangeStart = date.getFirstDayOfWeek();
        SimpleDate rangeEnd = date.getLastDayOfWeek();
        rangeEnd.getCalendar().add(Calendar.HOUR_OF_DAY, 23);

        Timber.i("start: " + rangeStart.getFormatDateTime());
        Timber.i("end: " + rangeEnd.getFormatDateTime());

        //TODO
        boolean shouldRefresh = true;

        if(shouldRefresh) {
            refreshEvents(url, rangeStart);
        }

        List<Event> events = EVENTDAO.getAllByRange(rangeStart.getMillis(), rangeEnd.getMillis());

        Week week = new Week(rangeStart);
        week.insertEvents(events);

        return week;
    }

    private void refreshEvents(String url, SimpleDate date) {
        String fullURL = generateURL(url, date.getDay(), date.getMonth(), date.getYear());

//        Timber.i(fullURL);

        Document doc = loadDocument(fullURL);

        if(doc != null) {
            EVENTDAO.deleteAllByRange(date.getMillis(), date.getLastDayOfWeek().getMillis());

            List<Event> events = parseDocumentForEvents(doc, date);

            EVENTDAO.insertAll(events);
        }
    }


    private String generateURL(String url, int day, int month, int year) {
        if(day != 0 && month != 0 && year != 0) {
            url += "&day=" + day;
            url += "&month=" + (month + 1);
            url += "&year=" + year;
        }

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
                throw new IOException("response null!");
            }
        } catch(IOException e) {
            Timber.e(e.getMessage());
        }

        return null;
    }

    private List<Event> parseDocumentForEvents(Document doc, SimpleDate date) {
        List<Event> eventList = new ArrayList<>();

        Elements tableRows = doc.select("#calendar .week_table tbody tr");

        Element weekHeader = tableRows.first().getElementsByClass("week_header").first();
        String firstDateStr = weekHeader.getElementsByTag("nobr").first().text().split(" ")[1];
        int firstDay = Integer.parseInt(firstDateStr.substring(0, firstDateStr.length()-1).split("\\.")[0]);

        SimpleDate firstDate = new SimpleDate(date);
        firstDate.setDay(firstDay);

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

                    SimpleDate lStartDate = new SimpleDate(firstDay, date.getMonth(), date.getYear(), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
                    lStartDate.addDays(indexOfBlock);

                    SimpleDate lEndDate = new SimpleDate(firstDay, date.getMonth(), date.getYear(), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));
                    lEndDate.addDays(indexOfBlock);

                    StringBuilder titleBuilder = new StringBuilder();
                    for(int i = 2; i < linkSplit.length; i++) {
                        titleBuilder.append(linkSplit[i]);
                        titleBuilder.append(" ");
                    }
                    if (titleBuilder.length() > 0) {
                        titleBuilder.deleteCharAt(titleBuilder.length()-1);
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
                        event = new Event(new SimpleDate(), lStartDate, lEndDate, lTitle, lRoom, "", EventType.LECTURE);
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
