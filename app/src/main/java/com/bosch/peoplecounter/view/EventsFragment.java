package com.bosch.peoplecounter.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bosch.peoplecounter.R;

/**
 * @author letientai299@gmail.com
 */

public class EventsFragment extends Fragment {
  @Nullable @Override public View onCreateView(final LayoutInflater inflater,
      @Nullable final ViewGroup container,
      @Nullable final Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_events, container, false);
    return view;
  }
}
