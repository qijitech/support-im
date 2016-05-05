package support.im.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import support.im.demo.data.User;
import support.im.demo.features.profile.data.IconItem;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportCellsActivity;
import support.ui.cells.CellModel;

/**
 * Created by wangh on 2016-5-5-0005.
 */
public class UserProfileActivity extends SupportCellsActivity
    implements EasyViewHolder.OnItemClickListener {
  public final static String TAG = UserProfileActivity.class.getSimpleName();
  public final static int USER_AVATAR = 0;
  public final static int TOP_CONVERSATION = 1;
  public final static int NO_NOTIFITY = 2;
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
        case NO_NOTIFITY:
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
    //Resources res = SupportApp.appResources();
    items.add(CellModel.settingCell("添加成员").build());
    items.add(CellModel.emptyCell().build());
    items.add(
        CellModel.checkCell("置顶聊天").tag(TOP_CONVERSATION).needDivider(true).checked(false).build());
    items.add(CellModel.checkCell("消息免打扰").tag(NO_NOTIFITY).checked(false).build());

    items.add(CellModel.shadowCell().build());
    items.add(CellModel.settingCell("聊天图片与文件").tag(MSG_FILE_CACHE).needDivider(true).build());
    items.add(CellModel.settingCell("查找聊天记录").tag(QUERY_MSG_LOG).build());
    items.add(CellModel.shadowBottomCell().build());
    return items;
  }
}
