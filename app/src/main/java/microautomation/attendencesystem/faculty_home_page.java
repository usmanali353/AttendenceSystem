package microautomation.attendencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import fr.ganfra.materialspinner.MaterialSpinner;
import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.Students;
import microautomation.attendencesystem.Model.user;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.Arrays;

public class faculty_home_page extends AppCompatActivity {
    int images[]={R.drawable.view_attendence};
    String texts[]={"View Attendence"};
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index == 0) {
                                AlertDialog roll_no_dialog=new AlertDialog.Builder(faculty_home_page.this)
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
                                View v= LayoutInflater.from(faculty_home_page.this).inflate(R.layout.check_attendence_layout,null);
                                final TextInputEditText roll_no=v.findViewById(R.id.roll_no_txt);
                                roll_no_dialog.setView(v);
                                roll_no_dialog.show();
                                roll_no_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(roll_no.getText().toString().isEmpty()){
                                            roll_no.setError("Roll number is reuired");
                                        }else{
                                            Intent i=new Intent(faculty_home_page.this,Attendence_page.class);
                                            i.putExtra("roll_number",roll_no.getText().toString());
                                            startActivity(i);
                                        }
                                    }
                                });
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
            FirebaseAuth.getInstance().signOut();
            prefs.edit().remove("faculty_info").apply();
            startActivity(new Intent(faculty_home_page.this,Login_page.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
