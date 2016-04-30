package support.im.chats;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import support.ui.SupportSinglePaneActivity;

public class ChatsActivity extends SupportSinglePaneActivity {

  public static void startChats(Context context) {
    Intent intent = new Intent(context, ChatsActivity.class);
    context.startActivity(intent);
  }

  @Override protected Fragment onCreatePane() {
    return ChatsFragment.create();
  }
}
