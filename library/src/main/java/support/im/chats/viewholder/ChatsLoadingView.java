package support.im.chats.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import support.im.R;

public class ChatsLoadingView extends FrameLayout {

  public ChatsLoadingView(Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.chats_loading_view, this, false);
    addView(view);
  }

}
