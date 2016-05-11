package support.im.chats;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.sj.emoji.EmojiBean;
import de.greenrobot.event.EventBus;
import java.io.File;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
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
import support.im.utilities.PathUtils;
import support.im.utilities.ProviderPathUtils;
import support.ui.app.SupportFragment;
import support.ui.content.ContentPresenter;
import support.ui.content.ReflectionContentPresenterFactory;
import support.ui.content.RequiresContent;
import support.ui.utilities.ToastUtils;

@RequiresContent(loadView = ChatsLoadingView.class, emptyView = ChatsEmptyView.class)
public abstract class BaseChatsFragment extends SupportFragment implements FuncLayout.OnFuncKeyBoardListener,
    EmoticonClickListener {

  private static final int TAKE_IMAGE_REQUEST = 1;
  private static final int PICKER_IMAGE_REQUEST = 2;
  private static final int PICKER_IMAGE_KITKAT_REQUEST = 3;

  protected String localCameraPath;

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
    localCameraPath = PathUtils.getPicturePathByCurrentTime(getContext());
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

  public void onEvent(FuncViewClickEvent event) {
    final FuncItem funcItem = event.mFuncItem;
    switch (funcItem.tag) {
      case FuncItem.TAG_CAMERA:
        if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
          takeImage();
        } else {
          Nammu.askForPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionCallback() {
            @Override public void permissionGranted() {
              takeImage();
            }
            @Override public void permissionRefused() {
            }
          });
        }
        break;
      case FuncItem.TAG_GALLERY:
        if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
          pickerImage();
        } else {
          Nammu.askForPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionCallback() {
            @Override public void permissionGranted() {
              pickerImage();
            }
            @Override public void permissionRefused() {
            }
          });
        }
        break;
      case FuncItem.TAG_LOCATION:
        break;
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT) @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    switch (requestCode) {
      case PICKER_IMAGE_REQUEST:
      case PICKER_IMAGE_KITKAT_REQUEST:
        if (data == null) {
          ToastUtils.toast("return intent is null");
          return;
        }
        Uri uri;
        if (requestCode == PICKER_IMAGE_REQUEST) {
          uri = data.getData();
        } else {
          //for Android 4.4
          uri = data.getData();
        }
        String localSelectPath = ProviderPathUtils.getPath(getActivity(), uri);
        hideKeyBoard();
        sendImage(localSelectPath);
        break;
      case TAKE_IMAGE_REQUEST:
        hideKeyBoard();
        sendImage(localCameraPath);
        break;
    }
  }

  public void takeImage() {
    Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    Uri imageUri = Uri.fromFile(new File(localCameraPath));
    takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, TAKE_IMAGE_REQUEST);
    }
  }

  public void pickerImage() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(
          Intent.createChooser(intent, getResources().getString(
              R.string.si_activity_select_picture)), PICKER_IMAGE_REQUEST);
    } else {
      Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      intent.setType("image/*");
      startActivityForResult(intent, PICKER_IMAGE_KITKAT_REQUEST);
    }
  }

  protected abstract void sendImage(String imagePath);
  protected abstract void onSendImage(String imageUri);
  protected abstract void onSendBtnClick(String message);

}
