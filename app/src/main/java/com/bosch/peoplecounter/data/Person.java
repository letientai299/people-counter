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
  private boolean checked = false;
  @NotNull private String name = "";
  @NotNull private String phoneNumber = "";
  @NotNull private String group = "";
  @NotNull private boolean isMale = false;
  @NotNull private String room = "";
  @NotNull private String hotel = "";

  @Generated(hash = 449609675)
  public Person(Long id, boolean checked, @NotNull String name,
      @NotNull String phoneNumber, @NotNull String group, boolean isMale,
      @NotNull String room, @NotNull String hotel) {
    this.id = id;
    this.checked = checked;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.group = group;
    this.isMale = isMale;
    this.room = room;
    this.hotel = hotel;
  }

  @Generated(hash = 1024547259) public Person() {
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(final String group) {
    this.group = group;
  }

  public boolean isMale() {
    return isMale;
  }

  public void setMale(final boolean male) {
    isMale = male;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(final String room) {
    this.room = room;
  }

  public String getHotel() {
    return hotel;
  }

  public void setHotel(final String hotel) {
    this.hotel = hotel;
  }

  public boolean isChecked() {
    return checked;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(final String phoneNumber) {
    this.phoneNumber = phoneNumber;
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

  public void setChecked(final boolean checked) {
    this.checked = checked;
  }

  public boolean getIsMale() {
    return this.isMale;
  }

  public void setIsMale(boolean isMale) {
    this.isMale = isMale;
  }
}
