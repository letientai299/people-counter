package com.bosch.peoplecounter;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * @author letientai299@gmail.com
 */

public abstract class ThemedActivity extends Activity {
  @Override public void onCreate(final Bundle savedInstanceState,
      final PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
  }
}
