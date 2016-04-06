package com.taobao.openimui.imcore;

import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.tribe.YWTribeCreationParam;
import com.taobao.openimui.sample.LoginSampleHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 群聊相关的API调用示例（部分API，主要是onSucces需要参数转换的一些API） 其它API的说明请参见官方的文档
 * 
 * @author zhaoxu
 * 
 */
public class TribeSampleHelper {

	/**
	 * 请求回调
	 * 
	 * @author zhaoxu
	 * 
	 */
	private static abstract class MyCallback implements IWxCallback {

		@Override
		public void onError(int arg0, String arg1) {
            YWLog.e("TribeSampleHelper", "code=" + arg0 + " errInfo=" + arg1);
        }

		@Override
		public void onProgress(int arg0) {

        }
	}

	public static IYWTribeService getTribeService() {
		final YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
		if (imKit != null) {
			return imKit.getTribeService();
		}
		return null;
	}

	/**
	 *
	 * @param tribeType，具体见YWTribeType
	 *
	 */
	public static void createTribe_Sample(YWTribeType tribeType){
		final IYWTribeService tribeService = getTribeService();
		if (tribeService == null) {
			return;
		}

		YWTribeCreationParam tribeCreationParam = new YWTribeCreationParam();
		tribeCreationParam.setTribeName("tribeName");
		tribeCreationParam.setNotice("notice");
		tribeCreationParam.setTribeType(tribeType);

		if (tribeType == YWTribeType.CHATTING_GROUP){
			//讨论组需要指定用户
			final List<String> userList = new ArrayList<String>();
			final YWIMCore core = LoginSampleHelper.getInstance().getIMKit()
					.getIMCore();

			userList.add(core.getLoginUserId());// 当前登录的用户ID，这个必须要传
			userList.add("user2");
			tribeCreationParam.setUsers(userList);
		}

		tribeService.createTribe(new MyCallback() {
			@Override
			public void onSuccess(Object... result) {
				// 返回值为刚刚成功创建的群
				YWTribe tribe = (YWTribe) result[0];
				tribe.getTribeId();// 群ID，用于唯一标识一个群
			}
		}, tribeCreationParam);
	}

	/**
	 * 从服务器获取当前用户所在的所有群
	 */
	public static void getAllTribeFromServer_Sample() {
		final IYWTribeService tribeService = getTribeService();
		if (tribeService == null) {
			return;
		}
		tribeService.getAllTribesFromServer(new MyCallback() {

			@Override
			public void onSuccess(Object... arg0) {
				// 返回值为列表
				@SuppressWarnings("unchecked")
				List<YWTribe> tribeList = (List<YWTribe>) arg0[0];
				tribeList.size();
			}
		});
	}

	/**
	 * 从服务器获取单个群信息
	 * 
	 * @param tid
	 *            群ID
	 */
	public static void getTribeFromServer_Sample(long tid) {
		final IYWTribeService tribeService = getTribeService();
		if (tribeService == null) {
			return;
		}
		tribeService.getTribeFromServer(new MyCallback() {

			@Override
			public void onSuccess(Object... arg0) {
                YWTribe tribe = (YWTribe) arg0[0];
                tribe.getTribeId();
            }
		}, tid);
	}

	/**
	 * 获取指定群的成员列表
	 * 
	 * @param cb
	 *            回调接口, 其中 返回值为List<YWTribeMember>
	 * @param tid
	 *            群id
	 */
	public static void getMembersFromServer_Sample(long tid) {
		final IYWTribeService tribeService = getTribeService();
		if (tribeService == null) {
			return;
		}
		tribeService.getMembersFromServer(new MyCallback() {

			@Override
			public void onSuccess(Object... arg0) {
                @SuppressWarnings("unchecked")
                List<YWTribeMember> memberList = (List<YWTribeMember>) arg0[0];
                memberList.size();
            }
		}, tid);
	}

	/**
	 * 退出指定的群
	 * 
	 * @param tid
	 */
	public static void exitFromTribe_Sample(long tid) {
		final IYWTribeService tribeService = getTribeService();
		if (tribeService == null) {
			return;
		}
		tribeService.exitFromTribe(new MyCallback() {

			@Override
			public void onSuccess(Object... arg0) {
                // 成功就onSuccess，否则会调用onError
            }
		}, tid);
	}
}
