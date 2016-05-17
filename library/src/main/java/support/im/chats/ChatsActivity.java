package support.im.chats;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import com.raizlabs.android.dbflow.annotation.NotNull;
import java.util.ArrayList;
import support.im.leanclound.Constants;
import support.ui.app.SupportSinglePaneActivity;

public class ChatsActivity extends SupportSinglePaneActivity {

  protected ChatsFragment mChatsFragment;

  public static void startChatsWithMemberId(Context context, String memberId) {
    Intent intent = new Intent(context, ChatsActivity.class);
    intent.putExtra(Constants.EXTRA_MEMBER_ID, memberId);
    context.startActivity(intent);
  }

  public static void startChatsWithMemberIdList(Context context, @NotNull ArrayList memberIdList) {
    if (memberIdList == null || memberIdList.size() <= 0) {
      throw new IllegalArgumentException("memberIdList must not null or size > 0");
    } else {
      Intent intent = new Intent(context, ChatsActivity.class);
      intent.putStringArrayListExtra(Constants.EXTRA_MEMBER_LIST_ID, memberIdList);
      context.startActivity(intent);
    }
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
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }
}
