package de.gregoryseibert.vorlesungsplandhbw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;

import de.gregoryseibert.vorlesungsplandhbw.model.EventHolder;
import de.gregoryseibert.vorlesungsplandhbw.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventViewModel extends ViewModel {
    private EventRepository eventRepository;
    private ExecutorService executorService;

    private MutableLiveData<List<EventHolder>> events;

    public EventViewModel() {

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

        executorService.execute(() -> events.postValue(eventRepository.getAllEventsOfWeek(url, date)));
    }

    public LiveData<List<EventHolder>> getEvents() {
        return this.events;
    }
}
