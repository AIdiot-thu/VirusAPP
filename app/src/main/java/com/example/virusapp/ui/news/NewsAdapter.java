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

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    // 底部footView的ViewHolder，用以缓存findView操作
    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        FootHolder(View itemView) {
            super(itemView);
            tips = itemView.findViewById(R.id.foot_tips);
        }
    }

    public NewsAdapter(List<News> newsList, Context context){
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            //点击View时效果
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("news page", holder.newsTitle.getText().toString());
                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra("news_content", holder.news_content);
                    context.startActivity(intent);
                }
            });
            return holder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            FootHolder footHolder = new FootHolder(v);
            return footHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof ViewHolder) {
            News news = newsList.get(position);
            ViewHolder vh = (ViewHolder) holder;
            vh.newsImage.setImageResource(news.getImage_id());
            vh.newsTitle.setText(news.getTitle());
            vh.news_content = news.getText();
        }
        else {
            FootHolder fh = (FootHolder)holder;
            fh.tips.setVisibility(View.VISIBLE);
            fh.tips.setText("正在加载更多...");
        }
    }

    @Override
    public int getItemCount(){
        return newsList == null ? 0 : newsList.size()+1;
    }

    // 自定义方法，获取数据的最后一个位置，不计上footView
    public int getRealLastPosition() {
        return newsList.size();
    }

    // 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
