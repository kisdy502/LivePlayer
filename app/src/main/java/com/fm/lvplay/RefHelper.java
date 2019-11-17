package com.fm.lvplay;

import android.content.Context;

import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2019/3/28.
 */

public class RefHelper {

    public static DexClassLoader getClassLoader(Context context, String dexPath, String optimizedDirectory) {
        DexClassLoader classLoader = new DexClassLoader(dexPath, optimizedDirectory,
                null, context.getClassLoader());
        return classLoader;
    }

    public static Object getFiledValue(DexClassLoader classLoader, String className, String
            fieldName) {
        try {
            Class clsConstant = classLoader.loadClass(className);
            Field f = clsConstant.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
