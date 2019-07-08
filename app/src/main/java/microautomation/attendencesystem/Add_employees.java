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
import fr.ganfra.materialspinner.MaterialSpinner;
import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.user;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Add_employees extends AppCompatActivity {
Bitmap bitmap;
Uri image_uri;
    ImageView upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employees);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextInputEditText name=findViewById(R.id.name_txt);
        final TextInputEditText roll_no=findViewById(R.id.roll_no_txt);
        final TextInputEditText email=findViewById(R.id.email_txt);
        final TextInputEditText password=findViewById(R.id.password_txt);
        final MaterialSpinner select_role=findViewById(R.id.user_role);
        Button add_employees=findViewById(R.id.add_employees);
         upload=findViewById(R.id.avatar);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1000);
            }
        });
        String[] ITEMS = {"Faculty Member","Guard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_employees.this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_role.setAdapter(adapter);
        add_employees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()||name.toString().equals("")||name.getText().toString()==null){
                    name.setError("Name is Required");
                }else if(roll_no.getText().toString().isEmpty()||roll_no.toString().equals("")||roll_no.getText().toString()==null){
                    roll_no.setError("Employee Number is Required");
                }else if(password.getText().toString().isEmpty()||password.toString().equals("")||password.getText().toString()==null){
                    password.setError("Password is Required");
                }else if(password.getText().toString().length()<6){
                    password.setError("Password too short");
                }else if(email.getText().toString().isEmpty()){
                    email.setError("Enter Email");
                }else if(!isValidEmail(email.getText().toString())){
                    email.setError("Invalid Email");
                }else if(select_role.getSelectedItem()==null){
                    select_role.setError("Please Select Role");
                }else if(bitmap==null||image_uri==null){
                  Toast.makeText(Add_employees.this,"Select Image",Toast.LENGTH_LONG).show();
                }else {
                    firebase_operations.add_faculty_member_or_guard(Add_employees.this, name.getText().toString(),email.getText().toString(), password.getText().toString(),roll_no.getText().toString(),select_role.getSelectedItem().toString(),  image_uri );
                    name.setText("");
                    email.setText("");
                    password.setText("");
                    roll_no.setText("");
                    bitmap=null;
                    upload.setImageResource(R.drawable.upload);
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
                upload.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

}
