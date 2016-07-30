package com.example.jowang.pintu0729;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean gamestart=false;
    private boolean isrun=false;
    private ImageView[][] iv_game_arr=new ImageView[3][5];
    private GridLayout game;
    private ImageView nullimg;
    private GestureDetector gestureDetector;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureDetector=new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                int type=getGesture(motionEvent.getX(),motionEvent.getY(),motionEvent1.getX(),motionEvent1.getY());
                changeGesture(type);
                return true;
            }
        });
        game=(GridLayout)findViewById(R.id.grid);
        Bitmap bigm=((BitmapDrawable)getResources().getDrawable(R.drawable.pin)).getBitmap();
        int tuW=bigm.getWidth()/5;
        for (int i=0;i<iv_game_arr.length;i++){
            for (int j=0;j<iv_game_arr[i].length;j++){
                Bitmap bm=Bitmap.createBitmap(bigm,j*tuW,i*tuW,tuW,tuW);
                iv_game_arr[i][j]=new ImageView(this);
                iv_game_arr[i][j].setImageBitmap(bm);
                iv_game_arr[i][j].setPadding(2,2,2,2);
                iv_game_arr[i][j].setTag(new GameData(i,j,bm));
                iv_game_arr[i][j].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean flag=isNear((ImageView) view);
//                        Toast.makeText(MainActivity.this,""+flag,Toast.LENGTH_SHORT).show();
                        if (flag){
                            Exchange((ImageView)view);
                        }
                    }
                });
            }
        }
        for (int i=0;i<iv_game_arr.length;i++) {
            for (int j = 0; j < iv_game_arr[i].length; j++) {
                GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                p.width = Resources.getSystem().getDisplayMetrics().widthPixels / 5;
                p.height = p.width;
                game.addView(iv_game_arr[i][j], p);
            }
        }
        empty(iv_game_arr[2][4]);
        random();
        gamestart=true;
    }
    private void isGameOver(){
        boolean isGameover=true;
        for (int i=0;i<iv_game_arr.length;i++){
            for (int j=0;j<iv_game_arr[i].length;j++){
                if (iv_game_arr[i][j]==nullimg){
                    continue;
                }
                GameData gameData=(GameData)iv_game_arr[i][j].getTag();
                if (gameData.isTrue()){
                    isGameover=false;
                    break;
                }
            }
        }
        if (isGameover){
            Toast.makeText(this,"over",Toast.LENGTH_SHORT).show();
        }
    }

    private void random(){
        for (int i=0;i<20;i++){
            int type=(int)(Math.random()*4)+1;
            changeGesture(type,false);
        }
    }
    private void changeGesture(int type){
        changeGesture(type,true);
    }
    private void changeGesture(int type,boolean isAnim){
        GameData gameData=(GameData)nullimg.getTag();
        int newx=gameData.x;
        int newy=gameData.y;
        if (type==1){
            newx++;
        }else if (type==2){
            newx--;
        }else if (type==3){
            newy++;
        }else if (type==4){
            newy--;
        }
        if (newx>=0&&newx<iv_game_arr.length&&newy>=0&&newy<iv_game_arr[0].length){
            if (isAnim){
                Exchange(iv_game_arr[newx][newy]);
            }else {
                Exchange(iv_game_arr[newx][newy],isAnim);
            }

        }else {

        }
    }
    //gesture 1:up;2:down;3:left;4:right
    private int getGesture(float start_x,float start_y,float end_x,float end_y){
        boolean isLeftorRight=(Math.abs(start_x-start_y)>Math.abs(start_y-end_y))?true:false;
        if (isLeftorRight){
            boolean isLeft=start_x-end_x>0?true:false;
            if (isLeft){
                return 3;
            }else {
                return 4;
            }

        }else {
            boolean isUp=start_y-end_y>0?true:false;
            if (isUp){
                return 1;
            }else {
                return 2;
            }

        }
    }
    private void Exchange(final ImageView imageView){
        Exchange(imageView,true);
    }
    private void Exchange(final ImageView imageView,boolean isAnim){
        if (isrun){
            return;
        }

        if (!isAnim){
            GameData gameData=(GameData)imageView.getTag();
            nullimg.setImageBitmap(gameData.bitmap);
            GameData nullData=(GameData)nullimg.getTag();
            nullData.bitmap=gameData.bitmap;
            nullData.p_x=gameData.x;
            nullData.p_y=gameData.y;
            empty(imageView);
            if (gamestart){
                isGameOver();
            }
            return;
        }
        TranslateAnimation translateAnimation=null;
        if (imageView.getX()>nullimg.getX()){
            translateAnimation=new TranslateAnimation(0.1f,-imageView.getWidth(),0.1f,0.1f);
        }else if (imageView.getX()<nullimg.getX()){
            translateAnimation=new TranslateAnimation(0.1f,imageView.getWidth(),0.1f,0.1f);
        }else if (imageView.getY()>nullimg.getY()){
            translateAnimation=new TranslateAnimation(0.1f,0.1f,0.1f,-imageView.getHeight());
        }else if (imageView.getY()<nullimg.getY()){
            translateAnimation=new TranslateAnimation(0.1f,0.1f,0.1f,imageView.getHeight());
        }
        translateAnimation.setDuration(70);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isrun=true;
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                isrun=false;
                imageView.clearAnimation();
                GameData gameData=(GameData)imageView.getTag();
                nullimg.setImageBitmap(gameData.bitmap);
                GameData nullData=(GameData)nullimg.getTag();
                nullData.bitmap=gameData.bitmap;
                nullData.p_x=gameData.x;
                nullData.p_y=gameData.y;
                empty(imageView);
                if (gamestart){

                    isGameOver();
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(translateAnimation);
    }
    private void empty(ImageView imageView){
        imageView.setImageBitmap(null);
        nullimg=imageView;
    }
    private Boolean isNear(ImageView imageView){

        GameData nullgrid=(GameData)nullimg.getTag();
        GameData normalgrid=(GameData)imageView.getTag();
        if (nullgrid.y==normalgrid.y&&normalgrid.x+1==nullgrid.x){//upper than the empltygrid
            return true;
        }else if (nullgrid.y==normalgrid.y&&normalgrid.x==nullgrid.x+1){
            return true;
        }else if (nullgrid.y+1==normalgrid.y&&normalgrid.x==nullgrid.x){
            return true;
        }else if (nullgrid.y==normalgrid.y+1&&normalgrid.x==nullgrid.x){
            return true;
        }
        return false;
    }
    //each grid data:x,y
    class GameData{
        private int x;
        private int y;
        private Bitmap bitmap;
        private int p_x;
        private int p_y;

        private GameData(int x, int y,Bitmap bitmap) {
            this.x = x;
            this.y = y;
            this.bitmap = bitmap;
            this.p_x = x;
            this.p_y = y;
        }
        private boolean isTrue(){
            if (x==p_x&&y==p_y){
                return true;
            }
            return false;
        }
    }
}



















