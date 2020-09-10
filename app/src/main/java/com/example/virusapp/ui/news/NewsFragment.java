package com.example.virusapp.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.virusapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private Integer test_num = 0;
    private ImageButton addBTN;
    private List<NewsTagFragment> fragment_list = new ArrayList<>();
    private TabLayout tablayout;
    private ViewPager viewPager;
    private boolean open = false;

    // Tag buttons
    private Button domain_news_button;
    private Button aboard_news_button;
    private Button yc_news_button;
    private Button fake_news_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout root = (LinearLayout) inflater.inflate(R.layout.fragment_news, container, false);
        final NewsTagFragment domain = new NewsTagFragment("domain");
        final NewsTagFragment aboard = new NewsTagFragment("aboard");
        addBTN = root.findViewById(R.id.addBTN);
        domain_news_button = root.findViewById(R.id.btn_domain_news);
        aboard_news_button = root.findViewById(R.id.btn_aboard_news);
        yc_news_button = root.findViewById(R.id.btn_yc_news);
        fake_news_button = root.findViewById(R.id.btn_fk_news);

        /*******************************root**********************************/
        //tablayout & viewPager
        tablayout = root.findViewById(R.id.tableLayout);
        viewPager = root.findViewById(R.id.viewPager);
        TabLayout.Tab tab1 = tablayout.newTab();
        TabLayout.Tab tab2 = tablayout.newTab();
        tab1.setText("国内新闻");
        tab2.setText("国外新闻");
        tablayout.addTab(tab1);
        tablayout.addTab(tab2);
        fragment_list.add(domain);
        fragment_list.add(aboard);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        class mPagerAdapter extends FragmentStatePagerAdapter{
            public mPagerAdapter(@NonNull FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return fragment_list.get(position);
            }

            @Override
            public int getCount() {
                return fragment_list.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragment_list.get(position).getTag_name();
            }

            @Override
            public int getItemPosition(Object object){
                return PagerAdapter.POSITION_NONE;
            }
        }

        final mPagerAdapter pagerAdapter = new mPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(pagerAdapter);

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
                int position = tab.getPosition();
                Integer pos = position;
                Log.d("test list", pos.toString());
                if(open) {
                    String tab_name = tab.getText().toString();
                    tablayout.removeTab(tab);
                    if(tab_name.equals("国内新闻")){
                        domain_news_button.setVisibility(View.VISIBLE);
                        fragment_list.remove(position);
                        pagerAdapter.notifyDataSetChanged();
                    }
                    else if(tab_name.equals("国外新闻")){
                        aboard_news_button.setVisibility(View.VISIBLE);
                        fragment_list.remove(position);
                        pagerAdapter.notifyDataSetChanged();
                    }
                    else if(tab_name.equals("洋葱新闻")){
                        yc_news_button.setVisibility(View.VISIBLE);
                        fragment_list.remove(position);
                        pagerAdapter.notifyDataSetChanged();
                    }
                    else if(tab_name.equals("fake news")){
                        fake_news_button.setVisibility(View.VISIBLE);
                        fragment_list.remove(position);
                        pagerAdapter.notifyDataSetChanged();
                    }
                    return;
                }
            }
        });

        //set add button
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout tv = root.findViewById(R.id.TagChoseLayout);
                if(!open) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
                    addBTN.setImageResource(R.drawable.ic_baseline_remove_24);
                    open = true;
                }
                else{
                    tv.setVisibility(View.INVISIBLE);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    addBTN.setImageResource(R.drawable.ic_baseline_add);
                    open = false;
                }
            }
        });

        //Set Tag buttons
        domain_news_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsTagFragment domain_news = new NewsTagFragment("domain");
                domain_news_button.setVisibility(View.GONE);
                TabLayout.Tab tab = tablayout.newTab();
                tab.setText("国内新闻");
                tablayout.addTab(tab);
                fragment_list.add(domain_news);
                pagerAdapter.notifyDataSetChanged();
            }
        });

        aboard_news_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsTagFragment domain_news = new NewsTagFragment("aboard");
                aboard_news_button.setVisibility(View.GONE);
                TabLayout.Tab tab = tablayout.newTab();
                tab.setText("国外新闻");
                tablayout.addTab(tab);
                fragment_list.add(domain_news);
                pagerAdapter.notifyDataSetChanged();
            }
        });

        yc_news_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsTagFragment domain_news = new NewsTagFragment("yc");
                yc_news_button.setVisibility(View.GONE);
                TabLayout.Tab tab = tablayout.newTab();
                tab.setText("洋葱新闻");
                tablayout.addTab(tab);
                fragment_list.add(domain_news);
                pagerAdapter.notifyDataSetChanged();
            }
        });

        fake_news_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsTagFragment domain_news = new NewsTagFragment("fake");
                fake_news_button.setVisibility(View.GONE);
                TabLayout.Tab tab = tablayout.newTab();
                tab.setText("fake news");
                tablayout.addTab(tab);
                fragment_list.add(domain_news);
                pagerAdapter.notifyDataSetChanged();
            }
        });
        /********************************root**********************************/

        return root;
    }
}