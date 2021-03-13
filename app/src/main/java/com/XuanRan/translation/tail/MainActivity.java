package com.XuanRan.translation.tail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.translate.demo.TransApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button bn;
    EditText editText;
    final TransApi api = new TransApi(QQ_MessageHook.APP_ID, QQ_MessageHook.SECURITY_KEY);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, Environment.getDownloadCacheDirectory().toString(), Toast.LENGTH_SHORT).show();

          tv = findViewById(R.id.text);
         bn = findViewById(R.id.bn );
         editText =  findViewById(R.id.edit);
        
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tv.setText(getData());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getData() throws InterruptedException, JSONException {
        final String[] a = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
               a[0] =  api.getTransResult(editText.getText().toString(),"auto","en");
            }
        }).start();
        for (int i = 0; i < 100; i++) {
            if (a[0] != null){
                break;
            }
            Thread.sleep(10);
        }
        JSONObject json = new JSONObject(a[0]);
        String ret = editText.getText().toString() + "\n---------------\n" + json.getJSONArray("trans_result").getJSONObject(0).getString("dst");

        return ret;
    }
}
