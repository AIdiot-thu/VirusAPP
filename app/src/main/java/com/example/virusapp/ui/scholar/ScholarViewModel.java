package com.example.virusapp.ui.scholar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScholarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ScholarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这是知疫学者页面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}