package com.example.aatish.tuteyou;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;

public class TeacherActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE=101;
    ImageView teacher;
    EditText Name1;
    EditText Phone;
    EditText Wages;
    Uri uriprofileimage;
    ProgressBar progressBar;
    String ProfileImageUrl;
    Button save;
    Spinner Qualifications;
    Spinner Money;
    FirebaseAuth mAuth;

    DatabaseReference databaseTeacher;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        mAuth=FirebaseAuth.getInstance();
        databaseTeacher = FirebaseDatabase.getInstance().getReference("Teacher");

        Name1 =(EditText) findViewById(R.id.Teacher_Name);
        teacher = (ImageView) findViewById(R.id.pro_teacher);
        progressBar= (ProgressBar) findViewById(R.id.progressBar1);
        Phone = (EditText) findViewById(R.id.Mobile);
        Wages= (EditText) findViewById(R.id.Money);
        save =(Button) findViewById(R.id.ButtonSave);

        final String DisplayName=Name1.getText().toString();
        final String Mobile=Phone.getText().toString();
        final String Wage=Wages.getText().toString();

        final String[] select_qualification = {"Select Subject", "Physics", "Chemistry", "Mathematics", "Biology","Computer", "English"};

        Spinner spinner = (Spinner) findViewById(R.id.Teaches);
        ArrayList<StateVO> listVOs = new ArrayList<>();
        int a=select_qualification.length;
        for (int i = 0; i < a; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(TeacherActivity.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);


        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
                addDetails();
                    Toast.makeText(TeacherActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeacherActivity.this, StudentHome.class));
             }
        });

        Qualifications=(Spinner) findViewById(R.id.Quali);
        ArrayAdapter<String> myAdapter2= new ArrayAdapter<String>(TeacherActivity.this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.Qualifications));
        myAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Qualifications.setAdapter(myAdapter2);

        Money=(Spinner) findViewById(R.id.MoneyType);
        ArrayAdapter<String> myAdapter3= new ArrayAdapter<String>(TeacherActivity.this,R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.Wages_paid));
        myAdapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Money.setAdapter(myAdapter3);

    }


    private void addDetails()
    {
        String NameofTeacher=Name1.getText().toString();
        String Qual=Qualifications.getSelectedItem().toString();
        String Mon=Money.getSelectedItem().toString();
        String Con=Phone.getText().toString().trim();
        String Amount=Wages.getText().toString().trim();
        if (NameofTeacher.equals("") || Qual.equals("") || Mon.equals("") || Con.equals("") || Amount.equals("")) {

               Toast.makeText(TeacherActivity.this,"Enter information",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String id = databaseTeacher.push().getKey();
            TeacherDetails.Teacher teacher = new TeacherDetails.Teacher(NameofTeacher, Qual, Mon, Con, Amount);
            databaseTeacher.child(id).setValue(teacher);
        }




    }




    private void saveUserInfo()
    {
        final String DisplayName=Name1.getText().toString();
        final String Mobile=Phone.getText().toString();
        final String Wage=Wages.getText().toString();
        if(DisplayName.isEmpty())
        {
            Name1.setError("Name Required");
            //Name1.requestFocus();
        }
        if(Mobile.isEmpty())
        {
            Phone.setError("Enter Mobile Number");
            //Phone.requestFocus();
        }
        if(Wage.isEmpty())
        {
            Wages.setError("Enter Wages");
            //Wages.requestFocus();
        }




        FirebaseUser User=mAuth.getCurrentUser();
        if(User!=null && ProfileImageUrl!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(DisplayName).setPhotoUri(Uri.parse(ProfileImageUrl)).build();

            User.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    Toast.makeText(TeacherActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();



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
                teacher.setBackground(null);
                teacher.setImageBitmap(bitmap);

                uploadImagetoFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImagetoFirebase()
    {
        final StorageReference ProfileImageRef= FirebaseStorage.getInstance().getReference("profile_teacher/"+ System.currentTimeMillis() + ".jpg");

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
                            Toast.makeText(TeacherActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

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


    public class MyAdapter extends ArrayAdapter<StateVO> {
        private Context mContext;
        private ArrayList<StateVO> listState;
        private MyAdapter myAdapter;
        private boolean isFromView = false;

        public MyAdapter(Context context, int resource, List<StateVO> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.listState = (ArrayList<StateVO>) objects;
            this.myAdapter = this;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(final int position, View convertView,
                                  ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(mContext);
                convertView = layoutInflator.inflate(R.layout.spinner_xml, null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById(R.id.text);
                holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextView.setText(listState.get(position).getTitle());

            // To check weather checked event fire from getview() or user input
            isFromView = true;
            holder.mCheckBox.setChecked(listState.get(position).isSelected());
            isFromView = false;

            if ((position == 0)) {
                holder.mCheckBox.setVisibility(View.INVISIBLE);

            } else {
                holder.mCheckBox.setVisibility(View.VISIBLE);
            }
            holder.mCheckBox.setTag(position);
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();


                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView mTextView;
            private CheckBox mCheckBox;
        }
    }
}


