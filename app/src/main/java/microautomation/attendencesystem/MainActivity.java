package microautomation.attendencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.Arrays;

import dmax.dialog.SpotsDialog;
import microautomation.attendencesystem.Barcode_Scanner.Barcode_Scanner;
import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.Students;

public class MainActivity extends AppCompatActivity {
int images[]={R.drawable.mark_attendence,R.drawable.view_attendence};
String texts[]={"Mark Attendence","View Attendence"};
AlertDialog dialog;
SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog=new SpotsDialog.Builder().setContext(MainActivity.this).build();
        prefs= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                             if(index==0){
                                 Intent i=new Intent(getApplicationContext(), Barcode_Scanner.class);
                                 startActivity(i);
                             }else if(index==1){
                                    startActivity(new Intent(MainActivity.this,Attendence_page.class));
                             }
                        }
                    })
                    .normalImageRes(images[i])
                    .normalText(texts[i])
                    .shadowEffect(true)
                    .containsSubText(false);

            bmb.addBuilder(builder);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder add_student_dialog=new AlertDialog.Builder(MainActivity.this);
            add_student_dialog.setTitle("Add Student");
            add_student_dialog.setMessage("Provide Student Information");
            View v= getLayoutInflater().inflate(R.layout.add_students,null);
            final TextInputEditText name=v.findViewById(R.id.name_txt);
            final TextInputEditText roll_no=v.findViewById(R.id.roll_no_txt);
            final TextInputEditText password=v.findViewById(R.id.password_txt);
            final TextInputEditText subjects=v.findViewById(R.id.subjects_txt);
            add_student_dialog.setView(v);
            add_student_dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
                    }else {
                        Students s = new Students(name.getText().toString(), password.getText().toString(), roll_no.getText().toString(), Arrays.asList(subjects.getText().toString().split(",")));
                        firebase_operations.add_students(MainActivity.this, s);
                    }
                }
            });
            add_student_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            add_student_dialog.show();
        }else if(item.getItemId()==R.id.log_out){
            prefs.edit().remove("student_info").apply();
            startActivity(new Intent(MainActivity.this,Login_page.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
