package de.gregoryseibert.vorlesungsplandhbw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.AppDatabase;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventViewModel extends ViewModel {
    private MutableLiveData<List<Event>> events;
    private AppDatabase appDatabase;
    private EventRepository eventRepository;

    private String baseURL;

    public EventViewModel() {

    }

    public void init(Context context, String baseURL, SimpleDate date) {
        this.baseURL = baseURL;

        if(appDatabase == null) {
            appDatabase = AppDatabase.getAppDatabase(context);
        }

        this.eventRepository = new EventRepository(appDatabase.eventDao());

        if(events == null) {
            this.events = new MutableLiveData<>();
        }

        ExecutorService service =  Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                events.postValue(eventRepository.getEvents(baseURL, date));
            }
        });
    }

    public LiveData<List<Event>> getEvents() {
        return this.events;
    }
}
