package com.XuanRan.translation.tail;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created By XuanRan on 2021/3/12
 */
public class XpInit implements IXposedHookLoadPackage {

    public static final String QQ = "com.tencent.mobileqq";
    public static final String ME = "com.XuanRan.translation.tail";


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {


        if(lpparam.packageName.equals(QQ) || lpparam.packageName.equals(ME)) {

            if (lpparam.packageName.equals(ME)) {
              //  new TestHook().handleLoadPackage(lpparam);
                ShowTips(lpparam);
            }
            if (lpparam.packageName.equals(QQ)){
                new QQ_MessageHook().handleLoadPackage(lpparam);
            }

        }
    }

    private void ShowTips(XC_LoadPackage.LoadPackageParam lpparam) {

        
        Class<?> clazz = XposedHelpers.findClass("com.XuanRan.translation.tail.MainActivity",lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                activity.setTitle("Hook已启动");
                Toast.makeText(activity, "Xposed已启动", Toast.LENGTH_SHORT).show();
            }
        });
        
    }
}
