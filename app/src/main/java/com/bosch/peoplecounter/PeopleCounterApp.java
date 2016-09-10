package com.bosch.peoplecounter;

import android.app.Application;

/**
 * @author letientai299@gmail.com
 */
public class PeopleCounterApp extends Application {
  private static PeopleCounterApp instance;
  private Graph graph;

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
    graph = Graph.Initializer.init();
  }

  public static PeopleCounterApp getInstance() {
    return instance;
  }

  public Graph getGraph() {
    return graph;
  }
}
