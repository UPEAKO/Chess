package com.example.ubd.chess.music;

import android.media.MediaPlayer;
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
        musicService.initPlayer();
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
                    case 4:
                        String URL = (String) msg.obj;
                        musicService.mediaPlayer.stop();
                        musicService.mediaPlayer.reset();
                        try {
                            musicService.mediaPlayer.setDataSource(URL);
                            musicService.mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        musicService.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                musicService.mediaPlayer.start();
                            }
                        });
                        break;
                    case 5:
                        String URlNext = (String) msg.obj;
                        musicService.mediaPlayer.stop();
                        musicService.mediaPlayer.reset();
                        try {
                            musicService.mediaPlayer.setDataSource(URlNext);
                            musicService.mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        musicService.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                musicService.mediaPlayer.start();
                            }
                        });
                        break;
                    case 6:
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
