package com.bosch.peoplecounter.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bosch.peoplecounter.R;

/**
 * @author letientai299@gmail.com
 */
public class ListingFragment extends Fragment {
  public static final java.lang.String KEY_TITTLE = "fragmentTitle";
  private Unbinder unbinder;

  public ListingFragment() {
    super();
    // Android require an empty public constructor
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_listing, container, false);
    unbinder = ButterKnife.bind(this, view);
    TextView title = ButterKnife.findById(view, R.id.sampleTitle);
    title.setText(getArguments().getString(KEY_TITTLE));
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
