package support.im.feature.photodraweeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import butterknife.ButterKnife;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.imagepipeline.image.ImageInfo;
import support.im.R;
import support.im.feature.photodraweeview.drawable.CircularDrawable;
import support.im.feature.photodraweeview.widget.OnPhotoTapListener;
import support.im.feature.photodraweeview.widget.OnViewTapListener;
import support.im.feature.photodraweeview.widget.PhotoDraweeView;

/**
 * Created by wangh on 2016-5-13-0013.
 */
public class SingleDraweeActivity extends AppCompatActivity {

  private PhotoDraweeView mPhotoDraweeView;

  public static final String IMG_URL = "img_url";

  private String mImgUrl;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
    setContentView(R.layout.activity_single_drawee_view);
    ButterKnife.bind(this);
    mPhotoDraweeView = ButterKnife.findById(this, R.id.support_im_single_drawee_view);
    mImgUrl = getIntent().getStringExtra(IMG_URL);
    if (TextUtils.isEmpty(mImgUrl)) {
      throw new IllegalArgumentException("你必须通过img_url键传递一个非空的url地址");
    } else {
      initPhotoView();
    }
  }

  private void initPhotoView() {
    GenericDraweeHierarchy hierarchy = mPhotoDraweeView.getHierarchy();
    hierarchy.setProgressBarImage(new CircularDrawable());
    mPhotoDraweeView.setHierarchy(hierarchy);
    PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
    controller.setUri(Uri.parse(mImgUrl));
    controller.setOldController(mPhotoDraweeView.getController());
    controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
      @Override public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        super.onFinalImageSet(id, imageInfo, animatable);
        if (imageInfo == null || mPhotoDraweeView == null) {
          return;
        }
        mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
      }
    });
    controller.setAutoPlayAnimations(true);
    mPhotoDraweeView.setController(controller.build());

    mPhotoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
      @Override public void onPhotoTap(View view, float x, float y) {
        finish();
      }
    });
    mPhotoDraweeView.setOnViewTapListener(new OnViewTapListener() {
      @Override public void onViewTap(View view, float x, float y) {
        //skip
      }
    });
  }

  public static void startBroswerSingleImg(Activity activity, String url) {
    Intent intent = new Intent(activity, SingleDraweeActivity.class);
    intent.putExtra(IMG_URL, url);
    activity.startActivity(intent);
    activity.overridePendingTransition(0, 0);
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }
}
