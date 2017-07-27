package com.xman.demo.push;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity {

    public static List<String> logList = new CopyOnWriteArrayList<String>();

    private TextView mLogView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Application.setMainActivity(this);
        mLogView = (TextView) findViewById(R.id.log);
        // 设置别名
        findViewById(R.id.set_alias).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.set_alias)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.setAlias(MainActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 撤销别名
        findViewById(R.id.unset_alias).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unset_alias)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.unsetAlias(MainActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();

            }
        });
        // 设置帐号
        findViewById(R.id.set_account).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.set_account)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.setUserAccount(MainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();

            }
        });
        // 撤销帐号
        findViewById(R.id.unset_account).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unset_account)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.unsetUserAccount(MainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 设置标签
        findViewById(R.id.subscribe_topic).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.subscribe_topic)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.subscribe(MainActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 撤销标签
        findViewById(R.id.unsubscribe_topic).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unsubscribe_topic)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.unsubscribe(MainActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 设置接收消息时间
        findViewById(R.id.set_accept_time).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimeIntervalDialog(MainActivity.this, new TimeIntervalDialog.TimeIntervalInterface() {

                    @Override
                    public void apply(int startHour, int startMin, int endHour,
                                      int endMin) {
                        MiPushClient.setAcceptTime(MainActivity.this, startHour, startMin, endHour, endMin, null);
                    }

                    @Override
                    public void cancel() {
                        //ignore
                    }

                })
                        .show();
            }
        });
        // 暂停推送
        findViewById(R.id.pause_push).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MiPushClient.pausePush(MainActivity.this, null);
            }
        });

        findViewById(R.id.resume_push).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MiPushClient.resumePush(MainActivity.this, null);
            }
        });

        findViewById(R.id.start_face_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingleFaceActivity.class);
                intent.putExtra("face","http://xman.studio:8088/image_cache/1493355287030/face_0.jpg");
                intent.putExtra("result","http://xman.studio:8088/image_cache/1493355287030/result.jpg");
                intent.putExtra("timestamp", "1493355287030");
                startActivity(intent);
            }
        });

        findViewById(R.id.start_faces_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaceListActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.get_face_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.post("http://192.168.8.251/HelloWorld/rpi_face_list")
                        .addBodyParameter("devid", "mac")
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                Log.d(Application.TAG, response.toString());
                                try {
                                    JSONArray faceListJson = response.getJSONArray("facelist");
                                    for (int i = 0; i < faceListJson.length(); i++) {
                                        JSONObject faceItemJson = faceListJson.getJSONObject(i);
                                        Log.d(Application.TAG, "time: "+faceItemJson.get("time"));
                                        Log.d(Application.TAG, "photo: "+faceItemJson.get("photo"));
                                        JSONArray facesJson = faceItemJson.getJSONArray("faces");
                                        for (int j = 0; j < facesJson.length(); j++) {
                                            Log.d(Application.TAG, "face_"+j+": "+facesJson.getString(j));
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d(Application.TAG, error.getErrorDetail());
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Application.setMainActivity(null);
    }

    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
        mLogView.setText(AllLog);
    }
}
