package com.aye.intelrobot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ListView lvContent;
    private EditText etContent;
    private Button btSend;

    private List<ChatBean> chatBeanList;
    private ChatAdapter chatAdapter;

    private String sendMsg;
    private ChatBean chatBean;

    private String welcome[];
    private static final String WEB_SITE = "http://www.tuling123.com/openapi/api";
    private static final String KEY = "3e7ac6cff6b64a7d939d09a6d6c29642";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContent = (ListView) findViewById(R.id.lvContent);
        etContent = (EditText) findViewById(R.id.etContent);
        btSend = (Button) findViewById(R.id.btSend);
        welcome=getResources().getStringArray(R.array.welcome);
        chatBeanList=new ArrayList<>();
        chatAdapter=new ChatAdapter(this,chatBeanList);
        lvContent.setAdapter(chatAdapter);
        int position=(int)(Math.random()*welcome.length-1);
        showData(welcome[position]);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();   //发送信息
            }
        });
        etContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                sendData();
                return false;
            }
        });
    }

    private void showData(String msg) {
        ChatBean chatBean=new ChatBean(ChatBean.SEND,msg);
        chatBeanList.add(chatBean);
        Log.i("Aye-showData",chatBean.toString());
        chatAdapter.notifyDataSetChanged();
    }

    private void sendData() {
        sendMsg=etContent.getText().toString();
        if (TextUtils.isEmpty(sendMsg)){
            Toast.makeText(this,"输入为空",Toast.LENGTH_SHORT).show();
            return;
        }
        etContent.setText("");
        sendMsg=sendMsg.replaceAll("","").replaceAll("","").trim();
        chatBean=new ChatBean(ChatBean.RECEIVE,sendMsg);
        chatBeanList.add(chatBean);
        chatAdapter.notifyDataSetChanged();
        getDataFromServer();

    }

    private void getDataFromServer() {
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(WEB_SITE+"?key="+KEY+"&info="+sendMsg).build();
        Log.i("Aye",WEB_SITE+"?key="+KEY+"&info="+sendMsg);
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Aye-OkHttp","未知错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg=response.body().string();
                Message resMsg=new Message();
                resMsg.what=1;
                resMsg.obj=msg;
                Log.i("Aye-response",msg);
                handler.sendMessage(resMsg);
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj!=null){
                        String result=(String)msg.obj;
                        Log.i("Aye-handler",result);
                        paresData(result);
                    }
                    break;
            }
        }
    };

    private void paresData(String content) {
            try {
                JSONObject jsonObject=new JSONObject(content);
                String resContent=jsonObject.getString("text");
                int code=jsonObject.getInt("code");
                Log.i("Aye-parse",resContent+","+code);
                updateView(code,resContent);
            } catch (JSONException e) {
                e.printStackTrace();
        }
    }

    private void updateView(int code, String resContent) {
        switch (code) {
            case 40001:
                showData("亲爱的，未找到对应的用户信息，请稍后重试。");
            case 40004:
                showData("主人，今天我累了，我要休息了，明天再来找我耍吧");
                break;
            case 40005:
                showData("主人，你说的是外星语吗？");
                break;
            case 40006:
                showData("主人，我今天要去约会哦，暂不接客啦");
                break;
            case 40007:
                showData("主人，明天再和你耍啦，我生病了，呜呜......");
                break;
            default:
                showData(resContent);
                break;
        }
    }
}