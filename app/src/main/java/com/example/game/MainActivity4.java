package com.example.game;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity4 extends AppCompatActivity {


    private int m;
    private int buttonSize;
    private int currentPlayer = 1;
    private int[][] scores;
    private int[][] grid;
    private RelativeLayout layout;
    private GridLayout outerContainer;
    private TextView textView1,textView2;
    private List<String> gameHistory = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 10;
    private SoundPool soundPool;
    private int clickSoundId;
    private int expandId;
    private TextView player1,player2;
    private String player1name,player2name;
    private int wrngbtnID;
    private boolean player1FirstTurn=true;
    private boolean player2FirstTurn=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        player1=findViewById(R.id.player1name);
        player2=findViewById(R.id.player2name);
        player1name=getIntent().getStringExtra("player1");
        player2name=getIntent().getStringExtra("player2");
        player1.setText(player1name);
        player2.setText(player2name);

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

        clickSoundId = soundPool.load(this, R.raw.sound1, 1);
        expandId = soundPool.load(this, R.raw.sound2, 1);
        wrngbtnID=soundPool.load(this, R.raw.wrongbtn, 1);
        layout=findViewById(R.id.relative);
        outerContainer = findViewById(R.id.buttonContainer);
        outerContainer.setColumnCount(1);
        Button button1 = findViewById(R.id.xbutton);
        textView1=findViewById(R.id.score1);
        textView2=findViewById(R.id.score2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1=new Dialog(MainActivity4.this);
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
                        Intent intent=new Intent(MainActivity4.this, MainActivity.class);
                        intent.putStringArrayListExtra("gameHistory", (ArrayList<String>) gameHistory);
                        startActivity(intent);
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        });

        GridLayout innerContainer = new GridLayout(this);
        innerContainer.setColumnCount(m);
        outerContainer.addView(innerContainer);

        if(m==5) {
            buttonSize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1);
        } else if (m==7) {
            buttonSize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)+20;
        } else if (m==6) {
            buttonSize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)+10;
        } else if (m==4) {
            buttonSize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)-20;
        } else{
            buttonSize = Math.min(getResources().getDisplayMetrics().widthPixels / m, getResources().getDisplayMetrics().heightPixels / m) - 10 * (m - 1)-30;
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
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
            button.setGravity(Gravity.CENTER);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = buttonSize;
            params.height = buttonSize;
            params.setMargins(10, 10, 10, 10);

            innerContainer.addView(button, params);
        }
    }
    private void onButtonClick(Button button) {
        soundPool.play(clickSoundId, 1, 1, 0, 0, 1);
        int index = (int) button.getTag();
        int row = index / m;
        int col = index % m;
        if(grid[row][col]==currentPlayer || grid[row][col]==0) {

            ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
            ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(button, "scaleX", 1.2f, 1f);
            ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);
            ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(button, "scaleY", 1.2f, 1f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleXUp, scaleYUp, scaleXDown, scaleYDown);
            animatorSet.setDuration(200);
            animatorSet.start();
            grid[row][col] = currentPlayer;
            if (currentPlayer == 1 && player1FirstTurn) {
                scores[row][col] = 3;
                player1FirstTurn = false;
            } else if (currentPlayer == 2 && player2FirstTurn) {
                scores[row][col] = 3;
                player2FirstTurn = false;
            } else {
                scores[row][col] += 1;
            }
            if (scores[row][col] >= 4) {
                scores[row][col] = 0;
                grid[row][col]=0;
                button.setTextColor(getResources().getColor(android.R.color.white));
                button.setTypeface(null,Typeface.BOLD);
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
                button.setText("");
                expand(row, col);
            }
            else {
                button.setBackground(currentPlayer == 1 ? ResourcesCompat.getDrawable(getResources(), R.drawable.butto, null) : ResourcesCompat.getDrawable(getResources(), R.drawable.butto2, null));
                button.setText(String.valueOf(scores[row][col]));
                button.setTypeface(null,Typeface.BOLD);
                button.setTextColor(getColor(R.color.white));
            }
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            if(currentPlayer==1){
                layout.setBackgroundResource(R.drawable.backround);
                outerContainer.setBackgroundResource(R.drawable.backround);
                rotateButtons();


            }
            else {
                layout.setBackgroundResource(R.drawable.backround2);
                outerContainer.setBackgroundResource(R.drawable.backround2);
                rotateButtons();
            }
            int i=check(grid);
            if(i==1){

                updateHistory(player1name+ " wins!");


                terminate(player1name);
            }
            else if(i==2){
                updateHistory(player2name + " wins!");
                terminate(player2name);
            }
        }
        else  soundPool.play(wrngbtnID, 1, 1, 0, 0, 1);
        updateScores();

    }

    private void expand(int row, int col) {
        soundPool.play(expandId, 1, 1, 0, 0, 1);
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];
            if (isValid(newRow, newCol)) {

                scores[newRow][newCol]++;
                grid[newRow][newCol]=currentPlayer;
                Button button = getButtonAt(newRow, newCol);
                ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
                ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(button, "scaleX", 1.2f, 1f);
                ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);
                ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(button, "scaleY", 1.2f, 1f);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleXUp, scaleYUp, scaleXDown, scaleYDown);
                animatorSet.setDuration(200);
                animatorSet.start();
                if (scores[newRow][newCol] >= 4) {
                    scores[newRow][newCol] = 0;
                    grid[newRow][newCol]=0;
                    button.setTypeface(null,Typeface.BOLD);
                    button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
                    button.setText("");
                    expand(newRow, newCol);
                }

                else {
                    button.setBackground(currentPlayer == 1 ? ResourcesCompat.getDrawable(getResources(), R.drawable.butto, null) : ResourcesCompat.getDrawable(getResources(), R.drawable.butto2, null));
                    button.setTextColor(getColor(R.color.white));
                    button.setTypeface(null,Typeface.BOLD);
                    button.setText(String.valueOf(scores[newRow][newCol]));

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
                Intent intent=new Intent(MainActivity4.this, MainActivity.class);
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
        outerContainer.setBackgroundResource(R.drawable.backround);
        player1FirstTurn=true;
        player2FirstTurn=true;



        rotateButtons();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                scores[i][j] = 0;
                grid[i][j]=0;
                Button button=getButtonAt(i,j);
                button.setTextColor(getResources().getColor(android.R.color.white));
                button.setTypeface(null,Typeface.BOLD);
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.gridbox, null));
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
    private void updateHistory(String result) {
        gameHistory.add(0, result);
        if (gameHistory.size() > MAX_HISTORY_SIZE) {
            gameHistory.remove(gameHistory.size() - 1);
        }
    }

    private void rotateButtons() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                Button button = getButtonAt(i, j);
                if (button != null) {
                    if (currentPlayer == 1) {
                        button.setRotationX(0);
                        button.setRotationY(0);

                    } else {
                        button.setRotationX(180);
                        button.setRotationY(180);


                    }
                }
            }
        }
    }
}