package com.example.myapplication.ui.signin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SigninViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SigninViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is siginin fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
