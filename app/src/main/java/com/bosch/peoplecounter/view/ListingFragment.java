package com.bosch.peoplecounter.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bosch.peoplecounter.MainActivity;
import com.bosch.peoplecounter.PeopleCounterApp;
import com.bosch.peoplecounter.R;
import com.bosch.peoplecounter.Utils;
import com.bosch.peoplecounter.data.Person;
import com.bosch.peoplecounter.data.PersonStorage;
import com.mancj.materialsearchbar.MaterialSearchBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static butterknife.ButterKnife.findById;
import static com.bosch.peoplecounter.R.id.personName;

/**
 * @author letientai299@gmail.com
 */
public class ListingFragment extends Fragment
    implements PersonCardActionHandler,
    PersonStorage.StorageChangeListener<Person>,
    MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener,
    MaterialSearchBarSearchOnTyping.SearchQueryListener {

  private static final String KEY_SORT_NAME =
      ListingFragment.class.getSimpleName() + "/sortByName";
  private static final String KEY_SORT_STATUS =
      ListingFragment.class.getSimpleName() + "/sortByStatus";

  private Unbinder unbinder;

  @BindView(R.id.people_list) RecyclerView peopleList;
  @BindView(R.id.searchBar) MaterialSearchBarSearchOnTyping searchBar;

  @Inject PersonStorage storage;

  /**
   * Make the people list auto sorted by person name.
   */
  private final List<Person> people = new ArrayList<Person>() {
    public boolean add(Person mt) {
      int index = Collections.binarySearch(this, mt, personComparator);
      if (index < 0) index = ~index;
      super.add(index, mt);
      return true;
    }
  };

  private boolean isCountingMode;
  private boolean isAscendingStatusOder;
  private boolean isAscendingNameOrder;
  private final Comparator<Person> personComparator = (o1, o2) -> {
    if (isCountingMode) {
      int compareStatus = Boolean.compare(o1.getChecked(), o2.getChecked());
      if (!isAscendingStatusOder) compareStatus = -compareStatus;
      if (compareStatus != 0) return compareStatus;
    }

    int compareName = o1.getName().compareTo(o2.getName());
    if (!isAscendingNameOrder) compareName = -compareName;
    return compareName;
  };

  private PersonRecyclerViewAdapter peopleListAdapter;

  public ListingFragment() {
    super();
    // Android require an empty param public constructor
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    PeopleCounterApp.getInstance().getGraph().inject(this);
    isCountingMode = getModeFromPref();
    View view = inflater.inflate(R.layout.fragment_listing, container, false);
    unbinder = ButterKnife.bind(this, view);

    peopleListAdapter = new PersonRecyclerViewAdapter(people, this);
    peopleListAdapter.setCountingMode(isCountingMode);
    peopleList.setLayoutManager(new LinearLayoutManager(getContext()));
    peopleList.setAdapter(peopleListAdapter);
    storage.addStorageChangeListener(this);
    updatePeopleList();

    configSearchBar();

    return view;
  }

  private void configSearchBar() {
    isAscendingStatusOder = getSharedPref().getBoolean(KEY_SORT_STATUS, true);
    isAscendingNameOrder = getSharedPref().getBoolean(KEY_SORT_NAME, true);

    searchBar.setOnSearchActionListener(this);
    searchBar.setQueryListener(this);

    searchBar.inflateMenu(R.menu.sorting);
    PopupMenu menu = searchBar.getMenu();
    menu.setOnMenuItemClickListener(this);
    updateSortNameMenuItemTitle(menu.getMenu().getItem(0));
    updateSortStatusMenuItemTitle(menu.getMenu().getItem(1));
    // Disable sort by status if not in counting mode
    if (!isCountingMode) {
      menu.getMenu().getItem(1).setEnabled(false);
    }
  }

  private void updatePeopleList() {
    people.clear();
    peopleListAdapter.notifyDataSetChanged();
    storage.getPeople()
        .flatMap(Observable::from)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::addCard);
  }

  private void addCard(final Person p) {
    people.add(p);
    peopleListAdapter.notifyDataSetChanged();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    getSharedPref().edit()
        .putBoolean(KEY_SORT_NAME, isAscendingNameOrder)
        .putBoolean(KEY_SORT_STATUS, isAscendingStatusOder)
        .apply();
  }

  @Override public void call(final String number) {

    final Intent intent = new Intent(Intent.ACTION_DIAL);
    intent.setData(Uri.fromParts("tel", number, null));
    getActivity().startActivity(intent);
  }

  @Override public void sms(final String number) {
    getActivity().startActivity(
        new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
  }

  @Override public void openContextMenu(final Person p) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final View view =
        View.inflate(getActivity(), R.layout.dialog_person_context_menu, null);
    changeThemeBasedOnMode(ButterKnife.findById(view, R.id.personNameTextView));
    TextView personName = findById(view, R.id.personNameTextView);
    personName.setText(p.getName());

    builder.setView(view);
    AlertDialog personContextDialog = builder.create();

    ButterKnife.findById(view, R.id.updatePersonInfoButton)
        .setOnClickListener(v -> {
          personContextDialog.dismiss();
          openPersonEditingDialog(p);
        });

    ButterKnife.findById(view, R.id.removePersonButton)
        .setOnClickListener(v -> {
          personContextDialog.dismiss();
          Utils.askForDoSomething(getActivity(),
              getString(R.string.ask_for_delete_person), () -> storage.delete(p)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe((aVoid) -> {
                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT)
                        .show();
                  }));
        });

    personContextDialog.show();
  }

  @Override public void toggleCheck(final Person p) {
    p.setChecked(!p.isChecked());
    storage.update(p).subscribe();
  }

  private void changeThemeBasedOnMode(final View view) {
    view.setBackgroundColor(ContextCompat.getColor(getActivity(),
        isCountingMode ? R.color.colorPrimaryCounting : R.color.colorPrimary));
  }

  private void openPersonEditingDialog(Person p) {
    final View view =
        View.inflate(getActivity(), R.layout.dialog_person_editing, null);
    TextView titleTextView = ButterKnife.findById(view, R.id.dialogTitle);
    changeThemeBasedOnMode(titleTextView);
    titleTextView.setText(R.string.dialog_edit_person_title);
    EditText nameEditText = ButterKnife.findById(view, personName);
    nameEditText.setText(p.getName());
    EditText phoneNumberEditText = ButterKnife.findById(view, R.id.phoneNumber);
    phoneNumberEditText.setText(p.getPhoneNumber());
    new AlertDialog.Builder(getActivity()).setCancelable(false)
        .setView(view)
        .setPositiveButton("Save", (dialog, which) -> {
          final String name =
              ((TextView) ButterKnife.findById(view, personName)).getText()
                  .toString();
          final String phone = ((TextView) ButterKnife.findById(view,
              R.id.phoneNumber)).getText().toString();
          p.setName(name);
          p.setPhoneNumber(phone);
          storage.update(p).subscribe(person -> {
            getActivity().runOnUiThread(
                () -> Toast.makeText(getActivity(), "Updated",
                    Toast.LENGTH_SHORT).show());
          });
        })
        .setNegativeButton("Cancel", null)
        .show();
  }

  @Override public void onAdd(final Person item) {
    getActivity().runOnUiThread(() -> addCard(item));
  }

  @Override public void onDelete(final Person item) {
    getActivity().runOnUiThread(() -> {
      people.remove(item);
      peopleListAdapter.notifyDataSetChanged();
    });
  }

  @Override public void onClearAll() {
    getActivity().runOnUiThread(this::updatePeopleList);
  }

  @Override public void onUpdate(final Person item) {
    getActivity().runOnUiThread(() -> {
      int updatedId = -1;
      for (int i = 0; i < people.size(); i++) {
        Person p = people.get(i);
        if (Objects.equals(p.getId(), item.getId())) {
          updatedId = i;
          break;
        }
      }
      if (updatedId != -1) {
        people.remove(updatedId);
        people.add(item);
        peopleListAdapter.notifyDataSetChanged();
      }
    });
  }

  public boolean getModeFromPref() {
    SharedPreferences sharedPref = getSharedPref();
    return sharedPref.getBoolean(MainActivity.PREF_THEME, false);
  }

  private SharedPreferences getSharedPref() {
    return PreferenceManager.getDefaultSharedPreferences(
        PeopleCounterApp.getInstance());
  }

  @Override public void onSearchStateChanged(final boolean state) {
    // ignore
  }

  @Override public void onSearchConfirmed(final CharSequence query) {
    filterPeopleList(query.toString());
  }

  @Override public void onButtonClicked(final int i) {
    // ignore
  }

  @Override public boolean onMenuItemClick(final MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sort_by_name:
        isAscendingNameOrder = !isAscendingNameOrder;
        updateSortNameMenuItemTitle(item);
        updatePeopleListOrder();
        break;
      case R.id.sort_by_status:
        isAscendingStatusOder = !isAscendingStatusOder;
        updateSortStatusMenuItemTitle(item);
        updatePeopleListOrder();
        break;
      default:
        // Should never get there
        break;
    }

    return true;
  }

  private void updatePeopleListOrder() {
    Collections.sort(people, personComparator);
    peopleListAdapter.notifyDataSetChanged();
  }

  private void updateSortStatusMenuItemTitle(final MenuItem item) {
    String title =
        isAscendingStatusOder ? getString(R.string.sort_by_status_ascending)
            : getString(R.string.sort_by_status_descending);
    item.setTitle(title);
  }

  private void updateSortNameMenuItemTitle(final MenuItem item) {
    String title =
        isAscendingNameOrder ? getString(R.string.sort_by_name_ascending)
            : getString(R.string.sort_by_name_descending);
    item.setTitle(title);
  }

  @Override public void onQueryChange(final String query) {
    filterPeopleList(query);
  }

  private void filterPeopleList(final String query) {
    Timber.d("Search Query updated: %s", query);
  }
}
