package com.bosch.peoplecounter.data;

/**
 * Provide emtpy implementation for the {@link StorageChangeListener<Person>}.
 *
 * @author letientai299@gmail.com
 */

public abstract class PersonStorageChangeListenerAdapter
    implements StorageChangeListener<Person> {
  @Override public void onAdd(final Person item) {

  }

  @Override public void onDelete(final Person item) {

  }

  @Override public void onClearAll() {

  }

  @Override public void onUpdate(final Person item) {

  }
}
