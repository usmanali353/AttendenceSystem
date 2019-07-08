package microautomation.attendencesystem;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import microautomation.attendencesystem.Model.user;

import android.view.View;
import android.widget.ImageView;

public class scanned_barcode_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         TextInputEditText name=findViewById(R.id.name_txt);
         TextInputEditText roll_no=findViewById(R.id.roll_no_txt);
         TextInputEditText email=findViewById(R.id.email_txt);
         TextInputEditText password=findViewById(R.id.password_txt);
         ImageView avatar=findViewById(R.id.avatar);
        user u=new Gson().fromJson(getIntent().getStringExtra("scanned_barcode_details"),user.class);
        password.setVisibility(View.GONE);
        name.setEnabled(false);
        roll_no.setEnabled(false);
        email.setEnabled(false);

        if(u!=null) {
            name.setText(u.getName());
            roll_no.setText(u.getId());
            email.setText(u.getEmail());
            Picasso.get().load(u.getImage_url()).into(avatar);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(scanned_barcode_details.this,guard_home_page.class));
        finish();
    }
}
