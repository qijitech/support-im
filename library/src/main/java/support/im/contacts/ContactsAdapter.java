package support.im.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import support.im.R;
import support.im.data.SupportUser;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;

public class ContactsAdapter extends EasyRecyclerAdapter implements
    StickyRecyclerHeadersAdapter<EasyViewHolder> {

  public ContactsAdapter(Context context) {
    super(context);
  }

  public int getPositionForSection(char section) {
    for (int index = 0; index < getItemCount(); index++) {
      final Object object = get(index);
      if (object instanceof SupportUser) {
        SupportUser supportUser = (SupportUser) object;
        String sortStr = supportUser.getSortLetters();
        char firstChar = sortStr.toUpperCase().charAt(0);
        if (firstChar == section) {
          return index;
        }
      }
    }
    return RecyclerView.NO_POSITION;
  }

  @Override public long getHeaderId(int position) {
    final Object object = get(position);
    if (object instanceof SupportUser) {
      SupportUser supportUser = (SupportUser) object;
      return supportUser.getSortLetters().charAt(0);
    }
    return RecyclerView.NO_POSITION;
  }

  @Override public EasyViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
    return new EasyViewHolder(parent.getContext(), parent, R.layout.header) {
      @Override public void bindTo(int position, Object value) {
      }
    };
  }

  @Override public void onBindHeaderViewHolder(EasyViewHolder holder, int position) {
    TextView textView = (TextView) holder.itemView;
    final Object object = get(position);
    if (object instanceof SupportUser) {
      SupportUser supportUser = (SupportUser) object;
      String showValue = String.valueOf(supportUser.getSortLetters().charAt(0));
      textView.setText(showValue);
    }
  }
}
