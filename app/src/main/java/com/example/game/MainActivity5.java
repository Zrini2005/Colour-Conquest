package com.example.game;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity5 extends AppCompatActivity {
    private int m;
    private int buttonsize;
    private boolean timevar;
    private int currentPlayer =1;
    private int[][] scores;
    private int[][] grid;
    private RelativeLayout layout;
    private GridLayout outercontainer;
    private TextView textView1,textView2,textView3;
    private List<String> gameHistory = new ArrayList<>();
    private SoundPool soundPool;
    private int clicksound;
    private TextView player1,player2,player3;
    private String player1name,player2name,player3name;
    private int expandsound;
    private int wrngbtn;
    private TextView timerTextView;
    private TextView timerTextView3;
    private long timerStartTime;
    private long pausetime;
    private long totalTime;
    private boolean timerPaused;
    private TextView timerTextView2;
    private CountDownTimer timer2;
    private CountDownTimer timer3;
    private CountDownTimer timer;

    private long timerStartTime2;
    private long pausetime2;
    private long totalTime2;
    private boolean timerPaused2;
    private long timerStartTime3;
    private long pausetime3;
    private long totalTime3;
    private boolean timerPaused3;
    private boolean player1out=false;
    private boolean player2out=false;
    private boolean player3out=false;

    private boolean player1firstturn = true;
    private boolean player2firstturn = true;
    private boolean player3firstturn = true;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private boolean layoutstyle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        timerTextView = findViewById(R.id.Timer1);
        timerTextView2 = findViewById(R.id.Timer2);
        timerTextView3 = findViewById(R.id.Timer3);
        timevar= getIntent().getBooleanExtra("timevar",false);
        layoutstyle=getIntent().getBooleanExtra("layout",true);
        if(timevar) {
            startTimer(30000);
            startTimer2(30000);
            startTimer3(30000);
            pauseTimer2();
            pauseTimer3();
        }
        player1=findViewById(R.id.player1name);
        player2=findViewById(R.id.player2name);
        player3=findViewById(R.id.player3name);
        player1name=getIntent().getStringExtra("player1");
        player2name=getIntent().getStringExtra("player2");
        player3name=getIntent().getStringExtra("player3");
        player1.setText(player1name);
        player2.setText(player2name);
        player3.setText(player3name);
        String mstring= getIntent().getStringExtra("m");
        m = Integer.parseInt(mstring);
        scores = new int[m][m];
        grid = new int[m][m];
        progressBar=findViewById(R.id.progressBar);
        progressBar.setMax(30);
        progressBar2=findViewById(R.id.progressBar2);
        progressBar2.setMax(30);
        progressBar3=findViewById(R.id.progressBar3);
        progressBar3.setMax(30);
        if(!timevar){
            progressBar.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            progressBar3.setVisibility(View.GONE);
        }


        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();

        clicksound = soundPool.load(this, R.raw.sound1, 1);
        expandsound = soundPool.load(this, R.raw.sound2, 1);
        wrngbtn=soundPool.load(this, R.raw.wrongbtn, 1);
        layout=findViewById(R.id.relative);
        outercontainer = findViewById(R.id.buttonContainer);
        outercontainer.setColumnCount(1);
        Button button1 = findViewById(R.id.xbutton);
        textView1=findViewById(R.id.score1);
        textView2=findViewById(R.id.score2);
        textView3 = findViewById(R.id.score3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1=new Dialog(MainActivity5.this);
                dialog1.setContentView(R.layout.dialog3);
                Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog1.setCancelable(false);
                dialog1.getWindow().getAttributes().windowAnimations = R.style.animation;

                Button button1=dialog1.findViewById(R.id.button3);
                Button button2=dialog1.findViewById(R.id.button4);
                Button button3=dialog1.findViewById(R.id.button5);
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reset();
                        dialog1.dismiss();

                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();

                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity5.this, MainActivity.class);
                        intent.putStringArrayListExtra("gameHistory", (ArrayList<String>) gameHistory);
                        startActivity(intent);
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        });

        GridLayout innercontainer = new GridLayout(this);
        innercontainer.setColumnCount(m);
        outercontainer.addView(innercontainer);
        if(m==5) {
             buttonsize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1);
        } else if (m==7) {
             buttonsize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)+20;
        } else if (m==6) {
             buttonsize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)+10;
        } else if (m==4) {
             buttonsize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)-20;
        } else{
             buttonsize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)-30;
        }
        for (int i = 0; i < m * m; i++) {
            Button button = new Button(this);
            button.setTag(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick((Button) v);
                }
            });

            button.setTextColor(getColor(android.R.color.white));
            button.setTypeface(null,Typeface.BOLD);
            if(layoutstyle) {
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
            }
            else{
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox2, null));
            }
            button.setGravity(Gravity.CENTER);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = buttonsize;
            params.height = buttonsize;
            params.setMargins(10, 10, 10, 10);

            innercontainer.addView(button, params);
        }




    }
    private void onButtonClick(Button button) {
        soundPool.play(clicksound, 1, 1, 0, 0, 1);
        int index = (int) button.getTag();
        int row = index / m;
        int col = index % m;




        if(grid[row][col]==currentPlayer || grid[row][col]==0) {
            ObjectAnimator XUp = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
            ObjectAnimator XDown = ObjectAnimator.ofFloat(button, "scaleX", 1.2f, 1f);
            ObjectAnimator YUp = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);
            ObjectAnimator YDown = ObjectAnimator.ofFloat(button, "scaleY", 1.2f, 1f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(XUp,YUp,XDown, YDown);
            animatorSet.setDuration(200);
            animatorSet.start();
            grid[row][col] = currentPlayer;
            if (currentPlayer == 1 && player1firstturn) {
                scores[row][col] = 3;
                player1firstturn = false;
            } else if (currentPlayer == 2 && player2firstturn) {
                scores[row][col] = 3;
                player2firstturn = false;
            } else if (currentPlayer == 3 && player3firstturn) {
                scores[row][col] = 3;
                player3firstturn = false;
            } else {
                scores[row][col] += 1;
            }
            if (scores[row][col] >= 4) {
                scores[row][col] = 0;
                grid[row][col]=0;
                button.setTextColor(getResources().getColor(android.R.color.white));
                button.setTypeface(null,Typeface.BOLD);
                if(layoutstyle) {
                    button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
                }
                else{
                    button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox2, null));
                }
                button.setText("");
                expand(row, col);
            }
            else {
                if(currentPlayer==1) {
                    if (layoutstyle) {
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto, null));
                    }
                    else{
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo1, null));

                    }
                }
                else if(currentPlayer==2){
                    if (layoutstyle) {
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto2, null));
                    }
                    else{
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo2, null));

                    }
                }
                else{
                    if (layoutstyle) {
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto3, null));
                    }
                    else{
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo3, null));

                    }
                }
                button.setText(String.valueOf(scores[row][col]));
                button.setTypeface(null,Typeface.BOLD);
                button.setTextColor(getColor(R.color.white));
            }
            if(currentPlayer==1) currentPlayer=2;
            else if(currentPlayer==2) currentPlayer=3;
            else currentPlayer=1;
            if(currentPlayer==1 && player1out){
                currentPlayer=2;
            }
            if(currentPlayer==2 && player2out){
                currentPlayer=3;
            }
            if(currentPlayer==3 && player3out){
                currentPlayer=1;
            }
            if(currentPlayer==1){
                layout.setBackgroundResource(R.drawable.backround);
                outercontainer.setBackgroundResource(R.drawable.backround);
                rotateButtons();
                if(timevar) {

                    resumeTimer();
                    if(!timerPaused2) {
                        pauseTimer2();
                    }
                    if (!timerPaused3) {
                        pauseTimer3();
                    }

                }
            }

            else if(currentPlayer==2) {
                layout.setBackgroundResource(R.drawable.backround2);
                outercontainer.setBackgroundResource(R.drawable.backround2);
                rotateButtons();
                if(timevar) {
                    resumeTimer2();
                    if (!timerPaused) {
                        pauseTimer();
                    }
                    if (!timerPaused3) {
                        pauseTimer3();
                    }

                }
            }
            else{
                layout.setBackgroundResource(R.drawable.backround3);
                outercontainer.setBackgroundResource(R.drawable.backround3);
                rotateButtons();
                if(timevar) {
                    resumeTimer3();
                    if (!timerPaused) {
                        pauseTimer();
                    }
                    if (!timerPaused2) {
                        pauseTimer2();
                    }

                }
            }
            int i=check(grid);
            if(i==1){
                if(timevar) {
                    updateHistory(player1name + " wins! in Timed Mode");
                }
                else{
                    updateHistory(player1name + " wins!");
                }

                terminate(player1name);
            }
            else if(i==2){
                if(timevar) {
                    updateHistory(player2name + " wins! in Timed Mode");
                }
                else{
                    updateHistory(player2name + " wins!");
                }

                terminate(player2name);
            } else if (i==3) {
                if(timevar) {
                    updateHistory(player3name + " wins! in Timed Mode");
                }
                else{
                    updateHistory(player3name + " wins!");
                }

                terminate(player3name);
            }
        }
        else  soundPool.play(wrngbtn, 1, 1, 0, 0, 1);
        updateScores();

    }

    private void expand(int row, int col) {
        soundPool.play(expandsound, 1, 1, 0, 0, 1);
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int newrow = row + dx[i];
            int newcol = col + dy[i];
            if (isValid(newrow, newcol)) {

                scores[newrow][newcol]++;
                grid[newrow][newcol]=currentPlayer;
                Button button = getButtonAt(newrow, newcol);
                ObjectAnimator XUp = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
                ObjectAnimator XDown = ObjectAnimator.ofFloat(button, "scaleX", 1.2f, 1f);
                ObjectAnimator YUp = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);
                ObjectAnimator YDown = ObjectAnimator.ofFloat(button, "scaleY", 1.2f, 1f);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(XUp,YUp, XDown,YDown);
                animatorSet.setDuration(200);
                animatorSet.start();
                if (scores[newrow][newcol] >= 4) {
                    scores[newrow][newcol] = 0;
                    grid[newrow][newcol]=0;
                    button.setTextColor(getColor(android.R.color.white));
                    button.setTypeface(null,Typeface.BOLD);
                    if(layoutstyle) {
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
                    }
                    else{
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox2, null));
                    }
                    button.setText("");
                    expand(newrow, newcol);
                }

                else {

                    if(currentPlayer==1) {
                        if (layoutstyle) {
                            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto, null));
                        }
                        else{
                            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo1, null));

                        }
                    }
                    else if(currentPlayer==2){
                        if (layoutstyle) {
                            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto2, null));
                        }
                        else{
                            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo2, null));

                        }
                    }
                    else{
                        if (layoutstyle) {
                            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto3, null));
                        }
                        else{
                            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo3, null));

                        }
                    }
                    button.setTextColor(getColor(R.color.white));
                    button.setTypeface(null,Typeface.BOLD);
                    button.setText(String.valueOf(scores[newrow][newcol]));

                }

            }

        }
    }

    @Nullable
    private Button getButtonAt(int row, int col) {
        int index = row * m + col;
        View view = findViewById(R.id.buttonContainer).findViewWithTag(index);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < m && col >= 0 && col < m;
    }
    private int check(int[][] grid){
        boolean a=false,b=false,c=false;
        int sum=0;
        for(int i=0;i<m;i++){
            for(int j=0;j<m;j++){
                if(grid[i][j]==1){
                    a=true;
                }
                else if(grid[i][j]==2){
                    b=true;
                } else if (grid[i][j]==3) {
                    c=true;
                }
                sum+=grid[i][j];

            }
        }
        if(a && !b && !c && sum!=1){
            return 1;
        }
        else if(!a && b && !c){
            return 2;
        } else if (!a && !b && c){
            return 3;
        }
        return 0;
    }
    private void terminate(String s){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog2);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView textView1 = dialog.findViewById(R.id.textview1);
        textView1.setText(s);
        TextView text1 = dialog.findViewById(R.id.okay_text);
        TextView text2 = dialog.findViewById(R.id.cancel_text);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                 reset();
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity5.this, MainActivity.class);
                intent.putStringArrayListExtra("gameHistory", (ArrayList<String>) gameHistory);
                startActivity(intent);
                dialog.dismiss();

            }
        });


        dialog.show();

    }
    private void reset(){
        currentPlayer = 1;
        layout.setBackgroundResource(R.drawable.backround);
        outercontainer.setBackgroundResource(R.drawable.backround);
        player1firstturn = true;
        player2firstturn = true;
        player3firstturn = true;
        player1out=false;
        player2out=false;
        player3out=false;
        progressBar.setProgress(30);
        progressBar2.setProgress(30);
        progressBar3.setProgress(30);
        rotateButtons();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                scores[i][j] = 0;
                grid[i][j]=0;
                Button button=getButtonAt(i,j);
                button.setTextColor(getResources().getColor(android.R.color.white));
                button.setTypeface(null,Typeface.BOLD);
                if(layoutstyle) {
                    button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
                }
                else{
                    button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox2, null));
                }
                button.setText("");

            }
        }
        if (timevar) {

            if (timer != null) {
                timer.cancel();
                timerTextView.setText("00:30");
            }
            if (timer2 != null) {
                timer2.cancel();
                timerTextView2.setText("00:30");
            }
            if (timer3 != null) {
                timer3.cancel();
                timerTextView3.setText("00:30");
            }
            startTimer(30000);
            startTimer2(30000);
            startTimer3(30000);

            pauseTimer2();

            pauseTimer3();
        }
    }
    private int score(int a){
        int sum1=0;
        int sum2=0;
        int sum3=0;
        for(int i=0;i<m;i++){
            for(int j=0;j<m;j++){
                if(grid[i][j]==1){
                    sum1+=scores[i][j];
                }
                else if(grid[i][j]==2){
                    sum2+=scores[i][j];
                }
                else if(grid[i][j]==3){
                    sum3+=scores[i][j];
                }
            }
        }
        if(a==1) return sum1;
        else if(a==2) return sum2;
        else if(a==3) return sum3;
        return 0;
    }
    private void updateScores() {
        textView1.setText(String.valueOf(score(1)));
        textView2.setText(String.valueOf(score(2)));
        textView3.setText(String.valueOf(score(3)));
    }
    private void updateHistory(String result) {
        if (gameHistory.size() >=10) {
            gameHistory.remove(gameHistory.size() - 1);
        }
        gameHistory.add(0, result);

    }
    private void rotateButtons() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                Button button = getButtonAt(i, j);
                if (button != null) {
                    if (currentPlayer == 1) {
                        button.setRotationX(0);
                        button.setRotationY(0);
                        button.setRotation(0);

                    } else if (currentPlayer == 2) {
                        button.setRotationX(180);
                        button.setRotationY(180);
                        button.setRotation(0);


                    }else{
                        button.setRotation(90);
                        button.setRotationX(0);
                        button.setRotationY(0);
                    }
                }
            }
        }
    }
    private void startTimer(long time) {
        totalTime = (int)time;
        timerStartTime = System.currentTimeMillis();
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long remainingTime = millisUntilFinished;
                long seconds = remainingTime / 1000;
                timerTextView.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                progressBar.setProgress((int) seconds);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");

                player1out=true;
                currentPlayer=2;
                layout.setBackgroundResource(R.drawable.backround2);
                outercontainer.setBackgroundResource(R.drawable.backround2);
                rotateButtons();
                if(timevar) {
                    resumeTimer2();
                    if(!timerPaused) {
                        pauseTimer();
                    }
                    if(!timerPaused3) {
                        pauseTimer3();
                    }

                }
                if(player2out && player1out && !player3out){
                    updateHistory(player3name + " wins! in Timed Mode");
                    terminate(player3name );
                }
                if(!player2out && player1out && player3out){
                    updateHistory(player2name + " wins! in Timed Mode");
                    terminate(player2name);
                }
                if(player2out && !player1out && player3out){
                    updateHistory(player1name + " wins! in Timed Mode");
                    terminate(player1name );
                }
            }
        }.start();
        timerPaused = false;
    }

    private void pauseTimer() {
        if (timer != null) {
            pausetime=System.currentTimeMillis();
            timerPaused = true;
            timer.cancel();
        }
    }

    private void resumeTimer() {
        if (timer != null && timerPaused) {
            long remainingTime = totalTime - (pausetime - timerStartTime);
            if (remainingTime > 0) {
                startTimer(remainingTime);
                timerPaused = false;
            } else {
                timerTextView.setText("Time's up!");

            }
        }
    }
    private void startTimer2(long time) {
        totalTime2 = (int)time;
        timerStartTime2 = System.currentTimeMillis();
        timer2 = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long remainingTime2 = millisUntilFinished;
                long seconds = remainingTime2 / 1000;
                timerTextView2.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                progressBar2.setProgress((int) seconds);
            }

            @Override
            public void onFinish() {
                timerTextView2.setText("Time's up!");
                player2out=true;
                currentPlayer=3;
                layout.setBackgroundResource(R.drawable.backround3);
                outercontainer.setBackgroundResource(R.drawable.backround3);
                rotateButtons();
                if(timevar) {
                    resumeTimer3();
                    if(!timerPaused) {
                        pauseTimer();
                    }
                    if(!timerPaused2) {
                        pauseTimer2();
                    }

                }
                if(player2out && player1out && !player3out){
                    updateHistory(player3name + " wins! in Timed Mode");
                    terminate(player3name );
                }
                if(!player2out && player1out && player3out){
                    updateHistory(player2name + " wins! in Timed Mode");
                    terminate(player2name );
                }
                if(player2out && !player1out && player3out){
                    updateHistory(player1name + " wins! in Timed Mode");
                    terminate(player1name );
                }
            }
        }.start();
        timerPaused2 = false;
    }

    private void pauseTimer2() {
        if (timer2 != null) {
            pausetime2=System.currentTimeMillis();
            timerPaused2 = true;
            timer2.cancel();
        }
    }

    private void resumeTimer2() {
        if (timer2 != null && timerPaused2) {
            long remainingTime2 = totalTime2 - (pausetime2 - timerStartTime2);
            if (remainingTime2 > 0) {
                startTimer2(remainingTime2);
                timerPaused2 = false;
            } else {
                timerTextView2.setText("Time's up!");

            }
        }
    }
    private void startTimer3(long time) {
        totalTime3 = (int)time;
        timerStartTime3 = System.currentTimeMillis();
        timer3 = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long remainingTime3 = millisUntilFinished;
                long seconds = remainingTime3 / 1000;
                timerTextView3.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                progressBar3.setProgress((int) seconds);
            }

            @Override
            public void onFinish() {
                timerTextView2.setText("Time's up!");
                player3out=true;
                currentPlayer=1;
                layout.setBackgroundResource(R.drawable.backround);
                outercontainer.setBackgroundResource(R.drawable.backround);
                rotateButtons();
                if(timevar) {
                    resumeTimer();
                    if(!timerPaused3) {
                        pauseTimer3();
                    }
                    if(!timerPaused2) {
                        pauseTimer2();
                    }
                }
                if(player2out && player1out && !player3out){
                    updateHistory(player3name + " wins! in Timed Mode");
                    terminate(player3name );
                }
                if(!player2out && player1out && player3out){
                    updateHistory(player2name + " wins! in Timed Mode");
                    terminate(player2name);
                }
                if(player2out && !player1out && player3out){
                    updateHistory(player1name + " wins! in Timed Mode");
                    terminate(player1name );
                }
            }
        }.start();
        timerPaused3 = false;
    }

    private void pauseTimer3() {
        if (timer3 != null) {
            pausetime3=System.currentTimeMillis();
            timerPaused3 = true;
            timer3.cancel();
        }
    }

    private void resumeTimer3() {
        if (timer3 != null && timerPaused3) {
            long remainingTime3 = totalTime3 - (pausetime3 - timerStartTime3);
            if (remainingTime3 > 0) {
                startTimer3(remainingTime3);
                timerPaused3 = false;
            } else {
                timerTextView3.setText("Time's up!");


            }
        }
    }
}