package support.im.utilities;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import support.ui.utilities.AndroidUtilities;
import support.ui.utilities.BuildCompat;

/**
 * http://www.jianshu.com/p/5364957dcf49?plg_nld=1&plg_uin=1&plg_auth=1&plg_nld=1&plg_usr=1&plg_vkey=1&plg_dev=1
 */
public class FrescoDisplayUtils {

  public static void initialize(Context context) {
    ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
        .setWebpSupportEnabled(true)
        .setDownsampleEnabled(true).build();
    Fresco.initialize(context, config);
  }

  public static void showThumb(Uri uri, final SimpleDraweeView draweeView, int width, int height) {
    ImageRequest request = buildImageRequest(uri, width, height);

    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setImageRequest(request)
        .setOldController(draweeView.getController())
        .setControllerListener(new BaseControllerListener<ImageInfo>())
        .build();
    draweeView.setController(controller);
  }

  public static void showThumb(Uri uri, SimpleDraweeView draweeView) {
    showThumb(uri, draweeView, 144, 144);
  }

  private static ImageRequest buildImageRequest(Uri uri, int width, int height) {
    return ImageRequestBuilder.newBuilderWithSource(uri)
        .setResizeOptions(new ResizeOptions(AndroidUtilities.dp(width), AndroidUtilities.dp(height)))
        .build();
  }

  /**
   * https://github.com/facebook/fresco/issues/738
   */
  public class LollipopBitmapMemoryCacheParamsSupplier implements Supplier {

    private ActivityManager activityManager;

    public LollipopBitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
      this.activityManager = activityManager;
    }

    @Override public Object get() {
      if (BuildCompat.hasLollipop()) {
        return new MemoryCacheParams(getMaxCacheSize(), 56, Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE);
      }
      return new MemoryCacheParams(
          getMaxCacheSize(),
          256,
          Integer.MAX_VALUE,
          Integer.MAX_VALUE,
          Integer.MAX_VALUE);

    }

    private int getMaxCacheSize() {
      final int maxMemory = Math.min(activityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
      if (maxMemory < 32 * ByteConstants.MB) {
        return 4 * ByteConstants.MB;
      }
      if (maxMemory < 64 * ByteConstants.MB) {
        return 6 * ByteConstants.MB;
      }
      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
        return 8 * ByteConstants.MB;
      }
      return maxMemory / 4;
    }
  }

}
