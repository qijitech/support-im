package support.im.chats;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import java.util.Locale;
import support.im.R;

public class ChatsAudioViewHolder extends ChatsViewHolder {

  protected PlayButton mPlayButton;
  protected TextView mDurationTextView;

  public ChatsAudioViewHolder(Context context, ViewGroup parent, boolean isLeft) {
    super(context, parent, isLeft);
  }

  @Override protected void setupView() {
    super.setupView();
    if (mIsLeft) {
      mContentContainer.addView(View.inflate(mContext, R.layout.chats_incoming_item_audio, null));
    } else {
      mContentContainer.addView(View.inflate(mContext, R.layout.chats_outgoing_item_audio, null));
    }
    mPlayButton = ButterKnife.findById(itemView, R.id.btn_support_im_chats_item_audio_play);
    mDurationTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_audio_duration);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMAudioMessage) {
      AVIMAudioMessage audioMessage = (AVIMAudioMessage)value;
      mDurationTextView.setText(String.format(Locale.CHINA, "%.0f\"", audioMessage.getDuration()));
      String localFilePath = audioMessage.getLocalFilePath();
      if (!TextUtils.isEmpty(localFilePath)) {
        mPlayButton.setPath(localFilePath);
      } else {
        //String path = PathUtils.getChatFilePath(getContext(), audioMessage.getMessageId());
        //mPlayButton.setPath(path);
        // TODO
        //LocalCacheUtils.downloadFileAsync(audioMessage.getFileUrl(), path);
      }
    }
  }
}
