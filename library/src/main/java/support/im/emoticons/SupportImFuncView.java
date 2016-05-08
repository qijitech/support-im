package support.im.emoticons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import com.google.common.collect.Lists;
import de.greenrobot.event.EventBus;
import java.util.List;
import support.im.R;
import support.im.events.FuncViewClickEvent;
import support.ui.app.SupportApp;

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
    GridView gridView = ButterKnife.findById(view, R.id.func_grid_view);
    final List<FuncItem> funcItems = Lists.newArrayList();
    funcItems.add(new FuncItem(R.drawable.si_selector_attach_camera,
        SupportApp.getInstance().getString(R.string.si_attach_camera), FuncItem.TAG_CAMERA));
    funcItems.add(new FuncItem(R.drawable.si_selector_attach_gallery,
        SupportApp.getInstance().getString(R.string.si_attach_gallery), FuncItem.TAG_GALLERY));
    funcItems.add(new FuncItem(R.drawable.si_selector_attach_location,
        SupportApp.getInstance().getString(R.string.si_attach_location), FuncItem.TAG_LOCATION));
    FuncViewAdapter adapter = new FuncViewAdapter(context, funcItems);
    gridView.setAdapter(adapter);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FuncItem funcItem = funcItems.get(position);
        EventBus.getDefault().post(new FuncViewClickEvent(funcItem));
      }
    });
  }
}
