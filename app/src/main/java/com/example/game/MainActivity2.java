package com.example.game;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private Button button, button1, button2;
    private TextView gridsize,textView1,textView2;
    private LinearLayout linearLayout, linearLayout2;
    private boolean allplayersentered = true;
    int selectedsize = -1;
    ArrayList<Integer> sizeList = new ArrayList<>();
    private String getplayernumber;
    String[] sizeArray = {"3", "4", "5", "6", "7"};
    private String[] player= new String[4];
    private EditText[] editTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getplayernumber=getIntent().getStringExtra("playernumber");
        String[] playernumberpart = getplayernumber.split(" ");
        String firstWord = playernumberpart[0];
        int noplayers = Integer.parseInt(firstWord);

        gridsize = findViewById(R.id.gridsize);
        linearLayout = findViewById(R.id.linearLayout);
        editTexts = new EditText[noplayers];

        for (int i = 1; i <= noplayers; i++) {
            RelativeLayout playerlayout = createplayerlayout(i);
            RelativeLayout playerlayout2 = createplayerlayout2(i);

            LinearLayout playerrow = new LinearLayout(this);
            playerrow.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.setMargins(10, 10, 10, 10);

            playerlayout.setLayoutParams(params);
            playerlayout2.setLayoutParams(params);

            playerrow.addView(playerlayout);
            playerrow.addView(playerlayout2);

            linearLayout.addView(playerrow);

        }
        for (int i = 1; i <= noplayers; i++) {
            editTexts[i-1] = getEditText(i);
        }



        gridsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
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
                            gridsize.setText(sizeArray[selectedsize]);
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
                        gridsize.setText("");
                    }
                });
                builder.show();
            }
        });

        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allplayersentered=true;
                for (int i = 0; i < noplayers; i++) {
                    player[i] = editTexts[i].getText().toString();
                    if (player[i].isEmpty()) {
                        allplayersentered = false;
                        break;
                    }
                }
                if(!allplayersentered && gridsize.getText().toString().isEmpty()){
                    final Dialog dialog = new Dialog(MainActivity2.this);
                    dialog.setContentView(R.layout.dialog4);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                    button = dialog.findViewById(R.id.button1);
                    textView1=dialog.findViewById(R.id.textview1);
                    textView2 = dialog.findViewById(R.id.textview2);
                    textView1.setText("ERROR!");
                    textView2.setText("Please Provide Player Names and Grid Size");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(!allplayersentered && !gridsize.getText().toString().isEmpty()){
                    final Dialog dialog = new Dialog(MainActivity2.this);
                    dialog.setContentView(R.layout.dialog4);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                    button = dialog.findViewById(R.id.button1);
                    textView1=dialog.findViewById(R.id.textview1);
                    textView2 = dialog.findViewById(R.id.textview2);
                    textView1.setText("ERROR!");
                    textView2.setText("Please Provide Player Names");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if (!gridsize.getText().toString().isEmpty() && allplayersentered ) {

                    if (noplayers == 2) {
                        final Dialog dialog = new Dialog(MainActivity2.this);
                        dialog.setContentView(R.layout.gamemodedialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                        button1 = dialog.findViewById(R.id.timedmode);
                        button2 = dialog.findViewById(R.id.normalmode);
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                                intent.putExtra("m", gridsize.getText().toString());
                                intent.putExtra("player1", player[0]);
                                intent.putExtra("player2", player[1]);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
                                intent.putExtra("m", gridsize.getText().toString());
                                intent.putExtra("player1", player[0]);
                                intent.putExtra("player2", player[1]);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else if (noplayers==3) {
                        final Dialog dialog = new Dialog(MainActivity2.this);
                        dialog.setContentView(R.layout.gamemodedialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                        button1 = dialog.findViewById(R.id.timedmode);
                        button2 = dialog.findViewById(R.id.normalmode);
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean timevar=true;
                                Intent intent = new Intent(MainActivity2.this, MainActivity5.class);
                                intent.putExtra("m", gridsize.getText().toString());
                                intent.putExtra("player1", player[0]);
                                intent.putExtra("player2", player[1]);
                                intent.putExtra("player3", player[2]);
                                intent.putExtra("timevar", timevar);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean timevar=false;
                                Intent intent = new Intent(MainActivity2.this, MainActivity5.class);
                                intent.putExtra("m", gridsize.getText().toString());
                                intent.putExtra("player1", player[0]);
                                intent.putExtra("player2", player[1]);
                                intent.putExtra("player3", player[2]);
                                intent.putExtra("timevar", timevar);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    } else if (noplayers==4) {
                        final Dialog dialog = new Dialog(MainActivity2.this);
                        dialog.setContentView(R.layout.gamemodedialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                        button1 = dialog.findViewById(R.id.timedmode);
                        button2 = dialog.findViewById(R.id.normalmode);
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean timevar=true;
                                Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
                                intent.putExtra("m", gridsize.getText().toString());
                                intent.putExtra("player1", player[0]);
                                intent.putExtra("player2", player[1]);
                                intent.putExtra("player3", player[2]);
                                intent.putExtra("timevar", timevar);
                                intent.putExtra("player4", player[3]);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean timevar=false;
                                Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
                                intent.putExtra("m", gridsize.getText().toString());
                                intent.putExtra("player1", player[0]);
                                intent.putExtra("player2", player[1]);
                                intent.putExtra("player3", player[2]);
                                intent.putExtra("player4", player[3]);
                                intent.putExtra("timevar", timevar);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    } else if (noplayers==1) {
                        Intent intent = new Intent(MainActivity2.this, MainActivity7.class);
                        intent.putExtra("m", gridsize.getText().toString());
                        intent.putExtra("player1", player[0]);
                        startActivity(intent);

                    }
                }
                else if(gridsize.getText().toString().isEmpty() && allplayersentered) {
                    final Dialog dialog = new Dialog(MainActivity2.this);
                    dialog.setContentView(R.layout.dialog4);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                    button = dialog.findViewById(R.id.button1);
                    textView1=dialog.findViewById(R.id.textview1);
                    textView2 = dialog.findViewById(R.id.textview2);
                    textView1.setText("ERROR!");
                    textView2.setText("Please Select the GridSize");
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
    }
    @Nullable
    private EditText getEditText(int n) {
        View view = findViewById(R.id.linearLayout).findViewWithTag(n);
        if (view instanceof EditText) {
            return (EditText) view;
        }
        return null;
    }

    private RelativeLayout createplayerlayout(int playerNumber) {
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relLayoutParams.setMargins(10, 10, 10, 10);
        relativeLayout.setLayoutParams(relLayoutParams);
        relativeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.playerinfobox));

        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams imgLayoutParams = new RelativeLayout.LayoutParams(
                200, 200
        );
        imgLayoutParams.setMargins(0,10,0,0);
        imgLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(imgLayoutParams);
        imageView.setId(View.generateViewId());
        if(playerNumber==1) {
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.playerimg1));
        } else if (playerNumber==2) {
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.playerimg2));
        } else if (playerNumber==3) {
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.playerimg3));
        }else{
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.playerimg4));
        }

        relativeLayout.addView(imageView);

        EditText editText = new EditText(this);
        RelativeLayout.LayoutParams editLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        editLayoutParams.addRule(RelativeLayout.BELOW, imageView.getId());
        editText.setLayoutParams(editLayoutParams);
        editText.setHint("Enter Player-" + playerNumber + " Name");
        editText.setGravity(Gravity.CENTER);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextColor(ContextCompat.getColor(this, R.color.white));
        if(playerNumber==1) {
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.playerone));
        } else if (playerNumber==2) {
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.playertwo));
        } else if (playerNumber==3) {
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.playerthree));
        }else{
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.playerfour));
        }
        editText.setTextSize(18);
        editText.setTag(playerNumber);
        editText.setId(View.generateViewId());

        relativeLayout.addView(editText);
        ImageView separator = new ImageView(this);
        RelativeLayout.LayoutParams sepLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                25
        );
        sepLayoutParams.addRule(RelativeLayout.BELOW, editText.getId());
        sepLayoutParams.setMargins(0,0,0,20);
        separator.setLayoutParams(sepLayoutParams);
        if(playerNumber==1) {
            separator.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted));
        } else if (playerNumber==2) {
            separator.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted2));
        } else if (playerNumber==3) {
            separator.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted3));
        }else{
            separator.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted4));
        }

        relativeLayout.addView(separator);

        return relativeLayout;
    }

    private RelativeLayout createplayerlayout2(int playerNumber) {
        RelativeLayout relativeLayout2 = new RelativeLayout(this);
        RelativeLayout.LayoutParams relLayoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relLayoutParams2.setMargins(10, 10, 10, 10);
        relativeLayout2.setLayoutParams(relLayoutParams2);
        relativeLayout2.setBackground(ContextCompat.getDrawable(this, R.drawable.playerinfobox));

        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(
                200, 215
        );
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        textView.setTypeface(boldTypeface);
        textLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        textView.setLayoutParams(textLayoutParams);
        textView.setId(View.generateViewId());
        textView.setTag(playerNumber);
        textLayoutParams.setMargins(0, 10, 0, 0);

        textView.setText(String.valueOf(playerNumber));
        if(playerNumber==1) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.playerone));
        } else if (playerNumber==2) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.playertwo));
        } else if (playerNumber==3) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.playerthree));
        }else{
            textView.setTextColor(ContextCompat.getColor(this, R.color.playerfour));
        }

        textView.setGravity(Gravity.CENTER);

        textView.setTextSize(60);
         relativeLayout2.addView(textView);

        TextView textView2 = new TextView(this);
        RelativeLayout.LayoutParams textLayoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        textLayoutParams2.addRule(RelativeLayout.BELOW, textView.getId());
        textLayoutParams2.setMargins(0, 20, 0, 20);
        textView2.setLayoutParams(textLayoutParams2);
        textView2.setText("Player-" + playerNumber + " Name");
        textView2.setGravity(Gravity.CENTER);
        if(playerNumber==1) {
            textView2.setBackgroundColor(ContextCompat.getColor(this, R.color.playerone));
        } else if (playerNumber==2) {
            textView2.setBackgroundColor(ContextCompat.getColor(this, R.color.playertwo));
        } else if (playerNumber==3) {
            textView2.setBackgroundColor(ContextCompat.getColor(this, R.color.playerthree));
        }else{
            textView2.setBackgroundColor(ContextCompat.getColor(this, R.color.playerfour));
        }
        textView2.setId(View.generateViewId());
        textView2.setTextColor(ContextCompat.getColor(this, R.color.white));
        textView2.setTextSize(18);
        relativeLayout2.addView(textView2);

        ImageView separator2 = new ImageView(this);
        RelativeLayout.LayoutParams sepLayoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                25
        );
        sepLayoutParams2.addRule(RelativeLayout.BELOW, textView2.getId());
        sepLayoutParams2.setMargins(0,0,0,20);
        separator2.setLayoutParams(sepLayoutParams2);
        if(playerNumber==1) {
            separator2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted));
        } else if (playerNumber==2) {
            separator2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted2));
        } else if (playerNumber==3) {
            separator2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted3));
        }else{
            separator2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.doted4));
        }
        relativeLayout2.addView(separator2);

        return relativeLayout2;
    }
}
