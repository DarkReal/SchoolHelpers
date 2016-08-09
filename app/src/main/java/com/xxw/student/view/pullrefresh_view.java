package com.xxw.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.utils.LogUtils;

/**
 * 下拉刷新样式
 * Created by DarkReal on 2016/5/5.
 */
public class pullrefresh_view extends LinearLayout {

    private static final String TAG = "PullToRefreshView";
    // refresh states
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    // pull state
    private static final int PULL_UP_STATE = 0;
    private static final int PULL_DOWN_STATE = 1;

    private int mLastMotionY;
    private boolean mLock;
    private View mHeaderView;
    private AdapterView<?> mAdapterView;
    private ScrollView mScrollView;
    private int mHeaderViewHeight;
    private TextView mHeaderTextView;
    private TextView mHeaderUpdateTextView;
    private ProgressBar mHeaderProgressBar;
    private LayoutInflater mInflater;
    private int mHeaderState;

    /**
     * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE  pull的状态
     */
    private int mPullState;

    private OnHeaderRefreshListener mOnHeaderRefreshListener;
    /**
     * last update time
     */
	private String mLastUpdateTime;

    public pullrefresh_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public pullrefresh_view(Context context) {
        super(context);
        init();
    }


    private void init() {

        mInflater = LayoutInflater.from(getContext());
        // header view 在此添加,保证是第一个添加到linearlayout的最上端
        addHeaderView();
    }

