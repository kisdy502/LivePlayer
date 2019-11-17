package com.fm.lvplay;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MyApp extends Application {

    private DexClassLoader kankanDexClsLoader;
    private Object kkApplicationObj;

    private void kkAttach() {
        try {
            Class kkApplicationCls = kankanDexClsLoader.loadClass("com.ksp.lib.KkApplication");
            Method method = kkApplicationCls.getMethod("getApp", null);
            kkApplicationObj = method.invoke(null, null);

            Class[] parmas = new Class[]{
                    Context.class
            };
            Method attchMethod = kkApplicationCls.getMethod("attachBaseContext", Context.class);
            attchMethod.invoke(kkApplicationObj, this);
            Log.d("App", "KkApplication.attachBaseContext");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void kkAppOnCreate() {
        try {
            if (kkApplicationObj == null) {
                Log.d("App", "KkApplication.attach failed");
                return;
            }
            Class kkApplicationCls = kankanDexClsLoader.loadClass("com.ksp.lib.KkApplication");
            Method method = kkApplicationCls.getMethod("onCreate", null);
            method.invoke(kkApplicationObj, null);
            Log.d("App", "KkApplication.onCreate");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /*AssetsHelper.cpAssetToFile(base, "subgroup_sdk.jar", getFileParent(base).getAbsolutePath(),
                "ksp_SDK.jar");
        String internalPath = getFileParent(this).getAbsolutePath() + File.separator + "ksp_SDK.jar";
        kankanDexClsLoader = new DexClassLoader(internalPath, getFileParent(this).getAbsolutePath(), null, getClassLoader());
        kkAttach();*/
    }


    @Override
    public void onCreate() {
        super.onCreate();
        /*kkAppOnCreate();*/
    }


    public static File getFileParent(Context context) {
        return context.getDir("dynamic_jars", Context.MODE_PRIVATE);
    }
}
