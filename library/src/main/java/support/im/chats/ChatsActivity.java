package support.im.chats;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import support.im.R;
import support.im.leanclound.Constants;
import support.ui.app.SupportSinglePaneActivity;

public class ChatsActivity extends SupportSinglePaneActivity {

  protected ChatsFragment mChatsFragment;

  public static void startChatsWithMemberId(Context context, String memberId) {
    Intent intent = new Intent(context, ChatsActivity.class);
    intent.putExtra(Constants.EXTRA_MEMBER_ID, memberId);
    context.startActivity(intent);
  }

  public static void startChatsWithConversationId(Context context, String conversationId) {
    Intent intent = new Intent(context, ChatsActivity.class);
    intent.putExtra(Constants.EXTRA_CONVERSATION_ID, conversationId);
    context.startActivity(intent);
  }

  @Override protected Fragment onCreatePane() {
    return mChatsFragment = ChatsFragment.create();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.single_chats_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int itemId = item.getItemId();
    if (itemId == R.id.menu_support_im_chats_single) {
      startActivity(new Intent(this, UserProfileActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