    private void addHeaderView() {
        // header view
        mHeaderView = mInflater.inflate(R.layout.custiom_refresh_header, this, false);


        mHeaderTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_text);
        mHeaderUpdateTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_updated_at);
        mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.pull_to_refresh_progress);
        // header layout
        measureView(mHeaderView);


        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                mHeaderViewHeight);
        // 设置topMargin的值为负的header View高度,即将其隐藏在最上方
        params.topMargin = -(mHeaderViewHeight);
        // mHeaderView.setLayoutParams(params1);
        addView(mHeaderView, params);

    }

    //重载方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initContentAdapterView();
    }
    /**
     * init AdapterView like ListView,GridView and so on;or init ScrollView
     *
     * @description hylin 2012-7-30下午8:48:12
     */
    private void initContentAdapterView() {
        //必须使用第二个子元素，因为获取到的第一个子元素一定是headerview
        View view = getChildAt(1);
        if (view instanceof AdapterView<?>) {
            mAdapterView = (AdapterView<?>) view;
        }
        if (view instanceof ScrollView) {
            mScrollView = (ScrollView) view;
        }

        if (mAdapterView == null && mScrollView == null) {
            throw new IllegalArgumentException(
                    "must contain a AdapterView or ScrollView in this layout!");
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 首先拦截down事件,记录y坐标
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 是向下运动,< 0是向上运动
                int deltaY = y - mLastMotionY;
                if (mLastMotionY<450&&deltaY>0&&isRefreshViewScroll(deltaY)) {
                    return true;
                }else if(mLastMotionY>450&&deltaY>0){
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /*
     * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return
     * false)则由PullToRefreshView 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLock) {
            return true;
        }
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // onInterceptTouchEvent已经记录
                // mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastMotionY;
                if (mPullState == PULL_DOWN_STATE) {
                    // PullToRefreshView执行下拉
                    LogUtils.v("pull down!parent view move!");
                    headerPrepareToRefresh(deltaY);
                    // setHeaderPadding(-mHeaderViewHeight);
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int topMargin = getHeaderTopMargin();
                if (mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        // 开始刷新
                        headerRefreshing();
                    } else {
                        // 还没有执行刷新，重新隐藏
                        setHeaderTopMargin(-mHeaderViewHeight);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否应该到了父View,即PullToRefreshView滑动
     *
     * @param deltaY
     *            , deltaY > 0 是向下运动,< 0是向上运动
     * @return
     */
    private boolean isRefreshViewScroll(int deltaY) {
        if (mHeaderState == REFRESHING) {
            return false;
        }

        if (mScrollView != null) {
            // 子scroll view滑动到最顶端
            View child = mScrollView.getChildAt(0);
            LogUtils.v(mScrollView.getChildAt(0).toString());//linearlayout

            if (deltaY > 0 && mScrollView.getScrollY() == 0) {
                mPullState = PULL_DOWN_STATE;
                return true;
            }
        }
        return false;
    }

    /**
     * header 准备刷新,手指移动过程,还没有释放
     *
     * @param deltaY
     *            ,手指滑动的距离
     */
    private void headerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
        if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
            mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
            mHeaderUpdateTextView.setVisibility(View.VISIBLE);
            mHeaderTextView.setVisibility(View.VISIBLE);


            mHeaderState = RELEASE_TO_REFRESH;
        } else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
            mHeaderTextView.setVisibility(View.VISIBLE);
            // mHeaderImageView.
            mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
            mHeaderState = PULL_TO_REFRESH;
        }
    }

    /**
     * 修改Header view top margin的值
     *
     * @description
     * @param deltaY
     * @return hylin 2012-7-31下午1:14:31
     */
    private int changingHeaderViewTopMargin(int deltaY) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        float newTopMargin = params.topMargin + deltaY * 0.3f;

        //表示如果是在上拉后一段距离,然后直接下拉
        if(deltaY>0&&mPullState == PULL_UP_STATE&& Math.abs(params.topMargin) <= mHeaderViewHeight){
            return params.topMargin;
        }
        //同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
        if(deltaY<0&&mPullState == PULL_DOWN_STATE&& Math.abs(params.topMargin)>=mHeaderViewHeight){
            return params.topMargin;
        }
        params.topMargin = (int) newTopMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
    }

    /**
     * header refreshing
     *
     * @description hylin 2012-7-31上午9:10:12
     */
    private void headerRefreshing() {
        mHeaderState = REFRESHING;
        setHeaderTopMargin(0);

        mHeaderProgressBar.setVisibility(View.VISIBLE);
        mHeaderTextView.setVisibility(View.GONE);
        if (mOnHeaderRefreshListener != null) {
            mOnHeaderRefreshListener.onHeaderRefresh(this);
        }
    }

    /**
     * 设置header view 的topMargin的值
     *
     * @description
     * @param topMargin
     *            ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
     *            hylin 2012-7-31上午11:24:06
     */
    private void setHeaderTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
    }

    /**
     * header view 完成更新后恢复初始状态
     *
     * @description hylin 2012-7-31上午11:54:23
     */
    public void onHeaderRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);

        mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
        mHeaderProgressBar.setVisibility(View.GONE);
        // mHeaderUpdateTextView.setText("");
        mHeaderState = PULL_TO_REFRESH;
    }

    /**
     * Resets the list to a normal state after a refresh.
     *
     * @param lastUpdated
     *            Last updated at.
     */
    public void onHeaderRefreshComplete(CharSequence lastUpdated) {
        setLastUpdated(lastUpdated);
        //防止参数为空
        onHeaderRefreshComplete();
    }


    /**
     * Set a text to represent when the list was last updated.
     *
     * @param lastUpdated
     *            Last updated at.
     */
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mHeaderUpdateTextView.setVisibility(View.VISIBLE);
            mHeaderUpdateTextView.setText(lastUpdated);
        } else {
            mHeaderUpdateTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取当前header view 的topMargin
     *
     * @description
     * @return hylin 2012-7-31上午11:22:50
     */
    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        return params.topMargin;
    }

    /**
     * set headerRefreshListener
     *
     * @description
     * @param headerRefreshListener
     *            hylin 2012-7-31上午11:43:58
     */
    public void setOnHeaderRefreshListener(OnHeaderRefreshListener headerRefreshListener) {
        mOnHeaderRefreshListener = headerRefreshListener;
    }

    /**
     * Interface definition for a callback to be invoked when list/grid header
     * view should be refreshed.
     */
    public interface OnHeaderRefreshListener {
        public void onHeaderRefresh(pullrefresh_view view);
    }
}
