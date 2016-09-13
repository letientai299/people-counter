package com.bosch.peoplecounter.data;

/**
 * @author letientai299@gmail.com
 */
public interface StorageChangeListener<T> {
  void onAdd(T item);

  void onDelete(T item);

  void onClearAll();

  void onUpdate(T item);
}
