package com.example.ubd.chess.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ubd.chess.R;

public class MusicActivity extends AppCompatActivity {
    public musicService musicService;
    public MusicDatabaseHelper musicDatabaseHelper;

    /**
     * activity与service的链接
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((musicService.MyBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * bindServer与startService区别：
     * 1.bindService在service未启动的情况下,也可以启动service并绑定；否则仅仅绑定
     * 2.startService仅仅启动服务

     *这些为软链接
     *sdcard/1688quick
     *storage/sdcard0/1688quick
     *实际位置
     *storage/emulated/0/1688quick

     *一个Service被使用startService方法启动，
     * 不管是否调用了bindService（绑定服务）或unbindService（解除绑定服务）到该Service，
     * 该Service都会在后台运行并不受影响。
     * 一个Service被使用startService方法启动多少次，
     * onCreate方法只会调用一次，
     * onStartCommand方法将会被调用多次（与startService的次数一致），
     * 且系统只会创建一个Service实例（结束该Service也只需要调用一次stopService），
     * 该Service会一直在后台运行直至调用stopService或调用自身的stopSelf方法。

     * 问：当 flags 不等于 BIND_AUTO_CREATE 时，bindService还会自动启动service吗？
     *答：不会。
     *问：当 flags 不等于 BIND_AUTO_CREATE，调用bindService前service未启动。一段时间之后，service被start，此时系统还会尝试之前的bind动作吗？
     *答：会。
     *其中BIND_AUTO_CREATE为l6进制int数

     * 2.BindService
     * 如果一个Service在某个Activity中被调用bindService方法启动，
     * 不论bindService被调用几次，Service的onCreate方法只会执行一次，
     * 同时onStartCommand方法始终不会调用。当建立连接后，Service会一直运行，
     * 除非调用unbindService来接触绑定、断开连接或调用该Service的Context不存在了
     * （如Activity被Finish——即通过bindService启动的Service的生命周期依附于启动它的Context），
     * 系统在这时会自动停止该Service。
     *
     * 3.StartService AND BindService：
     * 当一个Service在被启动(startService 的同时又被绑定(bindService ),
     * 该Service将会一直在后台运行，并且不管调用几次，
     * onCreate方法始终只会调用一次，
     * onStartCommand的调用次数与startService 调用的次数一致（使用bindService 方法不会调用onStartCommand）。
     * 同时，调用unBindService 将不会停止Service，必须调用stopService 或Service自身的stopSelf 来停止服务。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        /*这些操作针对本地音乐*/
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        /*添加新歌*/
        Button buttonSet = findViewById(R.id.buttonSetText);
        final EditText editText = findViewById(R.id.editText);
        final EditText editTextSongName = findViewById(R.id.editTextSongName);
        /*启动并绑定服务*/
        Intent intent = new Intent(this,musicService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        /*数据库建立*/
        musicDatabaseHelper = new MusicDatabaseHelper(this,"musicDatabase",1);
        /*在activity中设置recyclerView*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MusicAdapter musicAdapter = new MusicAdapter(this);
        /*添加默认分割线*/
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(musicAdapter);
        /*播放*/
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.startMusic();
            }
        });
         /*暂停*/
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.pauseMusic();
            }
        });
        /*下一首*/
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.nextMusic();
            }
        });
        /*将音乐链接存入数据库*/
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringUrl = editText.getText().toString();
                String stringName = editTextSongName.getText().toString();
                writeMyURL(stringUrl,stringName);
                editText.setText("");
                editTextSongName.setText("");
            }
        });
    }

    /**
     * 插入歌曲数据
     */
    public void writeMyURL(String stringUrl,String stringSongName) {
        /*插入歌曲数据*/
        musicDatabaseHelper.getReadableDatabase().execSQL("insert into musicUrl values(null,?,?)",new String[] {stringUrl,stringSongName});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*断开与数据库的链接*/
        musicDatabaseHelper.close();
        /*activity挂了，自动解除绑定*/
        if (!musicService.mediaPlayer.isPlaying()) {
            musicService.stopSelf();
        }
    }
}
