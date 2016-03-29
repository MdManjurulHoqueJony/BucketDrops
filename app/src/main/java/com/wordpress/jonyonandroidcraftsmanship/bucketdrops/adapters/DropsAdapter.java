package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.R;

import java.util.ArrayList;

public class DropsAdapter extends RecyclerView.Adapter<DropsAdapter.DropHolder> {

    private LayoutInflater mLayoutInflater = null;
    private ArrayList<String> mItems = new ArrayList<>();

    public DropsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mItems = generateValues();
    }

    private ArrayList<String> generateValues() {
        ArrayList<String> dummyValues = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            dummyValues.add("Item "+i);
        }
        return dummyValues;
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_drop, parent, false);
        DropHolder dropHolder = new DropHolder(view);
        return dropHolder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        holder.tvWhat.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class DropHolder extends RecyclerView.ViewHolder {
        TextView tvWhat=null;
        public DropHolder(View itemView) {
            super(itemView);
            tvWhat= (TextView) itemView.findViewById(R.id.tvWhat);
        }
    }
}
