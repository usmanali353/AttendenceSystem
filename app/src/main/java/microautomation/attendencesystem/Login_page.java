package microautomation.attendencesystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.Students;

public class Login_page extends AppCompatActivity {
Button btn;
TextInputEditText email_txt,password_txt;
CollectionReference students_ref;
SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        btn=findViewById(R.id.btn);
        email_txt=findViewById(R.id.email_txt);
        password_txt=findViewById(R.id.password_txt);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        students_ref= FirebaseFirestore.getInstance().collection("Students");
        Students s=new Gson().fromJson(prefs.getString("student_info",null),Students.class);
        if(s!=null){
            startActivity(new Intent(Login_page.this,MainActivity.class));
            finish();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_txt.getText().toString().isEmpty()||email_txt.toString().equals("")||email_txt.getText().toString()==null){
                    email_txt.setError("Roll Number is Required");
                }else if(password_txt.getText().toString().isEmpty()||password_txt.toString().equals("")||password_txt.getText().toString()==null){
                    password_txt.setError("Password is Required");
                }else if(password_txt.getText().toString().length()<6){
                    password_txt.setError("Password too short");
                }else {
                    firebase_operations.sign_in(Login_page.this, email_txt.getText().toString(), password_txt.getText().toString());
                }
            }
        });
    }

}
