package com.example.virusapp.ui.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.virusapp.R;
import com.example.virusapp.data.Entity;
import com.example.virusapp.ui.news.NewsAdapter;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<Entity> entities;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.info_content);
        }
    }
    InfoAdapter(Context context,List<Entity> hh){
        this.context=context;
        this.entities=hh;
    }
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("on creating viewholder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info,parent,false);
        final InfoAdapter.ViewHolder holder = new InfoAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTv.setText(entities.get(position).label);
    }

    @Override
    public int getItemCount() {
        if (entities==null)
            return 0;
        return entities.size();
    }



}


