package com.example.game;

import static android.app.PendingIntent.getActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity7 extends AppCompatActivity {
    private int m ;
    private int buttonsize;
    private int currentPlayer = 1;
    private int[][] scores;
    private int[][] grid;
    private RelativeLayout layout;
    private GridLayout outercontainer;
    private TextView textView1,textView2;
    private List<String> gameHistory = new ArrayList<>();
    private SoundPool soundPool;
    private TextView player1;
    private String player1name;
    private int clicksound;
    private int expandsound;
    private int wrngbtn;
    private boolean player1firstturn=true;
    private boolean player2firstturn=true;
    private boolean layoutstyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        player1=findViewById(R.id.player1name);
        player1name=getIntent().getStringExtra("player1");
        player1.setText(player1name);
        layoutstyle=getIntent().getBooleanExtra("layout",true);
        String mstring= getIntent().getStringExtra("m");
        m = Integer.parseInt(mstring);
        scores = new int[m][m];
        grid = new int[m][m];
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

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1=new Dialog(MainActivity7.this);
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
                        Intent intent=new Intent(MainActivity7.this, MainActivity.class);
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
            button.setTypeface(null, Typeface.BOLD);
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
            animatorSet.playTogether(XUp, YUp, XDown, YDown);
            animatorSet.setDuration(200);
            animatorSet.start();
            grid[row][col] = currentPlayer;
            if (currentPlayer == 1 && player1firstturn) {
                scores[row][col] = 3;
                player1firstturn = false;
            } else if (currentPlayer == 2 && player2firstturn) {
                scores[row][col] = 3;
                player2firstturn = false;
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
                if(layoutstyle) {
                    button.setBackground(currentPlayer == 1 ? ResourcesCompat.getDrawable(getResources(), R.drawable.butto, null) : ResourcesCompat.getDrawable(getResources(), R.drawable.butto2, null));
                }
                else{
                    button.setBackground(currentPlayer == 1 ? ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo1, null) : ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo2, null));

                }
                button.setText(String.valueOf(scores[row][col]));
                button.setTypeface(null,Typeface.BOLD);
                button.setTextColor(getColor(R.color.white));
            }
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            updateScores();
            if (currentPlayer == 2) {
                if(check(grid)==1){
                    updateHistory(player1name+ "wins!");

                    terminate(player1name);
                }
                else{
                    aiMove();
                }

            }
            if(currentPlayer==1){
                layout.setBackgroundResource(R.drawable.backround);
                outercontainer.setBackgroundResource(R.drawable.backround);

            }
            else {
                layout.setBackgroundResource(R.drawable.backround2);
                outercontainer.setBackgroundResource(R.drawable.backround2);
            }
            int i=check(grid);
            if(i==1){
                 updateHistory(player1name+ "wins!");

                terminate(player1name);
            }
            else if(i==2){
                 updateHistory("Computer"+ "wins!");

                terminate("Computer");
            }

        }
        else  soundPool.play(wrngbtn, 1, 1, 0, 0, 1);


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
                animatorSet.playTogether(XUp, YUp, XDown,YDown);
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
                    if(layoutstyle) {
                        button.setBackground(currentPlayer == 1 ? ResourcesCompat.getDrawable(getResources(), R.drawable.butto, null) : ResourcesCompat.getDrawable(getResources(), R.drawable.butto2, null));
                    }
                    else{
                        button.setBackground(currentPlayer == 1 ? ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo1, null) : ResourcesCompat.getDrawable(getResources(), R.drawable.buttoo2, null));

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
        boolean a=false,b=false;
        int sum=0;
        for(int i=0;i<m;i++){
            for(int j=0;j<m;j++){
                if(grid[i][j]==1){
                    a=true;
                }
                else if(grid[i][j]==2){
                    b=true;
                }
                sum+=grid[i][j];

            }
        }
        if(a && !b && sum!=1){
            return 1;
        }
        else if(!a && b){
            return 2;
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
                Intent intent=new Intent(MainActivity7.this, MainActivity.class);
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
        player1firstturn=true;
        player2firstturn=true;
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
    }
    private int score(int a){
        int sum1=0;
        int sum2=0;
        for(int i=0;i<m;i++){
            for(int j=0;j<m;j++){
                if(grid[i][j]==1){
                    sum1+=scores[i][j];
                }
                else if(grid[i][j]==2){
                    sum2+=scores[i][j];
                }
            }
        }
        if(a==1) return sum1;
        else if(a==2) return sum2;
        return 0;
    }
    private void updateScores() {
        textView1.setText(String.valueOf(score(1)));
        textView2.setText(String.valueOf(score(2)));
    }
    private void aiMove() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if (scores[i][j] == 3 && grid[i][j] == 0) {
                    Button button1 = getButtonAt(i, j);
                    onButtonClick(button1);
                    return;
                }
            }
        }
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(m);
            col = random.nextInt(m);
        } while (grid[row][col] != 0 && grid[row][col] != 2);

        Button button = getButtonAt(row, col);
        onButtonClick(button);
        ObjectAnimator XUp = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
        ObjectAnimator XDown = ObjectAnimator.ofFloat(button, "scaleX", 1.2f, 1f);
        ObjectAnimator YUp = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);
        ObjectAnimator YDown = ObjectAnimator.ofFloat(button, "scaleY", 1.2f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(XUp, YUp, XDown,YDown);
        animatorSet.setDuration(200);
        animatorSet.start();

    }
    private void updateHistory(String result) {
        if (gameHistory.size() >= 10) {
            gameHistory.remove(gameHistory.size() - 1);
        }
        gameHistory.add(0, result);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPlayer", currentPlayer);
        outState.putBoolean("player1firstturn", player1firstturn);
        outState.putBoolean("player2firstturn", player2firstturn);
        outState.putSerializable("scores", scores);
        outState.putSerializable("grid", grid);
        outState.putStringArrayList("gameHistory", (ArrayList<String>) gameHistory);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPlayer = savedInstanceState.getInt("currentPlayer");
        player1firstturn = savedInstanceState.getBoolean("player1firstturn");
        player2firstturn = savedInstanceState.getBoolean("player2firstturn");
        scores = (int[][]) savedInstanceState.getSerializable("scores");
        grid = (int[][]) savedInstanceState.getSerializable("grid");
        gameHistory = savedInstanceState.getStringArrayList("gameHistory");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                Button button = getButtonAt(i, j);
                if (grid[i][j] == 1) {
                    button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto, null));
                } else if (grid[i][j] == 2) {
                    button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.butto2, null));
                } else {
                    if (layoutstyle) {
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
                    } else {
                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox2, null));
                    }
                }
                if (scores[i][j] > 0) {
                    button.setText(String.valueOf(scores[i][j]));
                } else {
                    button.setText("");
                }
            }
        }
        updateScores();

    }
}