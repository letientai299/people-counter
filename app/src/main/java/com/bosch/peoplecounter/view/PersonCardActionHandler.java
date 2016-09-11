package com.bosch.peoplecounter.view;

import com.bosch.peoplecounter.data.Person;

/**
 * @author letientai299@gmail.com
 */
interface PersonCardActionHandler {
  void call(String number);

  void sms(String number);

  void openContextMenu(Person p);
}
