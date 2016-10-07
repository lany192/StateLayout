package com.lany.msv;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MultiStateView extends FrameLayout {
    private final String TAG = this.getClass().getSimpleName();
    public static final int STATE_UNKNOWN = -1;
    public static final int STATE_CONTENT = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_LOADING = 3;
    public static final int STATE_NETWORK = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_UNKNOWN, STATE_CONTENT, STATE_ERROR, STATE_EMPTY, STATE_LOADING, STATE_NETWORK})
    public @interface ViewState {
        
    }

    private LayoutInflater mInflater;

    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mNetworkView;

    @ViewState
    private int mViewState = STATE_UNKNOWN;

    private OnRetryClickListener mListener;

    public interface OnRetryClickListener {
        void onRetry();
    }

    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.mListener = listener;
    }

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView);

        int loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, R.layout.msv_loading_view);
        mLoadingView = mInflater.inflate(loadingViewResId, this, false);
        addView(mLoadingView, mLoadingView.getLayoutParams());

        int emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, R.layout.msv_empty_view);
        mEmptyView = mInflater.inflate(emptyViewResId, this, false);
        mEmptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry();
                }
            }
        });
        addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        int errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, R.layout.msv_error_view);
        mErrorView = mInflater.inflate(errorViewResId, this, false);
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry();
                }
            }
        });
        addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        int networkViewResId = a.getResourceId(R.styleable.MultiStateView_msv_networkView, R.layout.msv_network_view);
        mNetworkView = mInflater.inflate(networkViewResId, this, false);
        mNetworkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry();
                }
            }
        });
        addView(mNetworkView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        int viewState = a.getInt(R.styleable.MultiStateView_msv_viewState, STATE_CONTENT);

        switch (viewState) {
            case STATE_CONTENT:
                mViewState = STATE_CONTENT;
                break;
            case STATE_ERROR:
                mViewState = STATE_ERROR;
                break;
            case STATE_EMPTY:
                mViewState = STATE_EMPTY;
                break;
            case STATE_LOADING:
                mViewState = STATE_LOADING;
                break;
            case STATE_NETWORK:
                mViewState = STATE_NETWORK;
                break;
            case STATE_UNKNOWN:
            default:
                mViewState = STATE_UNKNOWN;
                break;
        }
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) throw new IllegalArgumentException("Content view is not defined");
        setView(STATE_UNKNOWN);
    }

    @Override
    public void addView(View child) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Nullable
    public View getView(@ViewState int state) {
        switch (state) {
            case STATE_LOADING:
                return mLoadingView;
            case STATE_CONTENT:
                return mContentView;
            case STATE_EMPTY:
                return mEmptyView;
            case STATE_ERROR:
                return mErrorView;
            case STATE_NETWORK:
                return mNetworkView;
            default:
                return null;
        }
    }

    @ViewState
    public int getViewState() {
        return mViewState;
    }

    public void setViewState(@ViewState int state) {
        setViewState(state, null);
    }

    public void setViewState(@ViewState int state, String customMsg) {
        if (state != mViewState) {
            int previous = mViewState;
            mViewState = state;
            setView(previous, customMsg);
        }
    }

    private void setView(@ViewState int previousState, String customMsg) {
        switch (mViewState) {
            case STATE_LOADING:
                if (mLoadingView == null) {
                    throw new NullPointerException("Loading View");
                }
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if (mNetworkView != null) mNetworkView.setVisibility(View.GONE);

                mLoadingView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(customMsg)) {
                    try {
                        ((TextView) mLoadingView.findViewById(R.id.msv_loading_msg_text)).setText(customMsg);
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Have to a 'R.id.msv_loading_msg_text' id in custom loading layout.");
                    }
                }
                break;
            case STATE_EMPTY:
                if (mEmptyView == null) {
                    throw new NullPointerException("Empty View");
                }
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mNetworkView != null) mNetworkView.setVisibility(View.GONE);

                mEmptyView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(customMsg)) {
                    try {
                        ((TextView) mEmptyView.findViewById(R.id.msv_empty_msg_text)).setText(customMsg);
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Have to a 'R.id.msv_empty_msg_text' id in custom empty layout.");
                    }
                }
                break;
            case STATE_ERROR:
                if (mErrorView == null) {
                    throw new NullPointerException("Error View");
                }
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if (mNetworkView != null) mNetworkView.setVisibility(View.GONE);

                mErrorView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(customMsg)) {
                    try {
                        ((TextView) mErrorView.findViewById(R.id.msv_error_msg_text)).setText(customMsg);
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Have to a 'R.id.msv_error_msg_text' id in custom error layout.");
                    }
                }
                break;
            case STATE_NETWORK:
                if (mErrorView == null) {
                    throw new NullPointerException("Network View");
                }
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);

                mNetworkView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(customMsg)) {
                    try {
                        ((TextView) mNetworkView.findViewById(R.id.msv_network_msg_text)).setText(customMsg);
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Have to a 'R.id.msv_network_msg_text' id in custom network layout.");
                    }
                }
                break;
            case STATE_CONTENT:
            default:
                if (mContentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw new NullPointerException("Content View");
                }
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if (mNetworkView != null) mNetworkView.setVisibility(View.GONE);

                mContentView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setView(@ViewState int previousState) {
        setView(previousState, null);
    }

    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }
        return view != mLoadingView && view != mErrorView && view != mEmptyView && view != mNetworkView;
    }

    public void setViewForState(View view, @ViewState int state, boolean switchToState) {
        switch (state) {
            case STATE_LOADING:
                if (mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;
            case STATE_EMPTY:
                if (mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case STATE_ERROR:
                if (mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;
            case STATE_CONTENT:
                if (mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
            case STATE_NETWORK:
                if (mNetworkView != null) removeView(mNetworkView);
                mNetworkView = view;
                addView(mNetworkView);
                break;
        }

        setView(STATE_UNKNOWN);
        if (switchToState) setViewState(state);
    }

    public void setViewForState(View view, @ViewState int state) {
        setViewForState(view, state, false);
    }

    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state, boolean switchToState) {
        if (mInflater == null) mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state) {
        setViewForState(layoutRes, state, false);
    }
}
