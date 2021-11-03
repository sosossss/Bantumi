package es.upm.miw.bantumi.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.Date;

import es.upm.miw.bantumi.*;

public class ComparedComponent extends LinearLayout {
    private final View target;

    public ComparedComponent(Context context) {
        this(context, null);
    }

    public ComparedComponent(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComparedComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        target = LayoutInflater.from(context).inflate(R.layout.component_compared, this);
    }

    public void setPlayer1(String name) {
        TextView player = target.findViewById(R.id.player1);
        player.setText(name);
    }

    public void setData1(String text) {
        TextView data = target.findViewById(R.id.data1);
        data.setText(text);
    }

    public void setPlayer2(String name) {
        TextView player = target.findViewById(R.id.player2);
        player.setText(name);
    }

    public void setData2(String text) {
        TextView data = target.findViewById(R.id.data2);
        data.setText(text);
    }

    public void setWinnerName(String name) {
        TextView winer = target.findViewById(R.id.winnerName);
        winer.setText(name);
    }

    public void setWinnerData(String text) {
        TextView data = target.findViewById(R.id.winnerData);
        data.setText(text);
    }

    public void setDate(Date date) {
        TextView dateTime = target.findViewById(R.id.date);
        dateTime.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date));
    }
}