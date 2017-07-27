package com.xman.demo.push;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.xman.face.model.FaceItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String DEVICE_AWS_T2 = "aws_t2";
    private static final String DEVICE_RPI = "rpi";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mLoadingTextView;
    private List<FaceItem> faceList;
    private String currentDeviceID = DEVICE_AWS_T2;

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public View rootLayout;
            public TextView titleDate;
            public LinearLayout faceLayout;
            public TextView time;
            public ANImageView face;
            public TextView person;

            public MyViewHolder(View view) {
                super(view);
                rootLayout = view;
                titleDate = (TextView) view.findViewById(R.id.adapter_tv_date);
                faceLayout = (LinearLayout) view.findViewById(R.id.adapter_layout_item);
                time = (TextView) view.findViewById(R.id.adapter_tv_time);
                face = (ANImageView) view.findViewById(R.id.adapter_iv_face);
                person = (TextView) view.findViewById(R.id.adapter_tv_person);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_face, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final FaceItem face = faceList.get(position);
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
            }
            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(Application.TAG, "on click");
                    if (!face.isDate) {
                        Intent intent = new Intent(HomeActivity.this, SingleFaceActivity.class);
                        intent.putExtra("face", face.faces.get(0));
                        intent.putExtra("result", face.photo);
                        intent.putExtra("timestamp", face.time);
                        startActivity(intent);
                    }
                }
            });
            /*
            holder.rootLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    CharSequence options[] = {"Delete"};
                    builder.setTitle(R.string.unknown_person)
                            .setItems(options, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                }
                            });
                    builder.create().show();
                    return false;
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return faceList.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.face_swipe_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//        mRefreshLayout.setDistanceToTriggerSync(500);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingData(currentDeviceID);
            }
        });
        mLoadingTextView = (TextView) findViewById(R.id.face_loading_text_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.face_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadingData(DEVICE_AWS_T2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_dev_0) {
            currentDeviceID = DEVICE_RPI;
            loadingData(currentDeviceID);
        } else if (id == R.id.nav_dev_1) {
            currentDeviceID = DEVICE_AWS_T2;
            loadingData(currentDeviceID);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void loadingData(String devID) {
        mLoadingTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
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
                        mRefreshLayout.setRefreshing(false);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d(Application.TAG, error.getErrorDetail());
                        mRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void parseFaceData(JSONObject response) {

        faceList = new ArrayList<FaceItem>();
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
        mLoadingTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
