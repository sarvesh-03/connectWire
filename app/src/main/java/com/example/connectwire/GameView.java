package com.example.connectwire;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameView extends View {
    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private Integer seconds;
    private Paint Red;
    private Paint Green;
    private Paint Blue;
    private Paint paintText1;
    private Paint paintText2;
    private ArrayList<Integer> Order;
    private ArrayList<Rect> rects;
    public void init(@Nullable AttributeSet attributeSet){
        seconds=10;
        paintText1=new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText2=new Paint(Paint.ANTI_ALIAS_FLAG);
        Red=new Paint(Paint.ANTI_ALIAS_FLAG);
        Red.setColor(Color.parseColor("#FF0000"));
        Green=new Paint(Paint.ANTI_ALIAS_FLAG);
        Green.setColor(Color.parseColor("#00FF00"));
        Blue=new Paint(Paint.ANTI_ALIAS_FLAG);
        Blue.setColor(Color.parseColor("#0000FF"));
        Order=new ArrayList<>();
        rects=new ArrayList<>();
        paintText1.setColor(Color.parseColor("#FFFFFF"));
        paintText1.setTextAlign(Paint.Align.CENTER);
        paintText1.setTextSize(150);
        paintText2.setColor(Color.parseColor("#FFFFFF"));
        paintText2.setTextAlign(Paint.Align.CENTER);
        paintText2.setTextSize(75);
        for(int i=0;i<6;i++){
            rects.add(new Rect());
        }

    }
    private int Height;
    private int Width;
    public boolean toStart=true;
    @Override
    protected void onDraw(Canvas canvas) {
          Height=getHeight();
          Width=getWidth();
          if(toStart){
              StartGame();
          }
        canvas.drawColor(Color.parseColor("#000000"));
          if(!isGameOver) {
              canvas.drawText(String.format("%02d",seconds),Width/2,150,paintText2);

              for (int i = 0; i < 6; i++) {
                  switch (Order.get(i)) {
                      case 1:
                          canvas.drawRect(rects.get(i), Red);

                          break;
                      case 2:
                          canvas.drawRect(rects.get(i), Green);

                          break;
                      case 3:
                          canvas.drawRect(rects.get(i), Blue);

                          break;
                      default:
                          break;
                  }

              }
          }
          else {
              canvas.drawText("GAME OVER",Width/2,Height/2,paintText1);
          }


    }


    public void AssignColor(){
        int a=-1,b=-1,c=-1;
        Random random=new Random();
        for(int i=0;i<1;i++)
            for (int j=1;j<=3;j++) {
                Order.add(j);
                Order.add(j);
            }
        Collections.shuffle(Order);
        if (Order.get(0) == Order.get(1) && Order.get(2) == Order.get(3)) {
            a=random.nextInt(2)+4;
            b=random.nextInt(4);
            c= Order.get(a);
            Order.set(a, Order.get(b));
            Order.set(b,c);

        }

    }
    public boolean isGameOver=false;
    private float initialY;
    private float diff;
    private float CurrentY;
    private int a=-1;
    private CountDownTimer countDownTimer;
    public boolean onTouchEvent(MotionEvent motionEvent){


        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
            initialY= motionEvent.getY();
            a=FindClickedWire(initialY);
            Log.v("tag",""+a);
            return true;
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_MOVE){

        }

        if(motionEvent.getAction()==MotionEvent.ACTION_MOVE&&a!=-1){
            if(rects.get(a).bottom>=300) {
                CurrentY = motionEvent.getY();
                diff = (int) (CurrentY - initialY);
                rects.get(a).top += diff;
                rects.get(a).bottom += diff;
                initialY = CurrentY;
            }
            else {
                diff=0;
                rects.get(a).top += 10;
                rects.get(a).bottom += 10;
            }

            invalidate();
            return true;
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_UP&&a!=-1){
            UpdateData();
            return false;
        }

        invalidate();
        boolean value=super.onTouchEvent(motionEvent);
        return value;
    }
    private void StartGame(){
        toStart=false;
        seconds=10;
        AssignColor();
        int gap=(Height-400)/6;
        for(int i=0;i<6;i++){
            rects.get(i).right=Width-40;
            rects.get(i).left=40;
            rects.get(i).top=(300+gap*i+20);
            rects.get(i).bottom=300+i*gap;
        }
        countDownTimer=new CountDownTimer(seconds*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds--;
                invalidate();
            }

            @Override
            public void onFinish() {
                 isGameOver=true;
                 invalidate();
            }
        }.start();


    }
    public int FindClickedWire(float x){
        int index=0;
        for(int i=0;i<6;i++){
            if(x>rects.get(i).bottom-40&&x<rects.get(i).top+40){
                return i;
            }

        }
        return -1;
    }

    public void UpdateData(){
        int swappedIndex=0;
        ArrayList<Integer> y=new ArrayList<>();
        for(int i=0;i<6;i++){
            y.add(rects.get(i).bottom);
        }
        Collections.sort(y);
        for(int i=0;i<6;i++){
            if(y.get(i)==rects.get(a).bottom){
                swappedIndex=i;
                break;
            }
        }
        Log.v("tag",""+a+"  "+swappedIndex);
        if(a!=swappedIndex) {
            int c = Order.get(a);
            Order.remove(a);
            Order.add(swappedIndex,c);
            Rect rect=rects.get(a);
            rects.remove(a);
            rects.add(swappedIndex,rect);
        }

        if(Order.get(0)==Order.get(1)&&Order.get(2)==Order.get(3)){
            Toast.makeText(getContext(),"U Won",Toast.LENGTH_SHORT).show();
            isGameOver=true;
            if(countDownTimer!=null)
            {
                countDownTimer.cancel();
                countDownTimer=null;
            }

        }
        invalidate();

    }
    public void finish(){
        Order.clear();
        rects.clear();
    }


}
