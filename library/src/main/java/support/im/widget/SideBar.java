package support.im.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import support.im.R;
import support.ui.utilities.AndroidUtilities;

/**
 * create by wangh
 * 侧边栏
 */
public class SideBar extends View {
  // 触摸事件
  private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
  // 26个字母
  public static String[] b = {
      "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
      "S", "T", "U", "V", "W", "X", "Y", "Z"
  };
  private int choose = -1;// 选中
  private Paint paint = new Paint();
  private final static int CHAR_HEIGHT_DP = 12;

  private List<String> mContainerCharList;

  private final float charHeight;

  private TextView mTextDialog;

  private float mFirstCharY;
  private float mLastCharY;

  private TextView mBubble;

  public void setCenterDialog(TextView mTextDialog) {
    this.mTextDialog = mTextDialog;
  }

  private Context mContext;

  public void setBubble(TextView bubble) {
    this.mBubble = bubble;
  }

  public SideBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
    charHeight = AndroidUtilities.dp(CHAR_HEIGHT_DP);
  }

  public SideBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    charHeight = AndroidUtilities.dp(CHAR_HEIGHT_DP);
  }

  public SideBar(Context context) {
    super(context);
    mContext = context;
    charHeight = AndroidUtilities.dp(CHAR_HEIGHT_DP);
  }

  public void setUpCharList(List<String> charList) {
    if (this.mContainerCharList == null) {
      this.mContainerCharList = new ArrayList<>();
    }
    this.mContainerCharList.clear();
    this.mContainerCharList.addAll(charList);
    List<String> tempList = Arrays.asList(b);
    List<String> afterCharList = new ArrayList<String>();
    for (String item : tempList) {
      if (mContainerCharList.contains(item)) {
        afterCharList.add(item);
      }
    }
    if (!mContainerCharList.contains("#")) {
      afterCharList.add("#");
    }
    mContainerCharList.clear();
    mContainerCharList.addAll(afterCharList);
    afterCharList = null;
    tempList = null;
    invalidate();
  }

  /**
   * 重写这个方法
   */
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int width = getWidth(); // 获取对应宽度

    float eachHeight;
    eachHeight = charHeight;
    int len;
    if (mContainerCharList != null) {
      len = mContainerCharList.size();
    } else {
      len = b.length;
    }
    /**
     * 只需要初始位置确定后续连加即可
     * 初始位置为每一份的间隔
     */
    float firstPos;
    if (mContainerCharList != null) {
      firstPos = (getHeight() - eachHeight * mContainerCharList.size()) / 2;
    } else {
      firstPos = (getHeight() - (paint.measureText(b[0]) * b.length / 2)) / b.length;
    }

    for (int i = 0; i < len; i++) {
      paint.setColor(Color.rgb(86, 86, 86));
      paint.setTypeface(Typeface.DEFAULT);
      paint.setAntiAlias(true);
      paint.setTextSize(30);

      if (i == choose) {
        paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paint.setFakeBoldText(true);
      }

      // x坐标等于中间-字符串宽度的一半.
      float xPos;
      if (mContainerCharList != null) {
        xPos = width / 2 - paint.measureText(mContainerCharList.get(i)) / 2;
      } else {
        xPos = width / 2 - paint.measureText(b[i]) / 2;
      }

      float yPos = firstPos + eachHeight * i;
      if (i == 0) {
        mFirstCharY = yPos;
      }
      if (i == len - 1) {
        mLastCharY = yPos;
      }

      if (mContainerCharList != null) {
        canvas.drawText(mContainerCharList.get(i), xPos, yPos, paint);
      } else {
        canvas.drawText(b[i], xPos, yPos, paint);
      }
      paint.reset();// 重置画笔
    }
  }

  @Override public boolean dispatchTouchEvent(MotionEvent event) {
    if (mBubble == null && mTextDialog == null) {
      throw new IllegalArgumentException("至少调用一个setBubble或者setCenterDialog方法以显示指示器");
    }
    final int action = event.getAction();
    final float y = event.getY();// 点击y坐标
    final int oldChoose = choose;
    final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
    int cTemp = -1;
    if (mContainerCharList != null) {
      if (y > mFirstCharY || y < mLastCharY) {
        for (int i = 0; i < mContainerCharList.size(); i++) {
          if (y > mFirstCharY + charHeight * i && y < mFirstCharY + charHeight * (i + 1)) {
            cTemp = i;
          }
        }
      }
    } else {
      cTemp = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
    }
    final int c = cTemp;
    switch (action) {
      case MotionEvent.ACTION_UP:
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        choose = -1;//

        invalidate();
        hideBubble();
        if (mTextDialog != null) {
          mTextDialog.setVisibility(View.INVISIBLE);
        }
        break;
      default:
        setBackgroundDrawable(new ColorDrawable(0x13161316));
        if (oldChoose != c) {
          int len;
          if (mContainerCharList != null) {
            len = mContainerCharList.size();
          } else {
            len = b.length;
          }
          if (c >= 0 && c < len) {
            if (listener != null) {
              if (mContainerCharList != null) {
                listener.onTouchingLetterChanged(mContainerCharList.get(c));
              } else {
                listener.onTouchingLetterChanged(b[c]);
              }
            }
            if (mTextDialog != null) {
              if (mContainerCharList != null) {
                mTextDialog.setText(mContainerCharList.get(c));
              } else {
                mTextDialog.setText(b[c]);
              }
              mTextDialog.setVisibility(View.VISIBLE);
            }

            if (mContainerCharList != null && mBubble != null) {
              mBubble.setText(mContainerCharList.get(c));
              setBubblePosition(event.getY());
              showBubble();
            }

            choose = c;
            invalidate();
          }
        }

        break;
    }
    return true;
  }

  /**
   * 向外公开的方法
   */
  public void setOnTouchingLetterChangedListener(
      OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
    this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
  }

  /**
   * 接口
   *
   * @author coder
   */
  public interface OnTouchingLetterChangedListener {
    void onTouchingLetterChanged(String s);
  }

  private int getValueInRange(int min, int max, int value) {
    int minimum = Math.max(min, value);
    return Math.min(minimum, max);
  }

  private void setBubblePosition(float y) {

    if (mBubble != null) {
      int bubbleHeight = mBubble.getHeight();
      mBubble.setY(getValueInRange(0, getHeight() - bubbleHeight / 2, (int) (y - bubbleHeight)));
    }
  }

  private void showBubble() {
    if (mBubble == null) return;
    if (mBubble.getVisibility() == INVISIBLE) {
      if (mContainerCharList != null) {
        mBubble.setVisibility(VISIBLE);
      }
    }
  }

  private void hideBubble() {
    if (mBubble == null) return;
    mBubble.setVisibility(INVISIBLE);
  }
}
