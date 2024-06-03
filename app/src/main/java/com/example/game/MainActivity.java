package com.example.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button button, playButton,clearButton;
    private ImageView imageView2;
    private TextView playNumber;
    private TextView textView,textView1,textView2;
    private List<String> gameHistory1;

    int selectedsize = -1;
    String[] sizeArray = {"1 Player(vs Computer)", "2 Player (Classic)", "3", "4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.question);
        imageView2 = findViewById(R.id.imgbutton);
        playButton = findViewById(R.id.button);
        playNumber = findViewById(R.id.playernumber);

        imageView.setClickable(true);
        imageView.bringToFront();

        playNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select GridSize");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(sizeArray, selectedsize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedsize = i;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedsize != -1) {
                            playNumber.setText(sizeArray[selectedsize]);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedsize = -1;
                        playNumber.setText("");
                    }
                });
                builder.show();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playNumber.getText().toString().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("playernumber", playNumber.getText().toString());
                    startActivity(intent);
                }
                else{
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog4);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                    button = dialog.findViewById(R.id.button1);
                    textView1=dialog.findViewById(R.id.textview1);
                    textView2=dialog.findViewById(R.id.textview2);
                    textView1.setText("ERROR!");
                    textView2.setText("Please Select the Number of Players");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        SwitchCompat switchCompat = findViewById(R.id.switch1);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setText("Set Light Mode  ");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchCompat.setText("Set Dark Mode  ");
        }

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDarkModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                    switchCompat.setText("Set Dark Mode  ");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                    switchCompat.setText("Set Light Mode  ");
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                button = dialog.findViewById(R.id.button1);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        gameHistory1 = getIntent().getStringArrayListExtra("gameHistory");
        if (gameHistory1 != null) {
            appendAndSaveGameHistory(gameHistory1);
        } else {
            gameHistory1 = new ArrayList<>();
        }

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_history);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                button = dialog.findViewById(R.id.closeHistoryButton);
                clearButton = dialog.findViewById(R.id.clearHistoryButton);
                textView = dialog.findViewById(R.id.historyTextView);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                clearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearGameHistory();
                        textView.setText("No game history");
                    }
                });
                List<String> gameHistory = getGameHistory();
                if (gameHistory != null && !gameHistory.isEmpty()) {
                    StringBuilder historyStringBuilder = new StringBuilder();
                    for (String move : gameHistory) {
                        historyStringBuilder.append(move).append("\n");
                    }
                    textView.setText(historyStringBuilder.toString());
                } else {
                    textView.setText("No game history");
                }
                dialog.show();
            }
        });
    }

    private void appendAndSaveGameHistory(List<String> newGameHistory) {
        List<String> existingGameHistory = getGameHistory();
        if (existingGameHistory.isEmpty()) {
            existingGameHistory = new ArrayList<>();
        }
        existingGameHistory.addAll(0, newGameHistory);
        saveGameHistory(existingGameHistory);
    }

    private void saveGameHistory(List<String> gameHistory) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> gameHistorySet = new HashSet<>(gameHistory);
        editor.putStringSet("gameHistory", gameHistorySet);
        editor.apply();
    }

    private List<String> getGameHistory() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> gameHistorySet = sharedPreferences.getStringSet("gameHistory", new HashSet<String>());
        return new ArrayList<>(gameHistorySet);
    }
    private void clearGameHistory() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("gameHistory");
        editor.apply();
    }
}
