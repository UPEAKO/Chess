package com.example.ubd.chess.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ubd.chess.R;

import java.io.IOException;

public class ChessActivity extends AppCompatActivity {

    /**
     *
     */
    DoChess doChess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        doChess = findViewById(R.id.mChess);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("返回测试", "onStop: 停止");
        try {
            doChess.internetPartOfChess.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
