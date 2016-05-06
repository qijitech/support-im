package support.im.chats;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.ui.content.EmptyView;

public class ChatsEmptyView extends FrameLayout implements EmptyView {

  private TextView titleTextView;

  public ChatsEmptyView(Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.chats_empty_view, this, false);
    addView(view);
  }

  @Override public EmptyView buildEmptyImageView(@DrawableRes int drawableRes) {
    return null;
  }

  @Override public EmptyView buildEmptyTitle(@StringRes int stringRes) {
    if (titleTextView() != null) {
      titleTextView().setText(stringRes);
    }
    return this;
  }

  @Override public EmptyView buildEmptyTitle(String title) {
    if (titleTextView() != null) {
      titleTextView().setText(title);
    }
    return this;
  }

  @Override public EmptyView buildEmptySubtitle(@StringRes int stringRes) {
    return null;
  }

  @Override public EmptyView buildEmptySubtitle(String subtitle) {
    return null;
  }

  @Override public EmptyView shouldDisplayEmptySubtitle(boolean display) {
    return null;
  }

  @Override public EmptyView shouldDisplayEmptyTitle(boolean display) {
    return null;
  }

  @Override public EmptyView shouldDisplayEmptyImageView(boolean display) {
    return null;
  }

  @Override public void setOnEmptyViewClickListener(OnEmptyViewClickListener listener) {
  }

  public TextView titleTextView() {
    if (titleTextView == null) {
      titleTextView = ButterKnife.findById(this, R.id.support_im_chats_empty_title);
    }
    return titleTextView;
  }

}
