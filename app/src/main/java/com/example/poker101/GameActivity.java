package com.example.poker101;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poker101.date.Comanda;
import com.example.poker101.date.Pariu;
import com.example.poker101.date.Tura;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

public class GameActivity extends AppCompatActivity {
    public static boolean goBack = false;
    public static TextView chips_total_text;
    public static TextView chips_played_text;
    public static TextView opponent_chips_played_text;
    public static TextView raise_text;
    public static TextView turn_action;
    public static TextView turn_player;
    public static ImageView card1, card2;
    public static ImageView dealerCard1, dealerCard2, dealerCard3, dealerCard4, dealerCard5, opponentCard1, opponentCard2;

    public static Button btn_check;
    public static Button btn_fold;
    public static Button btn_call;
    public static Button btn_raise;
    public static Button raise_increment;
    public static Button raise_decrement;
    public static int tura_curenta = 0;

    public static void endTurn() {
        User.yourTurn = false;
        btn_call.setEnabled(false);
        btn_raise.setEnabled(false);
        btn_fold.setEnabled(false);
        btn_check.setEnabled(false);
        btn_fold.setTextColor(User.context.getResources().getColor(R.color.colorDarkText));
        btn_raise.setTextColor(User.context.getResources().getColor(R.color.colorDarkText));
        btn_call.setTextColor(User.context.getResources().getColor(R.color.colorDarkText));
        btn_check.setTextColor(User.context.getResources().getColor(R.color.colorDarkText));
        turn_player.setText(R.string.opponent_turn);
    }

