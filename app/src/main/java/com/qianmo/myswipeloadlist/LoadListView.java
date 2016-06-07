package com.qianmo.myswipeloadlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 下滑到底部自动加载ListView
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {

    public interface OnLoadScrollListener {
        public void onScrollStateChanged(AbsListView view, int scrollState);

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    private OnLoadListener mOnLoadListener;
    private OnLoadScrollListener mOnLoadScrollListener;
    private View mLoadView;
    private boolean mIsLoading;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private int mTotalItemCount;
    private boolean mIsLoadEnable = true;
    private boolean mShouldLoad = true;

    /**
     * 回调接口，用于数据加载
     */
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
        if (mOnLoadScrollListener != null) {
            mOnLoadScrollListener.onScrollStateChanged(view, scrollState);
        }
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
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
        if (mOnLoadScrollListener != null) {
            mOnLoadScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        mTotalItemCount = totalItemCount;
        if (mTotalItemCount > mVisibleItemCount) {
            mShouldLoad = true;
        } else {
            mShouldLoad = false;
        }
    }

    /**
     * 设置load view
     *
     * @param view
     */
    public void setLoadView(View view) {
        mLoadView = view;
    }

    /**
     * 设置load listener
     *
     * @param onLoadListener
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    /**
     * 判断是否正在加载中
     *
     * @return
     */
    public boolean isLoading() {
        return mIsLoading;
    }

    private void load() {
        mIsLoading = true;
        if (mLoadView != null) {
            this.addFooterView(mLoadView);
        }
        mOnLoadListener.loadData();
    }

    /**
     * 数据加载结束后调用此方法
     */
    public void onLoadComplete() {
        mIsLoading = false;
        if (mLoadView != null) {
            this.removeFooterView(mLoadView);
        }
    }

    /**
     * 判断是否可以自动加载
     *
     * @return
     */
    public boolean getLoadEnable() {
        return mIsLoadEnable;
    }

    /**
     * 设置是否启用自动加载功能
     *
     * @param isEnable
     */
    public void setLoadEnable(boolean isEnable) {
        mIsLoadEnable = isEnable;
    }

    /**
     * 兼容ListView原本的OnScrollListener
     *
     * @param onScrollListener
     */
    public void setLoadScrollListener(OnLoadScrollListener onScrollListener) {
        mOnLoadScrollListener = onScrollListener;
    }
}
