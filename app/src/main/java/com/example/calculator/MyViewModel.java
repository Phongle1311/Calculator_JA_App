package com.example.calculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> input;
    public LiveData<String> getInput() {
        // singleton pattern
        if (input == null) {
            input = new MutableLiveData<String>();
            //loadInput();
            input.setValue("");
        }
        return input;
    }

    private void loadInput() {
        // Do an asynchronous operation to fetch users.
    }
}