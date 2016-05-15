package support.im.chats;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import de.greenrobot.event.EventBus;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import sj.keyboard.XhsEmoticonsKeyBoard;
import support.im.R;
import support.im.events.ChatRecordEvent;
import support.im.utilities.PathUtils;

public class SupportEmoticonsKeyBoard extends XhsEmoticonsKeyBoard {

  public SupportEmoticonsKeyBoard(Context context, AttributeSet attrs) {
    super(context, attrs);
    initRecordBtn();
  }

  @Override protected void inflateKeyboardBar() {
    mInflater.inflate(R.layout.view_support_keyboard, this);
  }

  @Override public void onClick(final View v) {
    if (v.getId() == R.id.btn_voice_or_text && this.mRlInput.isShown()) {
      if (Nammu.checkPermission(Manifest.permission.RECORD_AUDIO)) {
        super.onClick(v);
      } else {
        Nammu.askForPermission((Activity) getContext(), Manifest.permission.RECORD_AUDIO,
          new PermissionCallback() {
            @Override public void permissionGranted() {
              SupportEmoticonsKeyBoard.super.onClick(v);
            }
            @Override public void permissionRefused() {
            }
          });
      }
      return;
    }
    super.onClick(v);
  }

  /**
   * 初始化录音按钮
   */
  private void initRecordBtn() {
    final RecordButton recordButton = (RecordButton) mBtnVoice;
    recordButton.setSavePath(PathUtils.getRecordPathByCurrentTime(getContext()));
    recordButton.setRecordEventListener(new RecordButton.RecordEventListener() {
      @Override
      public void onFinishedRecord(final String audioPath, int secs) {
        EventBus.getDefault().post(new ChatRecordEvent(audioPath, secs));
      }

      @Override
      public void onStartRecord() {}
    });
  }
}
