package microautomation.attendencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.biometric.BiometricPrompt;
import dmax.dialog.SpotsDialog;
import microautomation.attendencesystem.Barcode_Scanner.Barcode_Scanner;
import microautomation.attendencesystem.Firebase_Operations.firebase_operations;
import microautomation.attendencesystem.Model.Students;

public class MainActivity extends AppCompatActivity {
int images[]={R.drawable.view_attendence,R.drawable.profile,R.drawable.scan_fingerprint};
String texts[]={"View Attendence","Profile","Attendence by Fingerprint"};
AlertDialog dialog;
Students s;
    String[] listitems;
String current_item;
SharedPreferences prefs;
ImageView logo;
RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog=new SpotsDialog.Builder().setContext(MainActivity.this).build();
        root=findViewById(R.id.root_layout);
        logo=findViewById(R.id.logo);

        prefs= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        s=new Gson().fromJson(prefs.getString("student_info",""),Students.class);
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                             if(index==0){
                                 startActivity(new Intent(MainActivity.this,Attendence_page.class));
                             }else if(index==1){
                                 androidx.appcompat.app.AlertDialog.Builder profile=new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                                 profile.setTitle("Profile");
                                 View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.profile_layout,null);
                                 final TextInputEditText name=v.findViewById(R.id.name_txt);
                                 final TextInputEditText roll_no=v.findViewById(R.id.roll_no_txt);
                                 final TextInputEditText email=v.findViewById(R.id.email_txt);
                                 final TextInputEditText password=v.findViewById(R.id.password_txt);
                                 ImageView avatar=findViewById(R.id.avatar);

                                 //final TextInputEditText phone=v.findViewById(R.id.phone_txt);
                                 Students s=new Gson().fromJson(prefs.getString("student_info",""),Students.class);
                                 if(s!=null){
                                     name.setEnabled(false);
                                     roll_no.setEnabled(false);
                                     email.setEnabled(false);
                                     password.setEnabled(false);
                                     name.setText(s.getName());
                                     roll_no.setText(s.getId());
                                     email.setText(s.getEmail());
                                     password.setText(s.getPassword());
                                     Picasso.get().load(s.getImage_url()).into(avatar);
                                 }
                                 profile.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         dialog.dismiss();
                                     }
                                 }).setView(v).show();
                             }else if(index==2){
                                 scan_fingerprint();
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
            prefs.edit().remove("student_info").apply();
            startActivity(new Intent(MainActivity.this,Login_page.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void scan_fingerprint(){
        Executor executor = Executors.newSingleThreadExecutor();
        final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                }else{
                    Toast.makeText(MainActivity.this,errString.toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //TODO: Called when a biometric is recognized.
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        listitems = new String[s.getSubjects().size()];
                        listitems = s.getSubjects().toArray(listitems);


                        Log.e("size", String.valueOf(s.getSubjects().size()));
                        AlertDialog.Builder status_dialog=new AlertDialog.Builder(MainActivity.this);
                        status_dialog.setTitle("Choose Subject");
                        status_dialog.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                current_item =listitems[which];
                            }
                        }).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(current_item !=null){
                                    firebase_operations.add_attendence_to_firebase(MainActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),current_item);
                                }else{
                                    Toast.makeText(MainActivity.this,"Select Subject",Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                });
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,"Invalid Fingerprint",Toast.LENGTH_LONG).show();
                    }
                });

                //TODO: Called when a biometric is valid but not recognized.
            }
        });
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Scan Fingerprint.")
                .setDescription("Scan your Fingerprint to mark your Attendence")
                .setNegativeButtonText("Cancel")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
