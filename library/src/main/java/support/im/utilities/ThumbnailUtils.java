package support.im.utilities;

import support.ui.utilities.ScreenUtils;

public class ThumbnailUtils {

  private static final int maxThumbWidth;
  private static final int maxThumbHeight;

  static {
    final int screenWidth = ScreenUtils.getScreenWidth();
    final int screenHeight = ScreenUtils.getScreenHeight();
    maxThumbWidth = 2 * screenWidth / 5;
    maxThumbHeight = 2 * screenHeight / 5;
  }

  public static int[] getThumbSize(int sourceThumbWidth, int sourceThumbHeight) {
    if (sourceThumbHeight <= 0 || sourceThumbWidth <= 0) {
      return new int[] {maxThumbWidth, maxThumbHeight};
    }

    if (sourceThumbHeight <= maxThumbHeight && sourceThumbWidth <= maxThumbWidth) {
      return new int[] { sourceThumbWidth, sourceThumbHeight };
    }

    double aspectRatio = sourceThumbHeight * 1.0 / sourceThumbWidth;
    int targetHeight = (int) (maxThumbWidth * aspectRatio); // 默认按照宽度为标准
    if (targetHeight > maxThumbHeight) { // 如果按照宽度来计算高度大于最大高度,那就以最大高度为标准进行缩放
      aspectRatio = sourceThumbWidth * 1.0 / sourceThumbHeight;
      int targetWidth = (int) (aspectRatio * maxThumbHeight);
      return new int[] {targetWidth, maxThumbHeight};
    }
    return new int[] {maxThumbWidth, targetHeight};
  }
}
