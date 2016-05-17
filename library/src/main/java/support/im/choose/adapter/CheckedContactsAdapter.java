package support.im.choose.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;
import support.im.choose.viewholder.CheckedContactViewHolder;
import support.im.data.User;

/**
 * Created by wangh on 2016-5-17-0017.
 */
public class CheckedContactsAdapter extends RecyclerView.Adapter {
  private Context mContext;
  private List<User> mCheckedUserList;

  public CheckedContactsAdapter(Context context, List<User> checkedUserList) {
    mContext = context;
    mCheckedUserList = checkedUserList;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CheckedContactViewHolder(parent.getContext(), parent);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ((CheckedContactViewHolder) holder).bindTo(position, mCheckedUserList.get(position));
  }

  @Override public int getItemCount() {
    return mCheckedUserList.size();
  }
}
