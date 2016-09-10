package com.bosch.peoplecounter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bosch.peoplecounter.view.ListingFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.tabs) TabLayout tabs;

  private Unbinder unbinder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    unbinder = ButterKnife.bind(this);
    ViewPager pager = ButterKnife.findById(this, R.id.pager);
    setupViewPager(pager);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int id = item.getItemId();
    return id == R.id.action_settings || super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  public void setupViewPager(final ViewPager pager) {
    final List<Fragment> fragments = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      fragments.add(initFragment());
    }
    TabPagerAdapter adapter =
        new TabPagerAdapter(getSupportFragmentManager(), fragments,
            Arrays.asList("Listing", "Counting", "Events"));

    pager.setAdapter(adapter);
    tabs.setupWithViewPager(pager);
  }

  private Fragment initFragment() {
    return new ListingFragment();
  }

  private static class TabPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments;
    private final List<String> titles;

    TabPagerAdapter(final FragmentManager fm, final List<Fragment> fragments,
        final List<String> titles) {
      super(fm);
      this.fragments = fragments;
      this.titles = titles;
    }

    @Override public int getCount() {
      return fragments.size();
    }

    @Override public Fragment getItem(final int position) {
      return fragments.get(position);
    }

    @Override public CharSequence getPageTitle(final int position) {
      if (titles.size() > position) return titles.get(position);
      return "Fragment #" + position;
    }
  }
}
