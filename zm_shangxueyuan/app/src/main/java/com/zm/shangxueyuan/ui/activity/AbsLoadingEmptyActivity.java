package com.zm.shangxueyuan.ui.activity;

import android.view.View;
import android.view.ViewStub;

import com.zm.shangxueyuan.R;
import com.zm.shangxueyuan.ui.widget.LoadingEmptyView;

/**
 * Creator: dengshengjin on 16/4/17 18:51
 * Email: deng.shengjin@zuimeia.com
 */
public abstract class AbsLoadingEmptyActivity extends AbsActionBarActivity {
    protected ViewStub mViewStub;
    protected LoadingEmptyView mLoadingEmptyView;

    protected void initLoadingEmptyView() {
        if (mViewStub == null) {
            mViewStub = (ViewStub) findViewById(R.id.view_stub);
            mViewStub.inflate();

            mLoadingEmptyView = (LoadingEmptyView) findViewById(R.id.loading_empty_box);
        }
        mViewStub.setVisibility(View.VISIBLE);
    }

    protected void showLoading() {
        initLoadingEmptyView();
        if (mLoadingEmptyView != null) {
            mLoadingEmptyView.updateLoadView();
        }
    }

    protected void hideLoading() {
        if (mViewStub != null) {
            mViewStub.setVisibility(View.GONE);
        }
    }

    protected void showLoadFail(LoadingEmptyView.LoadViewCallback loadViewCallback) {
        initLoadingEmptyView();
        if (mViewStub != null) {
            mLoadingEmptyView.updateEmptyView(loadViewCallback);
        }
    }

    protected void showEmpty() {
        initLoadingEmptyView();
        if (mLoadingEmptyView != null) {
            mLoadingEmptyView.updateEmptyView();
        }
    }
}
