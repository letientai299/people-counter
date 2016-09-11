package com.bosch.peoplecounter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bosch.peoplecounter.view.ListingFragment;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener {

  @BindView(R.id.tabs) TabLayout tabs;
  @BindView(R.id.left_drawer) ListView drawerList;
  @BindView(R.id.drawer_layout) DrawerLayout drawer;

  private Unbinder unbinder;
  final List<String> tabTitles = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    unbinder = ButterKnife.bind(this);
    ViewPager pager = ButterKnife.findById(this, R.id.pager);
    setupViewPager(pager);
    setupDrawer(drawerList);
    Toast.makeText(this, "Hello world", Toast.LENGTH_SHORT).show();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  private TabPagerAdapter tabLayoutAdapter;

  public void setupViewPager(final ViewPager pager) {
    tabTitles.add(getString(R.string.tab_title_listing));
    tabTitles.add(getString(R.string.tab_title_events));
    final List<Fragment> fragments = new ArrayList<>();
    fragments.add(initFragment());
    fragments.add(initFragment());
    tabLayoutAdapter =
        new TabPagerAdapter(getSupportFragmentManager(), fragments, tabTitles);
    pager.setAdapter(tabLayoutAdapter);
    tabs.setupWithViewPager(pager);
  }

  private Fragment initFragment() {
    return new ListingFragment();
  }

  private List<String> drawerActions;

  private BaseAdapter drawerAdapter;

  public void setupDrawer(final ListView drawer) {
    drawerActions = new ArrayList<>();
    drawerActions.add(getString(R.string.drawer_tittle_start_counting));
    drawerActions.add(getString(R.string.drawer_tittle_add_new_person));
    drawerActions.add(getString(R.string.drawer_tittle_import));
    drawerActions.add(getString(R.string.drawer_tittle_reset_database));
    drawerActions.add(getString(R.string.drawer_tittle_gen_fake_data));
    drawerAdapter =
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
            drawerActions);
    drawer.setAdapter(drawerAdapter);
    drawer.setOnItemClickListener(this);
  }

  /**
   * On drawer item clicked.
   */
  @Override public void onItemClick(final AdapterView<?> adapterView,
      final View view, final int i, final long l) {
    switch (i) {
      case 0: // counting
        toggleCountingMode();
        break;
      case 1:
        break;
      case 2:
        break;
      case 3:
        break;
      default:
        // should never happen.
        break;
    }
    drawer.closeDrawers();
  }

  private boolean isCountingMode = false;

  private void toggleCountingMode() {
    if (isCountingMode) {
      tabTitles.set(0, getString(R.string.tab_title_listing));
      drawerActions.set(0, getString(R.string.drawer_tittle_start_counting));
    } else {
      tabTitles.set(0, getString(R.string.tab_title_counting));
      drawerActions.set(0, getString(R.string.drawer_tittle_stop_counting));
    }

    isCountingMode = !isCountingMode;
    tabLayoutAdapter.notifyDataSetChanged();
    drawerAdapter.notifyDataSetChanged();
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
