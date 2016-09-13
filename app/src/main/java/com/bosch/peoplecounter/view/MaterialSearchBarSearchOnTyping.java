package com.bosch.peoplecounter.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import com.bosch.peoplecounter.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import java.util.concurrent.TimeUnit;
import rx.subjects.PublishSubject;

/**
 * Extend the {@link MaterialSearchBar} to add search on typing feature.
 *
 * @author letientai299@gmail.com
 */
public class MaterialSearchBarSearchOnTyping extends MaterialSearchBar
    implements TextWatcher {
  final EditText searchEdit;
  private SearchQueryListener queryListener;

  public void setQueryListener(final SearchQueryListener queryListener) {
    this.queryListener = queryListener;
  }

  private PublishSubject<String> queryPublishSubject;

  public MaterialSearchBarSearchOnTyping(final Context context,
      final AttributeSet attrs) {
    super(context, attrs);

    searchEdit = (EditText) this.findViewById(R.id.mt_editText);
    searchEdit.addTextChangedListener(this);

    queryPublishSubject = PublishSubject.create();
    // Trigger search action after some delay during typing
    queryPublishSubject.throttleWithTimeout(300, TimeUnit.MILLISECONDS)
        .subscribe(q -> {
         this.queryListener.onQueryChange(q);
        });
  }

  @Override public void beforeTextChanged(final CharSequence s, final int start,
      final int count, final int after) {
    // don't care
  }

  @Override public void onTextChanged(final CharSequence s, final int start,
      final int before, final int count) {
    queryPublishSubject.onNext(s.toString());
  }

  @Override public void afterTextChanged(final Editable s) {
    // don't care
  }

  public interface SearchQueryListener {
    void onQueryChange(String query);
  }
}
