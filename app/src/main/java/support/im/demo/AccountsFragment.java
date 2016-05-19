package support.im.demo;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import java.util.ArrayList;
import support.im.data.SupportUser;
import support.im.demo.features.auth.LoginActivity;
import support.ui.app.SupportApp;
import support.ui.app.SupportCellsFragment;
import support.ui.cells.CellModel;

public class AccountsFragment extends SupportCellsFragment {

  private final static int TAG_FEEDBACK = 1;
  private final static int TAG_LOGOUT = 2;
  private final static int TAG_DISPLAY_NAME = 3;

  @Override public void onResume() {
    super.onResume();
    getActivity().setTitle(R.string.label_account);
    clearAll();
    addAll(buildAccountData());
  }

  private ArrayList<CellModel> buildAccountData() {
    ArrayList<CellModel> items = new ArrayList<>();
    final Resources r = SupportApp.appResources();
    items.add(CellModel.emptyCell(20).build());
    items.add(CellModel.textCell(r.getString(R.string.account_feedback))
        .drawable(SupportApp.drawable(R.drawable.ic_feedback))
        .tag(TAG_FEEDBACK)
        .build());
    items.add(CellModel.shadowCell(40).build());

    if (SupportUser.getCurrentUser() != null) {
      items.add(CellModel.textCell(SupportUser.getCurrentUser().getDisplayName())
          .tag(TAG_DISPLAY_NAME)
          .build());
      items.add(CellModel.shadowCell(40).build());
    }

    items.add(CellModel.textCell("退出登录")
        .tag(TAG_LOGOUT)
        .build());
    return items;
  }

  public static Fragment create() {
    return new AccountsFragment();
  }

  @Override protected void onItemClick(CellModel cellModel) {
    switch (cellModel.tag) {
      case TAG_FEEDBACK:
        FeedbackAgent agent = new FeedbackAgent(getContext());
        agent.startDefaultThreadActivity();
        break;
      case TAG_LOGOUT:
        SupportUser.logoutFromLeanCloud(new AVIMClientCallback() {
          @Override public void done(AVIMClient avimClient, AVIMException e) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
          }
        });
        break;
    }
  }
}
