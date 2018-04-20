package com.example.ubd.chess.music;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

public class MusicThread extends Thread {
    public Handler mHandler;
    private musicService musicService;

    public MusicThread(musicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void run() {
        super.run();
        musicService.initPlayer();
        Looper.prepare();
        Looper mLooper = Looper.myLooper();
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
    }
}
