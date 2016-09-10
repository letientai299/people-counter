package com.bosch.peoplecounter.data;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * @author letientai299@gmail.com
 */
@Module public class PersonStorageModule {
  @Provides @Singleton PersonStorage provideStorage() {
    return new PersonStorage();
  }
}
