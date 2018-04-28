package com.example.ubd.chess.chess;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.ubd.chess.FirstActivity;
import com.example.ubd.chess.R;

import java.io.IOException;

public class ChessActivity extends FirstActivity {
    DoChess doChess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        doChess = findViewById(R.id.mChess);
        /*toolbar设置*/
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            doChess.internetPartOfChess.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
