package com.clwater.bd;

import android.app.Activity;

import com.clwater.bd.lib.TestAnnotation;

import java.lang.reflect.Constructor;

public class ViewBindUtil {

    /**
     * 绑定Activity
     * */
    public static void bind(Activity activity) {
        if (activity == null) {
            return;
        }
        String activityName = activity.getClass().getName();//获取类的全限定名
        ClassLoader classLoader = activity.getClass().getClassLoader();//获得类加载器
        try {
            Class<?> loadClass = classLoader.loadClass(activityName + TestAnnotation.SUFFIX);//加载类
            Constructor<?> constructor = loadClass.getConstructor(activity.getClass());
            constructor.newInstance(activity);//调用其构造方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}