package com.example.ubd.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ubd.chess.chess.ChessActivity;
import com.example.ubd.chess.music.MusicActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("functionTest", "onCreate: MainActivityOnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonChess = findViewById(R.id.chessButton);
        Button buttonMusic = findViewById(R.id.musicButton);
        buttonChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChessActivity.class);
                startActivity(intent);
            }
        });
        buttonMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MusicActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("functionTest", "onCreate: MainActivityDestroy");
    }
}
