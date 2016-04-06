package com.taobao.openimui.imcore;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.IYWConversationListener;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationBody;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.taobao.openimui.sample.LoginSampleHelper;

import java.util.List;

/**
 * 会话相关的数据操作
 * 相关接口和对象说明：
 * IYWConversationService是会话的操作入口，所有会话相关都通过此接口来进行访问
 * YWConversation，代表一个会话的实例
 * @author zhaoxu
 *
 */
public class ConversationSampleHelper {
	
	/**
	 * 会话相关的操作统一通过IYWConversationService接口来进行操作
	 * @return
	 */
	public IYWConversationService getConversationService(){
		final YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
		return imKit.getConversationService();
	}
	
	/**
	 * 获取当前所有的会话，无序
	 * @return
	 */
	public List<YWConversation> getAllConversations(){
		return getConversationService().getConversationList();
	}
	
	/**
	 * 根据用户ID，获取最新的一条消息
	 * @param userId
	 * @return
	 */
	public YWConversation getConversation(String userId){
		IYWConversationService conversationService = getConversationService();
		//根据用户ID，获取指定的会话
		return conversationService.getConversationByUserId(userId);
	}
	
	/**
	 * 获取某个联系人的最新一条消息
	 * @param userId
	 * @return
	 */
	public YWMessage getLatestMessage(String userId){
		YWConversation conversation = getConversation(userId);
		if (conversation != null){
			return conversation.getLastestMessage();
		}
		return null;
	}
	
	/**
	 * 总的未读消息数
	 * @return
	 */
	public int getTotalUnreadCount(){
		return getConversationService().getAllUnreadCount();
	}
	
	/**
	 * 指定会话的未读数
	 * @param userId
	 * @return
	 */
	public int getUnreadCount(String userId){
		YWConversation conversation = getConversation(userId);
		if (conversation != null){
			return conversation.getUnreadCount();
		}
		return 0;
	}
	
	/**
	 * 指定会话的最新一条消息
	 * @param userId
	 * @return
	 */
	public long getLatestMessageTime(String userId){
		YWMessage msg = getLatestMessage(userId);
		if (msg != null ){
			//单位为毫秒
			return msg.getTimeInMillisecond();
		}
		return 0;
	}
	
	/**
	 * 从会话中获取当前聊天的用户ID
	 * @param conversation
	 * @return
	 */
	public String getUserIdFromConversation(YWConversation conversation){
		if (conversation == null){
			return "";
		}

		if (conversation.getConversationType() == YWConversationType.P2P){
			YWP2PConversationBody conversationBody = (YWP2PConversationBody)conversation.getConversationBody();
			IYWContact contact = conversationBody.getContact();
			return contact.getUserId();
		}
		return "";
	}
	
	/**
	 * 从会话获取群聊ID
	 * @param conversation
	 * @return
	 */
	public long getTribeIdFromConversation(YWConversation conversation){
		if (conversation == null){
			return 0;
		}

		//群聊会话才支持
		if (conversation.getConversationType() == YWConversationType.Tribe){
			YWTribeConversationBody conversationBody = (YWTribeConversationBody)conversation.getConversationBody();
			YWTribe tribe = conversationBody.getTribe();
			return tribe.getTribeId();
		}
		return 0;
	}

    /**
     * 创建跨appKey会话
     * @param userId 聊天对象id
     * @param targetAppkey 聊天对象appKey
     * @return 会话对象
     */
    public YWConversation getCrossAppConversation(String userId, String targetAppkey){
        IYWConversationService service = getConversationService();
        YWConversation conversation = service.getConversationByUserId(userId, targetAppkey);
        if (conversation == null) {
            IYWContact contact = YWContactFactory.createAPPContact(userId, targetAppkey);
            conversation = service.getConversationCreater().createConversationIfNotExist(contact);
        }
        return conversation;
    }

    /**
     * 创建客服会话
     * @param userId   顾客id
     * @param groupId  客服分组id
     * @return
     */
    public YWConversation getEServiceConversation(String userId, int groupId){
        IYWConversationService service = getConversationService();
        EServiceContact contact = new EServiceContact(userId, groupId);
        YWConversation conversation  = service.getConversation(contact);
        if (conversation == null) {
            conversation = service.getConversationCreater().createConversation(contact);
        }
        return conversation;
    }


	private IYWConversationListener conversationListener = new IYWConversationListener() {
		@Override
		public void onItemUpdated() {

			List<YWConversation> conversations = getAllConversations();

			//这里进行自定义排序处理
			if (conversations != null && !conversations.isEmpty()){

			}

			//或者移除conversation
			//getConversationService().deleteConversation(YWConversation);

			//通知更新
			LoginSampleHelper.getInstance().getIMKit().getContactService().notifyContactProfileUpdate();
		}
	};


	/**
	 * 添加会话列表更改的监听
	 */
	public void initConversationListListener(){
		getConversationService().removeConversationListener(conversationListener);
		getConversationService().addConversationListener(conversationListener);
	}

    public void removeConversation(YWConversation conversation){
        getConversationService().deleteConversation(conversation);
    }
}
