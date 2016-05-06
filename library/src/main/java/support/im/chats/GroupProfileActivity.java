package support.im.chats;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import support.im.R;
import support.im.data.IconItem;
import support.im.data.User;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;
import support.ui.app.SupportCellsActivity;
import support.ui.cells.CellModel;

/**
 * Created by wangh on 2016-5-5-0005.
 */
public class GroupProfileActivity extends SupportCellsActivity
    implements EasyViewHolder.OnItemClickListener {

  public final static String TAG = UserProfileActivity.class.getSimpleName();
  public final static int TOP_CONVERSATION = 1;
  public final static int NO_NOTIFY = 2;
  public final static int MSG_FILE_CACHE = 3;
  public final static int QUERY_MSG_LOG = 4;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getAdapter().bind(User.class, UserHeadCell.class);
    getAdapter().bind(IconItem.class, IconItemCell.class);
    addItem(new User());
    addItem(new IconItem());
    appendAll(buildData());
  }

  @Override protected void onItemClick(Object object) {
    if (object instanceof CellModel) {
      switch (((CellModel) object).tag) {
        case TOP_CONVERSATION:
          Log.d(TAG, "置顶会话");
          break;
        case NO_NOTIFY:
          Log.d(TAG, "消息免打扰");
          break;
        case MSG_FILE_CACHE:
          Log.d(TAG, "聊天文件和图片");
          break;
        case QUERY_MSG_LOG:
          Log.d(TAG, "查询聊天记录");
          break;
      }
    } else if (object instanceof User) {
      Log.d(TAG, "头像点击");
    }
  }

  private ArrayList<CellModel> buildData() {
    ArrayList<CellModel> items = new ArrayList<>();
    Resources res = SupportApp.appResources();
    items.add(CellModel.emptyCell().build());
    items.add(
        CellModel.checkCell(res.getString(R.string.support_im_label_chats_single_top_conversation))
            .tag(TOP_CONVERSATION)
            .needDivider(true)
            .checked(false)
            .build());
    items.add(
        CellModel.checkCell(res.getString(R.string.support_im_label_chats_single_msg_no_notify))
            .tag(NO_NOTIFY)
            .checked(false)
            .build());

    items.add(CellModel.shadowCell().build());
    items.add(CellModel.settingCell(res.getString(R.string.support_im_label_chats_single_msg_cache))
        .tag(MSG_FILE_CACHE)
        .needDivider(true)
        .build());
    items.add(CellModel.settingCell(res.getString(R.string.support_im_label_chats_single_chat_log))
        .tag(QUERY_MSG_LOG)
        .build());
    items.add(CellModel.shadowBottomCell().build());
    return items;
  }
}
