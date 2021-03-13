package com.XuanRan.translation.tail;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.translate.demo.TransApi;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;

import java.util.ArrayList;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Created By XuanRan on 2021/3/12
 */
public class QQ_MessageHook implements IXposedHookLoadPackage {

    public static final String APP_ID = "20180105000112196";
    public static final String SECURITY_KEY = "szOaGQPSw3aqfasbo0Gy";
    public static final String TAG = "XuanRan";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        Method method = null;

        if (method == null){

            final Class<?> send = XposedHelpers.findClass("acvv",lpparam.classLoader);
            for (Method mi: send.getDeclaredMethods()) {
                if (!mi.getReturnType().equals(long[].class)) continue;
                Class[] argt = mi.getParameterTypes();
                if (argt.length != 6) continue;
                if (argt[1].equals(Context.class) //&& argt[2].equals(_SessionInfo())
                        && argt[3].equals(String.class) && argt[4].equals(ArrayList.class)) {
                    method = mi;
                    method.setAccessible(true);
                    break;
                }
            }

          //  Log.e("XuanRanQQHook",Cache.size()+"缓存小尾巴数据：\n"+method.getName());
        }

        final TransApi api = new TransApi(APP_ID, SECURITY_KEY);


      XposedBridge.hookMethod(method, new XC_MethodHook() {
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
              super.beforeHookedMethod(param);
              final String msg = (String) param.args[3];

              final String[] a = {null};

              new Thread(new Runnable() {
                  @Override
                  public void run() {
                      String y =  api.getTransResult(msg,"auto","en");
                      a[0] = y;
                  }
              }).start();

              for (int i = 0; i < 50; i++) {
                  if (a[0] != null ){
                      break;
                  }
                  Thread.sleep(10);
              }
              JSONObject json = new JSONObject(a[0]);
              String ret = msg + "\n-----------------\n" + json.getJSONArray("trans_result").getJSONObject(0).getString("dst");

              param.args[3] = ret;

          }
      });
    }
}
