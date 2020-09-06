package com.example.virusapp.ui.news;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mView;

    public NewsViewModel() {
        mView = new MutableLiveData<>();
        mView.setValue("新闻界面");
    }

    public MutableLiveData<String> getText() {
        return mView;
    }
}