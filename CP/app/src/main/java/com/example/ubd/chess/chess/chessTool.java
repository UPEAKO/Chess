package com.example.ubd.chess.chess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.ubd.chess.R;

import java.util.ArrayList;

public class chessTool {
    /**
     * 自定义view对象
     */
    private DoChess doChess;

    /**
     * 判断红黑方,决定选择哪个棋盘
     */
    public boolean isRed = true;

    /**
     * 控制己方是否可走子
     */
    boolean isMeToMove = true;

    /**
     * 画笔
     */
    private Paint paint = new Paint();

    /**
     * 棋盘点位List
     */
    ArrayList<Point> chessLocation = new ArrayList<>();

    /**
     * 当前所有棋子的位置
     */
    private ArrayList<Sub_rec> currentChessLocation = new ArrayList<>();

    /**
     * 获取控件尺寸到参数,在onMeasure()中指定
     */
    public int height = 0;
    public int width =0;

    /**
     * 棋子对应的所有可走位置
     */
    private ArrayList<Integer> allSubscriptOfAvaliblePoint = new ArrayList<>();

    /**
     * 构造函数
     */
    chessTool(DoChess doChess){
        this.doChess = doChess;
        //对手棋子初始化
        for (int i = 0; i < 9; i++) {
            Sub_rec sub_rec = new Sub_rec(i,true);
            currentChessLocation.add(sub_rec);
        }
        Sub_rec sub_rec19 = new Sub_rec(19,true);
        currentChessLocation.add(sub_rec19);
        Sub_rec sub_rec24 = new Sub_rec(25,true);
        currentChessLocation.add(sub_rec24);
        for (int i = 27; i <= 35; i+=2) {
            Sub_rec sub_rec = new Sub_rec(i,true);
            currentChessLocation.add(sub_rec);
        }
        //己方棋子初始化
        for (int i = 54; i <= 62; i+=2) {
            Sub_rec sub_rec = new Sub_rec(i,true);
            currentChessLocation.add(sub_rec);
        }
        Sub_rec sub_rec64 = new Sub_rec(64,true);
        currentChessLocation.add(sub_rec64);
        Sub_rec sub_rec70 = new Sub_rec(70,true);
        currentChessLocation.add(sub_rec70);
        for (int i = 81; i <= 89; i++) {
            Sub_rec sub_rec = new Sub_rec(i,true);
            currentChessLocation.add(sub_rec);
        }
    }

