package de.gregoryseibert.vorlesungsplandhbw.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.concurrent.ExecutorService;

import de.gregoryseibert.vorlesungsplandhbw.service.EventRepository;

/**
 * Created by Gregory Seibert on 24.01.2018.
 */

public class EventViewModelFactory implements ViewModelProvider.Factory {
    private EventRepository eventRepository;
    private ExecutorService executorService;

    public EventViewModelFactory(EventRepository eventRepository, ExecutorService executorService) {
        this.eventRepository = eventRepository;
        this.executorService = executorService;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventViewModel.class)) {
            return (T) new EventViewModel(eventRepository, executorService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}