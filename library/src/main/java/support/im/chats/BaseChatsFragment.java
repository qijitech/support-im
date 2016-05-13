package support.im.chats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.fuck_boilerplate.rx_paparazzo.RxPaparazzo;
import com.fuck_boilerplate.rx_paparazzo.entities.Response;
import com.sj.emoji.EmojiBean;
import de.greenrobot.event.EventBus;
import rx.functions.Action1;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;
import support.im.R;
import support.im.chats.viewholder.ChatsCommonViewHolder;
import support.im.chats.viewholder.ChatsLoadingView;
import support.im.chats.viewholder.ChatsViewHolderFactory;
import support.im.emoticons.ChatsUtils;
import support.im.emoticons.Constants;
import support.im.emoticons.FuncItem;
import support.im.emoticons.SupportImFuncView;
import support.im.events.FuncViewClickEvent;
import support.im.leanclound.event.ImTypeMessageEvent;
import support.im.location.Location;
import support.im.location.LocationPickerActivity;
import support.ui.app.SupportFragment;
import support.ui.content.ContentPresenter;
import support.ui.content.ReflectionContentPresenterFactory;
import support.ui.content.RequiresContent;

@RequiresContent(loadView = ChatsLoadingView.class, emptyView = ChatsEmptyView.class)
public abstract class BaseChatsFragment extends SupportFragment implements FuncLayout.OnFuncKeyBoardListener,
    EmoticonClickListener {

  private static final int REQUEST_CODE_LOCATION = 1000;

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
    mAdapter.bind(AVIMTypedMessage.class, ChatsCommonViewHolder.class);
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

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
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

  @Override public void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
  }

  @Override public void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
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
      hideKeyBoard();
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

  protected void hideKeyBoard() {
    mEmoticonsKeyBoard.reset();
  }

  /**
   * 处理推送过来的消息
   * 同理，避免无效消息，此处加了 conversation id 判断
   */
  public void onEvent(ImTypeMessageEvent event) {
    mAdapter.add(event.message);
    scrollToBottom();
  }

  @SuppressWarnings("unused") public void onEvent(FuncViewClickEvent event) {
    final FuncItem funcItem = event.mFuncItem;
    switch (funcItem.tag) {
      case FuncItem.TAG_CAMERA:
        RxPaparazzo.takeImage(this)
            .usingCamera()
            .subscribe(new Action1<Response<BaseChatsFragment, String>>() {
              @Override
              public void call(Response<BaseChatsFragment, String> response) {
                if (response.resultCode() == Activity.RESULT_OK) {
                  hideKeyBoard();
                  response.targetUI().sendImage(response.data());
                }
              }
            });
        break;
      case FuncItem.TAG_GALLERY:
        RxPaparazzo.takeImage(this)
            .usingGallery()
            .subscribe(new Action1<Response<BaseChatsFragment, String>>() {
              @Override public void call(Response<BaseChatsFragment, String> response) {
                if (response.resultCode() == Activity.RESULT_OK) {
                  hideKeyBoard();
                  response.targetUI().sendImage(response.data());
                }
              }
            });
        break;
      case FuncItem.TAG_LOCATION:
        startActivityForResult(new Intent(getContext(), LocationPickerActivity.class), REQUEST_CODE_LOCATION);
        break;
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case REQUEST_CODE_LOCATION:
          Location location = data.getParcelableExtra(LocationPickerActivity.EXTRA_LOCATION);
          sendLocation(location);
          break;
      }
    }
  }

  protected abstract void sendLocation(Location location);
  protected abstract void sendImage(String imagePath);
  protected abstract void onSendImage(String imageUri);
  protected abstract void onSendBtnClick(String message);

}
