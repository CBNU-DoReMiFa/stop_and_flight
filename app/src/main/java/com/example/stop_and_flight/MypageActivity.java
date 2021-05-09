package com.example.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MypageActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonDeleteID;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonDeleteID = (Button) findViewById(R.id.DeleteID);
        buttonDeleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MypageActivity.this, "계정이 정상적으로 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MypageActivity.this, "다시 시도 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}