package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.R;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;

import io.realm.RealmResults;

public class DropsAdapter extends RecyclerView.Adapter<DropsAdapter.DropHolder> {

    private LayoutInflater mLayoutInflater = null;
    private RealmResults<Drop> mResults = null;


    public DropsAdapter(Context context,RealmResults<Drop> results) {
        mLayoutInflater = LayoutInflater.from(context);
        update(results);
    }

    public void update(RealmResults<Drop> results){
        mResults=results;
        notifyDataSetChanged();
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_drop, parent, false);
        DropHolder dropHolder = new DropHolder(view);
        return dropHolder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        Drop drop=mResults.get(position);
        holder.tvWhat.setText(drop.getWhat());
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class DropHolder extends RecyclerView.ViewHolder {
        TextView tvWhat=null;
        public DropHolder(View itemView) {
            super(itemView);
            tvWhat= (TextView) itemView.findViewById(R.id.tvWhat);
        }
    }
}
