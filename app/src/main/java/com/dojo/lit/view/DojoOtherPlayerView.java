package com.dojo.lit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dojo.lit.R;

public class DojoOtherPlayerView extends ConstraintLayout {

    private TextView mPlayerNameTv;
    private TextView mPlayerCardsTv;

    public DojoOtherPlayerView(Context context) {
        this(context, null);
    }

    public DojoOtherPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.other_player_view, this, true);
        this.setPaddingRelative(0, 0, 0, 0);
        mPlayerNameTv = findViewById(R.id.player_name);
        mPlayerCardsTv = findViewById(R.id.cards_held_tv);
    }

    public void setData(String playerName, String cardsText, int cardsColor){
        setNameText(playerName);
        setCardsText(cardsText);
        setCardsColor(cardsColor);
    }

    public void setNameText(String playerName){
        mPlayerNameTv.setText(playerName);
    }

    public void setCardsText(String cardsText){
        mPlayerCardsTv.setText(cardsText);
    }

    public void setCardsColor(int cardsColor){
        mPlayerCardsTv.setBackgroundTintList(getResources().getColorStateList(cardsColor));
    }
}
