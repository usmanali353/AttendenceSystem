package microautomation.attendencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.Arrays;

import fr.ganfra.materialspinner.MaterialSpinner;
import microautomation.attendencesystem.Barcode_Scanner.Barcode_Scanner;
import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.Students;
import microautomation.attendencesystem.Model.user;

public class admin_home_page extends AppCompatActivity {
    int images[]={R.drawable.view_attendence,R.drawable.add_student,R.drawable.add_student};
    String texts[]={"View Attendence","Add Student","Add Employee"};
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if(index==0){
                                AlertDialog roll_no_dialog=new AlertDialog.Builder(admin_home_page.this)
                                .setTitle("View Attendence")
                                .setMessage("Enter Roll no of Student")
                                .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                                View v= LayoutInflater.from(admin_home_page.this).inflate(R.layout.check_attendence_layout,null);
                                final TextInputEditText roll_no=v.findViewById(R.id.roll_no_txt);
                                roll_no_dialog.setView(v);
                                roll_no_dialog.show();
                                roll_no_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(roll_no.getText().toString().isEmpty()){
                                           roll_no.setError("Roll number is reuired");
                                        }else{
                                            Intent i=new Intent(admin_home_page.this,Attendence_page.class);
                                            i.putExtra("roll_number",roll_no.getText().toString());
                                            startActivity(i);

                                        }
                                    }
                                });
                            }else if(index==1){
                                Intent i=new Intent(admin_home_page.this,Add_students.class);
                                startActivity(i);
                            }else if(index==2){
                                Intent i=new Intent(admin_home_page.this,Add_employees.class);
                                startActivity(i);

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
        if(item.getItemId()==R.id.log_out){
            prefs.edit().remove("admin_info").apply();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(admin_home_page.this, Login_page.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
