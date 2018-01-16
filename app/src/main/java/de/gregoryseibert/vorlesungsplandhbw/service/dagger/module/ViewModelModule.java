package de.gregoryseibert.vorlesungsplandhbw.service.dagger.module;

import android.arch.lifecycle.ViewModelProviders;

import dagger.Module;
import dagger.Provides;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.scope.AppComponentScope;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListFragment;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@Module
public class ViewModelModule {
        private final EventListFragment EVENTLISTFRAGMENT;

        public ViewModelModule(EventListFragment eventListFragment) {
            this.EVENTLISTFRAGMENT = eventListFragment;
        }

        @Provides
        @AppComponentScope
        public EventViewModel eventViewModel(EventRepository eventRepository) {
            EventViewModel eventViewModel = ViewModelProviders.of(EVENTLISTFRAGMENT).get(EventViewModel.class);
            eventViewModel.setEventRepository(eventRepository);
            return eventViewModel;
        }
}
