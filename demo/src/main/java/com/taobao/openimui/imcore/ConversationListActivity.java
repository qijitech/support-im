package com.taobao.openimui.imcore;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.IYWConversationListener;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshBase;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshListView;
import com.alibaba.openIMUIDemo.R;
import java.util.List;


/**
 * 会话列表页面，该文件是为了给使用IMCore集成的开发者提供示例代码，开发者可以在开发会话列表页面时可以参考该文件
 * 该文件主要是为了介绍怎样初始化消息列表和新消息达到后怎样刷新页面
 * 为了简单起见，这里只是创建了一个adapter，并没有实现，开发者需要根据自己的需求实现自己的adapter
 */
public class ConversationListActivity extends Activity {

    private YWIMCore mIMCore;
    private IYWConversationService mConversationService;
    private List<YWConversation> mConversationList;
    private ConversationListAdapter mAdapter;

    private YWPullToRefreshListView mPullToRefreshListView;
    private ListView mListView;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        init();
    }

    private void init(){
        mPullToRefreshListView = (YWPullToRefreshListView) findViewById(R.id.conversation_list);
        mPullToRefreshListView.setMode(YWPullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        mPullToRefreshListView.setShowIndicator(false);
        mPullToRefreshListView.setDisableScrollingWhileRefreshing(false);
        mPullToRefreshListView.setRefreshingLabel("同步群成员列表");
        mPullToRefreshListView.setReleaseLabel("松开同步群成员列表");
        mPullToRefreshListView.setDisableRefresh(false);
        mPullToRefreshListView.setOnRefreshListener(new YWPullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncRecentConversations();
            }
        });
        mListView = mPullToRefreshListView.getRefreshableView();

        mIMCore = InitSample.getInstance().getIMCore();
        mConversationService = mIMCore.getConversationService();
        //初始化最近联系人列表
        mConversationList = mConversationService.getConversationList();
        //初始化最近联系人adpter
        mAdapter = new ConversationListAdapter(mConversationList);
        //设置mListView的adapter
        mListView.setAdapter(mAdapter);

        //添加会话列表变更监听，收到该监听回调时更新adapter就可以刷新页面了
        mConversationService.addConversationListener(mConversationListener);
    }

    private void syncRecentConversations(){
        mConversationService.syncRecentConversations(new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChangedWithAsyncLoad();
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

    IYWConversationListener mConversationListener = new IYWConversationListener() {
        @Override
        public void onItemUpdated() {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChangedWithAsyncLoad();
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //请务必在该方法中移除会话监听，以免多次添加监听
        if (mConversationService != null){
            mConversationService.removeConversationListener(mConversationListener);
        }
    }
}
