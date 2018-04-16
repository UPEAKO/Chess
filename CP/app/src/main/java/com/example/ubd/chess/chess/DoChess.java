package com.example.ubd.chess.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

public class DoChess extends View {
    /**
     * 子线程
     */
    public InternetPartOfChess internetPartOfChess;

    /**
     * uiHandler,这里处理uiHandler发送过来的消息
     */
    Handler uiHandler = new MyHandler(this);

    /**
     * chessTool对象
     */
    public chessTool chessTool;

    /**
     * 判断所有点位是否初始化的标志，避免重复执行
     */
    private boolean isInitChess = false;

    /**
     * 弱引用
     */
    private static class MyHandler extends Handler {
        private final WeakReference<DoChess> mDchess;

        private MyHandler(DoChess doChess) {
            mDchess = new WeakReference<>(doChess);
        }

        @Override
        public void handleMessage(Message msg) {
            DoChess doChess = mDchess.get();
            if (doChess != null) {
                super.handleMessage(msg);
                int chessNum = msg.arg1;
                int newChessLocation = msg.arg2;
                Log.d("测试", "doFor696:什么 "+msg.what);
                Log.d("测试", "doFor696:什么2 "+msg.arg1);
                switch (msg.what) {
                    case -2:
                        doChess.chessTool.doForChessMove(chessNum,newChessLocation);
                        break;
                    case -3:
                        doChess.chessTool.doForChessEat(chessNum,newChessLocation);
                        break;
                    case 666999:
                        if (chessNum == 666)
                            doChess.chessTool.doFor666();
                        else if (chessNum == 999)
                            doChess.chessTool.doFor999();
                        else if (chessNum == 696)
                            doChess.chessTool.doFor696();
                        break;
                    case 555:
                        //我方将领已亡,toast失败,设置不接受触碰消息,即能断绝一切交流
                        doChess.chessTool.gameOver(chessNum,newChessLocation);
                        break;
                    default:
                        break;
                }
            }
        }
    }



    /**
     * 构造函数,何时调用？
     * @param context
     * <p>上下文环境</p>
     */
    public DoChess(Context context) {
        super(context);
    }

    /**
     *
     * 构造函数
     * @param context
     * <p></p>
     * @param attrs
     * <p></p>
     */
    public DoChess(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        chessTool = new chessTool(this);
        internetPartOfChess = new InternetPartOfChess(uiHandler);
        internetPartOfChess.start();
    }

    /**
     * 构造函数,何时调用？
     * @param context
     * <p></p>
     * @param attrs
     * <p></p>
     * @param defStyleAttr
     ** <p></p>
     */
    public DoChess(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /**
     * 重写,获取父类给的尺寸,并能改变当前尺寸
     * @param widthMeasureSpec
     * <p>父类传来的宽度</p>
     * @param heightMeasureSpec
     * <p>父类传来的高度</p>
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        chessTool.width = MeasureSpec.getSize(widthMeasureSpec);
        chessTool.height = 10*chessTool.width/9;
        setMeasuredDimension(chessTool.width,chessTool.height);
        if (!isInitChess) {
            //初始化所有点位
            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 9; x++) {
                    Point point = new Point(x * chessTool.width / 9, y * chessTool.height / 10);
                    chessTool.chessLocation.add(point);
                }
            }
            isInitChess = true;
        }
    }

    /**
     * 重写
     * @param canvas
     * <p>画布</p>
     */
    @Override
    protected void onDraw(Canvas canvas) {
        chessTool.drawCheckerboard(canvas);
        if (chessTool.isRed)
            chessTool.drawRedChess(canvas);
        else
            chessTool.drawBlackChess(canvas);
    }

    /**
     * 点击事件，处理的出发地
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (chessTool.isMeToMove)
                chessTool.startDealTouch(currentX,currentY);
        }
        return true;
    }
}
