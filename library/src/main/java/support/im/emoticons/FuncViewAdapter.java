package support.im.emoticons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.R;

public class FuncViewAdapter extends BaseAdapter {

  private LayoutInflater inflater;
  private Context mContext;
  private List<FuncItem> mData = Lists.newArrayList();

  public FuncViewAdapter(Context context, List<FuncItem> data) {
    this.mContext = context;
    this.inflater = LayoutInflater.from(context);
    mData.clear();
    mData.addAll(data);
  }

  @Override public int getCount() {
    return mData.size();
  }

  @Override public Object getItem(int position) {
    return mData.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      convertView = inflater.inflate(R.layout.chats_func_item, null);
      viewHolder.mIconView = ButterKnife.findById(convertView, android.R.id.icon1);
      viewHolder.mTextView = ButterKnife.findById(convertView, android.R.id.text1);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final FuncItem funcItem = mData.get(position);
    if (funcItem != null) {
      viewHolder.mIconView.setBackgroundResource(funcItem.icon);
      viewHolder.mTextView.setText(funcItem.funcName);
    }
    return convertView;
  }

  static class ViewHolder {
    public ImageView mIconView;
    public TextView mTextView;
  }
}
