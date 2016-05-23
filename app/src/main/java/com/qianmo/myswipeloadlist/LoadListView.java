package com.qianmo.myswipeloadlist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 下滑到底部自动加载ListView
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {

    private OnLoadListener mOnLoadListener;
    private View mLoadView;
    private boolean mIsLoading;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private int mTotalItemCount;
    private boolean mIsLoadEnable = true;
    private boolean mShouldLoad = true;

    //用于外部回调
    public interface OnLoadListener {
        void loadData();
    }

    public LoadListView(Context context) {
        this(context, null);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (!mShouldLoad) return;
            if (mIsLoading || !mIsLoadEnable) return;
            int lastItemIndex = mFirstVisibleItem + mVisibleItemCount;
            if (lastItemIndex >= mTotalItemCount && mOnLoadListener != null) {
                load();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("fff", "----firstVisibleItem---==" + firstVisibleItem);
        Log.e("fff", "----visibleItemCount---==" + visibleItemCount);
        Log.e("fff", "----totalItemCount---==" + totalItemCount);
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        mTotalItemCount = totalItemCount;
        if (mTotalItemCount > mVisibleItemCount) {
            Log.e("fff", "----有第二页----");
            mShouldLoad = true;
        } else {
            Log.e("fff", "----没第二页----");
            mShouldLoad = false;
        }
    }

    public void setLoadView(View view) {
        mLoadView = view;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    private void load() {
        mIsLoading = true;
        mOnLoadListener.loadData();
        if (mLoadView != null) {
            this.addFooterView(mLoadView);
        }
    }

    public void onLoadComplete() {
        mIsLoading = false;
        if (mLoadView != null) {
            this.removeFooterView(mLoadView);
        }
    }

    public boolean getLoadEnable() {
        return mIsLoadEnable;
    }

    public void setLoadEnable(boolean isEnable) {
        mIsLoadEnable = isEnable;
    }
}
