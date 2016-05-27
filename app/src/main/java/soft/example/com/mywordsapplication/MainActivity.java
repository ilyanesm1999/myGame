package soft.example.com.mywordsapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by admin on 27.12.2015.
 */
public class MainActivity extends Activity {

    public int row_count = 1,col_count = 1;
    MyGame game;
    MyDBHelper dbhelper;
    TableLayout tbl;
    TableRow tr;
    ListView listFounded;
    Button loose;
    Button refresh;
    Button back;
    MyView[][] mvs = new MyView[row_count][col_count];
    String touchedWord;

    TextView scoreView;
    public void refreshGameField(View v){
        game.mode = 1;
        for(int i=0;i<row_count;i++) {
            for (int j = 0; j < col_count; j++) {
                mvs[i][j].filled = false;
                //mvs[i][j].touched = false;
            }
        }
        game.fillWords(0, 0, game.getRandomWord(row_count * col_count),0);
        //
        //очистка founded
        dbhelper.clearFoundedDatabase();

    }


    public  void setFieldSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        row_count = (height / MyView.VIEW_HEIGHT)-1;
        col_count = width / MyView.VIEW_WIDTH;
        mvs = new MyView[row_count][col_count];
    }

    public void loose(View vv){
        int color;
        game.mode = 2;
        for(int i=0;i<row_count;i++) {
            for (int j = 0; j < col_count; j++) {
                color = mvs[i][j].looseColor;
                mvs[i][j].setTextColor(color);
            }
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new MyDBHelper(this);

        setFieldSize();

        game = new MyGame(dbhelper,row_count,col_count);

        setContentView(R.layout.main_activity);

        tbl = (TableLayout)findViewById(R.id.myGameTableLetters);
        scoreView = (TextView)findViewById(R.id.scoreTextView);
        scoreView.setText(String.valueOf(game.score));

        //tbl.setFocusable(true);
        //tbl.setFocusableInTouchMode(true);
        //listFounded = (ListView)findViewById(R.id.myListFoundedWords);

        drawGameField();
        game.fillWords(0, 0, game.getRandomWord(row_count * col_count),0);
        Log.i("mytag", "Hello 99");
        dbhelper.clearFoundedDatabase();
    }

    public void goBack(View v){
        /*
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
*/
        //If you don't want to return data:

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        //finishActivity(1);

    }



    private void drawGameField(){
        MyView mv;

        //tbl.splitMotionEvents();
        for(int i=0;i<row_count;i++){
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(

                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            //tr.setFocusable(true);
            //tr.setFocusableInTouchMode(true);
            for(int j=0;j<col_count;j++){
                mv = new MyView(this);
/*
                ShapeDrawable sd = new ShapeDrawable();
                sd.setShape(new RectShape());
                sd.getPaint().setColor(Color.RED);
                sd.getPaint().setStrokeWidth(10f);
                sd.getPaint().setStyle(Paint.Style.STROKE);
*/

                // from xml
                mv.setBackgroundResource(R.drawable.button_bg_stroke);

                GradientDrawable gd = new GradientDrawable(); //(GradientDrawable) mv.getBackground();
                gd.setColor(Color.parseColor("#FFDAB9"));

                int width_px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, getResources().getDisplayMetrics());
                gd.setStroke(width_px, Color.parseColor("#191970"));
                mv.setBackground(gd);



                //mv.setBackgroundColor(defaultButtonBackColor);



                mv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                // set
                /*
                mv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
*/
                mv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int x = (int)event.getRawX();
                        int y = (int)event.getRawY();
                        MyView me = (MyView)v;
                        for(int ii=0;ii < row_count;ii++) {
                            for(int jj=0;jj < col_count;jj++) {
                                if ((!mvs[ii][jj].touched) && (mvs[ii][jj] != me) && inViewInBounds(mvs[ii][jj], x, y)) {
                                    mvs[ii][jj].dispatchTouchEvent(event);
                                    mvs[ii][jj].setBackgroundResource(R.drawable.button_stoke);;
                                }
                            }
                        }
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            touchedWord = me.letter;
                            me.setBackgroundResource(R.drawable.button_stoke);;
                            me.touched = true;
                        }
                        if(event.getAction() == MotionEvent.ACTION_UP){
                            //Log.i("mytag", "touched word is " + touchedWord);
                            if(game.isWord(touchedWord)){
                                if(game.isFoundedWord(touchedWord)){
                                    Log.i("mytag", " Founded yet " + touchedWord);
                                    Toast.makeText(MainActivity.this, "Находили ранее-"+touchedWord, Toast.LENGTH_LONG).show();
                                }else {
                                    // перебрать все views в пути и каждому установить founded = true
                                    Log.i("mytag", " GOOD touched word is " + touchedWord);
                                    Toast.makeText(MainActivity.this, "Вы нашли слово-"+touchedWord, Toast.LENGTH_LONG).show();
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    //toast.setText("пришли из main по cancel");
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    toast.setView(layout);

                                    Uri notification = Uri.parse("android.resource://soft.example.com.mywordsapplication/" + R.raw.win);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                    r.play();


                                    toast.show();

                                    if(game.mode != 2){
                                        game.score += touchedWord.length() * 1;
                                        scoreView.setText(String.valueOf(game.score));
                                        // сохраняем в БД
                                        dbhelper.setValueByKey("score", game.score);
                                    }
                                    game.setWordFounded(touchedWord);
                                }
                            }else{
                                Log.i("mytag", " NOT WORD touched word is " + touchedWord);
                                Toast.makeText(MainActivity.this, "Такого слова нет-"+touchedWord, Toast.LENGTH_LONG).show();
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                //toast.setText("пришли из main по cancel");
                                toast.setDuration(Toast.LENGTH_SHORT);
                                //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                toast.setView(layout);

                                Uri notification = Uri.parse("android.resource://soft.example.com.mywordsapplication/" + R.raw.lose);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                r.play();


                                toast.show();
                            }

                            for(int ii=0;ii < row_count;ii++) {
                                for(int jj=0;jj < col_count;jj++) {
                                    mvs[ii][jj].touched = false;
                                    //mvs[ii][jj].setBackgroundColor(defaultButtonBackColor);
                                    mvs[ii][jj].setBackgroundResource(R.drawable.button_bg_stroke);
                                }
                            }
                        }
                        if(event.getAction() == MotionEvent.ACTION_MOVE){
                            if(!me.touched){
                                touchedWord += me.letter;
                                //me.dispatchTouchEvent(new MotionEvent.ACTION_DOWN);

                                me.touched = true;
                            }
                        }


                        return false;
                    }
                });

                // \set
                //mv.setLetter(String.valueOf(k));k++;

                mv.setFocusable(true);
                mv.setFocusableInTouchMode(true);
                tr.addView(mv);
                mvs[i][j] = mv;
            }
            tbl.addView(tr);
        }
        game.views = mvs;

    }

    Rect outRect = new Rect();
    int[] location = new int[2];

    private boolean inViewInBounds(View view, int x, int y){
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }


}