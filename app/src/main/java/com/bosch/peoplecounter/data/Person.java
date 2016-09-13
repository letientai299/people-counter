package com.bosch.peoplecounter.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * POJO object for storing person's information.
 *
 * @author letientai299@gmail.com
 */

@Entity(
    indexes = {
        @Index(value = "id DESC", unique = true)
    }) public class Person {

  @Id(autoincrement = true) private Long id;

  public void setName(final String name) {
    this.name = name;
  }

  public void setPhoneNumber(final String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(final boolean checked) {
    this.checked = checked;
  }

  private boolean checked = false;

  @NotNull private String name;

  private String phoneNumber;

  @Generated(hash = 1748790763)
  public Person(Long id, boolean checked, @NotNull String name,
      String phoneNumber) {
    this.id = id;
    this.checked = checked;
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

  @Override public String toString() {
    return "Person{" +
        "id=" + id +
        ", checked=" + checked +
        ", name='" + name + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        '}';
  }

  public boolean getChecked() {
    return this.checked;
  }
}
