package com.xxw.student.myView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 自定义滑动scrollview
 * Created by xxw on 2016/4/11.
 */
public class mineScrollView extends ScrollView {

    private int maxH = 150;
    private View inner;
    private Rect normal = new Rect();//方便存储layout四个坐标
    private float y ;

    public mineScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

     @Override
     public boolean onTouchEvent(MotionEvent e) {
         float x = e.getX();
         if(e.getX()<=20){
             return false;
         }else {
//             LogUtils.v("ScrollView onTouchEvent start");
             boolean s = super.onTouchEvent(e);
//             LogUtils.v("onTouchEvent value "+s);
//        System.out.println("onTouchEvent");
             if (inner == null) {
                 return super.onTouchEvent(e);
             } else {
                 commOnTouchEvent(e);
             }
             return s;
         }
     }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
//        LogUtils.v("ScrollView onInterceptTouchEvent start");
        boolean s = super.onInterceptTouchEvent(event);
//        LogUtils.v("onInterceptTouchEvent value: "+s);
        return s;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogUtils.v("ScrollView dispatchTouchEvent start");
        boolean s = super.dispatchTouchEvent(ev);
//        LogUtils.v("dispatchTouchEvent value " + s);
        //可以保证button等的点击事件可以被执行
        if (inner != null) {
            commOnTouchEvent(ev);
        }
        return s;
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:

                if (isNeedAnimation()) {
                    animation();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY);
                // 滚动
                scrollBy(0, deltaY);

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());

                    }
                    // 移动布局
                    if(inner.getTop()<=maxH&&inner.getTop()>=-maxH)
                        inner.layout(inner.getLeft(), inner.getTop() - deltaY/3,
                                inner.getRight(), inner.getBottom() - deltaY/3);
                }
                break;

            default:
                break;
        }
    }

    // 开启动画移动

    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(500);
//        ta.setInterpolator(new BounceInterpolator());//这个可以注释掉
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);

        normal.setEmpty();

    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {

        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

}
