package com.clwater.bd;

import android.os.Bundle;

import com.clwater.bd.lib.TestAnnotation;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;

import androidx.navigation.ui.AppBarConfiguration;

import com.clwater.bd.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

//    @TestAnnotation(value = "6")
//    public String testField = "testField";

    @TestAnnotation(R.id.testButton)
    Button testButton;

//    @TestAnnotation()
//    public String testField2 = "testField";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
//        Log.d("MainActivity", "onCreate: " + MyClass.test());

        //通过注解生成View；
//        getAllAnnotationView();

        ViewBindUtil.bind(this);
        testButton.setText("666");
//        Log.d("gzb", "testField : " + testField);
//        Log.d("gzb", "testField2: " + testField2);
    }

}