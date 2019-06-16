package microautomation.attendencesystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;


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
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        heading =new ArrayList<>();
        final Students s=new Gson().fromJson(prefs.getString("student_info",null),Students.class);
        if(s!=null){
            for(String subs:s.getSubjects()){
                heading.add(subs);
            }
        }
        spinner_nav.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,heading));
        spinner_nav.setSelection(0);
        attendence_list=findViewById(R.id.attendence_list);
        attendence_list.setLayoutManager(new LinearLayoutManager(this));
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) spinner_nav.getSelectedView()).setTextColor(getResources().getColor(android.R.color.white));
                firebase_operations.get_attendence_of_student(Attendence_page.this,s.getRoll_no(),spinner_nav.getSelectedItem().toString(),attendence_list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



}
