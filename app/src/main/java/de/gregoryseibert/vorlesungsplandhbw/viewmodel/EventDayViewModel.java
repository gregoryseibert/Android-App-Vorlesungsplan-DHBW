package de.gregoryseibert.vorlesungsplandhbw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventDayViewModel extends ViewModel {
    private EventRepository eventRepository;
    private ExecutorService executorService;

    private MutableLiveData<List<Event>> events;

    public EventDayViewModel() {

    }

    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void init(String url, SimpleDate date) {
        if(events == null) {
            this.events = new MutableLiveData<>();
        }

        executorService.execute(() -> events.postValue(eventRepository.getEvents(url, date)));
    }

    public LiveData<List<Event>> getEvents() {
        return this.events;
    }
}