    public static void updateGame(Tura tura) {
        if (tura.opponent_action != null && tura.opponent_action.equals("fold")) {
            goBack = true;
            turn_action.setText((User.context.getResources().getString(R.string.opponent_folded)));
            return;
        }
        if (tura.winner != null) {
            opponent_chips_played_text.setText(Integer.toString(tura.opponent_bani));
            Resources res = User.context.getResources();
            for (int i = 0; i < tura.carti.size(); i++) {
                if (tura.carti.get(i) != null) {
                    String cardName = "_" + tura.carti.get(i).getNumar() + tura.carti.get(i).getTip() + "";
                    int resID1 = res.getIdentifier(cardName, "drawable", User.context.getPackageName());
                    if (i == 0) {
                        Picasso.get().load(resID1).fit().into(dealerCard1);
                    } else if (i == 1) {
                        Picasso.get().load(resID1).fit().into(dealerCard2);
                    } else if (i == 2) {
                        Picasso.get().load(resID1).fit().into(dealerCard3);
                    } else if (i == 3) {
                        Picasso.get().load(resID1).fit().into(dealerCard4);
                    } else if (i == 4) {
                        Picasso.get().load(resID1).fit().into(dealerCard5);
                    }
                } else {
                    break;
                }
            }
            if (tura.opponent_carte1 != null) {
                String card1Name = "_" + tura.opponent_carte1.getNumar() + tura.opponent_carte1.getTip() + "";
                int resID1 = res.getIdentifier(card1Name, "drawable", User.context.getPackageName());
                Picasso.get().load(resID1).fit().into(opponentCard1);
                String card2Name = "_" + tura.opponent_carte2.getNumar() + tura.opponent_carte2.getTip() + "";
                int resID2 = res.getIdentifier(card2Name, "drawable", User.context.getPackageName());
                Picasso.get().load(resID2).fit().into(opponentCard2);
            }
            goBack = true;
            try {
                if (tura.winner.equals(User.user.getString("id"))) {
                    turn_action.setText(R.string.you_win);
                } else if (tura.winner.equals(User.currentOpponent)) {
                    turn_action.setText(R.string.opponent_win);
                } else {
                    turn_action.setText(R.string.draw);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            turn_action.setText(("Opponent " + tura.opponent_action));
            User.yourTurn = true;
            btn_fold.setEnabled(true);
            btn_raise.setEnabled(true);
            btn_fold.setTextColor(User.context.getResources().getColor(R.color.disabledButton));
            btn_raise.setTextColor(User.context.getResources().getColor(R.color.disabledButton));

            if (tura.opponent_action.equals("raise")) {
                btn_call.setEnabled(true);
                btn_call.setTextColor(User.context.getResources().getColor(R.color.disabledButton));
            }

            if (tura.opponent_action.equals("check")) {
                btn_check.setEnabled(true);
                btn_check.setTextColor(User.context.getResources().getColor(R.color.disabledButton));
            }

            turn_player.setText(R.string.your_turn);
            Resources res = User.context.getResources();
            if (tura.tura_curenta != GameActivity.tura_curenta) {
                GameActivity.tura_curenta = tura.tura_curenta;
                if (!btn_call.isEnabled()) {
                    btn_check.setEnabled(true);
                    btn_check.setTextColor(User.context.getResources().getColor(R.color.disabledButton));
                }

                for (int i = 0; i < tura.carti.size(); i++) {
                    if (tura.carti.get(i) != null) {
                        String cardName = "_" + tura.carti.get(i).getNumar() + tura.carti.get(i).getTip() + "";
                        int resID1 = res.getIdentifier(cardName, "drawable", User.context.getPackageName());
                        if (i == 0) {
                            Picasso.get().load(resID1).fit().into(dealerCard1);
                        } else if (i == 1) {
                            Picasso.get().load(resID1).fit().into(dealerCard2);
                        } else if (i == 2) {
                            Picasso.get().load(resID1).fit().into(dealerCard3);
                        } else if (i == 3) {
                            Picasso.get().load(resID1).fit().into(dealerCard4);
                        } else if (i == 4) {
                            Picasso.get().load(resID1).fit().into(dealerCard5);
                        }
                    } else {
                        break;
                    }
                }

            }
            opponent_chips_played_text.setText(Integer.toString(tura.opponent_bani));
            int pariati1 = Integer.parseInt(chips_played_text.getText().toString());
            int total = Integer.parseInt(chips_total_text.getText().toString());
            int pariati2 = Integer.parseInt(opponent_chips_played_text.getText().toString());
            if ((total - (pariati2 - pariati1)) <= 0) {
                btn_call.setText(R.string.all_in);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        chips_total_text = findViewById(R.id.chips_total_text);
        chips_total_text.setText(Integer.toString(User.player_info.bani_totali));

        chips_played_text = findViewById(R.id.chips_played_text);
        chips_played_text.setText("0");

        opponent_chips_played_text = findViewById(R.id.opponent_chips_played_text);
        opponent_chips_played_text.setText("0");

        raise_text = findViewById(R.id.raise_text);
        raise_text.setText("1");

        turn_action = findViewById(R.id.turn_action);
        turn_action.setText("");

        turn_player = findViewById(R.id.turn_player);
        if (User.yourTurn) {
            turn_player.setText(R.string.your_turn);
        } else {
            turn_player.setText(R.string.opponent_turn);
        }

        Resources res = getResources();

        card1 = findViewById(R.id.player_card1);
        String card1Name = "_" + User.player_info.carte1.getNumar() + User.player_info.carte1.getTip() + "";
        int resID1 = res.getIdentifier(card1Name, "drawable", getPackageName());
        Picasso.get().load(resID1).fit().into(card1);

        card2 = findViewById(R.id.player_card2);
        String card2Name = "_" + User.player_info.carte2.getNumar() + User.player_info.carte2.getTip() + "";
        int resID2 = res.getIdentifier(card2Name, "drawable", getPackageName());
        Picasso.get().load(resID2).fit().into(card2);

        dealerCard1 = findViewById(R.id.dealer_card1);
        dealerCard2 = findViewById(R.id.dealer_card2);
        dealerCard3 = findViewById(R.id.dealer_card3);
        dealerCard4 = findViewById(R.id.dealer_card4);
        dealerCard5 = findViewById(R.id.dealer_card5);
        opponentCard1 = findViewById(R.id.opponent_card1);
        opponentCard2 = findViewById(R.id.opponent_card2);

        if (User.card_id == 0) {
            Picasso.get().load(R.drawable.card_back_default).fit().into(dealerCard1);
            Picasso.get().load(R.drawable.card_back_default).fit().into(dealerCard2);
            Picasso.get().load(R.drawable.card_back_default).fit().into(dealerCard3);
            Picasso.get().load(R.drawable.card_back_default).fit().into(dealerCard4);
            Picasso.get().load(R.drawable.card_back_default).fit().into(dealerCard5);
            Picasso.get().load(R.drawable.card_back_default).fit().into(opponentCard1);
            Picasso.get().load(R.drawable.card_back_default).fit().into(opponentCard2);
        } else if (User.card_id == 1) {
            Picasso.get().load(R.drawable.card_back1).fit().into(dealerCard1);
            Picasso.get().load(R.drawable.card_back1).fit().into(dealerCard2);
            Picasso.get().load(R.drawable.card_back1).fit().into(dealerCard3);
            Picasso.get().load(R.drawable.card_back1).fit().into(dealerCard4);
            Picasso.get().load(R.drawable.card_back1).fit().into(dealerCard5);
            Picasso.get().load(R.drawable.card_back1).fit().into(opponentCard1);
            Picasso.get().load(R.drawable.card_back1).fit().into(opponentCard2);
        } else if (User.card_id == 2) {
            Picasso.get().load(R.drawable.card_back2).fit().into(dealerCard1);
            Picasso.get().load(R.drawable.card_back2).fit().into(dealerCard2);
            Picasso.get().load(R.drawable.card_back2).fit().into(dealerCard3);
            Picasso.get().load(R.drawable.card_back2).fit().into(dealerCard4);
            Picasso.get().load(R.drawable.card_back2).fit().into(dealerCard5);
            Picasso.get().load(R.drawable.card_back2).fit().into(opponentCard1);
            Picasso.get().load(R.drawable.card_back2).fit().into(opponentCard2);
        }

        btn_check = findViewById(R.id.btn_check);
        btn_call = findViewById(R.id.btn_call);
        btn_raise = findViewById(R.id.btn_raise);
        btn_fold = findViewById(R.id.btn_fold);
        raise_increment = findViewById(R.id.raise_increment);
        raise_decrement = findViewById(R.id.raise_decrement);

        if (!User.yourTurn) {
            btn_check.setEnabled(false);
            btn_call.setEnabled(false);
            btn_raise.setEnabled(false);
            btn_fold.setEnabled(false);
            btn_check.setTextColor(getResources().getColor(R.color.colorDarkText));
            btn_raise.setTextColor(getResources().getColor(R.color.colorDarkText));
            btn_call.setTextColor(getResources().getColor(R.color.colorDarkText));
            btn_fold.setTextColor(getResources().getColor(R.color.colorDarkText));
        } else {
            btn_call.setEnabled(false);
            btn_call.setTextColor(getResources().getColor(R.color.colorDarkText));
        }

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendCommand(new Comanda("check", User.user.getString("id")));
                    turn_action.setText(R.string.you_checked);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendCommand(new Comanda("fold", User.user.getString("id")));
                    goBack = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int pariati1 = Integer.parseInt(chips_played_text.getText().toString());
                    int total = Integer.parseInt(chips_total_text.getText().toString());
                    int pariati2 = Integer.parseInt(opponent_chips_played_text.getText().toString());
                    if ((total - (pariati2 - pariati1)) <= 0) {
                        chips_total_text.setText("0");
                        chips_played_text.setText(Integer.toString(total + pariati1));
                    } else {
                        chips_played_text.setText(opponent_chips_played_text.getText());
                        chips_total_text.setText(Integer.toString(total - (pariati2 - pariati1)));
                    }
                    sendCommand(new Comanda("call", User.user.getString("id")));
                    User.player_info.bani_pariati = Integer.parseInt(chips_played_text.getText().toString());
                    User.player_info.bani_totali = Integer.parseInt(chips_total_text.getText().toString());
                    turn_action.setText(R.string.you_called);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_raise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int pariati1 = Integer.parseInt(chips_played_text.getText().toString());
                    int total = Integer.parseInt(chips_total_text.getText().toString());
                    int pariati2 = Integer.parseInt(opponent_chips_played_text.getText().toString());
                    int raised = Integer.parseInt(raise_text.getText().toString());
                    if (raised + (pariati2 - pariati1) > total) {
                        Toast.makeText(getApplicationContext(), "Insufficient funds!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    chips_total_text.setText(Integer.toString(total - (pariati2 - pariati1) - raised));
                    chips_played_text.setText(Integer.toString(pariati2 + raised));
                    Pariu pariu = new Pariu();
                    pariu.id_facebook = User.user.getString("id");
                    pariu.bani = raised;
                    sendCommand(new Comanda("raise", pariu));
                    User.player_info.bani_pariati = Integer.parseInt(chips_played_text.getText().toString());
                    User.player_info.bani_totali = Integer.parseInt(chips_total_text.getText().toString());
                    turn_action.setText(R.string.you_raised);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        raise_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(raise_text.getText().toString());
                int total = Integer.parseInt(chips_total_text.getText().toString());
                if (!(value * 2 > total)) {
                    value *= 2;
                    raise_text.setText(Integer.toString(value));
                }
            }
        });

        raise_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(raise_text.getText().toString());
                if (value / 2 >= 1) {
                    value /= 2;
                    raise_text.setText(Integer.toString(value));
                }
            }
        });
    }

    void sendCommand(final Comanda comanda) {
        endTurn();
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    User.oos.writeObject(comanda);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            }


        };
        Thread myThread = new Thread(myRunnable);
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User.context = getApplicationContext();
        User.getMessage=true;
    }

    @Override
    protected void onPause() {
        User.getMessage=false;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (goBack) {
            GameActivity.goBack = false;
            User.goToMenu("fromGameEnd");
        }
    }
}
