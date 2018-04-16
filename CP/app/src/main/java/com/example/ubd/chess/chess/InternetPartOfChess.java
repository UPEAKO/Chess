package com.example.ubd.chess.chess;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class InternetPartOfChess extends Thread{
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;
    public Socket socket = null;
    /**
     * UI的handler
     */
    private Handler uiHandler;
    public Handler mHandler;
    InternetPartOfChess(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }
    /*run()方法体为该线程的逻辑执行处,这里初始化流后就开启了loop循环使该线程一直进行下去*/
    @Override
    public void run() {
        super.run();
        /*连接服务端,初始化输入输出流*/
        try {
            socket = new Socket("172.96.219.84",30000);
            if (!socket.isConnected() || socket.isClosed()) {
                Log.d("Socket连接诶", "run: 失败");
                return;
            }
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataInputStream = new DataInputStream(inputStream);
            Log.d("Socket连接诶", "run: 流初始化完成");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        /*这里导致handler不能初始化*/
        /**
         * 在这个子线程中又开启一条子线程专门读取服务端传来的消息,并非共用一个子线程
         */
        new Thread() {
            @Override
            public void run() {
                super.run();
                /*接收服务端消息(归属何在！？)*/
                while (true) {
                    try {
                        int chessNumInOpponent = dataInputStream.readInt();
                        Log.d("测试", "doFor696:最初始接收的参数： "+chessNumInOpponent);
                        int chessLocationInOpponent = 0;
                        int sign = 0;
                        //如果为先验消息,则不读取下列
                        if (chessNumInOpponent != 666&&chessNumInOpponent != 999&& chessNumInOpponent != 696) {
                             chessLocationInOpponent = dataInputStream.readInt();
                             sign = dataInputStream.readInt();
                        }
                        Log.d("Socket连接诶", "run: 若到此则未知阻塞");
                        Message msg = Message.obtain();
                        //移子
                        if (sign == -2) {
                            msg.what = -2;
                            msg.arg1 = chessNumInOpponent;
                            msg.arg2 = chessLocationInOpponent;
                            uiHandler.sendMessage(msg);
                        }
                        //吃子
                        if (sign == -3) {
                            msg.what = -3;
                            msg.arg1 = chessNumInOpponent;
                            msg.arg2 = chessLocationInOpponent;
                            uiHandler.sendMessage(msg);
                        }
                        //先验消息
                        if (sign == 0) {
                            msg.what = 666999;
                            msg.arg1 = chessNumInOpponent;
                            uiHandler.sendMessage(msg);
                        }
                        //我方将领死亡
                        if (sign == 555) {
                            msg.what = 555;
                            msg.arg1 = chessNumInOpponent;
                            msg.arg2 = chessLocationInOpponent;
                            uiHandler.sendMessage(msg);
                        }
                        Log.d("Socket连接诶", "run: 从服务端接收");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();



        /*消息循环开启就一直循环*/
        Looper.prepare();
        Looper mLooper = Looper.myLooper();

        mHandler = new Handler(mLooper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                /*这里导致每次发送消息都要等待接收消息*/
                switch (msg.what) {
                    case 11:
                        /*发送给服务端*/
                        int chessNumInOpponent2 = msg.arg1;
                        int chessLocationInOpponent2 = msg.arg2;
                        int sign2 = -2;
                        try {
                            dataOutputStream.writeInt(chessNumInOpponent2);
                            dataOutputStream.writeInt(chessLocationInOpponent2);
                            dataOutputStream.writeInt(sign2);
                            dataOutputStream.flush();
                            Log.d("Socket连接诶", "run: 发送给服务端");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 111:
                        int chessNumInOpponent3 = msg.arg1;
                        int chessLocationInOpponent3 = msg.arg2;
                        int sign3 = -3;
                        try {
                            dataOutputStream.writeInt(chessNumInOpponent3);
                            dataOutputStream.writeInt(chessLocationInOpponent3);
                            dataOutputStream.writeInt(sign3);
                            dataOutputStream.flush();
                            Log.d("Socket连接诶", "run: 发送给服务端");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 555:
                        int chessNumInOpponent4 = msg.arg1;
                        int chessLocationInOpponent4 = msg.arg2;
                        int sign4 = 555;
                        try {
                            dataOutputStream.writeInt(chessNumInOpponent4);
                            dataOutputStream.writeInt(chessLocationInOpponent4);
                            dataOutputStream.writeInt(sign4);
                            dataOutputStream.flush();
                            Log.d("Socket连接诶", "run: 发送给服务端");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
            /*这里不可*/
        };
        /*这里导致mHandler无法处理消息,即handlerMessage中的代码无法执行*/
        Looper.loop();
        /*这里开启循环后就到达不了*/
        Log.d("Socket连接诶", "run: 意味着到达loop循环外");

    }
}
