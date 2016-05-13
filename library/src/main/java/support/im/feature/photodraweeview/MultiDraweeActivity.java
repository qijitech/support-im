package support.im.feature.photodraweeview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import butterknife.ButterKnife;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.imagepipeline.image.ImageInfo;
import java.util.ArrayList;
import java.util.List;
import support.im.R;
import support.im.feature.photodraweeview.adapter.MultiTouchViewPager;
import support.im.feature.photodraweeview.drawable.CircularDrawable;
import support.im.feature.photodraweeview.widget.OnPhotoTapListener;
import support.im.feature.photodraweeview.widget.PhotoDraweeView;

/**
 * Created by wangh on 2016-5-13-0013.
 */
public class MultiDraweeActivity extends AppCompatActivity {

  private List<String> mUrlList;
  private int mCurrentPosition;
  private MultiTouchViewPager mMultiTouchViewPager;

  public static final String FLAG_URL_LIST = "flag_url_list";
  public static final String FLAG_IMG_POSITION = "flag_img_position";

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
    setContentView(R.layout.activity_multi_drawee_view);
    ButterKnife.bind(this);
    mMultiTouchViewPager = ButterKnife.findById(this, R.id.support_im_mulit_drawee_adapter);
    getImgList();
  }

  private void getImgList() {
    mUrlList = getIntent().getStringArrayListExtra(FLAG_URL_LIST);
    if (mUrlList == null || mUrlList.size() <= 0) {
      throw new IllegalArgumentException("传递的List不能为空并且长度必须大于0");
    }
    mCurrentPosition = getIntent().getIntExtra(FLAG_IMG_POSITION, 0);
    mMultiTouchViewPager.setAdapter(new TouchImageAdapter());
    mMultiTouchViewPager.setCurrentItem(mCurrentPosition);
  }

  class TouchImageAdapter extends PagerAdapter {
    @Override public int getCount() {
      return mUrlList.size();
    }

    @Override public View instantiateItem(ViewGroup container, int position) {
      final PhotoDraweeView photoDraweeView = new PhotoDraweeView(container.getContext());
      PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();

      controllerBuilder.setUri(Uri.parse(mUrlList.get(position)));
      GenericDraweeHierarchy hierarchy = photoDraweeView.getHierarchy();
      hierarchy.setProgressBarImage(new CircularDrawable());
      photoDraweeView.setHierarchy(hierarchy);
      photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
        @Override public void onPhotoTap(View view, float x, float y) {
          finish();
        }
      });
      controllerBuilder.setOldController(photoDraweeView.getController());
      controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
          super.onFinalImageSet(id, imageInfo, animatable);
          if (imageInfo == null) {
            return;
          }
          photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
        }
      });
      controllerBuilder.setAutoPlayAnimations(true);
      photoDraweeView.setController(controllerBuilder.build());

      try {
        container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return photoDraweeView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }
  }

  public static void startBroswerMultiImg(Context context, ArrayList<String> url,
      int currentPosition) {
    Intent intent = new Intent(context, SingleDraweeActivity.class);
    intent.putStringArrayListExtra(FLAG_URL_LIST, url);
    intent.putExtra(FLAG_IMG_POSITION, currentPosition);
    context.startActivity(intent);
  }
}
