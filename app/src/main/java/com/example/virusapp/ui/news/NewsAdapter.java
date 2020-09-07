package com.example.virusapp.ui.news;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.virusapp.MainActivity;
import com.example.virusapp.NewsActivity;
import com.example.virusapp.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView newsImage;
        TextView newsTitle;
        String news_content;

        public ViewHolder(View view)
        {
            super(view);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
        }
    }

    public NewsAdapter(List<News> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //点击View时效果
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("s", holder.newsTitle.getText().toString());
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("news_content", holder.news_content);
                context.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        News news = newsList.get(position);
        holder.newsImage.setImageResource(news.getImage_id());
        holder.newsTitle.setText(news.getTitle());
        holder.news_content = news.getText();
    }

    @Override
    public int getItemCount(){
        return newsList.size();
    }
}
