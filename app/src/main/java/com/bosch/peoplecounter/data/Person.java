package com.bosch.peoplecounter.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * POJO object for storing person's information.
 *
 * @author letientai299@gmail.com
 */

@Entity public class Person {
  @Id private Long id;

  public void setName(final String name) {
    this.name = name;
  }

  public void setPhoneNumber(final String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @NotNull private String name;

  private String phoneNumber;

  @Generated(hash = 426705471)
  public Person(Long id, @NotNull String name, String phoneNumber) {
    this.id = id;
    this.name = name;
    this.phoneNumber = phoneNumber;
  }

  @Generated(hash = 1024547259) public Person() {
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }
}
