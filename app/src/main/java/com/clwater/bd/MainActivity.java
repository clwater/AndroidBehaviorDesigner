package com.clwater.bd;

import android.os.Bundle;

import com.clwater.bd.lib.MyClass;
import com.clwater.bd.lib.TestAnnotation;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.navigation.ui.AppBarConfiguration;

import com.clwater.bd.databinding.ActivityMainBinding;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @TestAnnotation(value = "6")
    public String testField = "testField";

    @TestAnnotation()
    public String testField2 = "testField";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Log.d("MainActivity", "onCreate: " + MyClass.test());

        //通过注解生成View；
//        getAllAnnotationView();

        ViewBindUtil.bind(this);

        Log.d("gzb", "testField : " + testField);
        Log.d("gzb", "testField2: " + testField2);
    }

    private void getAllAnnotationView() {
        //获得成员变量
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                //判断注解
                field.getAnnotations();
                //确定注解类型
                if (field.isAnnotationPresent(TestAnnotation.class)) {
                    //允许修改反射属性
                    field.setAccessible(true);
                    TestAnnotation getViewTo = field.getAnnotation(TestAnnotation.class);
                    assert getViewTo != null;
                    field.set(this, getViewTo.value());
                }
            } catch (Exception ignored) {
            }
        }
    }

}