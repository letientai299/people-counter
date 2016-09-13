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
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * @author letientai299@gmail.com
 */

class PersonRecyclerViewAdapter
    extends RecyclerView.Adapter<PersonRecyclerViewAdapter.PersonViewHolder> {
  private final List<Person> people;
  private final List<Person> filteringPeople = new ArrayList<>();
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
      if (personMatchQuery(people.get(i))) filteringPeople.add(people.get(i));
    }
  }

  void setFilterQuery(String query) {
    this.filterQuery = query.toLowerCase();
    updateFilteringPeople();
  }

  PersonRecyclerViewAdapter(List<Person> people,
      PersonCardActionHandler handler) {
    this.people = people;
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
    holder.nameTextView.setText(person.getName());
    holder.phoneNumberTextView.setText(person.getPhoneNumber());
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
      holder.view.setOnClickListener(v -> actionHandler.toggleCheck(person));
      int checkedIconResource =
          person.isChecked() ? R.drawable.ic_check_circle_green_700_48dp
              : R.drawable.ic_check_circle_grey_700_48dp;
      holder.checkedIcon.setImageResource(checkedIconResource);
    } else {
      holder.checkedIcon.setVisibility(GONE);
    }
  }

  private boolean personMatchQuery(final Person person) {
    return person.getName().toLowerCase().contains(filterQuery)
        || person.getPhoneNumber().toLowerCase().contains(filterQuery);
  }

  @Override public int getItemCount() {
    return getPeopleList().size();
  }

  private List<Person> getPeopleList() {
    if (filterEnable) return filteringPeople;
    return people;
  }

  static class PersonViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.phoneNumberTextView) TextView phoneNumberTextView;
    @BindView(R.id.phoneButton) ImageButton phoneButton;
    @BindView(R.id.messageButton) ImageButton messageButton;
    @BindView(R.id.personCheckedIcon) ImageView checkedIcon;

    private View view;

    PersonViewHolder(final View itemView) {
      super(itemView);
      this.view = itemView;
      ButterKnife.bind(this, itemView);
    }
  }
}
