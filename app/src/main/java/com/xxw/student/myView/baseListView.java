package com.xxw.student.myView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import com.xxw.student.utils.LogUtils;

/**
 * 总体的baseListView,支持左滑删除，按钮还未添加上去
 * Created by xxw on 2016/4/18.
 */
public class baseListView extends ListView implements GestureDetector.OnGestureListener,View.OnTouchListener {

    private GestureDetector mGestureDetector;
    private boolean isOpenSlipBack = false;
    public baseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        this.setLongClickable(true);

        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);

    }

    public void setmGestureDetector(GestureDetector mGestureDetector){
        this.mGestureDetector = mGestureDetector;
    }

    public GestureDetector getmGestureDetector(){
        return this.mGestureDetector;
    }

    public void setIsOpenSlipBack(boolean isOpen){
        this.isOpenSlipBack = isOpen;
    }


    private float startY;
    private Rect storeList = new Rect();
    private boolean firstStore = true;
    @Override
    public boolean onDown(MotionEvent e) {
//        LogUtils.v("onDown");
//        LogUtils.v("左上角的点的坐标 ("+this.getLeft()+","+this.getTop()+"),设置好的top是 "+storeList.top);
        if (firstStore){
            storeList.set(this.getLeft(),this.getTop(),this.getRight(),this.getBottom());
            firstStore = false;
        }
        startY = e.getY();
        return false;
    }




    @Override
    public void onShowPress(MotionEvent e) {
//        LogUtils.v("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        LogUtils.v("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        LogUtils.v("左上角的点的坐标 ("+this.getLeft()+","+this.getTop()+"),设置好的top是 "+storeList.top);
        float y2 = e2.getY();
        float dy = y2-startY;
        startY = y2;
        if(isNeedMove()&& Math.abs(y2 - e1.getY())<=450){
            this.layout(this.getLeft(),this.getTop()+(int)dy/3,this.getRight(),this.getBottom()+(int)dy/3);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        LogUtils.v("onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        listMoveBack();
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOpenSlipBack){
            if (event.getX() <= 30) {
                LogUtils.v("ListView return false");
                return false;
            } else {
                return true;
            }
        }else {
            return true;
        }
    }


    //判断垂直滑动是否需要移动整个组件(即是否滑到顶部或者底部)
    public boolean isNeedMove(){
        int ScrollY = getScrollY();
        int ss = this.getMeasuredHeight()-this.getHeight();
        if (ScrollY==0||ScrollY==ss)
            return true;
        else
            return false;
    }

    public void listMoveBack(){
        Animation moveBack = new TranslateAnimation(this.getLeft(),this.getLeft(),this.getTop(),storeList.top);
        moveBack.setDuration(500);
        moveBack.setInterpolator(new DecelerateInterpolator());
        moveBack.setFillAfter(true);
        this.startAnimation(moveBack);
        this.layout(storeList.left,storeList.top,storeList.right,storeList.bottom);
    }

}
