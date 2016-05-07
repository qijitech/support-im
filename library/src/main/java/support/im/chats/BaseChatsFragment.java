package support.im.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.sj.emoji.EmojiBean;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;
import support.im.R;
import support.im.chats.viewholder.ChatsLoadingView;
import support.im.chats.viewholder.ChatsViewHolder;
import support.im.chats.viewholder.ChatsViewHolderFactory;
import support.im.emoticons.ChatsUtils;
import support.im.emoticons.Constants;
import support.im.emoticons.SupportImFuncView;
import support.ui.SupportFragment;
import support.ui.content.ContentPresenter;
import support.ui.content.ReflectionContentPresenterFactory;
import support.ui.content.RequiresContent;

@RequiresContent(loadView = ChatsLoadingView.class, emptyView = ChatsEmptyView.class)
public abstract class BaseChatsFragment extends SupportFragment implements FuncLayout.OnFuncKeyBoardListener,
    EmoticonClickListener {

  ReflectionContentPresenterFactory factory =
      ReflectionContentPresenterFactory.fromViewClass(getClass());

  protected ContentPresenter mContentPresenter;
  protected SupportEmoticonsKeyBoard mEmoticonsKeyBoard;
  protected FrameLayout mContainer;
  protected RecyclerView mRecyclerView;
  protected LinearLayoutManager mLayoutManager;
  protected ChatsAdapter mAdapter;

  @Override protected int getFragmentLayout() {
    return R.layout.chats;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter = new ChatsAdapter(getContext());
    mAdapter.bind(AVIMMessage.class, ChatsViewHolder.class);
    mAdapter.viewHolderFactory(new ChatsViewHolderFactory(getContext()));
    mLayoutManager = new LinearLayoutManager(getContext());
    mContentPresenter = factory.createContentPresenter();
    mContentPresenter.onCreate(getContext());
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mContentPresenter.onDestroy();
    mAdapter = null;
    mLayoutManager = null;
    mContentPresenter = null;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mEmoticonsKeyBoard = ButterKnife.findById(view, R.id.emoticons_key_board);
    mRecyclerView = ButterKnife.findById(view, R.id.support_ui_recycler_view);
    mContainer = ButterKnife.findById(view, R.id.support_ui_content_container);
    setupEmoticonsKeyBoardBar();
    setupRecyclerView();

    mContentPresenter.attachContainer(mContainer);
    mContentPresenter.attachContentView(mRecyclerView);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mRecyclerView = null;
    mEmoticonsKeyBoard = null;
    mContainer = null;
  }

  private void setupRecyclerView() {
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(mLayoutManager);
  }

  private void setupEmoticonsKeyBoardBar() {
    ChatsUtils.initEmoticonsEditText(mEmoticonsKeyBoard.getEtChat());
    mEmoticonsKeyBoard.setAdapter(ChatsUtils.getCommonAdapter(getContext(), this));
    mEmoticonsKeyBoard.addOnFuncKeyBoardListener(this);
    mEmoticonsKeyBoard.addFuncView(new SupportImFuncView(getContext()));

    mEmoticonsKeyBoard.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
      @Override public void onSizeChanged(int w, int h, int oldw, int oldh) {
        scrollToBottom();
      }
    });
    mEmoticonsKeyBoard.getBtnSend().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSendBtnClick(mEmoticonsKeyBoard.getEtChat().getText().toString());
        mEmoticonsKeyBoard.getEtChat().setText("");
      }
    });
    //mEmoticonsKeyBoard.getEmoticonsToolBarView().addFixedToolItemView();
  }

  @Override public void OnFuncPop(int position) {
    scrollToBottom();
  }

  @Override public void OnFuncClose() {
  }

  /**
   * 表情点进
   */
  @Override public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
    if (isDelBtn) {
      ChatsUtils.delClick(mEmoticonsKeyBoard.getEtChat());
      return;
    }

    if (o == null) {
      return;
    }

    if (actionType == Constants.EMOTICON_CLICK_BIGIMAGE && (o instanceof EmoticonEntity)) {
      onSendImage(((EmoticonEntity) o).getIconUri());
      return;
    }

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

  protected void scrollToBottom() {
    mLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
  }

  protected abstract void onSendImage(String imageUri);
  protected abstract void onSendBtnClick(String message);

}
