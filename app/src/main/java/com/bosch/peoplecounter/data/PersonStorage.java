package com.bosch.peoplecounter.data;

import com.bosch.peoplecounter.PeopleCounterApp;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;
import rx.Observable;
import timber.log.Timber;

/**
 * @author letientai299@gmail.com
 */

public class PersonStorage {
  private final RxDao<Person, Long> peopleDao;
  private final RxQuery<Person> peopleQuery;
  private List<StorageChangeListener<Person>> listeners = new ArrayList<>();

  public PersonStorage() {
    Timber.d("Init PersonStorage");
    DaoSession daoSession = PeopleCounterApp.getInstance().getDaoSession();
    PersonDao dao = daoSession.getPersonDao();
    peopleDao = dao.rx();
    peopleQuery = dao.queryBuilder().orderAsc(PersonDao.Properties.Name).rx();
    System.out.println("In db: " + peopleDao.count().toBlocking().last());
  }

  public void addStorageChangeListener(StorageChangeListener<Person> listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  public void clearStorageChangeListender(){
    this.listeners.clear();
  }

  public Observable<List<Person>> getPeople() {
    return peopleQuery.list();
  }

  private Person createPerson(final String name, final String number) {
    return new Person(null, false, name, number);
  }

  private SecureRandom random = new SecureRandom();

  private String randomPhoneNumber() {
    return new BigInteger(40, random).toString(10);
  }

  public Observable<Person> add(Person p) {
    Timber.d("Try to insert person: %s", p.toString());
    return peopleDao.insert(p).doOnCompleted(() -> {
      for (final StorageChangeListener<Person> listener : listeners) {
        listener.onAdd(p);
      }
    });
  }

  public Observable<Void> delete(Person p) {
    return peopleDao.delete(p).doOnCompleted(() -> {
      for (final StorageChangeListener<Person> listener : listeners) {
        listener.onDelete(p);
      }
    });
  }

  /**
   * Generate a number of fake people data.
   */
  public void gen(final int number) {
    List<Person> people = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      Person person = createPerson("Person #" + i, randomPhoneNumber());
      people.add(person);
    }

    peopleDao.insertInTx(people).flatMap(Observable::from).subscribe((p) -> {
      for (final StorageChangeListener<Person> listener : listeners) {
        listener.onAdd(p);
      }
    });
  }

  public Observable<Person> update(Person p) {
    return peopleDao.update(p).doOnCompleted(() -> {
      for (final StorageChangeListener<Person> listener : listeners) {
        listener.onUpdate(p);
      }
    });
  }

  public Observable<Void> clear() {
    return peopleDao.deleteAll().doOnCompleted(() -> {
      for (final StorageChangeListener<Person> listener : listeners) {
        listener.onClearAll();
      }
    });
  }

  public interface StorageChangeListener<T> {
    void onAdd(T item);

    void onDelete(T item);

    void onClearAll();

    void onUpdate(T item);
  }
}

