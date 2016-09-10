package com.bosch.peoplecounter.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bosch.peoplecounter.PeopleCounterApp;
import com.bosch.peoplecounter.R;
import com.bosch.peoplecounter.data.Person;
import com.bosch.peoplecounter.data.PersonStorage;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author letientai299@gmail.com
 */
public class ListingFragment extends Fragment {
  private Unbinder unbinder;

  @BindView(R.id.people_list) RecyclerView peopleList;

  @Inject PersonStorage storage;

  private final List<Person> people = new ArrayList<>();

  private PersonRecyclerViewAdapter adapter;

  public ListingFragment() {
    super();
    // Android require an empty param public constructor
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    PeopleCounterApp.getInstance().getGraph().inject(this);
    View view = inflater.inflate(R.layout.fragment_listing, container, false);
    unbinder = ButterKnife.bind(this, view);
    setupPeopleList();
    return view;
  }

  private void setupPeopleList() {
    adapter = new PersonRecyclerViewAdapter(people);
    peopleList.setLayoutManager(new LinearLayoutManager(getContext()));
    peopleList.setAdapter(adapter);

    storage.getPeople().subscribe(this::addCard);
  }

  private void addCard(final Person p) {
    people.add(p);
    adapter.notifyDataSetChanged();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
