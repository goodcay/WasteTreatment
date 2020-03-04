package com.waste.treatment.util;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesUtil {

    public static final String mTAG = "WasteTreatment";
    // 创建一个写入器
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedPreferencesUtil;

    // 构造方法
    public SharedPreferencesUtil(Context context) {
        mPreferences = context.getSharedPreferences(mTAG, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    // 单例模式
    public static SharedPreferencesUtil getInstance(Context context) {
        if (mSharedPreferencesUtil == null) {
            mSharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return mSharedPreferencesUtil;
    }

    // 存入数据
    public void putSP(String key, Object object) {
        if (object instanceof String) {
            mEditor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            mEditor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            mEditor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            mEditor.putLong(key, (Long) object);
        } else {
            mEditor.putString(key, object.toString());
        }
        mEditor.commit();

    }


    // 获取数据
    public Object getSP(String key,Object object) {
        if (object instanceof String) {
            return mPreferences.getString(key, (String) object);
        } else if (object instanceof Integer) {
            return mPreferences.getInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            return mPreferences.getBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            return mPreferences.getFloat(key, (Float) object);
        } else if (object instanceof Long) {
            return mPreferences.getLong(key, (Long) object);
        }
        return null;

    }

    // 移除数据
    public void removeSP(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }


}
