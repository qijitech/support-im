package support.im.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import butterknife.ButterKnife;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.sj.emoji.EmojiBean;
import java.util.List;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;
import support.im.Injection;
import support.im.R;
import support.im.emoticons.ChatsUtils;
import support.im.emoticons.Constants;
import support.im.emoticons.SupportImFuncView;
import support.ui.SupportFragment;
import support.ui.adapters.EasyRecyclerAdapter;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsFragment extends SupportFragment
    implements ChatsContract.View, FuncLayout.OnFuncKeyBoardListener {

  private SupportEmoticonsKeyBoard mEmoticonsKeyBoard;
  RecyclerView mRecyclerView;
  LinearLayoutManager mLayoutManager;
  EasyRecyclerAdapter mAdapter;

  ChatsContract.Presenter mPresenter;

  EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
    @Override public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
      if (isDelBtn) {
        ChatsUtils.delClick(mEmoticonsKeyBoard.getEtChat());
      } else {
        if (o == null) {
          return;
        }
        if (actionType == Constants.EMOTICON_CLICK_BIGIMAGE) {
          if (o instanceof EmoticonEntity) {
            OnSendImage(((EmoticonEntity) o).getIconUri());
          }
        } else {
          String content = null;
          if (o instanceof EmojiBean) {
            content = ((EmojiBean) o).emoji;
          } else if (o instanceof EmoticonEntity) {
            content = ((EmoticonEntity) o).getContent();
          }

          if (TextUtils.isEmpty(content)) {
            return;
          }
          int index = mEmoticonsKeyBoard.getEtChat().getSelectionStart();
          Editable editable = mEmoticonsKeyBoard.getEtChat().getText();
          editable.insert(index, content);
        }
      }
    }
  };

  public static ChatsFragment create() {
    return new ChatsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.chats;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.bind(AVIMMessage.class, ChatsViewHolder.class);
    mAdapter.viewHolderFactory(new ChatsViewHolderFactory(getContext()));
    mLayoutManager = new LinearLayoutManager(getContext());
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mAdapter = null;
    mLayoutManager = null;
  }

  @Override public void onResume() {
    super.onResume();
  }

  public void setConversation(AVIMConversation avimConversation) {
    new ChatsPresenter(Injection.provideChatsRepository(), this, avimConversation);
    mPresenter.start();
  }

  public void shouldShowDisplayName(boolean shouldShow) {

  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mEmoticonsKeyBoard = ButterKnife.findById(view, R.id.emoticons_key_board);
    mRecyclerView = ButterKnife.findById(view, android.R.id.list);
    setupEmoticonsKeyBoardBar();
    setupRecyclerView();
  }

  private void setupRecyclerView() {
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(mLayoutManager);
  }

  private void setupEmoticonsKeyBoardBar() {
    ChatsUtils.initEmoticonsEditText(mEmoticonsKeyBoard.getEtChat());
    mEmoticonsKeyBoard.setAdapter(ChatsUtils.getCommonAdapter(getContext(), emoticonClickListener));
    mEmoticonsKeyBoard.addOnFuncKeyBoardListener(this);
    mEmoticonsKeyBoard.addFuncView(new SupportImFuncView(getContext()));

    mEmoticonsKeyBoard.getEtChat()
        .setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
          @Override public void onSizeChanged(int w, int h, int oldw, int oldh) {
            scrollToBottom();
          }
        });
    mEmoticonsKeyBoard.getBtnSend().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mPresenter.sendTextMessage(mEmoticonsKeyBoard.getEtChat().getText().toString());
        mEmoticonsKeyBoard.getEtChat().setText("");
      }
    });
    //mEmoticonsKeyBoard.getEmoticonsToolBarView().addFixedToolItemView();
  }

  private void OnSendImage(String image) {
    if (!TextUtils.isEmpty(image)) {
      mPresenter.sendTextMessage("[img]" + image);
    }
  }

  private void scrollToBottom() {
    mLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
  }

  @Override public void OnFuncPop(int i) {
    scrollToBottom();
  }

  @Override public void OnFuncClose() {
  }

  ///////////////// ChatsContact View

  @Override public void setLoadingIndicator(boolean active) {

  }

  @Override public void notifyItemInserted(AVIMTypedMessage message) {
    mAdapter.add(message);
    scrollToBottom();
  }

  @Override public void notifyDataSetChanged() {
    mAdapter.notifyDataSetChanged();
  }

  @Override public void showMessages(List<AVIMMessage> messages) {
    mAdapter.addAll(messages);
    scrollToBottom();
  }

  @Override public void showNoMessages() {

  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void onDataNotAvailable(String error, AVException exception) {

  }

  @Override public void setPresenter(ChatsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}
