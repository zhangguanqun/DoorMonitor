package com.xman.demo.push;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SingleFaceActivity extends AppCompatActivity {

    private static final String TEST_RESULT_URL = "http://xman.studio:8088/image_cache/1493355287030/result.jpg";
    private static final String TEST_FACE_URL = "http://xman.studio:8088/image_cache/1493355287030/face_0.jpg";

    private ANImageView faceView;
    private ANImageView resultView;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_face);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        /**
         * extra String @param face
         * extra String @param result
         * extra String @param timestamp
         */
        String faceUrl = null;
        String resultUrl = null;
        String timeStamp = null;
        MiPushMessage msg = (MiPushMessage)getIntent().getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
        if (msg != null) {
            Map<String, String> intentExtra = msg.getExtra();
            faceUrl = intentExtra.get("face");//getIntent().getStringExtra("face");
            resultUrl = intentExtra.get("result");//getIntent().getStringExtra("result");
            timeStamp = intentExtra.get("timestamp");//getIntent().getStringExtra("timestamp");
        } else {
            faceUrl = getIntent().getStringExtra("face");
            resultUrl = getIntent().getStringExtra("result");
            timeStamp = getIntent().getStringExtra("timestamp");
        }

        faceView = (ANImageView) findViewById(R.id.face_view);
        faceView.setDefaultImageResId(R.mipmap.ic_launcher);
        faceView.setImageUrl(faceUrl);

        resultView = (ANImageView) findViewById(R.id.result_view);
        resultView.setDefaultImageResId(R.mipmap.ic_launcher);
        resultView.setImageUrl(resultUrl);

        time = (TextView) findViewById(R.id.tv_time);
        Date date = new Date(Long.parseLong(timeStamp));
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateFormatted = formatter.format(date);
        time.setText(dateFormatted);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
