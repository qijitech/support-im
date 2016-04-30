package support.im.chats;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.sj.emoji.EmojiBean;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;
import support.im.R;
import support.im.emoticons.ChatsUtils;
import support.im.emoticons.Constants;
import support.im.emoticons.SupportImFuncView;
import support.ui.SupportFragment;
import support.ui.adapters.EasyRecyclerAdapter;

public class ChatsFragment extends SupportFragment implements FuncLayout.OnFuncKeyBoardListener {

  private SupportEmoticonsKeyBoard mEmoticonsKeyBoard;
  RecyclerView mRecyclerView;
  LinearLayoutManager mLayoutManager;
  EasyRecyclerAdapter mAdapter;

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
    mAdapter.bind(AVIMMessage.class, ChatsRightViewHolder.class);
    mLayoutManager = new LinearLayoutManager(getContext());
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mAdapter = null;
    mLayoutManager = null;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mEmoticonsKeyBoard = ButterKnife.findById(view, R.id.emoticons_key_board);
    mRecyclerView = ButterKnife.findById(view, android.R.id.list);
    setupEmoticonsKeyBoardBar();
    setupRecyclerView();
  }

  private void loadChats() {
    if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
      //mPresenter.start();
    } else {
      Nammu.askForPermission(getActivity(), Manifest.permission.READ_CONTACTS,
          new PermissionCallback() {
            @Override public void permissionGranted() {
              //mPresenter.start();
            }

            @Override public void permissionRefused() {
              getActivity().finish();
            }
          });
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    mEmoticonsKeyBoard.getEtChat().setOnSizeChangedListener(
        new EmoticonsEditText.OnSizeChangedListener() {
      @Override public void onSizeChanged(int w, int h, int oldw, int oldh) {
        scrollToBottom();
      }
    });
    mEmoticonsKeyBoard.getBtnSend().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        OnSendBtnClick(mEmoticonsKeyBoard.getEtChat().getText().toString());
        mEmoticonsKeyBoard.getEtChat().setText("");
      }
    });
    //mEmoticonsKeyBoard.getEmoticonsToolBarView().addFixedToolItemView();
  }


  private void OnSendBtnClick(String msg) {
    if (!TextUtils.isEmpty(msg)) {
      AVIMMessage avimMessage = new AVIMMessage();
      mAdapter.add(avimMessage);
      scrollToBottom();
    }
  }

  private void OnSendImage(String image) {
    if (!TextUtils.isEmpty(image)) {
      OnSendBtnClick("[img]" + image);
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

}
