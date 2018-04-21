package com.example.ubd.chess.music;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

public class MusicThread extends Thread {
    public Handler mHandler;
    private musicService musicService;
    public Looper mLooper;

    public MusicThread(musicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void run() {
        super.run();
        android.util.Log.d("functionTest", "Thread1");
        musicService.initPlayer();
        android.util.Log.d("functionTest", "Thread2");
        Looper.prepare();
        mLooper = Looper.myLooper();
        mHandler = new Handler(mLooper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        musicService.mediaPlayer.start();
                        break;
                    case 2:
                        musicService.mediaPlayer.pause();
                        break;
                    case 3:
                        musicService.mediaPlayer.stop();
                        musicService.mediaPlayer.reset();
                        musicService.location++;
                        musicService.location = musicService.location%musicService.len;
                        try {
                            musicService.mediaPlayer.setDataSource(musicService.lastPathList.get(musicService.location));
                            musicService.mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        musicService.mediaPlayer.start();
                        break;
                }
            }
        };
        Looper.loop();
        android.util.Log.d("functionTest", "Thread3");
    }
}
