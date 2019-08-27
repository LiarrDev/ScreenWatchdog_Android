package com.liarr.screenwatchdog.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.liarr.screenwatchdog.Database.PauseTime;
import com.liarr.screenwatchdog.Database.ResumeTime;
import com.liarr.screenwatchdog.R;
import com.liarr.screenwatchdog.Util.LogUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView resumeListView, pauseListView;

    Date resumeDate, pauseDate;

    ResumeTime resumeToggle;
    PauseTime pauseToggle;

    List<String> resumeList = new ArrayList<>();
    List<String> pauseList = new ArrayList<>();

    ArrayAdapter<String> resumeAdapter;
    ArrayAdapter<String> pauseAdapter;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.nav_view);
        resumeListView = findViewById(R.id.resume_list);
        pauseListView = findViewById(R.id.pause_list);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Sure you wanna clear all?")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                        break;

                    case R.id.about:
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeDate = new Date(System.currentTimeMillis());
        resumeToggle = new ResumeTime();
        resumeToggle.setResumeDate(resumeDate);
        resumeToggle.save();
        LogUtil.d("===RESUME===", simpleDateFormat.format(resumeDate));

        resumeList.clear();
        pauseList.clear();

        List<ResumeTime> toggleResumeList = LitePal.findAll(ResumeTime.class);
        for (ResumeTime toggleTime : toggleResumeList) {
            String resumeTime = simpleDateFormat.format(toggleTime.getResumeDate());
            resumeList.add(resumeTime);
        }

        List<PauseTime> togglePauseList = LitePal.findAll(PauseTime.class);
        for (PauseTime toggleTime : togglePauseList) {
            String pauseTime = simpleDateFormat.format(toggleTime.getPauseDate());
            pauseList.add(pauseTime);
        }

        resumeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resumeList);
        pauseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pauseList);

        resumeListView.setAdapter(resumeAdapter);
        pauseListView.setAdapter(pauseAdapter);

        resumeAdapter.notifyDataSetChanged();
        pauseAdapter.notifyDataSetChanged();
        resumeListView.setSelection(0);
        pauseListView.setSelection(0);
    }

    @Override
    protected void onPause() {
        pauseDate = new Date(System.currentTimeMillis());
        pauseToggle = new PauseTime();
        pauseToggle.setPauseDate(pauseDate);
        pauseToggle.save();
        LogUtil.d("===PAUSE===", simpleDateFormat.format(pauseDate));
        super.onPause();
    }

    /**
     * 删除数据
     */
    private void deleteData() {
        LitePal.deleteAll(ResumeTime.class);
        LitePal.deleteAll(PauseTime.class);
    }
}
