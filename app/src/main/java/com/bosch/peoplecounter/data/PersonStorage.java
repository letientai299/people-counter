package com.bosch.peoplecounter.data;

import com.bosch.peoplecounter.PeopleCounterApp;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;
import rx.Observable;

/**
 * @author letientai299@gmail.com
 */

public class PersonStorage {
  private final RxDao<Person, Long> peopleDao;
  private final RxQuery<Person> peopleQuery;

  public PersonStorage() {
    DaoSession daoSession = PeopleCounterApp.getInstance().getDaoSession();
    PersonDao dao = daoSession.getPersonDao();
    peopleDao = dao.rx();
    peopleQuery = dao.queryBuilder().orderAsc(PersonDao.Properties.Name).rx();
  }

  public Observable<Person> getPeople() {
    final List<Person> people = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      people.add(createPerson("Person #" + i, randomPhoneNumber()));
    }
    return Observable.from(people.toArray(new Person[people.size()]));
  }

  private Person createPerson(final String name, final String number) {
    return new Person(1L, name, number);
  }

  private SecureRandom random = new SecureRandom();

  private String randomPhoneNumber() {
    return new BigInteger(40, random).toString(10);
  }
}
