package com.xman.demo.push;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.xman.face.model.Face;
import com.xman.face.model.FaceItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FaceListActivity extends AppCompatActivity {

    private static final String DEV_ID = "rpi";

    private TextView configView;
    private TextView loadingView;
    private ListView listView;
    private List<FaceItem> faceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faces);
//        faceList = getFaceListData();
        faceList = new ArrayList<FaceItem>();
        configView = (TextView) findViewById(R.id.start_settings);
        configView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaceListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        loadingView = (TextView) findViewById(R.id.face_list_loading);
        listView = (ListView) findViewById(R.id.face_list);
        loadingData(DEV_ID);
    }

    private void loadingData(String devID) {
        AndroidNetworking.post("http://xman.studio/rpi_face_list")
                .addBodyParameter("devid", devID)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.d(Application.TAG, response.toString());
                        parseFaceData(response);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d(Application.TAG, error.getErrorDetail());
                    }
                });
    }

    private void parseFaceData(JSONObject response) {
        try {
            JSONArray faceListJson = response.getJSONArray("facelist");
            String date = "";
            for (int i = 0; i < faceListJson.length(); i++) {
                JSONObject faceItemJson = faceListJson.getJSONObject(i);
                String time = faceItemJson.getString("time");
                String photo = faceItemJson.getString("photo");
                Log.d(Application.TAG, "time: "+time);
                Log.d(Application.TAG, "photo: "+photo);
                FaceItem item = new FaceItem(time, photo);
                JSONArray facesJson = faceItemJson.getJSONArray("faces");
                for (int j = 0; j < facesJson.length(); j++) {
                    Log.d(Application.TAG, "face_"+j+": "+facesJson.getString(j));
                    item.faces.add(facesJson.getString(j));
                }
                String cur_date = getDateFromTimeMillis(time);
                if (!date.equals(cur_date)) {
                    Log.d(Application.TAG, "new date");
                    date = cur_date;
                    FaceItem dateItem = new FaceItem();
                    dateItem.isDate = true;
                    dateItem.time = time;
                    faceList.add(dateItem);
                }
                faceList.add(item);
            }
            Log.d(Application.TAG, "list size = "+faceList.size());
        } catch (Exception e) {
            Log.d(Application.TAG, "ERROR "+e.getMessage());
            e.printStackTrace();
        }
        loadingView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(new MyListViewAdapter(FaceListActivity.this));
    }

    private List<Face> getFaceListData() {
        List<Face> list = new ArrayList<Face>();

        String date = "";
        Face face = new Face();
        face.person = "Unknown";
        face.timeStamp = "1493355287030";
        face.faceUrl = "http://xman.studio:8088/image_cache/1493355287030/face_0.jpg";
        face.photoUrl = "http://xman.studio:8088/image_cache/1493355287030/result.jpg";
        String tmp_date = getDateFromTimeMillis(face.timeStamp);
        if (!date.equals(tmp_date)) {
            date = tmp_date;
            Face dateFace = new Face();
            dateFace.date = true;
            dateFace.timeStamp = face.timeStamp;
            list.add(dateFace);
        }
        list.add(face);

        face = new Face();
        face.person = "Unknown";
        face.timeStamp = "1493355287030";
        face.faceUrl = "http://xman.studio:8088/image_cache/1493355287030/face_0.jpg";
        face.photoUrl = "http://xman.studio:8088/image_cache/1493355287030/result.jpg";
        tmp_date = getDateFromTimeMillis(face.timeStamp);
        if (!date.equals(tmp_date)) {
            date = tmp_date;
            Face dateFace = new Face();
            dateFace.date = true;
            dateFace.timeStamp = face.timeStamp;
            list.add(dateFace);
        }
        list.add(face);

        face = new Face();
        face.person = "Unknown";
        face.timeStamp = "1493355287030";
        face.faceUrl = "http://xman.studio:8088/image_cache/1493355287030/face_0.jpg";
        face.photoUrl = "http://xman.studio:8088/image_cache/1493355287030/result.jpg";
        tmp_date = getDateFromTimeMillis(face.timeStamp);
        if (!date.equals(tmp_date)) {
            date = tmp_date;
            Face dateFace = new Face();
            dateFace.date = true;
            dateFace.timeStamp = face.timeStamp;
            list.add(dateFace);
        }
        list.add(face);

        face = new Face();
        face.person = "Unknown";
        face.timeStamp = "1493455287030";
        face.faceUrl = "http://xman.studio:8088/image_cache/1493355287030/face_0.jpg";
        face.photoUrl = "http://xman.studio:8088/image_cache/1493355287030/result.jpg";
        tmp_date = getDateFromTimeMillis(face.timeStamp);
        if (!date.equals(tmp_date)) {
            date = tmp_date;
            Face dateFace = new Face();
            dateFace.date = true;
            dateFace.timeStamp = face.timeStamp;
            list.add(dateFace);
        }
        list.add(face);

        return list;
    }

    private String getTimeFromTimeMillis(String time) {
        Date date = new Date(Long.parseLong(time));
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    private String getDateFromTimeMillis(String time) {
        Date date = new Date(Long.parseLong(time));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    private class ViewHolder {
        TextView titleDate;
        LinearLayout faceLayout;
        TextView time;
        ANImageView face;
        TextView person;
    }

    private class MyListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyListViewAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return faceList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            final FaceItem face = faceList.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.adapter_view_face, null);
                holder = new ViewHolder();
                holder.titleDate = (TextView) convertView.findViewById(R.id.adapter_tv_date);
                holder.faceLayout = (LinearLayout) convertView.findViewById(R.id.adapter_layout_item);
                holder.time = (TextView) convertView.findViewById(R.id.adapter_tv_time);
                holder.face = (ANImageView) convertView.findViewById(R.id.adapter_iv_face);
                holder.person = (TextView) convertView.findViewById(R.id.adapter_tv_person);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (face.isDate) {
                holder.faceLayout.setVisibility(View.GONE);
                holder.titleDate.setVisibility(View.VISIBLE);
                holder.titleDate.setText(getDateFromTimeMillis(face.time));
            } else {
                holder.faceLayout.setVisibility(View.VISIBLE);
                holder.titleDate.setVisibility(View.GONE);
                holder.time.setText(getTimeFromTimeMillis(face.time));
                holder.face.setImageUrl(face.faces.get(0));
                holder.person.setText(face.person);
                holder.faceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(Application.TAG, "on click");
                        Intent intent = new Intent(FaceListActivity.this, SingleFaceActivity.class);
                        intent.putExtra("face", face.faces.get(0));
                        intent.putExtra("result", face.photo);
                        intent.putExtra("timestamp", face.time);
                        startActivity(intent);
                    }
                });
            }
            return convertView;
        }
    }
}
