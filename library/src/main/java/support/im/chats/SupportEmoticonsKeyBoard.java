package support.im.chats;

import android.content.Context;
import android.util.AttributeSet;
import de.greenrobot.event.EventBus;
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
