package com.zm.shangxueyuan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zm.shangxueyuan.R;
import com.zm.shangxueyuan.constant.CommonConstant;
import com.zm.shangxueyuan.db.SettingDBUtil;
import com.zm.shangxueyuan.model.UserModel;
import com.zm.shangxueyuan.restful.ReqRestAdapter;
import com.zm.shangxueyuan.restful.RestfulRequest;
import com.zm.shangxueyuan.restful.UserJsonObjectConverter;
import com.zm.shangxueyuan.ui.widget.LoadingDialog;
import com.zm.shangxueyuan.utils.ToastUtil;
import com.zm.utils.KeyBoardUtil;

import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Creator: dengshengjin on 16/4/24 12:30
 * Email: deng.shengjin@zuimeia.com
 */
public class UserLoginActivity extends AbsActionBarActivity {
    @Bind(R.id.account_edit)
    EditText mAccountText;

    @Bind(R.id.password_edit)
    EditText mPasswordText;

    @Bind(R.id.login)
    TextView mLogin;

    @Bind(R.id.forget_password_text)
    TextView mForgetPassword;

    private LoadingDialog mLoadingDialog;
    private RestfulRequest mRequest;
    private Handler mHandler = new Handler();
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UserLoginActivity.class);
        return intent;
    }

    @Override
    protected void initData() {
        mLoadingDialog = new LoadingDialog(UserLoginActivity.this, R.style.dialog_style);
        mRequest = ReqRestAdapter.getInstance(getContext(), CommonConstant.LOGIN_RES_URL, new UserJsonObjectConverter()).create(RestfulRequest.class);
    }

    @Override
    protected void initWidgets() {
        setActionTitle(R.string.user_login);
        setActionBackActions(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenSoftKeyboard()) {
                    KeyBoardUtil.hideSoftKeyboard(UserLoginActivity.this);
                }
            }
        });
    }

    @Override
    protected void initWidgetsActions() {
        mPasswordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    KeyBoardUtil.hideSoftKeyboard(UserLoginActivity.this);
                    onLoginAction();
                    return true;
                }
                return false;

            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginAction();
            }
        });
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(WebViewActivity.getIntent(UserLoginActivity.this, CommonConstant.LOGIN_WEB_URl, getString(R.string.user)));
            }
        });
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                KeyBoardUtil.showSoftKeyboard(mPasswordText, UserLoginActivity.this);
            }
        }, 300);
    }

    private void onLoginAction() {
        final String account = mAccountText.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            return;
        }
        String password = mPasswordText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            return;
        }
        mLoadingDialog.show();
        mRequest.login(account, password, CommonConstant.LOGIN_CLIENT_TYPE, new Callback<JSONObject>() {
            @Override
            public void success(final JSONObject jsonObject, Response response) {
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        UserModel userModel = UserModel.parse(jsonObject.toString());
                        SettingDBUtil.getInstance(getApplicationContext()).setUserTokenId(userModel.token_id);
                        SettingDBUtil.getInstance(getApplicationContext()).setUserAccountsId(userModel.accounts_id);
                        SettingDBUtil.getInstance(getApplicationContext()).setUserServer(jsonObject.toString());
                        SettingDBUtil.getInstance(getApplicationContext()).setUserAccount(account);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinishing()) {
                                    return;
                                }
                                mLoadingDialog.cancel();
                                UserLoginActivity.this.finish();
                            }
                        });
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.showToast(getApplicationContext(), R.string.login_fail);
                if (isFinishing()) {
                    return;
                }
                mLoadingDialog.cancel();
            }
        });

    }


    private boolean isOpenSoftKeyboard() {
        if (KeyBoardUtil.isOpenSoftKeyboard(mAccountText, getApplicationContext())) {
            return true;
        }
        if (KeyBoardUtil.isOpenSoftKeyboard(mPasswordText, getApplicationContext())) {
            return true;
        }

        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user_login;
    }
}