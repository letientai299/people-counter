package com.bosch.peoplecounter.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bosch.peoplecounter.R;
import com.bosch.peoplecounter.data.Person;
import java.util.List;

/**
 * @author letientai299@gmail.com
 */

class PersonRecyclerViewAdapter
    extends RecyclerView.Adapter<PersonRecyclerViewAdapter.PersonViewHolder> {
  private final List<Person> people;

  PersonRecyclerViewAdapter(List<Person> people) {
    this.people = people;
  }

  @Override public PersonViewHolder onCreateViewHolder(final ViewGroup parent,
      final int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.person_card_layout, parent, false);

    return new PersonViewHolder(view);
  }

  @Override public void onBindViewHolder(final PersonViewHolder holder,
      final int position) {
    Person person = people.get(position);
    holder.nameTextView.setText(person.getName());
    holder.phoneNumberTextView.setText(person.getPhoneNumber());
  }

  @Override public int getItemCount() {
    return people.size();
  }

  static class PersonViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.phoneNumberTextView) TextView phoneNumberTextView;

    PersonViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
