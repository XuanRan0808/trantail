package com.XuanRan.translation.tail;

import com.baidu.translate.demo.TransApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created By XuanRan on 2021/3/13
 */
public class TestHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XposedHelpers.findAndHookMethod("com.XuanRan.translation.tail.MainActivity",lpparam.classLoader, "getData", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                final String result = (String) param.getResult();
                final TransApi api = new TransApi(QQ_MessageHook.APP_ID, QQ_MessageHook.SECURITY_KEY);
                String result2 = api.getTransResult(result,"auto","en");

                for (int i = 0; i < 1000; i++) {
                    if (!result2.isEmpty()){
                        break;
                    }
                    Thread.sleep(10);
                }
             /*   final boolean[] flag = {false};
                final String[] data = {""};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection httpURLConnection = null;
                        BufferedReader reader = null;
                        try{
                             URL url = new URL("http://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=en&q=" + java.net.URLEncoder.encode(result));
                            //URL url = new URL("https://www.baidu.com" );
                            httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("GET");
                            httpURLConnection.setConnectTimeout(2000);
                            httpURLConnection.setReadTimeout(2000);
                            InputStream inputStream = httpURLConnection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader((inputStream)));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line = "";
                            while((line = reader.readLine())!=null){
                                stringBuilder.append(line);
                            }
                            data[0] = stringBuilder.toString();
                            XposedBridge.log(data[0]);
                            flag[0] = true;

                        } catch (Throwable e) {
                            e.printStackTrace();
                        }finally {
                            if (reader!=null){
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (httpURLConnection!=null){
                                httpURLConnection.disconnect();
                            }
                        }
                    }
                }).start();


                for(int i = 0; i < 500; i++)
                {
                    if(flag[0])
                    {
                        break;
                    }
                    Thread.sleep(10);
                }
*/
                param.setResult(result2);
            }
        });

    }
}
