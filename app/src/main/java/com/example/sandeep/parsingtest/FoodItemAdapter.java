package com.example.sandeep.parsingtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    private static ArrayList<Items> dataSet;
    int[] num = {0, 0, 0, 0 ,0, 0, 0, 0};
    public FoodItemAdapter(ArrayList<Items> item) {

        dataSet = item;
    }


    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.food_item_view, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodItemAdapter.ViewHolder viewHolder, int i) {

        Items fp = dataSet.get(i);

        viewHolder.tvName.setText(fp.getTitle());
        viewHolder.iconView.setImageResource(fp.getThumbnail());
        viewHolder.vote.setText(String.valueOf(fp.getNumber()));
        viewHolder.tvCost.setText(String.valueOf(fp.getCost()));
        viewHolder.feed = fp;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView iconView;
        public Button un_voteButton;
        public Button voteButton;
        public TextView vote;
        public TextView tvCost;
        public Items feed;
        public boolean order = false;
        public int number = 0;
        final String[] food_name = {"ABC", "MB", "Khichuri" , "Kwati", "Phapar", "SandhekoGundruk", "SellRoti", "Yomari"};

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName_cusine);
            iconView = (ImageView) itemLayoutView.findViewById(R.id.iconId_cusine);
            voteButton = (Button) itemLayoutView.findViewById(R.id.bt_vote);
            vote = (TextView) itemLayoutView.findViewById(R.id.vote_number);
            un_voteButton = (Button) itemLayoutView.findViewById(R.id.bt_unvote);
            tvCost = (TextView) itemLayoutView.findViewById(R.id.tv_cost);



            voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SendData send = new SendData();
                    order = true;
                    number++;
                    tvName.setTextColor(Color.BLUE);
                    vote.setTextColor(Color.BLUE);
                    vote.setText(String.valueOf(number));
                    feed.setNumber(number);
                    String write_data = tvName.getText().toString() + ":" + String.valueOf(number) + ";";
                    try {
                        send.commitToFile(write_data);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

            un_voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (number < 2 && number > 0) {
                        order = false;
                        number--;
                        tvName.setTextColor(Color.BLACK);
                        vote.setTextColor(Color.RED);
                        vote.setText(String.valueOf(number));
                        feed.setNumber(number);
                    }
                    else if(number < 1){
                        number = 0;
                    }
                    else {
                        number--;
                        vote.setText(String.valueOf(number));
                        feed.setNumber(number);
                    }
                    SendData send = new SendData();
                    String write_data = tvName.getText().toString() + ":" + String.valueOf(number)+";";
                    try {

                        send.commitToFile(write_data);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

        }

    }
}

