package support.im.mobilecontact;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import support.im.R;
import support.im.data.MobileContact;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;

public class MobileContactsAdapter extends EasyRecyclerAdapter implements
    StickyRecyclerHeadersAdapter<EasyViewHolder> {

  public MobileContactsAdapter(Context context) {
    super(context);
  }

  @Override public MobileContact get(int position) {
    return (MobileContact) super.get(position);
  }

  public int getPositionForSection(char section) {
    for (int i = 0; i < getItemCount(); i++) {
      String sortStr = get(i).getSortLetters();
      char firstChar = sortStr.toUpperCase().charAt(0);
      if (firstChar == section) {
        return i;
      }
    }
    return -1;
  }

  @Override public long getHeaderId(int position) {
    return get(position).getSortLetters().charAt(0);
  }

  @Override public EasyViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
    return new EasyViewHolder(parent.getContext(), parent, R.layout.header) {
      @Override public void bindTo(int position, Object value) {
      }
    };
  }

  @Override public void onBindHeaderViewHolder(EasyViewHolder holder, int position) {
    TextView textView = (TextView) holder.itemView;
    String showValue = String.valueOf(get(position).getSortLetters().charAt(0));
    if ("$".equals(showValue)) {
      textView.setText("群主");
    } else if ("%".equals(showValue)) {
      textView.setText("系统管理员");
    } else {
      textView.setText(showValue);
    }
  }
}
