package com.lany.state;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Keep;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

/**
 * 多状态试图
 */
@Keep
public class StateLayout extends FrameLayout {
    private final String TAG = "StateLayout";
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mNetworkView;

    @ViewType
    private int mViewType = ViewType.CONTENT;
    @Nullable
    private OnStateListener mStateListener;
    @Nullable
    private OnRetryListener mRetryListener;

    private LayoutInflater mLayoutInflater;
    @LayoutRes
    private int loadingLayoutId = R.layout.view_loading;
    @LayoutRes
    private int emptyLayoutId = R.layout.view_empty;
    @LayoutRes
    private int errorLayoutId = R.layout.view_error;
    @LayoutRes
    private int networkLayoutId = R.layout.view_network;

    public void setOnStateListener(OnStateListener listener) {
        mStateListener = listener;
    }

    public interface OnStateListener {
        void onStateChanged(@ViewType int state);
    }

    public interface OnRetryListener {
        void onRetry();
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mRetryListener = listener;
    }

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateLayout);
            loadingLayoutId = a.getResourceId(R.styleable.StateLayout_loadingView, R.layout.view_loading);
            emptyLayoutId = a.getResourceId(R.styleable.StateLayout_emptyView, R.layout.view_empty);
            errorLayoutId = a.getResourceId(R.styleable.StateLayout_errorView, R.layout.view_error);
            networkLayoutId = a.getResourceId(R.styleable.StateLayout_networkView, R.layout.view_network);
            mViewType = a.getInt(R.styleable.StateLayout_viewState, ViewType.CONTENT);
            a.recycle();
        }
        mLayoutInflater = LayoutInflater.from(getContext());
        addLoadingView();
        addEmptyView();
        addErrorView();
        addNetworkView();
    }

    private void addLoadingView() {
        mLoadingView = mLayoutInflater.inflate(loadingLayoutId, this, false);
        mLoadingView.setVisibility(GONE);
        addView(mLoadingView, mLoadingView.getLayoutParams());
    }

    private void addEmptyView() {
        mEmptyView = mLayoutInflater.inflate(emptyLayoutId, this, false);
        mEmptyView.setVisibility(GONE);
        mEmptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    mRetryListener.onRetry();
                }
            }
        });
        addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void addErrorView() {
        mErrorView = mLayoutInflater.inflate(errorLayoutId, this, false);
        mErrorView.setVisibility(GONE);
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    mRetryListener.onRetry();
                }
            }
        });
        addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void addNetworkView() {
        mNetworkView = mLayoutInflater.inflate(networkLayoutId, this, false);
        mNetworkView.setVisibility(GONE);
        mNetworkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    mRetryListener.onRetry();
                }
            }
        });
        addView(mNetworkView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) {
            throw new IllegalArgumentException("Content view is not defined");
        }
    }

    @Override
    public void addView(View child) {
        getContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        getContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        getContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        getContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        getContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        getContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        getContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    private void getContentView(View child) {
        if (!(mContentView != null && mContentView != child) && child != mLoadingView && child != mErrorView && child != mEmptyView && child != mNetworkView) {
            mContentView = child;
        }
    }

    private void switchViewState(@ViewType int type) {
        mViewType = type;
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mViewType == ViewType.LOADING ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("Loading View");
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(mViewType == ViewType.ERROR ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("ErrorView View");
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mViewType == ViewType.EMPTY ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("EmptyView View");
        }
        if (mNetworkView != null) {
            mNetworkView.setVisibility(mViewType == ViewType.NETWORK ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("NetworkView View");
        }
        if (mContentView != null) {
            mContentView.setVisibility(mViewType == ViewType.CONTENT ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("ContentView View");
        }
        if (mStateListener != null) {
            mStateListener.onStateChanged(mViewType);
        }
    }

    public void resetStateView(View view, @ViewType int state, boolean switchToState) {
        switch (state) {
            case ViewType.LOADING:
                if (mLoadingView != null) {
                    removeView(mLoadingView);
                }
                mLoadingView = view;
                addView(mLoadingView);
                break;
            case ViewType.EMPTY:
                if (mEmptyView != null) {
                    removeView(mEmptyView);
                }
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case ViewType.ERROR:
                if (mErrorView != null) {
                    removeView(mErrorView);
                }
                mErrorView = view;
                addView(mErrorView);
                break;
            case ViewType.CONTENT:
                if (mContentView != null) {
                    removeView(mContentView);
                }
                mContentView = view;
                addView(mContentView);
                break;
            case ViewType.NETWORK:
                if (mNetworkView != null) {
                    removeView(mNetworkView);
                }
                mNetworkView = view;
                addView(mNetworkView);
                break;
        }
        switchViewState(ViewType.CONTENT);
        if (switchToState) {
            switchViewState(state);
        }
    }

    public void resetStateView(View view, @ViewType int type) {
        resetStateView(view, type, false);
    }

    private void resetStateView(@LayoutRes int layoutRes, @ViewType int state, boolean switchToState) {
        View view = LayoutInflater.from(getContext()).inflate(layoutRes, this, false);
        resetStateView(view, state, switchToState);
    }

    public void resetStateView(@LayoutRes int layoutRes, @ViewType int state) {
        resetStateView(layoutRes, state, false);
    }

    /**
     * 使用默认的加载视图
     */
    public void showLoading() {
        switchViewState(ViewType.LOADING);
    }

    /**
     * 自定义加载视图显示内容
     *
     * @param message 要显示的内容
     */
    public void showLoading(CharSequence message) {
        switchViewState(ViewType.LOADING);
        if (TextUtils.isEmpty(message)) {
            Log.i(TAG, "showLoading: The message is empty, using default");
            return;
        }
        try {
            ((TextView) mLoadingView.findViewById(R.id.loading_msg_text)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The R.id.loading_msg_text is not found in the custom loading view");
        }
    }

    /**
     * 使用自定义的加载视图id显示内容
     *
     * @param message           要显示的内容
     * @param messageTextViewId 显示TextView控件的id
     */
    public void showLoading(CharSequence message, @IdRes int messageTextViewId) {
        switchViewState(ViewType.LOADING);
        try {
            ((TextView) mLoadingView.findViewById(messageTextViewId)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The " + messageTextViewId + " id is not found in the custom loading view");
        }
    }

    /**
     * 显示内容视图
     */
    public void showContent() {
        switchViewState(ViewType.CONTENT);
    }

    /**
     * 使用默认错误视图显示
     */
    public void showError() {
        switchViewState(ViewType.ERROR);
    }

    /**
     * 使用默认错误视图显示自定义的内容
     *
     * @param message 显示内容
     */
    public void showError(CharSequence message) {
        switchViewState(ViewType.ERROR);
        if (TextUtils.isEmpty(message)) {
            Log.i(TAG, "showError: The message is empty, using default");
            return;
        }
        try {
            ((TextView) mErrorView.findViewById(R.id.error_msg_text)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The R.id.error_msg_text is not found in the custom error view");
        }
    }

    /**
     * 使用自定义的错误视图id显示内容
     *
     * @param message           要显示的内容
     * @param messageTextViewId 显示TextView控件的id
     */
    public void showError(CharSequence message, @IdRes int messageTextViewId) {
        switchViewState(ViewType.ERROR);
        try {
            ((TextView) mErrorView.findViewById(messageTextViewId)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The " + messageTextViewId + " id is not found in the custom error view");
        }
    }

    public void showNetwork() {
        switchViewState(ViewType.NETWORK);
    }

    public void showNetwork(CharSequence message) {
        switchViewState(ViewType.NETWORK);
        if (TextUtils.isEmpty(message)) {
            Log.i(TAG, "showNetwork: The message is empty, using default");
            return;
        }
        try {
            ((TextView) mNetworkView.findViewById(R.id.network_msg_text_view)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The R.id.network_msg_text_view is not found in the custom empty view");
        }
    }

    /**
     * 使用自定义的网络视图id显示内容
     *
     * @param message           要显示的内容
     * @param messageTextViewId 显示TextView控件的id
     */
    public void showNetwork(CharSequence message, @IdRes int messageTextViewId) {
        switchViewState(ViewType.NETWORK);
        try {
            ((TextView) mNetworkView.findViewById(messageTextViewId)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The " + messageTextViewId + " id is not found in the custom network view");
        }
    }

    public void showEmpty() {
        switchViewState(ViewType.EMPTY);
    }

    public void showEmpty(CharSequence message) {
        switchViewState(ViewType.EMPTY);
        if (TextUtils.isEmpty(message)) {
            Log.i(TAG, "showEmpty: The message is empty, using default");
            return;
        }
        try {
            ((TextView) mEmptyView.findViewById(R.id.empty_msg_text)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The R.id.empty_msg_text is not found in the custom empty view");
        }
    }

    /**
     * 使用自定义的空视图id显示内容
     *
     * @param message           要显示的内容
     * @param messageTextViewId 显示TextView控件的id
     */
    public void showEmpty(CharSequence message, @IdRes int messageTextViewId) {
        switchViewState(ViewType.EMPTY);
        try {
            ((TextView) mEmptyView.findViewById(messageTextViewId)).setText(message);
        } catch (Exception e) {
            Log.e(TAG, "The " + messageTextViewId + " id is not found in the custom empty view");
        }
    }

    @Nullable
    public View getView(@ViewType int state) {
        switch (state) {
            case ViewType.LOADING:
                return mLoadingView;
            case ViewType.CONTENT:
                return mContentView;
            case ViewType.EMPTY:
                return mEmptyView;
            case ViewType.ERROR:
                return mErrorView;
            case ViewType.NETWORK:
                return mNetworkView;
            default:
                Log.e(TAG, "error!!!");
                return null;
        }
    }
}
