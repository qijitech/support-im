package support.im.leanclound.contacts;

import android.content.Context;
import android.support.v4.os.AsyncTaskCompat;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import java.util.List;
import support.im.R;
import support.im.data.SupportUser;
import support.im.data.User;
import support.im.service.PushManager;
import support.im.utilities.SimpleNetworkTask;
import support.im.utilities.SupportLog;
import support.ui.app.SupportApp;
import support.ui.utilities.ToastUtils;

public class AddRequestManager {

  private static AddRequestManager addRequestManager;

  /**
   * 用户端未读的邀请消息的数量
   */
  private int unreadAddRequestsCount = 0;

  public static synchronized AddRequestManager getInstance() {
    if (addRequestManager == null) {
      addRequestManager = new AddRequestManager();
    }
    return addRequestManager;
  }

  public AddRequestManager() {
  }

  /**
   * 是否有未读的消息
   */
  public boolean hasUnreadRequests() {
    return unreadAddRequestsCount > 0;
  }

  public int getUnreadAddRequestsCount() {
    return unreadAddRequestsCount;
  }

  /**
   * 推送过来时自增
   */
  public int unreadRequestsIncrement() {
    return ++unreadAddRequestsCount;
  }

  /**
   * 从 server 获取未读消息的数量
   */
  public void countUnreadRequests(final CountCallback countCallback) {
    AVQuery<AddRequest> addRequestAVQuery = AVObject.getQuery(AddRequest.class);
    addRequestAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
    addRequestAVQuery.whereEqualTo(AddRequest.TO_USER, SupportUser.getCurrentUser());
    addRequestAVQuery.whereEqualTo(AddRequest.IS_READ, false);
    addRequestAVQuery.countInBackground(new CountCallback() {
      @Override public void done(int i, AVException e) {
        unreadAddRequestsCount = i;
        if (null != countCallback) {
          countCallback.done(i, e);
        }
      }
    });
  }

  /**
   * 标记消息为已读，标记完后会刷新未读消息数量
   */
  public void markAddRequestsRead(List<AddRequest> addRequestList) {
    if (addRequestList != null) {
      for (AddRequest request : addRequestList) {
        request.put(AddRequest.IS_READ, true);
      }
      AVObject.saveAllInBackground(addRequestList, new SaveCallback() {
        @Override public void done(AVException e) {
          if (e == null) {
            countUnreadRequests(null);
          }
        }
      });
    }
  }

  public void findAddRequests(int skip, int limit, FindCallback<AddRequest> findCallback) {
    SupportUser user = SupportUser.getCurrentUser();
    AVQuery<AddRequest> q = AVObject.getQuery(AddRequest.class);
    q.include(AddRequest.FROM_USER);
    q.skip(skip);
    q.limit(limit);
    q.whereEqualTo(AddRequest.TO_USER, user);
    q.orderByDescending(AVObject.CREATED_AT);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(findCallback);
  }

  public void agreeAddRequest(final AddRequest addRequest, final SaveCallback saveCallback) {
    addFriend(addRequest.getFromUser().getObjectId(), new SaveCallback() {
      @Override public void done(AVException e) {
        if (e != null) {
          if (e.getCode() == AVException.DUPLICATE_VALUE) {
            addRequest.setStatus(AddRequest.STATUS_DONE);
            addRequest.saveInBackground(saveCallback);
          } else {
            saveCallback.done(e);
          }
        } else {
          addRequest.setStatus(AddRequest.STATUS_DONE);
          addRequest.saveInBackground(saveCallback);
        }
      }
    });
  }

  public static void addFriend(String friendId, final SaveCallback saveCallback) {
    SupportUser user = SupportUser.getCurrentUser();
    user.followInBackground(friendId, new FollowCallback() {
      @Override
      public void done(AVObject object, AVException e) {
        if (saveCallback != null) {
          saveCallback.done(e);
        }
      }
    });
  }

  private void createAddRequest(SupportUser toUser) throws Exception {
    SupportUser curUser = SupportUser.getCurrentUser();
    AVQuery<AddRequest> q = AVObject.getQuery(AddRequest.class);
    q.whereEqualTo(AddRequest.FROM_USER, curUser);
    q.whereEqualTo(AddRequest.TO_USER, toUser);
    q.whereEqualTo(AddRequest.STATUS, AddRequest.STATUS_WAIT);
    int count = 0;
    try {
      count = q.count();
    } catch (AVException e) {
      SupportLog.logException(e);
      if (e.getCode() == AVException.OBJECT_NOT_FOUND) {
        count = 0;
      } else {
        throw e;
      }
    }
    if (count > 0) {
      // 抛出异常，然后提示用户
      throw new IllegalStateException(SupportApp.getInstance().getString(R.string.support_im_add_request_sent));
    } else {
      AddRequest add = new AddRequest();
      add.setFromUser(curUser);
      add.setToUser(toUser);
      add.setStatus(AddRequest.STATUS_WAIT);
      add.setIsRead(false);
      add.save();
    }
  }

  public void createAddRequestInBackground(final Context context, final User user) {
    AsyncTaskCompat.executeParallel(new SimpleNetworkTask(context) {
      @Override protected void doInBackground() throws Exception {
        SupportUser supportUser = new SupportUser();
        supportUser.setObjectId(user.getObjectId());
        createAddRequest(supportUser);
      }
      @Override protected void onSucceed() {
        PushManager.getInstance().pushMessage(user.getObjectId(),
            context.getString(R.string.support_im_add_request_invitation),
                context.getString(R.string.support_im_add_request_invitation_action));
        ToastUtils.toast(R.string.support_im_add_request_send_succeed);
      }
    });
  }
}
