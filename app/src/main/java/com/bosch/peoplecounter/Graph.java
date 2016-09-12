package com.bosch.peoplecounter;

import com.bosch.peoplecounter.data.PersonStorageModule;
import com.bosch.peoplecounter.view.ListingFragment;
import dagger.Component;
import javax.inject.Singleton;

/**
 * @author letientai299@gmail.com
 */

@Singleton @Component(modules = { PersonStorageModule.class })
public interface Graph {
  void inject(ListingFragment listingFragment);

  void inject(MainActivity mainActivity);

  final class Initializer {
    static Graph init() {
      return DaggerGraph.builder().build();
    }
  }
}
