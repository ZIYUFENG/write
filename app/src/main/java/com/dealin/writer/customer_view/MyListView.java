package com.dealin.writer.customer_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TODO: document your custom view class.
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener{
    private Context context;
    private View headerView;
    private LinearLayout footerView;
    private int firstItemIndex;
    private int lastItemIndex;
    private int currentScrollState;
    private int startY;
    private boolean isRecorded=true;
    private boolean isFooterRecorded = true;
    private static final double PULL_FACTORY = 0.6;
    //回弹时每次减少的高度
    private static final int PULL_BACK_REDUCE_STEP = 1;
    //回弹时递减headview高度的频率, 注意以纳秒为单位
    private static final int PULL_BACK_TASK_PERIOD = 1000;
    //处理ui改变的逻辑
    private Handler handler;
    //该类能够按一定的频率重复多次执行一个任务
    private ScheduledExecutorService scheduleExecutor;

    public MyListView(Context context) {
        super(context);
        init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        context = getContext();
        setOnScrollListener(this);
        headerView = new View(context);
        footerView = new LinearLayout(context);
        headerView.setLayoutParams(new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        footerView.setLayoutParams(new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        addHeaderView(headerView,null,false);
        addFooterView(footerView,null,false);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (isRecorded) {
                    LayoutParams layoutParams = (LayoutParams) headerView.getLayoutParams();
                    layoutParams.height -= PULL_BACK_REDUCE_STEP;
                    headerView.setLayoutParams(layoutParams);
                    headerView.invalidate();
                    if (layoutParams.height <= 0) {
                        isRecorded = false;
                        isFooterRecorded = false;
                        scheduleExecutor.shutdownNow();
                    }
                } else {
                    LayoutParams layoutParams = (LayoutParams) footerView.getLayoutParams();
                    layoutParams.height -= PULL_BACK_REDUCE_STEP;
                    footerView.setLayoutParams(layoutParams);
                    footerView.invalidate();
                    if (layoutParams.height <= 0) {
                        isFooterRecorded = false;
                        isRecorded = false;
                        scheduleExecutor.shutdownNow();
                    }
                }

            }
        };
    }

    //分发触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下时，listview是否滑到最顶部
                if (firstItemIndex== 0) {
                    isRecorded = true;
                    Log.d("isRecorded", "true");
                    startY = (int) ev.getY();

                }
                if (lastItemIndex == getCount() - 1) {
                    isFooterRecorded = true;
                    Log.d("isFRecorded", "true");

                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //移动时，如果滑到第一个条目同样记录下来
                if (!isRecorded && firstItemIndex == 0) {
                    isRecorded = true;
                }
                if (!isFooterRecorded && lastItemIndex == getCount()-1) {
                    isFooterRecorded = true;
                }
                if (!isRecorded&&!isFooterRecorded) {
                    //未被记录，证明未滑到第一个条目，不执行弹性效果逻辑
                    break;
                }
                //记录下此时触摸的Y坐标
                int tempY = (int) ev.getY();
                //计算移动的距离
                int moveY = tempY - startY;
                if (moveY < 0) {
                    //向上滑动，
                    //执行弹性效果逻辑
                    Log.d("向上滑动", "sd");
                    LayoutParams lpa = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (moveY*PULL_FACTORY));
                    footerView.setLayoutParams(new android.widget.AbsListView.LayoutParams(lpa));
                    footerView.invalidate();

                } else {
                    //执行弹性效果逻辑
                    Log.d("向下滑动", "sd");
                    headerView.setLayoutParams(new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.MATCH_PARENT, (int) (moveY*PULL_FACTORY)));
                    headerView.invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //松开手指时执行回弹
                if (!isRecorded&&!isFooterRecorded) {
                    break;
                }
                scheduleExecutor = Executors.newScheduledThreadPool(1);
                scheduleExecutor.scheduleAtFixedRate(new Runnable() {

                    @Override
                    public void run() {
                        handler.obtainMessage().sendToTarget();

                        Log.i("testFixedRate", "xxxxxxxxxx");
                    }
                }, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public void onScroll(AbsListView view, int firstVisiableItem,
                         int visibleItemCount, int totalItemCount) {
        firstItemIndex = firstVisiableItem;
        lastItemIndex = firstVisiableItem + visibleItemCount;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        currentScrollState = scrollState;
    }
}
