package com.example.ubd.chess.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class musicService extends Service {
    MediaPlayer mediaPlayer = new MediaPlayer();
    /**
     * the music path list in sdcard
     */
    ArrayList<String> lastPathList = new ArrayList<>();
    /**
     * the number of music
     */
    int len = 0;
    /**
     * current music num
     */
    int location = 0;
    /**
     * myBinder object
     */
    private MyBinder myBinder = new MyBinder();
    /**
     * MusicObject
     */
    private MusicThread musicThread;

    public musicService() {

    }

    /**
     * personal binder definition
     */
    public class MyBinder extends Binder {
        musicService getService() {
            return musicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*new thread*/
        musicThread = new MusicThread(this);
        musicThread.start();
        /*监听并自动播放下一首*/
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Message msg = Message.obtain();
                msg.what = 6;
                musicThread.mHandler.sendMessage(msg);
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("functionTest", "onStartCommand: myOnStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*looper退出,子线程消息循环终止*/
        musicThread.mLooper.quit();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /**
     * 初始化本地音乐
     */
    public void initPlayer() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiami/audios");
        File [] fileList = file.listFiles();
        if (fileList.length > 0)
            for (File fileTemp:fileList) {
                String string = fileTemp.toString();
                if (string.endsWith(".mp3")||string.endsWith(".wav"))
                    lastPathList.add(string);
            }
        /*length*/
        len = lastPathList.size();
        if (lastPathList.size() > 0) {
            try {
                String str = lastPathList.get(location);
                mediaPlayer.setDataSource(str);
                /*must prepare at first*/
                mediaPlayer.prepare();
                //mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startMusic() {
        Message msg = Message.obtain();
        msg.what = 1;
        musicThread.mHandler.sendMessage(msg);
    }

    public void pauseMusic() {
        Message msg = Message.obtain();
        msg.what = 2;
        musicThread.mHandler.sendMessage(msg);
    }

    public void nextMusic() {
        Message msg = Message.obtain();
        msg.what = 3;
        musicThread.mHandler.sendMessage(msg);
    }

    public void currentUrlMusic(String string) {
        Message msg = Message.obtain();
        msg.obj = string;
        msg.what = 4;
        musicThread.mHandler.sendMessage(msg);
    }
}
