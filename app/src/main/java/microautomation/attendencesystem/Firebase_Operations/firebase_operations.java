package microautomation.attendencesystem.Firebase_Operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import microautomation.attendencesystem.Adapters.attendence_list_adapter;
import microautomation.attendencesystem.MainActivity;
import microautomation.attendencesystem.Model.Attendence;
import microautomation.attendencesystem.Model.Students;

public class firebase_operations {
    public static void add_students(final Context context, Students s){
       CollectionReference students_ref= FirebaseFirestore.getInstance().collection("Students");
       students_ref.document(s.getRoll_no()).set(s).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
             Toast.makeText(context,"Student is Added to the System",Toast.LENGTH_LONG).show();
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
           }
       });
    }
    public static void sign_in(final Context context, String roll_number, final String password){
       final AlertDialog dialog=new SpotsDialog.Builder().setContext(context).build();

        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        CollectionReference students_ref=FirebaseFirestore.getInstance().collection("Students");
        dialog.setMessage("Please Wait...");
        dialog.show();

        students_ref.document(roll_number).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                dialog.dismiss();
                if(task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        Students s = snapshot.toObject(Students.class);
                        if(s.getPassword().equals(password)) {
                            prefs.edit().putString("student_info", new Gson().toJson(s)).apply();
                            context.startActivity(new Intent(context, MainActivity.class));
                            ((AppCompatActivity) context).finish();
                        }else{
                            Toast.makeText(context,"Invalid Password",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Invalid Roll Number", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void add_attendence_to_firebase(final Context context, String roll_no,String subject){
        final AlertDialog dialog=new SpotsDialog.Builder().setContext(context).build();
        dialog.show();
        dialog.setMessage("Please Wait....");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c);
        Attendence a=new Attendence(formattedDate,subject,true);
        CollectionReference attendence_ref=FirebaseFirestore.getInstance().collection("Students").document(roll_no).collection("Attendence");
attendence_ref.add(a).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
    @Override
    public void onComplete(@NonNull Task<DocumentReference> task) {
        dialog.dismiss();
        if(task.isSuccessful()){
            Toast.makeText(context,"Your Attendence is marked",Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context,MainActivity.class));
            ((AppCompatActivity)context).finish();
        }
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        dialog.dismiss();
        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context,MainActivity.class));
        ((AppCompatActivity)context).finish();
    }
});
    }
    public static void get_attendence_of_student(final Context context, String roll_no, String subject, final RecyclerView attendence_list){
        final AlertDialog dialog=new SpotsDialog.Builder().setContext(context).build();
         final ArrayList<Attendence> attendences =new ArrayList<>();
         dialog.show();
         dialog.setMessage("Please Wait...");
        CollectionReference attendence_ref=FirebaseFirestore.getInstance().collection("Students").document(roll_no).collection("Attendence");
        attendence_ref.whereEqualTo("subject",subject).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                dialog.dismiss();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        attendences.add(snapshot.toObject(Attendence.class));
                    }
                    attendence_list.setAdapter(new attendence_list_adapter(attendences,context));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
              Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
