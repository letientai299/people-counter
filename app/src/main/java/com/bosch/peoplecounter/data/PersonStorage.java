package com.bosch.peoplecounter.data;

import io.reactivex.Observable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author letientai299@gmail.com
 */

public class PersonStorage {
  public Observable<Person> getPeople() {
    final List<Person> people = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      people.add(createPerson("Person #" + i, randomPhoneNumber()));
    }
    return Observable.fromArray(people.toArray(new Person[people.size()]));
  }

  private Person createPerson(final String name, final String number) {
    return new Person(name, number);
  }

  private SecureRandom random = new SecureRandom();

  private String randomPhoneNumber() {
    return new BigInteger(40, random).toString(10);
  }
}
