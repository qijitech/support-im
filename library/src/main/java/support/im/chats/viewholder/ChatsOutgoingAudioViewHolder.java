package support.im.chats.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import java.util.Locale;
import support.im.R;
import support.im.chats.PlayButton;
import support.im.utilities.LocalCacheUtils;
import support.im.utilities.PathUtils;

public class ChatsOutgoingAudioViewHolder extends ChatsOutgoingViewHolder {

  protected PlayButton mPlayButton;
  protected TextView mDurationTextView;

  public ChatsOutgoingAudioViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.chats_outgoing_item_audio);
    mPlayButton = ButterKnife.findById(itemView, R.id.btn_support_im_chats_item_audio_play);
    mDurationTextView =
        ButterKnife.findById(itemView, R.id.text_support_im_chats_item_audio_duration);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMAudioMessage) {
      AVIMAudioMessage audioMessage = (AVIMAudioMessage) value;
      mDurationTextView.setText(String.format(Locale.CHINA, "%.0f\"", audioMessage.getDuration()));
      String localFilePath = audioMessage.getLocalFilePath();
      if (!TextUtils.isEmpty(localFilePath)) {
        mPlayButton.setPath(localFilePath);
      } else {
        String path = PathUtils.getChatFilePath(itemView.getContext(), audioMessage.getMessageId());
        mPlayButton.setPath(path);
        LocalCacheUtils.downloadFileAsync(audioMessage.getFileUrl(), path);
      }
    }
  }
}
