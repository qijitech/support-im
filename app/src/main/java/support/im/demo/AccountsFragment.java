package support.im.demo;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import com.avos.avoscloud.feedback.FeedbackAgent;
import java.util.ArrayList;
import support.ui.app.SupportApp;
import support.ui.app.SupportCellsFragment;
import support.ui.cells.CellModel;

public class AccountsFragment extends SupportCellsFragment {

  private final static int TAG_FEEDBACK = 1;

  @Override public void onResume() {
    super.onResume();
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
    }
  }
}
