package com.bosch.peoplecounter.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
  private final PersonCardActionHandler actionHandler;

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
    Person person = people.get(position);
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
  }

  @Override public int getItemCount() {
    return people.size();
  }

  static class PersonViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.phoneNumberTextView) TextView phoneNumberTextView;
    @BindView(R.id.phoneButton) ImageButton phoneButton;
    @BindView(R.id.messageButton) ImageButton messageButton;

    private View view;

    PersonViewHolder(final View itemView) {
      super(itemView);
      this.view = itemView;
      ButterKnife.bind(this, itemView);
    }
  }
}
