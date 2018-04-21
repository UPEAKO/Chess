package com.example.ubd.chess.music;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.ubd.chess.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class musicService extends Service {
    //storage，标志为sharedPrefForsign的值
    SharedPreferences sharedPref;
    //为sharedPref记录位置，标志固定为sign,即其中只有一个数据
    SharedPreferences sharedPrefForsign;
    MediaPlayer mediaPlayer = new MediaPlayer();
    //the music path list in sdcard
    ArrayList<String> lastPathList = new ArrayList<>();
    //the number of music
    int len = 0;
    //current music num
    int location = 0;
    //current UrlMusic num
    int UrlLocation = 0;
    //myBinder object
    private MyBinder myBinder = new MyBinder();
    //MusicObject
    private MusicThread musicThread;


    public musicService() {

    }

    /**
     * write
     */
    public void writeMyURL(String string) {
        int defaultNum = 0;
        int sign = sharedPrefForsign.getInt("sign",defaultNum);
        SharedPreferences.Editor editorForSign = sharedPrefForsign.edit();
        editorForSign.putInt("sign",++sign);
        editorForSign.apply();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Integer.toString(sign),string);
        //异步写入
        editor.apply();
    }

    /**
     * read
     */
    public String readMyURL(String string) {
        String defaultURL = getString(R.string.defaultURL);
        return sharedPref.getString(string,defaultURL);
    }

    //personal binder definition
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
        //new thread
        musicThread = new MusicThread(this);
        musicThread.start();
        sharedPref = this.getSharedPreferences(getString(R.string.pre_key),Context.MODE_PRIVATE);
        sharedPrefForsign = this.getSharedPreferences(getString(R.string.pre_key_sign),Context.MODE_PRIVATE);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("functionTest", "onStartCommand: myOnStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //looper退出,子线程消息循环终止
        musicThread.mLooper.quit();
        Log.d("functionTest", "onDestroy: onDestroy");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /**
     * 遍历整个sdcard,找到所有音频文件
     * 可以成功遍历，但比较耗时，除了虾米音乐，其余都是无意义的音频,需要新的遍历思路
     * file.getName仅仅得到文件名，并不包含完整路径
     */
    /*private void getAllMusicInSdCard(File file) {
        if (!file.exists())
            return;
        File[] fileList = file.listFiles();
        if (fileList == null)
            return;
        for (File tempFile:fileList) {
            //toString可获得完整路径
            String string = tempFile.toString();
            if (tempFile.isFile()&&(string.endsWith(".mp3")||string.endsWith(".wav"))) {
                lastPathList.add(string);
                Log.d("functionTest", "music: "+string);
                }
            else if(tempFile.isDirectory()) {

                getAllMusicInSdCard(tempFile);
            }
        }
    }*/

    public void initPlayer() {
        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        //use too much time
        //getAllMusicInSdCard(file);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiami/audios");
        File [] fileList = file.listFiles();
        if (fileList.length > 0)
            for (File fileTemp:fileList) {
                String string = fileTemp.toString();
                if (string.endsWith(".mp3")||string.endsWith(".wav"))
                    lastPathList.add(string);
                Log.d("music", "initPlayer: "+string);
            }
        //length
        len = lastPathList.size();
        if (lastPathList.size() > 0) {
            try {
                String str = lastPathList.get(location);
                mediaPlayer.setDataSource(str);
                //must prepare at first
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

    public void UrlMusic(String string) {
        Message msg = Message.obtain();
        msg.obj = string;
        msg.what = 4;
        musicThread.mHandler.sendMessage(msg);
    }

    public void nextUrlMusic() {
        UrlLocation ++;
        int allMusic = sharedPrefForsign.getInt("sign",1);
        //%防止超出当前歌曲的范围
        UrlLocation = UrlLocation%allMusic;
        //下一首的URL地址
        String string = readMyURL(Integer.toString(UrlLocation));
        Message msg = Message.obtain();
        msg.what = 5;
        msg.obj = string;
        musicThread.mHandler.sendMessage(msg);
    }
}
