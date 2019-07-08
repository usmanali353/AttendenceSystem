package microautomation.attendencesystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.Students;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class Add_students extends AppCompatActivity {
Button add_students;
ImageView avatar;
Bitmap bitmap;
    Uri image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        avatar=findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1000);
            }
        });
         final TextInputEditText name=findViewById(R.id.name_txt);
         final TextInputEditText roll_no=findViewById(R.id.roll_no_txt);
         final TextInputEditText password=findViewById(R.id.password_txt);
         final TextInputEditText subjects=findViewById(R.id.subjects_txt);
         final TextInputEditText email=findViewById(R.id.email_txt);
         add_students=findViewById(R.id.add_students);
         add_students.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(name.getText().toString().isEmpty()||name.toString().equals("")||name.getText().toString()==null){
                     name.setError("Name is Required");
                 }else if(roll_no.getText().toString().isEmpty()||roll_no.toString().equals("")||roll_no.getText().toString()==null){
                     roll_no.setError("Roll Number is Required");
                 }else if(password.getText().toString().isEmpty()||password.toString().equals("")||password.getText().toString()==null){
                     password.setError("Password is Required");
                 }else if(subjects.getText().toString().isEmpty()||subjects.toString().equals("")||subjects.getText().toString()==null) {
                     subjects.setError("Provide Atleast one Subject");
                 }else if(password.getText().toString().length()<6){
                     password.setError("Password too short");
                 }else if(email.getText().toString().isEmpty()){
                     email.setError("Enter Email");
                 }else if(!isValidEmail(email.getText().toString())){
                     email.setError("Invalid Email");
                 }else if(bitmap==null||image_uri==null){
                      Toast.makeText(Add_students.this,"Select Image",Toast.LENGTH_LONG).show();
                 }else {
                     Students s = new Students();
                     firebase_operations.add_students(Add_students.this,name.getText().toString(), email.getText().toString(), password.getText().toString(),roll_no.getText().toString(), Arrays.asList(subjects.getText().toString().split(",")),"Students",image_uri);
                     name.setText("");
                     email.setText("");
                     password.setText("");
                     roll_no.setText("");
                     subjects.setText("");
                     bitmap=null;
                     avatar.setImageResource(R.drawable.upload);
                 }
             }
         });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                image_uri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(image_uri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                avatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
