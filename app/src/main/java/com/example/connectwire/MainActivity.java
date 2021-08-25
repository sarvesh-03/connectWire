package com.example.connectwire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
   private GameView gameView;
   private Button Start;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView=findViewById(R.id.gameView);
        ((View)gameView).setVisibility(View.INVISIBLE);
        Start=findViewById(R.id.button);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start.setVisibility(View.INVISIBLE);
                ((View)gameView).setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(Start.getVisibility()==View.VISIBLE) {
            super.onBackPressed();
        }
        else if(gameView.isGameOver){
            gameView.isGameOver=false;
            gameView.finish();
            gameView.init(null);
            ((View)gameView).setVisibility(View.INVISIBLE);
            Start.setVisibility(View.VISIBLE);
            gameView.toStart=true;
        }

    }
}