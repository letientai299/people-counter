package com.bosch.peoplecounter.view;

import com.bosch.peoplecounter.data.Person;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author letientai299@gmail.com
 */
class SortedList extends ArrayList<Person> {
  private final Comparator<Person> comparator;

  SortedList(Comparator<Person> comp) {
    this.comparator = comp;
  }

  public Comparator<Person> getComparator() {
    return comparator;
  }

  @Override public boolean add(final Person o) {
    return binaryInsert(o) != -1;
  }

  int binaryInsert(Person p) {
    int index = Collections.binarySearch(this, p, comparator);
    if (index < 0) index = ~index;
    super.add(index, p);
    return index;
  }
}
