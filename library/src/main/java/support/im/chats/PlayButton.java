package support.im.chats;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import support.im.R;
import support.im.leanclound.AudioHelper;

public class PlayButton extends TextView implements View.OnClickListener {

  private String path;
  private boolean leftSide;
  private AnimationDrawable anim;

  public PlayButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    leftSide = getLeftFromAttrs(context, attrs);
    setLeftSide(leftSide);
    setOnClickListener(this);
  }

  public void setLeftSide(boolean leftSide) {
    this.leftSide = leftSide;
    stopRecordAnimation();
  }

  public boolean getLeftFromAttrs(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatsPlayBtn);
    boolean left = true;
    for (int index = 0; index < typedArray.getIndexCount(); index++) {
      int attr = typedArray.getIndex(index);
      if (attr == R.styleable.ChatsPlayBtn_left) {
        left = typedArray.getBoolean(attr, true);
      }
    }
    typedArray.recycle();
    return left;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override public void onClick(View v) {
    if (AudioHelper.getInstance().isPlaying() == true && AudioHelper.getInstance()
        .getAudioPath()
        .equals(path)) {
      AudioHelper.getInstance().pausePlayer();
      stopRecordAnimation();
    } else {
      startRecordAnimation();
      AudioHelper.getInstance().playAudio(path, new Runnable() {
        @Override public void run() {
          stopRecordAnimation();
        }
      });
    }
  }

  private void startRecordAnimation() {
    setCompoundDrawablesWithIntrinsicBounds(leftSide ? R.drawable.support_im_chats_anim_voice_left : 0, 0,
        !leftSide ? R.drawable.support_im_chats_anim_voice_right : 0, 0);
    anim = (AnimationDrawable) getCompoundDrawables()[leftSide ? 0 : 2];
    anim.start();
  }

  private void stopRecordAnimation() {
    setCompoundDrawablesWithIntrinsicBounds(leftSide ? R.drawable.support_im_chats_voice_right3 : 0, 0,
        !leftSide ? R.drawable.support_im_chats_voice_left3 : 0, 0);
    if (anim != null) {
      anim.stop();
    }
  }
}
