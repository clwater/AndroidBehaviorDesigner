package com.clwater.bd;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.clwater.bd.databinding.ActivityMainBinding;
import com.clwater.lib.BDTestAnnotation;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @BDTestAnnotation(value = "1")
    public String test_1 = "";

    @BDTestAnnotation()
    public String test_2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Log.d("gzb", "test_1: " + test_1);
        Log.d("gzb", "test_2: " + test_2);

        TestBDUtil.bind(this);
        Log.d("gzb", "===============================");
        Log.d("gzb", "test_1: " + test_1);
        Log.d("gzb", "test_2: " + test_2);
    }

}