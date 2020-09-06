package com.example.virusapp.ui.news;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.virusapp.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView newsImage;
        TextView newsText;

        public ViewHolder(View view)
        {
            super(view);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            newsText = (TextView) view.findViewById(R.id.news_text);
        }
    }

    public NewsAdapter(List<News> newsList){
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("s", holder.newsText.getText().toString());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        News news = newsList.get(position);
        holder.newsImage.setImageResource(news.getImage_id());
        holder.newsText.setText(news.getText());
    }

    @Override
    public int getItemCount(){
        return newsList.size();
    }
}