    public void gameOver(int chessNum,int newChessLocation) {
        int chessNumToDeath = searchChessNumBySubscript(newChessLocation);
        if (chessNumToDeath >= 0)
            currentChessLocation.get(chessNumToDeath).isLive = false;
        currentChessLocation.get(chessNum).subScript = newChessLocation;
        doChess.invalidate();
        //己方子不可走
        isMeToMove = false;
        Toast toast=Toast.makeText(doChess.getContext(), "您失败了", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 该函数执行时机有待考究
     * 接收到参数666,设为黑方
     */
    public void doFor666() {
        isRed = false;
        //设为不能移动
        isMeToMove = false;
        doChess.invalidate();
        Log.d("测试", "doFor696:找到对手,红子先行 ");
        //dailog显示找到对手,红子先行
        Toast toast=Toast.makeText(doChess.getContext(), "找到对手,红子先行", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 该函数执行时机有待考究
     * 接收到参999,设为红方不能移动且显示未找到对手，等待对手进入
     */
    public void doFor999() {
        isRed = true;
        isMeToMove =  false;
        doChess.invalidate();
        Log.d("测试", "doFor696:未找到对手,等待对方进入 ");
        //dailog显示未找到对手,等待对方进入
        Toast toast=Toast.makeText(doChess.getContext(), "未找到对手,等待对方进入", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 该函数执行时机有待考究
     * 接收参数696,设置为可以移动
     */
    public void doFor696() {
        isMeToMove = true;
        Log.d("测试", "doFor696:设为可移动 ");
        //dailog显示找到对手,请开始
        Toast toast=Toast.makeText(doChess.getContext(), "找到对手,请开始", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示对手走子动作,并设定己方可走子
     * @param chessNum
     * 棋子序号
     * @param newChessLocation
     * 棋子新点
     */
    public void doForChessMove(int chessNum,int newChessLocation) {
        currentChessLocation.get(chessNum).subScript = newChessLocation;
        doChess.invalidate();
        //己方可走子了
        isMeToMove = true;
        Toast toast=Toast.makeText(doChess.getContext(), "该你了", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示对手走子动作,并设定己方可走子
     * @param chessNum
     * 棋子序号
     * @param newChessLocation
     * 棋子新点
     */
    public void doForChessEat(int chessNum,int newChessLocation) {
        int chessNumToDeath = searchChessNumBySubscript(newChessLocation);
        if (chessNumToDeath >= 0)
            currentChessLocation.get(chessNumToDeath).isLive = false;
        currentChessLocation.get(chessNum).subScript = newChessLocation;
        doChess.invalidate();
        //己方子可走
        isMeToMove = true;
        Toast toast=Toast.makeText(doChess.getContext(), "该你了", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 根据下标查找当前在这个下标位置存活的棋子的序号
     * @param newChessLocation
     * 下标
     * @return
     * 序号
     */
    private int searchChessNumBySubscript(int newChessLocation) {
        for (int i = 0; i <= 31; i++) {
            if (currentChessLocation.get(i).subScript == newChessLocation&&currentChessLocation.get(i).isLive)
                return i;
        }
        return -1;
    }

    /**
     * 绘制棋盘
     * @param canvas
     * <p>画布</p>
     */
    public void drawCheckerboard(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        int mWith = width/18;
        int mHeight = height/20;
        //外围
        path.moveTo(width/18,height/20);
        path.lineTo(width/18,19*height/20);
        path.lineTo(17*width/18,19*height/20);
        path.lineTo(17*width/18,height/20);
        path.close();
        //内部网格

        //所有横向线
        for (int i = 1; i <= 8; i++) {
            path.moveTo(mWith,(2*i+1)*mHeight);
            path.lineTo(17*mWith,(2*i+1)*mHeight);
        }
        //所有纵向线
        for (int i = 1; i <= 7; i++) {
            path.moveTo((2*i+1)*mWith,mHeight);
            path.lineTo((2*i+1)*mWith,9*mHeight);
            path.moveTo((2*i+1)*mWith,11*mHeight);
            path.lineTo((2*i+1)*mWith,19*mHeight);
        }
        //将帅区
        path.moveTo(7*mWith,mHeight);
        path.lineTo(11*mWith,5*mHeight);
        path.moveTo(11*mWith,mHeight);
        path.lineTo(7*mWith,5*mHeight);

        path.moveTo(7*mWith,15*mHeight);
        path.lineTo(11*mWith,19*mHeight);
        path.moveTo(7*mWith,19*mHeight);
        path.lineTo(11*mWith,15*mHeight);

        canvas.drawPath(path,paint);
    }

    /**
     * 红方绘制棋子
     * @param canvas
     * <p>画布</p>
     */
    public void drawRedChess(Canvas canvas) {
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b5),currentChessLocation.get(0),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b4),currentChessLocation.get(1),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b3),currentChessLocation.get(2),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b2),currentChessLocation.get(3),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b1),currentChessLocation.get(4),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b2),currentChessLocation.get(5),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b3),currentChessLocation.get(6),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b4),currentChessLocation.get(7),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b5),currentChessLocation.get(8),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b6),currentChessLocation.get(9),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b6),currentChessLocation.get(10),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(11),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(12),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(13),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(14),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(15),canvas);



        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(16),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(17),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(18),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(19),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(20),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r6),currentChessLocation.get(21),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r6),currentChessLocation.get(22),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r5),currentChessLocation.get(23),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r4),currentChessLocation.get(24),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r3),currentChessLocation.get(25),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r2),currentChessLocation.get(26),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r1),currentChessLocation.get(27),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r2),currentChessLocation.get(28),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r3),currentChessLocation.get(29),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r4),currentChessLocation.get(30),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r5),currentChessLocation.get(31),canvas);
    }

    /**
     * 黑方绘制棋子
     * @param canvas
     * <p>画布</p>
     */
    public void drawBlackChess(Canvas canvas) {
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r5),currentChessLocation.get(0),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r4),currentChessLocation.get(1),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r3),currentChessLocation.get(2),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r2),currentChessLocation.get(3),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r1),currentChessLocation.get(4),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r2),currentChessLocation.get(5),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r3),currentChessLocation.get(6),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r4),currentChessLocation.get(7),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r5),currentChessLocation.get(8),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r6),currentChessLocation.get(9),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r6),currentChessLocation.get(10),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(11),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(12),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(13),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(14),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.r7),currentChessLocation.get(15),canvas);



        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(16),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(17),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(18),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(19),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b7),currentChessLocation.get(20),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b6),currentChessLocation.get(21),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b6),currentChessLocation.get(22),canvas);

        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b5),currentChessLocation.get(23),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b4),currentChessLocation.get(24),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b3),currentChessLocation.get(25),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b2),currentChessLocation.get(26),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b1),currentChessLocation.get(27),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b2),currentChessLocation.get(28),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b3),currentChessLocation.get(29),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b4),currentChessLocation.get(30),canvas);
        judgeDrawBitmap(BitmapFactory.decodeResource(doChess.getResources(), R.drawable.b5),currentChessLocation.get(31),canvas);
    }

    /**
     * 改变bitmap大小,使之适应棋盘
     * @param bitmap
     * <p>有待缩放到bitmap</p>
     * @return
     * <p>返回缩放后的bitmap</p>
     */
    private Bitmap scaleBitmap(Bitmap bitmap){
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(((float)(width/9))/bitmapWidth,((float) (height/10))/bitmapHeight);
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmapWidth,bitmapHeight,matrix,false);
        return bitmap;
    }

    /**
     * 棋子绘制前判断存在性并绘制
     * @param bitmap
     * <p>可直接用于绘制的bitmap</p>
     * @param sub_rec
     * <p>当前用于棋子的点位信息</p>
     * @param canvas
     * <p>画布</p>
     */
    private void judgeDrawBitmap(Bitmap bitmap,Sub_rec sub_rec,Canvas canvas) {
        if (sub_rec.isLive)
            canvas.drawBitmap(scaleBitmap(bitmap),getLocationBySubscript(sub_rec.subScript).x,getLocationBySubscript(sub_rec.subScript).y,null);
    }

    /**
     * 由数组下标获取当前棋子坐标
     * @param subscript
     * <p>传入的数组下标</p>
     * @return
     * <p>返回棋子坐标</p>
     */
    private Point getLocationBySubscript(int subscript) {
        return new Point(chessLocation.get(subscript).x,chessLocation.get(subscript).y);
    }

    /**
     * 处理触碰坐标的开始处
     * @param x
     * <p>触碰点的x坐标</p>
     * @param y
     * <p>触碰点的y坐标</p>
     */
    public void startDealTouch(float x,float y) {
        actionForSubscript(x,y);
        doChess.invalidate();
    }

    /**
     * 判断xy坐标所对应的点位（xy只在当前控件内）
     * @param x
     * <p>触碰点x</p>
     * @param y
     * <p>触碰点y</p>
     * @return
     * xy对应数组下标,此处默认一定有对应关系,倘若没有则返回-1
     */
    private int getTouchLocationByXY(float x,float y) {
        for (int i = 0; i <= 89; i++) {
            if (x > chessLocation.get(i).x&&x < chessLocation.get(i).x+width/9&&y > chessLocation.get(i).y&&y < chessLocation.get(i).y+height/10)
                return i;
        }
        return -1;
    }

    /**
     * 判断触碰点是否在棋子上
     * @param touchSubscript
     * <p>触碰点对应点位下标</p>
     * @return
     * 在棋子上,返回棋子序号,否则返回-1
     */
    private int isOnChess(int touchSubscript) {
        for (int i = 0; i <= 31; i++) {
            if (currentChessLocation.get(i).subScript == touchSubscript&&currentChessLocation.get(i).isLive/*确保当前棋子存在*/)
                return i;
        }
        return -1;
    }

    /**
     * 分类处理触碰操作
     */
    private void actionForSubscript(float x,float y) {
        int currentLocation = getTouchLocationByXY(x,y);
        int chess = isOnChess(currentLocation);
        if (chess >= 16/*说明触碰点在己方棋子上*/) {
            //设置所有可走位置
            setAllAvaliblePoint(chess);
        }
        //发出两条消息,前一条使使死亡的对手仍然死亡,后一条将其救活,故组织后一条消息发出
        else if (chess >= 0) {
            //对手将位置为4,每次执行都判断是否对方将消失
            if (searchInallSubscriptOfAvaliblePoint(currentLocation)) {
                //下一步不能走子
                isMeToMove = false;
                //对方子消失
                currentChessLocation.get(chess).isLive = false;
                //己方子移过去
                currentChessLocation.get(allSubscriptOfAvaliblePoint.get(0)).subScript = currentLocation;
                //发送需要处理的消息到子线程（这里吃子,故需要传递两个参数,己方哪个棋子到了哪里,对于那个位置原有的棋子,由传过去的位置来查找,然后将其置为不可见
                int chessNumInOpponent = 31-allSubscriptOfAvaliblePoint.get(0);
                int chessLocationInOpponent = 89-currentLocation;
                Message msg = Message.obtain();
                msg.arg1 = chessNumInOpponent;
                msg.arg2 = chessLocationInOpponent;
                if (chess != 4) {
                    msg.what = 111;
                } else {
                    //如果消失的棋子为对方将
                    msg.what = 555;
                    //dailog显示我方胜利
                    Toast toast=Toast.makeText(doChess.getContext(), "你赢了", Toast.LENGTH_LONG);
                    toast.show();
                }
                doChess.internetPartOfChess.mHandler.sendMessage(msg);
                //最后清空
                allSubscriptOfAvaliblePoint.clear();
            }
        }
        else {
            if (searchInallSubscriptOfAvaliblePoint(currentLocation)) {
                //己方棋子移过去
                currentChessLocation.get(allSubscriptOfAvaliblePoint.get(0)).subScript = currentLocation;
                //下一步不能走子
                isMeToMove = false;
                Log.d("Socket连接诶", "run: 处理棋子");
                //发送需要处理的消息到子线程,消息内容（这里只走子,不吃子,故需要传递出己方哪个棋子的位置变到了哪里这个消息,且消息为转换后的消息,对方直接使用）：
                if (doChess.internetPartOfChess.mHandler == null) {
                    Log.d("Socket连接诶", "run: mHandler未能初始化");
                    return;
                }
                int chessNumInOpponent = 31-allSubscriptOfAvaliblePoint.get(0);
                int chessLocationInOpponent = 89-currentLocation;
                Message msg = Message.obtain();
                //两个数据,两个一
                msg.what = 11;
                msg.arg1 = chessNumInOpponent;
                msg.arg2 = chessLocationInOpponent;
                doChess.internetPartOfChess.mHandler.sendMessage(msg);
                //最后才能清空
                allSubscriptOfAvaliblePoint.clear();
            }
        }
    }

    /**
     * 查找当前点所对应的下标是否在存储点位中
     * @param currentLocation
     * <p>下标</p>
     * @return
     * 返回是否在存储点位中
     */
    private boolean searchInallSubscriptOfAvaliblePoint(int currentLocation) {
        for (int i = 1; i < allSubscriptOfAvaliblePoint.size(); i++) {
            if (currentLocation == allSubscriptOfAvaliblePoint.get(i))
                return true;
        }
        return false;
    }

    /**
     * 对己方棋子按不同角色设置所有可行路径
     * @param currentChess
     * <p>用于设置路径的初始棋子的序号</p>
     */
    private void setAllAvaliblePoint(int currentChess) {
        allSubscriptOfAvaliblePoint.clear();
        //己方兵,序号16~20
        if (currentChess >= 16&&currentChess <= 20) {
            allSubscriptOfAvaliblePoint.add(currentChess);
            setAllAvaliblePointOfBing(currentChess);
        }
        if (currentChess == 21||currentChess == 22) {
            allSubscriptOfAvaliblePoint.add(currentChess);
            setAllAvaliblePointOfPao(currentChess);
        }
        if (currentChess == 23||currentChess == 31) {
            allSubscriptOfAvaliblePoint.add(currentChess);
            setAllAvaliblePointOfJv(currentChess);
        }
        if (currentChess == 24||currentChess == 30) {
            allSubscriptOfAvaliblePoint.add(currentChess);
            setAllAvaliblePointOfMa(currentChess);
        }
        if (currentChess == 25||currentChess == 29) {
            allSubscriptOfAvaliblePoint.add(currentChess);
            setAllAvaliblePointOfXiang(currentChess);
        }
        if (currentChess == 26||currentChess == 28) {
            allSubscriptOfAvaliblePoint.add(currentChess);
            setAllAvaliblePointOfShi(currentChess);
        }
        if (currentChess == 27) {
            allSubscriptOfAvaliblePoint.add(currentChess);
            setAllAvaliblePointOfJiang(currentChess);
        }
    }

    /**
     * 设置兵的可行点
     * @param currentChess
     * <p>当前兵的序号</p>
     */
    private void setAllAvaliblePointOfBing(int currentChess) {
        //仍然在楚河汉界以南,前进
        if (currentChessLocation.get(currentChess).subScript >= 45) {
            if (landingFeasibleBecauseNoUs(currentChessLocation.get(currentChess).subScript - 9)) {
                allSubscriptOfAvaliblePoint.add(currentChessLocation.get(currentChess).subScript - 9);
            }
        }
        //在楚河汉界以北,前进,左,右
        else {
            //left
            if (getLocationBySubscript(currentChessLocation.get(currentChess).subScript).x - width / 9 >= 0 && landingFeasibleBecauseNoUs(currentChessLocation.get(currentChess).subScript - 1))
                allSubscriptOfAvaliblePoint.add(currentChessLocation.get(currentChess).subScript - 1);
            //right
            if (getLocationBySubscript(currentChessLocation.get(currentChess).subScript).x + width / 9 < width && landingFeasibleBecauseNoUs(currentChessLocation.get(currentChess).subScript + 1))
                allSubscriptOfAvaliblePoint.add(currentChessLocation.get(currentChess).subScript + 1);
            //front
            if (getLocationBySubscript(currentChessLocation.get(currentChess).subScript).y - height / 10 >= 0 && landingFeasibleBecauseNoUs(currentChessLocation.get(currentChess).subScript - 9))
                allSubscriptOfAvaliblePoint.add(currentChessLocation.get(currentChess).subScript - 9);
        }
    }

    /**
     * 设置当前炮的可行点
     * @param currentChess
     * <p>当前炮的序号</p>
     */
    private void setAllAvaliblePointOfPao(int currentChess) {
        //上
        int topSubscript = currentChessLocation.get(currentChess).subScript - 9;
        while (topSubscript >= 0) {
            if (landingFeasibleBecauseNoAll(topSubscript))
                allSubscriptOfAvaliblePoint.add(topSubscript);
            else
                break;
            topSubscript-=9;
        }
        topSubscript-=9;
        while (topSubscript >= 0) {
            if (landingFeasibleBecauseNoAll(topSubscript))
                topSubscript-=9;
            else {
                if (landingFeasibleBecauseNoUs(topSubscript))
                    allSubscriptOfAvaliblePoint.add(topSubscript);
                break;
            }
        }
        //下
        int bottomSubscript = currentChessLocation.get(currentChess).subScript + 9;
        while (bottomSubscript <= 89) {
            if (landingFeasibleBecauseNoAll(bottomSubscript))
                allSubscriptOfAvaliblePoint.add(bottomSubscript);
            else
                break;
            bottomSubscript+=9;
        }
        bottomSubscript+=9;
        while (bottomSubscript <= 89) {
            if (landingFeasibleBecauseNoAll(bottomSubscript))
                bottomSubscript+=9;
            else {
                if (landingFeasibleBecauseNoUs(bottomSubscript))
                    allSubscriptOfAvaliblePoint.add(bottomSubscript);
                break;
            }
        }
        //左
        int leftSubscript = currentChessLocation.get(currentChess).subScript - 1;
        int leftX = getLocationBySubscript(currentChessLocation.get(currentChess).subScript).x - width/9;
        while (leftSubscript >= 0&&leftX >= 0) {
            if (landingFeasibleBecauseNoAll(leftSubscript))
                allSubscriptOfAvaliblePoint.add(leftSubscript);
            else
                break;
            leftSubscript-=1;
            leftX-=width/9;
        }
        leftSubscript-=1;
        leftX-=width/9;
        while (leftSubscript >= 0&&leftX >= 0) {
            if (landingFeasibleBecauseNoAll(leftSubscript)) {
                leftSubscript-=1;
                leftX-=width/9;
            }
            else {
                if (landingFeasibleBecauseNoUs(leftSubscript))
                    allSubscriptOfAvaliblePoint.add(leftSubscript);
                break;
            }
        }
        //右
        int rightSubscript = currentChessLocation.get(currentChess).subScript + 1;
        int rightX = getLocationBySubscript(currentChessLocation.get(currentChess).subScript).x + width/9;
        while (rightSubscript <= 89&&rightX < width) {
            if (landingFeasibleBecauseNoAll(rightSubscript))
                allSubscriptOfAvaliblePoint.add(rightSubscript);
            else
                break;
            rightSubscript+=1;
            rightX+=width/9;
        }
        rightSubscript+=1;
        rightX+=width/9;
        while (rightSubscript <= 89&&rightX < width) {
            if (landingFeasibleBecauseNoAll(rightSubscript)) {
                rightSubscript+=1;
                rightX+=width/9;
            }
            else {
                if (landingFeasibleBecauseNoUs(rightSubscript))
                    allSubscriptOfAvaliblePoint.add(rightSubscript);
                break;
            }
        }
    }

    /**
     * 设置当前车的可行点
     * @param currentChess
     * 当前车的序号
     */
    private void setAllAvaliblePointOfJv(int currentChess) {
        //上
        int topSubscript = currentChessLocation.get(currentChess).subScript - 9;
        while (topSubscript >= 0) {
            if (landingFeasibleBecauseNoAll(topSubscript))
                allSubscriptOfAvaliblePoint.add(topSubscript);
            else
                break;
            topSubscript-=9;
        }
        if (landingFeasibleBecauseNoUs(topSubscript)&&topSubscript>=0)
            allSubscriptOfAvaliblePoint.add(topSubscript);
        //下
        int bottomSubscript = currentChessLocation.get(currentChess).subScript + 9;
        while (bottomSubscript <= 89) {
            if (landingFeasibleBecauseNoAll(bottomSubscript))
                allSubscriptOfAvaliblePoint.add(bottomSubscript);
            else
                break;
            bottomSubscript+=9;
        }
        if (landingFeasibleBecauseNoUs(bottomSubscript)&&bottomSubscript<=89)
            allSubscriptOfAvaliblePoint.add(bottomSubscript);
        //左
        int leftSubscript = currentChessLocation.get(currentChess).subScript - 1;
        int leftX = getLocationBySubscript(currentChessLocation.get(currentChess).subScript).x - width/9;
        while (leftSubscript >= 0&&leftX >= 0) {
            if (landingFeasibleBecauseNoAll(leftSubscript))
                allSubscriptOfAvaliblePoint.add(leftSubscript);
            else
                break;
            leftSubscript-=1;
            leftX-=width/9;
        }
        if (landingFeasibleBecauseNoUs(leftSubscript)&&leftX>=0)
            allSubscriptOfAvaliblePoint.add(leftSubscript);
        //右
        int rightSubscript = currentChessLocation.get(currentChess).subScript + 1;
        int rightX = getLocationBySubscript(currentChessLocation.get(currentChess).subScript).x + width/9;
        while (rightSubscript <= 89&&rightX < width) {
            if (landingFeasibleBecauseNoAll(rightSubscript))
                allSubscriptOfAvaliblePoint.add(rightSubscript);
            else
                break;
            rightSubscript+=1;
            rightX+=width/9;
        }
        if (landingFeasibleBecauseNoUs(rightSubscript)&&rightX<width)
            allSubscriptOfAvaliblePoint.add(rightSubscript);
    }

    /**
     * 设置当前马的可行点
     * @param currentChess
     * 当前马的序号
     */
    private void setAllAvaliblePointOfMa(int currentChess) {
        int mSubscript = currentChessLocation.get(currentChess).subScript;
        int currentX = getLocationBySubscript(mSubscript).x;
        int currentY = getLocationBySubscript(mSubscript).y;
        //上侧左边
        if (isLocationIn(currentX-width/9,currentY-2*height/10)&&landingFeasibleBecauseNoAll(mSubscript-9)&&landingFeasibleBecauseNoUs(mSubscript-19))
            allSubscriptOfAvaliblePoint.add(mSubscript-19);
        //上侧右边
        if (isLocationIn(currentX+width/9,currentY-2*height/10)&&landingFeasibleBecauseNoAll(mSubscript-9)&&landingFeasibleBecauseNoUs(mSubscript-17))
            allSubscriptOfAvaliblePoint.add(mSubscript-17);
        //左侧上面
        if (isLocationIn(currentX-2*width/9,currentY-height/10)&&landingFeasibleBecauseNoAll(mSubscript-1)&&landingFeasibleBecauseNoUs(mSubscript-11))
            allSubscriptOfAvaliblePoint.add(mSubscript-11);
        //左侧下面
        if (isLocationIn(currentX-2*width/9,currentY+height/10)&&landingFeasibleBecauseNoAll(mSubscript-1)&&landingFeasibleBecauseNoUs(mSubscript+7))
            allSubscriptOfAvaliblePoint.add(mSubscript+7);
        //右侧上面
        if (isLocationIn(currentX+2*width/9,currentY-height/10)&&landingFeasibleBecauseNoAll(mSubscript+1)&&landingFeasibleBecauseNoUs(mSubscript-7))
            allSubscriptOfAvaliblePoint.add(mSubscript-7);
        //右侧下面
        if (isLocationIn(currentX+2*width/9,currentY+height/10)&&landingFeasibleBecauseNoAll(mSubscript+1)&&landingFeasibleBecauseNoUs(mSubscript+11))
            allSubscriptOfAvaliblePoint.add(mSubscript+11);
        //下面左侧
        if (isLocationIn(currentX-width/9,currentY+2*height/10)&&landingFeasibleBecauseNoAll(mSubscript+9)&&landingFeasibleBecauseNoUs(mSubscript+17))
            allSubscriptOfAvaliblePoint.add(mSubscript+17);
        //下面右侧
        if (isLocationIn(currentX+width/9,currentY-2*height/10)&&landingFeasibleBecauseNoAll(mSubscript+9)&&landingFeasibleBecauseNoUs(mSubscript+19))
            allSubscriptOfAvaliblePoint.add(mSubscript+19);
    }

    /**
     * 设置当前象的可行点
     * @param currentChess
     * 当前象的序号
     */
    private void setAllAvaliblePointOfXiang(int currentChess) {
        int mSubscript = currentChessLocation.get(currentChess).subScript;
        int currentX = getLocationBySubscript(mSubscript).x;
        int currentY = getLocationBySubscript(mSubscript).y;
        //左上
        if (isLocationIn(currentX-2*width/9,currentY-2*height/10)&&landingFeasibleBecauseNoAll(mSubscript-10)&&landingFeasibleBecauseNoUs(mSubscript-20)&&(mSubscript-20)>45)
            allSubscriptOfAvaliblePoint.add(mSubscript-20);
        //右上
        if (isLocationIn(currentX+2*width/9,currentY-2*height/10)&&landingFeasibleBecauseNoAll(mSubscript-8)&&landingFeasibleBecauseNoUs(mSubscript-16)&&(mSubscript-16)>45)
            allSubscriptOfAvaliblePoint.add(mSubscript-16);
        //左下
        if (isLocationIn(currentX-2*width/9,currentY+2*height/10)&&landingFeasibleBecauseNoAll(mSubscript+8)&&landingFeasibleBecauseNoUs(mSubscript+16))
            allSubscriptOfAvaliblePoint.add(mSubscript+16);
        //右下
        if (isLocationIn(currentX+2*width/9,currentY+2*height/10)&&landingFeasibleBecauseNoAll(mSubscript+10)&&landingFeasibleBecauseNoUs(mSubscript+20))
            allSubscriptOfAvaliblePoint.add(mSubscript+20);
    }

    /**
     * 设置当前士的可行点
     * @param currentChess
     * 当前士的序号
     */
    private void setAllAvaliblePointOfShi(int currentChess) {
        int mSubscript = currentChessLocation.get(currentChess).subScript;
        //周围四个点
        if ((mSubscript == 66||mSubscript == 68||mSubscript == 84||mSubscript == 86)&&landingFeasibleBecauseNoUs(76))
            allSubscriptOfAvaliblePoint.add(76);
        //中心点
        if (mSubscript == 76&&landingFeasibleBecauseNoUs(66))
            allSubscriptOfAvaliblePoint.add(66);
        if (mSubscript == 76&&landingFeasibleBecauseNoUs(68))
            allSubscriptOfAvaliblePoint.add(68);
        if (mSubscript == 76&&landingFeasibleBecauseNoUs(84))
            allSubscriptOfAvaliblePoint.add(84);
        if (mSubscript == 76&&landingFeasibleBecauseNoUs(86))
            allSubscriptOfAvaliblePoint.add(86);
    }

    /**
     * 设置当前将的可行点
     * @param currentChess
     * 当前将的序号
     */
    private void setAllAvaliblePointOfJiang(int currentChess) {
        int mSubscript = currentChessLocation.get(currentChess).subScript;
        //left
        if (getLocationBySubscript(mSubscript).x - width / 9 >= 3*width/9 && landingFeasibleBecauseNoUs(mSubscript - 1))
            allSubscriptOfAvaliblePoint.add(mSubscript - 1);
        //right
        if (getLocationBySubscript(mSubscript).x + width / 9 <= 5*width/9 && landingFeasibleBecauseNoUs(mSubscript + 1))
            allSubscriptOfAvaliblePoint.add(mSubscript + 1);
        //front
        if (getLocationBySubscript(mSubscript).y - height / 10 >= 7*height/10 && landingFeasibleBecauseNoUs(mSubscript - 9))
            allSubscriptOfAvaliblePoint.add(mSubscript - 9);
        //behind
        if (getLocationBySubscript(mSubscript).y + height / 10 <= 9*height/10 && landingFeasibleBecauseNoUs(mSubscript + 9))
            allSubscriptOfAvaliblePoint.add(mSubscript + 9);
    }

    /**
     * 对于给定点,判断是否在棋盘内
     * @param nextX
     * <p>给定点的X</p>
     * @param nextY
     * <p>给定点的Y</p>
     * @return
     * 返回是否在棋盘内
     */
    private boolean isLocationIn(int nextX,int nextY) {
        return nextX >= 0&&nextX < width&&nextY >= 0&&nextY < height;
    }

    /**
     * 判断按规则产生的落点是否实际能够使用;否定该点为己方棋子
     * @param aroundSubscript
     * 周围点点位下标
     * @return
     * 是否己方棋子
     */
    private boolean landingFeasibleBecauseNoUs(int aroundSubscript) {
        for (int i = 16; i <= 31; i++) {
            if (aroundSubscript == currentChessLocation.get(i).subScript&&currentChessLocation.get(i).isLive)
                return false;
        }
        return true;
    }

    /**
     * 判断按规则产生的落点是否实际能够使用;否定该点为棋子
     * @param aroundSubscript
     * 周围点点位下标
     * @return
     * 是否棋子
     */
    private boolean landingFeasibleBecauseNoAll (int aroundSubscript) {
        for (int i = 0; i <= 31; i++) {
            if (aroundSubscript == currentChessLocation.get(i).subScript&&currentChessLocation.get(i).isLive)
                return false;
        }
        return true;
    }
}
