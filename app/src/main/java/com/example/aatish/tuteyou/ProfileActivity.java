package com.example.aatish.tuteyou;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.ButtonStudent).setOnClickListener(this);
        findViewById(R.id.ButtonTeacher).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ButtonTeacher:
                    startActivity(new Intent(this, TeacherActivity.class));
                    break;
            case R.id.ButtonStudent:
                startActivity(new Intent(this, StudentActivity.class));
                break;
        }

    }
}
