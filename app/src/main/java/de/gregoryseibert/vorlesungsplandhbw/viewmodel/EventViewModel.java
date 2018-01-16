package de.gregoryseibert.vorlesungsplandhbw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventViewModel extends ViewModel {
    private EventRepository eventRepository;

    private MutableLiveData<List<Event>> events;

    public EventViewModel() {

    }

    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void init(String url, SimpleDate date) {
        if(events == null) {
            this.events = new MutableLiveData<>();
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> events.postValue(eventRepository.getEvents(url, date)));
    }

    public LiveData<List<Event>> getEvents() {
        return this.events;
    }
}
