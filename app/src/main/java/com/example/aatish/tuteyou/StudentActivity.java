package com.example.aatish.tuteyou;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static com.google.firebase.database.DatabaseReference.*;

public class StudentActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE=101;
    ImageView student;
    EditText Name1;
    Uri uriprofileimage;
    ProgressBar progressBar;
    String ProfileImageUrl;
    Button Save;
    FirebaseAuth mAuth;
    Spinner StudyPlace;
    Spinner myClass;

    DatabaseReference databaseStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        mAuth=FirebaseAuth.getInstance();

        databaseStudent = FirebaseDatabase.getInstance().getReference("Student");


        Name1 =(EditText) findViewById(R.id.Teacher_Name);
        student = (ImageView) findViewById(R.id.student_profile);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        Save=(Button) findViewById(R.id.StudentNext);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();

            }
        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
                addPlaceClass();



            }
        });


        StudyPlace=(Spinner) findViewById(R.id.Place);
        ArrayAdapter<String> myAdapter1= new ArrayAdapter<String>(StudentActivity.this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.PlaceToStudy));
        myAdapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        StudyPlace.setAdapter(myAdapter1);

        myClass=(Spinner) findViewById(R.id.Class);
        ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(StudentActivity.this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.Classes));
        myAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        myClass.setAdapter(myAdapter);


        }

    private void addPlaceClass()

    {
        String NameofStudent=Name1.getText().toString();
        String Place=StudyPlace.getSelectedItem().toString();
        String Classin=myClass.getSelectedItem().toString();

        if (NameofStudent.equals(""))
            Toast.makeText(StudentActivity.this,"Enter Name",Toast.LENGTH_SHORT).show();
        else
        {
            String id=databaseStudent.push().getKey();
            StudentDetails.Student student = new StudentDetails.Student(NameofStudent,Place, Classin);
            databaseStudent.child(id).setValue(student);

            Toast.makeText(StudentActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StudentActivity.this, StudentHome.class));

        }



    }


   private void saveUserInfo()
        {
            String DisplayName=Name1.getText().toString();
            if(DisplayName.isEmpty())
            {
                Name1.setError("Name Required");
                Name1.requestFocus();
                return;
            }




            FirebaseUser User=mAuth.getCurrentUser();
            if(User!=null && ProfileImageUrl!=null){
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(DisplayName).setPhotoUri(Uri.parse(ProfileImageUrl)).build();

                User.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(StudentActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();

                    }
                });
            }

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uriprofileimage=data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriprofileimage);
                        student.setBackground(null);
                        student.setImageBitmap(bitmap);

                        uploadImagetoFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImagetoFirebase()
    {
        final StorageReference ProfileImageRef= FirebaseStorage.getInstance().getReference("profile_stud/"+ System.currentTimeMillis() + ".jpg");

        if(uriprofileimage!=null) {
            progressBar.setVisibility(View.VISIBLE);
            ProfileImageRef.putFile(uriprofileimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    ProfileImageUrl=taskSnapshot.getDownloadUrl().toString();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(StudentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
    private void showImageChooser()
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"),CHOOSE_IMAGE);


        }

}


