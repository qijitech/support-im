package support.im.emoticons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.R;

public class SupportImFuncView extends RelativeLayout {

  public SupportImFuncView(Context context) {
    this(context, null);
  }

  public SupportImFuncView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }

  protected void initialize(Context context) {
    final View view = LayoutInflater.from(context).inflate(R.layout.chats_func_view, this);
    GridView gv_apps = ButterKnife.findById(view, R.id.func_grid_view);
    List<FuncItem> funcItems = Lists.newArrayList();
    funcItems.add(new FuncItem(R.drawable.icon_photo, "图片"));
    funcItems.add(new FuncItem(R.drawable.icon_camera, "拍照"));
    funcItems.add(new FuncItem(R.drawable.icon_audio, "视频"));
    funcItems.add(new FuncItem(R.drawable.icon_contact, "联系人"));
    funcItems.add(new FuncItem(R.drawable.icon_file, "文件"));
    funcItems.add(new FuncItem(R.drawable.icon_loaction, "位置"));
    FuncViewAdapter adapter = new FuncViewAdapter(context, funcItems);
    gv_apps.setAdapter(adapter);
  }
}
