package com.bosch.peoplecounter.data;

/**
 * POJO object for storing person's information.
 *
 * @author letientai299@gmail.com
 */

public class Person {
  private final String name;
  private final String phoneNumber;

  public Person(final String name, final String phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
