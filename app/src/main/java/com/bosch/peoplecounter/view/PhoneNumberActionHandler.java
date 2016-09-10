package com.bosch.peoplecounter.view;

/**
 * @author letientai299@gmail.com
 */
interface PhoneNumberActionHandler {
  void call(String number);

  void sms(String number);
}
