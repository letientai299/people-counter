package com.bosch.peoplecounter;

import android.app.Application;
import com.bosch.peoplecounter.data.DaoMaster;
import com.bosch.peoplecounter.data.DaoSession;
import org.greenrobot.greendao.database.Database;
import timber.log.Timber;

/**
 * @author letientai299@gmail.com
 */
public class PeopleCounterApp extends Application {
  private static PeopleCounterApp instance;
  private Graph graph;
  private DaoSession daoSession;

  @Override public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
    instance = this;
    graph = Graph.Initializer.init();
    DaoMaster.DevOpenHelper helper =
        new DaoMaster.DevOpenHelper(this, "notes-db");
    Database db = helper.getWritableDb();
    daoSession = new DaoMaster(db).newSession();
  }

  public static PeopleCounterApp getInstance() {
    return instance;
  }

  public Graph getGraph() {
    return graph;
  }

  public DaoSession getDaoSession() {
    return this.daoSession;
  }
}
