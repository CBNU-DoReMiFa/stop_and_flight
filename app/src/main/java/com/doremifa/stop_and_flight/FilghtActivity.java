package com.doremifa.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.doremifa.stop_and_flight.fragments.Fragment_flight1;
import com.doremifa.stop_and_flight.utils.Accessibility;
import com.doremifa.stop_and_flight.utils.AppGuideAdapter;
import com.doremifa.stop_and_flight.model.AppInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilghtActivity extends AppCompatActivity implements Fragment_flight1.OnTimePickListener {

    private String arrive_time;
    private String depart_time;
    private String uid;

    private ArrayList<AppInfo> applist = new ArrayList<>();
    private DatabaseReference mDatabase;

    AppGuideAdapter adapter = new AppGuideAdapter(getSupportFragmentManager());

    @Override
    public void onTimeSelected(String arr, String dpt) {
        arrive_time=arr;
        depart_time=dpt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        setContentView(R.layout.activity_filght);
        ViewPager viewPager = findViewById(R.id.pager_filght);
        setupViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기

        mDatabase.child("APP").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    AppInfo appname = new AppInfo();
                    appname.setName(fileSnapshot.getValue(String.class));
                    applist.add(appname);
                }
                //accessibility 서비스 시작
                Intent sintent = new Intent(getApplicationContext(), Accessibility.class); // 이동할 컴포넌트
                sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sintent.putExtra("flight","1");
                sintent.putExtra("applist", applist);
                startService(sintent); // 서비스 시작
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
    }

    @Override
    protected void onStop() {
        Intent sintent = new Intent(getApplicationContext(),Accessibility.class); // 이동할 컴포넌트
        stopService(sintent); // 서비스 끝
        super.onStop();
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new Fragment_flight1(), "1");
        viewPager.setAdapter(adapter);
    }

}