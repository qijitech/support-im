package com.taobao.openimui.imcore;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ListView;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.channel.util.WxLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWMessageListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshBase;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshListView;
import com.alibaba.mobileim.kit.chat.presenter.ChattingDetailPresenter;
import com.alibaba.openIMUIDemo.R;

import java.util.List;

/**
 * 聊天窗口页面示例代码，该文件主要是为了介绍怎样初始化消息列表和新消息达到后怎样刷新页面
 * 为了简单起见，这里只是创建了一个adapter，并没有实现，开发者需要根据自己的需求实现自己的adapter
 */
public class ChattingActivity extends Activity {

    public static final String CVS_TYPE = "cvsType"; //会话类型
    public static final String CVS_ID = "cvsId"; //会话id
    public static final String TARGET_ID = "targetId";  //聊天对象id
    public static final String APP_KEY = "appKey";  //聊天对象appKey
    public static final String TRIBE_ID = "tribeId"; //群id

    private YWIMCore mIMCore;

    private IYWConversationService mConversationService;
    private YWConversation mConversation;
    private List<YWMessage> mMessageList;
    private ChattingAdapter mAdapter;

    private YWPullToRefreshListView mPullToRefreshListView;
    private ListView mListView;

    private int mCvsType;
    private String mCvsId;
    private String mTargetId;
    private String mAppKey;
    private long mTribeId;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        init();
    }

    private void initData() {
        mCvsType = getIntent().getIntExtra(CVS_TYPE,
                YWConversationType.P2P.getValue());

        if (mCvsType == YWConversationType.P2P.getValue()) { //如果是单聊会话需要把聊天对象id和聊天对象appKey传进来
            mTargetId = getIntent().getStringExtra(TARGET_ID);

            mAppKey = getIntent().getStringExtra(APP_KEY);
            if (TextUtils.isEmpty(mAppKey)) {
                mAppKey = mIMCore.getAppKey();
            }
        } else if (mCvsType == YWConversationType.Tribe.getValue()) { //如果是群聊会话需要把群id传进来
            mTribeId = getIntent().getLongExtra(TRIBE_ID, 0);
        } else if (mCvsType == YWConversationType.Custom.getValue()){ //如果是自定义会话需要把会话id传进来
            mCvsId = getIntent().getStringExtra(CVS_ID);
        }
    }

    private void init(){
        mPullToRefreshListView = (YWPullToRefreshListView) findViewById(R.id.conversation_list);
        mPullToRefreshListView.setMode(YWPullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        mPullToRefreshListView.setShowIndicator(false);
        mPullToRefreshListView.setDisableScrollingWhileRefreshing(false);
        mPullToRefreshListView.setRefreshingLabel("同步群成员列表");
        mPullToRefreshListView.setReleaseLabel("松开同步群成员列表");
        mPullToRefreshListView.setDisableRefresh(false);
        //下拉刷新时加载更多消息
        mPullToRefreshListView.setOnRefreshListener(new YWPullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreMessages();
            }
        });
        mListView = mPullToRefreshListView.getRefreshableView();

        initData();

        mIMCore = InitSample.getInstance().getIMCore();
        mConversationService = mIMCore.getConversationService();

        //根据会话类型初始化mConversation
        if (mCvsType == YWConversationType.P2P.getValue()){
            mConversation = mConversationService.getConversationByUserId(mTargetId, mAppKey);
            if (mConversation == null){ //这里必须判空
                IYWContact contact = YWContactFactory.createAPPContact(mTargetId, mAppKey);
                mConversation = mConversationService.getConversationCreater().createConversationIfNotExist(contact);
            }
        } else if (mCvsType == YWConversationType.Tribe.getValue()){
            mConversation = mConversationService.getTribeConversation(mTribeId);
            if (mConversation == null){ //这里必须判空
                mConversation = mConversationService.getConversationCreater().createTribeConversation(mTribeId);
            }
        } else if (mCvsType == YWConversationType.Custom.getValue()){
            mConversation = mConversationService.getConversationByConversationId(mCvsId);
            if (mConversation == null){ //这里必须判空
                mConversation = mConversationService.getConversationCreater().createCustomConversation(mCvsId, YWConversationType.Custom);
            }
        }

        //获取消息列表
        mMessageList = mConversation.getMessageLoader().loadMessage(20, null);
        //初始化消息列表adapter
        mAdapter = new ChattingAdapter(mMessageList);
        mListView.setAdapter(mAdapter);
        //添加新消息到达监听,监听到有新消息到达的时候或者消息类别有变更的时候应该更新adapter
        mConversation.getMessageLoader().addMessageListener(mMessageListener);
    }

    private void loadMoreMessages(){
        mConversation.getMessageLoader().loadMoreMessage(new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshListView.onRefreshComplete(false, true);
                    }
                });
            }

            @Override
            public void onError(int code, String info) {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshListView.onRefreshComplete(false, false);
                    }
                });
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    IYWMessageListener mMessageListener = new IYWMessageListener() {
        @Override
        public void onItemUpdated() {  //消息列表变更，例如删除一条消息，修改消息状态，加载更多消息等等
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChangedWithAsyncLoad();
                }
            });
        }

        @Override
        public void onItemComing() { //收到新消息
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChangedWithAsyncLoad();
                }
            });
        }

        @Override
        public void onInputStatus(byte status) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //请务必在销毁activity的时候移除该监听，否则可能会导致该监听被多次添加，造成内存泄露
        if (mConversation != null){
            mConversation.getMessageLoader().removeMessageListener(mMessageListener);
        }
    }
}
