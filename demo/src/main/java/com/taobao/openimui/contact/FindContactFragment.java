package com.taobao.openimui.contact;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.ui.contact.ContactsFragment;
import com.alibaba.mobileim.utility.ToastHelper;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.sample.LoginSampleHelper;

import java.util.ArrayList;
import java.util.List;

public  class FindContactFragment extends Fragment implements OnClickListener {

	private ProgressDialog mProgressView;
	private EditText searchKeywordEditText;
	private ImageView searchBtn;
	private AlertDialog dialog;
	private volatile boolean isStop;

	private Handler handler = new Handler();
    private View view;
    private boolean isFinishing;
	private Handler mHandler=new Handler();

	private String APPKEY;
	private String mUserId;
	private String TAG = FindContactFragment.class.getSimpleName();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserId = LoginSampleHelper.getInstance().getIMKit().getIMCore().getLoginUserId();
		if (TextUtils.isEmpty(mUserId)) {
			YWLog.i(TAG, "user not login");
		}
		APPKEY= LoginSampleHelper.getInstance().getIMKit().getIMCore().getAppKey();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        if(view!=null){
			ViewGroup parent = (ViewGroup)view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
            return view;
        }
        view = inflater.inflate(R.layout.demo_fragment_find_contact, null);
        init();
        return view;
    }
    private void initTitle(){

		RelativeLayout titleBar = (RelativeLayout) view.findViewById(R.id.title_bar);
		titleBar.setBackgroundColor(Color.parseColor("#00b4ff"));
		TextView leftButton = (TextView) view.findViewById(R.id.left_button);
		leftButton.setText("");
		leftButton.setTextColor(Color.WHITE);
		leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.demo_common_back_btn_white, 0, 0, 0);
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		TextView title = (TextView) view.findViewById(R.id.title_self_title);
		title.setTextColor(Color.WHITE);
		title.setText("添加");
		titleBar.setVisibility(View.VISIBLE);

    }

    private void init() {
        initTitle();
		initSearchView();

	}

	private void initSearchView() {
		searchKeywordEditText = (EditText) view.findViewById(R.id.aliwx_search_keyword);
		searchKeywordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		searchKeywordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId==KeyEvent.ACTION_DOWN||actionId==EditorInfo.IME_ACTION_DONE){
					String keyword = searchKeywordEditText.getText().toString();
					if (!TextUtils.isEmpty(keyword)) {
						searchContent(keyword);
						return true;
					}else{
						return false;
					}
				}
				return false;
			}
		});
		searchKeywordEditText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
					String keyword = searchKeywordEditText.getText().toString();
					if (!TextUtils.isEmpty(keyword)) {
						searchContent(keyword);
						return true;
					}else{
						return false;
					}
					}
				return false;
			}
		});
		searchBtn = (ImageView) view.findViewById(R.id.aliwx_search_btn);
		searchBtn.setOnClickListener(this);
		searchBtn.setEnabled(false);

		searchKeywordEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					searchBtn.setEnabled(true);
				} else {
					searchBtn.setEnabled(false);
				}

			}
		});
		searchKeywordEditText.requestFocus();
		showKeyBoard();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.aliwx_search_btn) {
			String keyword = searchKeywordEditText.getText().toString();
			if (!TextUtils.isEmpty(keyword)) {
				searchContent(keyword);
			}

		}

	}

	private void searchContent(String keyword) {
		if (YWChannel.getInstance().getNetWorkState().isNetWorkNull()) {
            ToastHelper.showToastMsg(this.getActivity(), this.getResources().getString(R.string.aliwx_net_null));
        } else {
            String key = keyword.replace(" ", "");
            String userId = key;
            ArrayList<String> userIds = new ArrayList<String>();
            userIds.add(userId);
            showProgress();
            IYWContactService contactService =getContactService();
            contactService.fetchUserProfile(userIds, APPKEY, new IWxCallback() {

                @Override
                public void onSuccess(final Object... result) {
                    if (result != null) {
                        List<YWProfileInfo> profileInfos = (List<YWProfileInfo>) (result[0]);
                        if (profileInfos == null || profileInfos.isEmpty()) {
                            handleResult((List) result[0]);
                            return;
                        }
                        YWProfileInfo mYWProfileInfo = profileInfos.get(0);
                        cancelProgress();

                         checkIfHasContact(mYWProfileInfo);
                        showSearchResult(mYWProfileInfo);
                    } else {
                        handleResult((List) result[0]);
                    }
                }

                @Override
                public void onError(int code, String info) {
                    handleResult(null);
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        }
	}


	private void showProgress() {
		if (mProgressView == null) {
			mProgressView = new ProgressDialog(this.getActivity());
			mProgressView.setMessage(getResources().getString(
					R.string.aliwx_search_friend_processing));
			mProgressView.setIndeterminate(true);
			mProgressView.setCancelable(true);
			mProgressView.setCanceledOnTouchOutside(false);
		}
		mProgressView.show();
	}

	private void cancelProgress() {
		if (mProgressView != null && mProgressView.isShowing()) {
			mProgressView.dismiss();
		}
	}

	private void handleResult(final List profileInfos) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (!isFinishing) {
					cancelProgress();
					if (profileInfos == null || profileInfos.isEmpty()) {
						if (isStop) {
							return;
						}
						if (dialog == null) {
							dialog = new YWAlertDialog.Builder(
									FindContactFragment.this.getActivity())
									.setTitle(R.string.aliwx_search_friend_not_found)
									.setMessage(
											R.string.aliwx_search_friend_not_found_message)
									.setPositiveButton(
											R.string.aliwx_confirm,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub

												}

											}).create();
						}
						dialog.show();
					}
				}
			}
		});

	}

	public boolean onBackPressed() {
        isFinishing=true;
		if (mProgressView != null && mProgressView.isShowing()) {
			mProgressView.dismiss();
			isStop = true;
			return false;
		}
		hideKeyBoard();
		getActivity().finish();
		return true;
	}

	private IParent getSuperParent(){
		IParent superParent = (IParent) getActivity();
		return superParent;
	}
	private IYWContactService getContactService(){
		IYWContactService contactService = LoginSampleHelper.getInstance().getIMKit().getContactService();
		return contactService;
	}
	//--------------------------[搜索到的联系人的展示]相关实现

	private List<IYWDBContact> contactsFromCache;
	public void showSearchResult(final YWProfileInfo lmYWProfileInfo){

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (lmYWProfileInfo == null || TextUtils.isEmpty(lmYWProfileInfo.userId)) {
					ToastHelper.showToastMsg(FindContactFragment.this.getActivity(), "服务开小差，建议您重试搜索");
					return;
				}
				IParent superParent = getSuperParent();
				superParent.setYWProfileInfo(lmYWProfileInfo);
				hideKeyBoard();
				superParent.addFragment(new ContactProfileFragment(), true);
			}
		});

	}




	private void checkIfHasContact(YWProfileInfo mYWProfileInfo){
		//修改hasContactAlready和contactsFromCache的Fragment生命周期缓存
		IParent superParent = getSuperParent();
		if(superParent!=null){
			contactsFromCache =getContactService().getContactsFromCache();
			for(IYWDBContact contact:contactsFromCache){
				if(contact.getUserId().equals(mYWProfileInfo.userId)){
					superParent.setHasContactAlready(true);
					return;
				}
			}
			superParent.setHasContactAlready(false);
		}

	}
	private void showKeyBoard() {
		View view = this.getActivity().getCurrentFocus();
		if (view != null) {
			((InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
					.showSoftInput(view, 0);
		}
	}
	protected void hideKeyBoard() {
		View view = this.getActivity().getCurrentFocus();
		if (view != null) {
			((InputMethodManager)  this.getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

}
