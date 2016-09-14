package com.bosch.peoplecounter.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bosch.peoplecounter.R;
import com.bosch.peoplecounter.data.Person;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.View.GONE;

/**
 * @author letientai299@gmail.com
 */

class PersonRecyclerViewAdapter
    extends RecyclerView.Adapter<PersonRecyclerViewAdapter.PersonViewHolder> {
  private final SortedList people;
  private final SortedList filteringPeople;
  private final PersonCardActionHandler actionHandler;
  private boolean isCountingMode = false;
  private boolean filterEnable = false;
  private String filterQuery = "";

  void setCountingMode(final boolean countingMode) {
    isCountingMode = countingMode;
  }

  void setFilterEnable(final boolean filterMode) {
    this.filterEnable = filterMode;
    if (filterEnable) {
      updateFilteringPeople();
    }
  }

  private void updateFilteringPeople() {
    filteringPeople.clear();
    for (int i = 0; i < people.size(); i++) {
      Person person = people.get(i);
      if (personMatchQuery(person)) {
        filteringPeople.add(people.get(i));
      }
    }
    notifyDataSetChanged();
  }

  void setFilterQuery(String query) {
    this.filterQuery = query.toLowerCase();
    updateFilteringPeople();
  }

  PersonRecyclerViewAdapter(Comparator<Person> comparator,
      PersonCardActionHandler handler) {
    this.people = new SortedList(comparator);
    this.filteringPeople = new SortedList(comparator);
    this.actionHandler = handler;
  }

  @Override public PersonViewHolder onCreateViewHolder(final ViewGroup parent,
      final int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.person_card_layout, parent, false);
    return new PersonViewHolder(view);
  }

  @Override public void onBindViewHolder(final PersonViewHolder holder,
      final int position) {
    Person person = getPeopleList().get(position);
    String displayName = person.getName();
    if (!person.getGroup().isEmpty()) {
      displayName += " (" + person.getGroup() + ")";
    }
    holder.nameTextView.setText(displayName);

    if (!person.getPhoneNumber().isEmpty()) {
      holder.phoneNumberTextView.setText(person.getPhoneNumber());
    } else {
      holder.phoneNumberTextView.setText("Missing");
    }

    String roomHotel = "";
    if (!person.getRoom().isEmpty()) {
      roomHotel += person.getRoom();
    } else {
      roomHotel += "???";
    }
    if (!person.getHotel().isEmpty()) {
      roomHotel += " - " + person.getHotel();
    } else {
      roomHotel += " - ???";
    }

    holder.roomHotelTextView.setText(roomHotel);

    holder.phoneButton.setOnClickListener(
        view -> actionHandler.call(person.getPhoneNumber()));
    holder.messageButton.setOnClickListener(
        view -> actionHandler.sms(person.getPhoneNumber()));

    holder.view.setOnLongClickListener(e -> {
      actionHandler.openContextMenu(person);
      return true;
    });

    // Only enable toggle action in counting mode
    if (isCountingMode) {
      int visibility = person.isChecked() ? View.VISIBLE : View.GONE;
      holder.checkedIcon.setVisibility(visibility);
    } else {
      holder.checkedIcon.setVisibility(GONE);
    }

    holder.view.setOnClickListener((v) -> {
      if (isCountingMode) {
        actionHandler.toggleCheck(person);
      }
    });
  }

  private boolean personMatchQuery(final Person person) {
    String s = person.getName() +
        " " +
        person.getPhoneNumber() +
        " " +
        person.getGroup() +
        " " +
        person.getHotel() +
        " " +
        person.getRoom();
    String[] splits = filterQuery.split(" ");

    boolean allMatch = true;
    for (String part : splits) {
      if (!s.toLowerCase().contains(part)) {
        allMatch = false;
        break;
      }
    }
    return allMatch;
  }

  @Override public int getItemCount() {
    return getPeopleList().size();
  }

  private List<Person> getPeopleList() {
    if (filterEnable) return filteringPeople;
    return people;
  }

  void clear() {
    this.people.clear();
    notifyDataSetChanged();
    updateFilteringPeople();
  }

  public void add(final Person p) {
    int index = people.binaryInsert(p);
    notifyItemInserted(index);
    updateFilteringPeople();
  }

  void delete(final Person p) {
    int index = people.indexOf(p);
    if (index != -1) {
      people.remove(index);
      notifyItemRemoved(index);
    }
    updateFilteringPeople();
  }

  void update(final Person item) {
    int updatedId = -1;
    for (int i = 0; i < people.size(); i++) {
      Person p = people.get(i);
      if (p.getId().equals(item.getId())) {
        updatedId = i;
        break;
      }
    }
    if (updatedId != -1) {
      people.remove(updatedId);
      notifyItemRemoved(updatedId);
      add(item);
    }
    updateFilteringPeople();
  }

  static class PersonViewHolder extends RecyclerView.ViewHolder {
    private final View view;
    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.phoneNumberTextView) TextView phoneNumberTextView;
    @BindView(R.id.phoneButton) ImageButton phoneButton;
    @BindView(R.id.messageButton) ImageButton messageButton;
    @BindView(R.id.personCheckedIcon) ImageView checkedIcon;
    @BindView(R.id.roomHotelTextView) TextView roomHotelTextView;

    PersonViewHolder(final View itemView) {
      super(itemView);
      this.view = itemView;
      ButterKnife.bind(this, itemView);
    }
  }

  void reorder(Comparator<Person> comparator) {
    Collections.sort(people, comparator);
    updateFilteringPeople();
  }
}
