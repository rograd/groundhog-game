package com.example.groundhog.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.groundhog.model.Player;
import com.example.groundhog.R;

import java.util.ArrayList;

public class Common {
    public static <T extends View> T createCopyOf(int resource, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource, null);
        return (T)view;
    }

    public static ArrayList<TableRow> createRows(ArrayList<Player> players, Context context) {
        ArrayList<TableRow> tableRows = new ArrayList<>();
        for (Player player : players) {
            TableRow row = createRow(player, context);
            tableRows.add(row);
        }

        return tableRows;
    }

    private static TableRow createRow(Player player, Context context) {
        TableRow rowCopy = Common.createCopyOf(R.layout.leaderboard_row, context);

        TextView txvTemp;

        txvTemp = rowCopy.findViewById(R.id.txvNickname);
        txvTemp.setText(player.getNickname());

        txvTemp = rowCopy.findViewById(R.id.txvScore);
        int score = player.getScore();
        txvTemp.setText(String.valueOf(score));

        return rowCopy;
    }
}
