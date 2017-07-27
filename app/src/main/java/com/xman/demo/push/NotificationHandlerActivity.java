package com.xman.demo.push;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHandler;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import java.util.Map;

public class NotificationHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_handler);

        MiPushMessage msg = (MiPushMessage)getIntent().getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
        String content = msg.getContent();
        Map<String, String> extraHello = msg.getExtra();
        Log.d("XMAN_PUSH", "content:"+content);
        Log.d("XMAN_PUSH", "Hello"+extraHello.get("Hello"));
    }
}
