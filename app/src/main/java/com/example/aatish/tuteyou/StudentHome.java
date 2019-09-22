package com.example.aatish.tuteyou;

import android.nfc.Tag;
import android.os.Bundle;
import android.renderscript.Long4;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class StudentHome extends AppCompatActivity {

    //TextView NameofTeacher;
    TextView Qual;
    TextView Mon;
    TextView Con;
    TextView Amount;
    private ArrayList<String> teachersList = new ArrayList<>();

    // Define a ListView to display the data
    private ListView listViewTeachers;

    // Define an ArrayAdapter for the list
    private ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        //NameofTeacher = (TextView) findViewById(R.id.Name);
       /* Qual = (TextView) findViewById(R.id.textViewQual);
        Mon = (TextView) findViewById(R.id.textViewType);
        Con = (TextView) findViewById(R.id.textViewNumber);
        Amount = (TextView) findViewById(R.id.textViewwages);*/

        //final ListView listView= (ListView) findViewById(R.id.listofteachers);




        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://teachme-addc8.firebaseio.com");
        DatabaseReference usersdRef = rootRef.child("Teacher");
        // Associate the teachers' list with the corresponding ListView
        listViewTeachers = (ListView) findViewById(R.id.TeacherName);

        // Set the ArrayAdapter to the ListView
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teachersList);
        listViewTeachers.setAdapter(arrayAdapter);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                //int n=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String post = ds.child("name").getValue(String.class);
                    ArrayList<String> array = new ArrayList<>();
                    arrayAdapter.add(post);
                    arrayAdapter.notifyDataSetChanged();

                    //ArrayAdapter adapter = new ArrayAdapter(StudentHome.this, android.R.layout.activity_list_item , array);

                    //listView.setAdapter(adapter);
                    //NameofTeacher.setText(post);
                //n=n+1;
                }
                //String strI = String.valueOf(n);
                //Amount.setText(strI);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);



        /*final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReferenceFromUrl("https://teachme-addc8.firebaseio.com/Teacher/-L8xuF4Q60tDeRP3CANj");

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post = dataSnapshot.child("name").getValue(String.class);
                NameofTeacher.setText(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });*/

    }
}

