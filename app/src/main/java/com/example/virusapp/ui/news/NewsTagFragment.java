package com.example.virusapp.ui.news;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.virusapp.MainActivity;
import com.example.virusapp.R;
import com.example.virusapp.data.DataManager;
import com.example.virusapp.data.NewsData;
import com.example.virusapp.data.NewsSumData;

import java.util.ArrayList;
import java.util.List;

public class NewsTagFragment extends Fragment {
    private String tag_name;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    int lastVisibleItem = 0;
    int front_in_database = 0;
    int back_in_database = 0;
    private final int Buffer_size = 21;
    private List<News> newsList;
    private Handler handler;
    private boolean valid = true;

    NewsTagFragment(String tag_name){
        this.tag_name = tag_name;
        newsList = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout root = (LinearLayout) inflater.inflate(R.layout.news_domain, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swipeLayout_domain);
        recyclerView = root.findViewById(R.id.news_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final NewsAdapter adapter = new NewsAdapter(newsList, getActivity());
        recyclerView.setAdapter(adapter);

//        UpdateNews();
//        adapter.notifyDataSetChanged();

        //judge valid
        if(tag_name.equals("domain") || tag_name.equals("aboard")){
            valid = true;
        }
        else{
            valid = false;
            swipeRefreshLayout.setVisibility(View.GONE);
        }

        //swipeRefresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onRefresh() {
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        try {
                            adapter.notifyDataSetChanged();
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                            Log.d("UpdateNews","手动捕获异常："+e.getMessage());
                        }
                    }
                };
                swipeRefreshLayout.setRefreshing(true);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(valid)UpdateNews();
                        swipeRefreshLayout.setRefreshing(false);
                        handler.sendEmptyMessage(0);
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                });
                t.start();
            }
        });

        //set recycler view scrollListener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果没有隐藏footView，那么最后一个条目的位置(带数据）就比我们的getItemCount少1
                    if (lastVisibleItem + 1 == adapter.getItemCount() && adapter.getItemCount()>1) {
                        // 然后调用updateRecyclerview方法更新RecyclerView
                        handler = new Handler(){
                            @Override
                            public void handleMessage(Message msg){
                                try {
                                    adapter.notifyDataSetChanged();
                                }catch (IndexOutOfBoundsException e){
                                    e.printStackTrace();
                                    Log.d("UpdateNews","手动捕获异常："+e.getMessage());
                                }
                            }
                        };
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(valid)updateRecyclerview();
                                handler.sendEmptyMessage(0);
                                Message msg = handler.obtainMessage();
                                handler.sendMessage(msg);
                            }
                        });
                        t.start();
                    }
                }
            }
            //滚动监听
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在滑动完成后，拿到最后一个可见的item的位置
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }

        });


        return root;
    }

    private void updateRecyclerview(){
        DataManager service = ((MainActivity) getActivity()).getmService();
        if (service == null || !service.init) return;
        ArrayList<NewsSumData> rawData = service.getNewsSumList();
        if (rawData == null) return;
        int rawdata_size = rawData.size();
        Integer bk = back_in_database;
        Log.d("up", bk.toString());
        for (int i = back_in_database - 1; i >= back_in_database - Buffer_size; i--) {
            NewsSumData sum_temp = rawData.get(i);
            NewsData data_temp = service.getNewsByIndex(i);
            String title = sum_temp.title;
            String text = data_temp.content;
            News news = new News(title, text, R.drawable.ic_news);
            newsList.add(news);
        }
        back_in_database -= Buffer_size;
    }

    private void UpdateNews() {
        try {
            DataManager service = ((MainActivity) getActivity()).getmService();
            if (service == null) return;

            if (!service.init) return;
            ArrayList<NewsSumData> rawData = service.getNewsSumList();
            if (rawData == null) return;
            //newsList.clear();
            int rawdata_size = rawData.size();
            if(front_in_database==0 && back_in_database==0) {
                front_in_database = rawdata_size - Buffer_size;
                back_in_database = front_in_database;
            }
            for (int i = rawdata_size - 1; i >= front_in_database; i--) {
                NewsSumData sum_temp = rawData.get(i);
                NewsData data_temp = service.getNewsByIndex(i);
                String title = sum_temp.title;
                String text = data_temp.content;
                News news = new News(title, text, R.drawable.ic_news);
                newsList.add(0,news);
            }
            front_in_database = rawdata_size;
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            Log.d("UpdateNews","手动捕获异常："+e.getMessage());
        }
    }

    public String getTag_name() {
        return tag_name;
    }
}
