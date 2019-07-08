package microautomation.attendencesystem;

import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import microautomation.attendencesystem.Firebase_Operations.firebase_operations;

public class Login_page extends AppCompatActivity {
Button btn;
TextInputEditText email_txt,password_txt;
SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        btn=findViewById(R.id.btn);
        email_txt=findViewById(R.id.email_txt);
        password_txt=findViewById(R.id.password_txt);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("faculty_info",null)!=null){
            Intent i=new Intent(this,faculty_home_page.class);
            startActivity(i);
            finish();
        }else if(prefs.getString("student_info",null)!=null){
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }else if(prefs.getString("guard_info",null)!=null){
            Intent i=new Intent(this,guard_home_page.class);
            startActivity(i);
            finish();
        }else if(prefs.getString("admin_info",null)!=null){
            Intent i=new Intent(this,admin_home_page.class);
            startActivity(i);
            finish();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_txt.getText().toString().isEmpty()||email_txt.toString().equals("")||email_txt.getText().toString()==null){
                    email_txt.setError("Email is Required");
                }else if(password_txt.getText().toString().isEmpty()||password_txt.toString().equals("")||password_txt.getText().toString()==null){
                    password_txt.setError("Password is Required");
                }else if(password_txt.getText().toString().length()<6){
                    password_txt.setError("Password too short");
                }else if(!isValidEmail(email_txt.getText().toString())){
                    password_txt.setError("Invalid Email");
                }else {
                    firebase_operations.sign_in(Login_page.this, email_txt.getText().toString(), password_txt.getText().toString());
                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
