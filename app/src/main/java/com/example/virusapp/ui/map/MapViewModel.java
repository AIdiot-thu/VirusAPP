package com.example.virusapp.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这是疫情地图界面，考虑使用ImageView?");
    }

    public LiveData<String> getText() {
        return mText;
    }
}