package support.im.picker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;
import support.im.picker.viewholder.ContactsCheckViewHolder;
import support.im.data.User;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-16-0016.
 */
public class ContactsAdapter extends RecyclerView.Adapter {

  private List<User> mUserList;
  private Context mContext;

  public ContactsAdapter(Context context, List<User> list) {
    mContext = context;
    mUserList = list;
  }

  public void setUpUserList(List<User> list) {
    mUserList.clear();
    mUserList.addAll(list);
    notifyDataSetChanged();
  }

  @Override public EasyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ContactsCheckViewHolder(parent.getContext(), parent);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ((ContactsCheckViewHolder) holder).bindTo(position, mUserList.get(position));
  }

  @Override public int getItemCount() {
    return mUserList.size();
  }
  
  
}
