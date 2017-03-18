package com.lany.view;


import android.content.Context;
import android.content.res.TypedArray;
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

public class StateView extends FrameLayout {
    private final String TAG = "StateView";
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mNetworkView;

    private State mViewState = State.UNKNOWN;

    enum State {
        UNKNOWN, CONTENT, ERROR, EMPTY, LOADING, NETWORK;
    }

    private OnRetryClickListener mListener;

    public interface OnRetryClickListener {
        void onRetry();
    }

    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.mListener = listener;
    }

    public StateView(Context context) {
        this(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateView);
        int loadingViewResId = a.getResourceId(R.styleable.StateView_sv_loadingView, R.layout.state_view_loading);
        int emptyViewResId = a.getResourceId(R.styleable.StateView_sv_emptyView, R.layout.state_view_empty);
        int errorViewResId = a.getResourceId(R.styleable.StateView_sv_errorView, R.layout.state_view_error);
        int networkViewResId = a.getResourceId(R.styleable.StateView_sv_networkView, R.layout.state_view_network);
        int viewState = a.getInt(R.styleable.StateView_sv_viewState, 0);
        switch (viewState) {
            case 0:
                mViewState = State.CONTENT;
                break;
            case 1:
                mViewState = State.ERROR;
                break;
            case 2:
                mViewState = State.EMPTY;
                break;
            case 3:
                mViewState = State.LOADING;
                break;
            case 4:
                mViewState = State.NETWORK;
                break;
            case -1:
            default:
                mViewState = State.UNKNOWN;
                break;
        }
        a.recycle();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        mLoadingView = inflater.inflate(loadingViewResId, this, false);
        addView(mLoadingView, mLoadingView.getLayoutParams());


        mEmptyView = inflater.inflate(emptyViewResId, this, false);
        mEmptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry();
                }
            }
        });
        addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


        mErrorView = inflater.inflate(errorViewResId, this, false);
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry();
                }
            }
        });
        addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


        mNetworkView = inflater.inflate(networkViewResId, this, false);
        mNetworkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry();
                }
            }
        });
        addView(mNetworkView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null)
            throw new IllegalArgumentException("Content view is not defined");
        setView(State.UNKNOWN);
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
    private View getView(State state) {
        switch (state) {
            case LOADING:
                return mLoadingView;
            case CONTENT:
                return mContentView;
            case EMPTY:
                return mEmptyView;
            case ERROR:
                return mErrorView;
            case NETWORK:
                return mNetworkView;
            default:
                return null;
        }
    }

    public State getViewState() {
        return mViewState;
    }

    private void setViewState(State state) {
        setViewState(state, null);
    }

    private void setViewState(State state, String customMsg) {
        if (state != mViewState) {
            State previous = mViewState;
            mViewState = state;
            setView(previous, customMsg);
        }
    }

    private void setView(State state, String customMsg) {
        switch (mViewState) {
            case LOADING:
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
                                "Have to a 'R.id.sv_loading_msg_text' id in custom loading layout.");
                    }
                }
                break;
            case EMPTY:
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
                                "Have to a 'R.id.sv_empty_msg_text' id in custom empty layout.");
                    }
                }
                break;
            case ERROR:
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
                                "Have to a 'R.id.sv_error_msg_text' id in custom error layout.");
                    }
                }
                break;
            case NETWORK:
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
                                "Have to a 'R.id.sv_network_msg_text' id in custom network layout.");
                    }
                }
                break;
            case CONTENT:
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

    private void setView(State state) {
        setView(state, null);
    }

    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }
        return view != mLoadingView && view != mErrorView && view != mEmptyView && view != mNetworkView;
    }

    private void setViewForState(View view, State state, boolean switchToState) {
        switch (state) {
            case LOADING:
                if (mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;
            case EMPTY:
                if (mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case ERROR:
                if (mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;
            case CONTENT:
                if (mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
            case NETWORK:
                if (mNetworkView != null)
                    removeView(mNetworkView);
                mNetworkView = view;
                addView(mNetworkView);
                break;
        }

        setView(State.UNKNOWN);
        if (switchToState)
            setViewState(state);
    }

    private void setViewForState(View view, State state) {
        setViewForState(view, state, false);
    }

    private void setViewForState(@LayoutRes int layoutRes, State state, boolean switchToState) {
        View view = LayoutInflater.from(getContext()).inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    private void setViewForState(@LayoutRes int layoutRes, State state) {
        setViewForState(layoutRes, state, false);
    }

    public void showLoadingView() {
        setViewState(State.LOADING);
    }

    public void showLoadingView(String msg) {
        setViewState(State.LOADING, msg);
    }

    public void showContentView() {
        setViewState(State.CONTENT);
    }

    public void showErrorView() {
        setViewState(State.ERROR);
    }

    public void showErrorView(String msg) {
        setViewState(State.ERROR, msg);
    }

    public void showNetworkView() {
        setViewState(State.NETWORK);
    }

    public void showNetworkView(String msg) {
        setViewState(State.NETWORK, msg);
    }

    public void showUnknownView() {
        setViewState(State.UNKNOWN);
    }

    public void showEmptyView() {
        setViewState(State.EMPTY);
    }

    public void showEmptyView(String msg) {
        setViewState(State.EMPTY, msg);
    }

    public void setEmptyView(@LayoutRes int layoutResId) {
        setViewForState(layoutResId, State.EMPTY, true);
    }

    public void setErrorView(@LayoutRes int layoutResId) {
        setViewForState(layoutResId, State.ERROR, true);
    }
}
