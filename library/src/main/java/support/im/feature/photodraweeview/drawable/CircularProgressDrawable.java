package support.im.feature.photodraweeview.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by wangh on 2016-5-13-0013.
 */
public class CircularProgressDrawable extends Drawable {

  public static final int PROGRESS_FACTOR = -360;

  public static final String CIRCLE_SCALE_PROPERTY = "circleScale";

  public static final String PROGRESS_PROPERTY = "progress";

  public static final String RING_COLOR_PROPERTY = "ringColor";

  public static final String CIRCLE_COLOR_PROPERTY = "circleColor";

  public static final String OUTLINE_COLOR_PROPERTY = "outlineColor";

  public static final String TAG = "CircularProgressDrawable";

  private final Paint paint;

  protected float progress;

  protected int outlineColor;

  protected int ringColor;

  protected int centerColor;

  protected final RectF arcElements;

  protected final int ringWidth;

  protected float circleScale;

  protected boolean indeterminate;

  CircularProgressDrawable(int ringWidth, float circleScale, int outlineColor, int ringColor,
      int centerColor) {
    this.progress = 0;
    this.outlineColor = outlineColor;
    this.ringColor = ringColor;
    this.centerColor = centerColor;
    this.paint = new Paint();
    this.paint.setAntiAlias(true);
    this.ringWidth = ringWidth;
    this.arcElements = new RectF();
    this.circleScale = circleScale;
    this.indeterminate = false;
  }

  @Override protected boolean onLevelChange(int level) {
    progress = level;
    indeterminate = true;
    if (level > 0 && level < 10000) {
      invalidateSelf();
      return true;
    } else {
      return false;
    }
  }

  @Override public void draw(Canvas canvas) {
    final Rect bounds = getBounds();

    // Calculations on the different components sizes
    int size = Math.min(bounds.height(), bounds.width());
    float outerRadius = (size / 2) - (ringWidth / 2);
    float innerRadius = outerRadius * circleScale;
    float offsetX = (bounds.width() - outerRadius * 2) / 2;
    float offsetY = (bounds.height() - outerRadius * 2) / 2;

    // Outline Circle
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(1);
    paint.setColor(outlineColor);
    canvas.drawCircle(bounds.centerX(), bounds.centerY(), outerRadius, paint);

    // Inner circle
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(centerColor);
    canvas.drawCircle(bounds.centerX(), bounds.centerY(), innerRadius, paint);

    int halfRingWidth = ringWidth / 2;
    float arcX0 = offsetX + halfRingWidth;
    float arcY0 = offsetY + halfRingWidth;
    float arcX = offsetX + outerRadius * 2 - halfRingWidth;
    float arcY = offsetY + outerRadius * 2 - halfRingWidth;

    // Outer Circle
    paint.setColor(ringColor);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(ringWidth);
    paint.setStrokeCap(Paint.Cap.ROUND);
    arcElements.set(arcX0, arcY0, arcX, arcY);
    if (indeterminate) {
      canvas.drawArc(arcElements, progress, 90, false, paint);
    } else {
      canvas.drawArc(arcElements, 89, progress, false, paint);
    }
  }

  @Override public void setAlpha(int alpha) {
    paint.setAlpha(alpha);
  }

  @Override public void setColorFilter(ColorFilter cf) {
    paint.setColorFilter(cf);
  }

  @Override public int getOpacity() {
    return 1 - paint.getAlpha();
  }

  public float getProgress() {
    return progress / PROGRESS_FACTOR;
  }

  public void setProgress(float progress) {
    if (indeterminate) {
      this.progress = progress;
    } else {
      this.progress = PROGRESS_FACTOR * progress;
    }
    invalidateSelf();
  }

  public float getCircleScale() {
    return circleScale;
  }

  public void setCircleScale(float circleScale) {
    this.circleScale = circleScale;
    invalidateSelf();
  }

  public boolean isIndeterminate() {
    return indeterminate;
  }

  public void setIndeterminate(boolean indeterminate) {
    this.indeterminate = indeterminate;
  }

  public int getOutlineColor() {
    return outlineColor;
  }

  public int getRingColor() {
    return ringColor;
  }

  public int getCenterColor() {
    return centerColor;
  }

  public void setOutlineColor(int outlineColor) {
    this.outlineColor = outlineColor;
    invalidateSelf();
  }

  public void setRingColor(int ringColor) {
    this.ringColor = ringColor;
    invalidateSelf();
  }

  public void setCenterColor(int centerColor) {
    this.centerColor = centerColor;
    invalidateSelf();
  }

  public static class Builder {

    int ringWidth;

    int outlineColor;

    int ringColor;

    int centerColor;

    float circleScale = 0.75f;

    public Builder setRingWidth(int ringWidth) {
      this.ringWidth = ringWidth;
      return this;
    }

    public Builder setOutlineColor(int outlineColor) {
      this.outlineColor = outlineColor;
      return this;
    }

    public Builder setRingColor(int ringColor) {
      this.ringColor = ringColor;
      return this;
    }

    public Builder setCenterColor(int centerColor) {
      this.centerColor = centerColor;
      return this;
    }

    public Builder setInnerCircleScale(float circleScale) {
      this.circleScale = circleScale;
      return this;
    }

    public CircularProgressDrawable create() {
      return new CircularProgressDrawable(ringWidth, circleScale, outlineColor, ringColor,
          centerColor);
    }
  }
}
