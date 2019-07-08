package microautomation.attendencesystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.Students;

public class Attendence_page extends AppCompatActivity {
RecyclerView attendence_list;
SharedPreferences prefs;
Spinner spinner_nav;
List<String> heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        spinner_nav=findViewById(R.id.spinner_nav);
        attendence_list = findViewById(R.id.attendence_list);
        attendence_list.setLayoutManager(new LinearLayoutManager(this));
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        heading =new ArrayList<>();
        //Admin Side
        if(getIntent().getStringExtra("roll_number")!=null) {
            FirebaseFirestore.getInstance().collection("Users").whereEqualTo("id", getIntent().getStringExtra("roll_number")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                       if(!queryDocumentSnapshots.isEmpty()&&queryDocumentSnapshots.size()>0&&queryDocumentSnapshots.getDocuments().get(0).exists()) {
                           Log.e("document_id", queryDocumentSnapshots.getDocuments().get(0).getId());
                           Students s = queryDocumentSnapshots.getDocuments().get(0).toObject(Students.class);
                           if (s != null) {
                               for (String subs : s.getSubjects()) {
                                   heading.add(subs);
                               }
                               spinner_nav.setAdapter(new ArrayAdapter<String>(Attendence_page.this, android.R.layout.simple_spinner_dropdown_item, heading));
                               spinner_nav.setSelection(0);
                               spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                   @Override
                                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                       ((TextView) spinner_nav.getSelectedView()).setTextColor(getResources().getColor(android.R.color.white));
                                       firebase_operations.get_attendence_of_student(Attendence_page.this, queryDocumentSnapshots.getDocuments().get(0).getId(), spinner_nav.getSelectedItem().toString(), attendence_list);
                                   }

                                   @Override
                                   public void onNothingSelected(AdapterView<?> parent) {

                                   }
                               });
                           }
                       }else{
                           Toast.makeText(Attendence_page.this,"Invalid Roll number",Toast.LENGTH_LONG).show();
                       }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Attendence_page.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            //Student Side
        }else {
            final Students s = new Gson().fromJson(prefs.getString("student_info", null), Students.class);
            if (s != null) {
                for (String subs : s.getSubjects()) {
                    heading.add(subs);
                }
            }
            spinner_nav.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, heading));
            spinner_nav.setSelection(0);

            spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) spinner_nav.getSelectedView()).setTextColor(getResources().getColor(android.R.color.white));
                    firebase_operations.get_attendence_of_student(Attendence_page.this, FirebaseAuth.getInstance().getCurrentUser().getUid(), spinner_nav.getSelectedItem().toString(), attendence_list);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }



}
