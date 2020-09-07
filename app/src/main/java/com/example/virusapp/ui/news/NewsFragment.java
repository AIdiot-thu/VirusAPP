package com.example.virusapp.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.virusapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private Integer test_num = 0;
    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tablayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout root = (LinearLayout) inflater.inflate(R.layout.fragment_news, container, false);
        final LinearLayout domain_layout = (LinearLayout) inflater.inflate(R.layout.news_domain, container, false);
        final LinearLayout aboard_layout = (LinearLayout) inflater.inflate(R.layout.news_aboard, container, false);

        /*******************************root**********************************/
        //tablayout & viewPager
        tablayout = root.findViewById(R.id.tableLayout);
        viewPager = root.findViewById(R.id.viewPager);
        TabLayout.Tab tab1 = tablayout.newTab();
        TabLayout.Tab tab2 = tablayout.newTab();
        tab1.setText(R.string.news_class_1);
        tab2.setText(R.string.news_class_2);
        tablayout.addTab(tab1);
        tablayout.addTab(tab2);
        viewPager.addView(domain_layout);
        viewPager.addView(aboard_layout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewPager.getChildCount();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {

                return viewPager.getChildAt(position);
            }
        });
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /********************************root**********************************/

        /********************************domain********************************/
        //recycler view settings
        swipeRefreshLayout = domain_layout.findViewById(R.id.swipeLayout_domain);
        recyclerView = domain_layout.findViewById(R.id.news_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(domain_layout.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final NewsAdapter adapter = new NewsAdapter(newsList, getActivity());
        recyclerView.setAdapter(adapter);

        addNews();

        //View Model
        newsViewModel = new NewsViewModel();

        newsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        //swipeRefresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addNews();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /*******************************domain*********************************/

        return root;
    }

    private void addNews(){
        test_num++;
        News a = new News("This is a big news, and I want to make it long enough" + test_num.toString(), "dhkafhlkdak", R.drawable.ic_news);
        newsList.add(a);
    }
}