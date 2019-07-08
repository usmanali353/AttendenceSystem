package microautomation.attendencesystem.Firebase_Operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import microautomation.attendencesystem.Adapters.attendence_list_adapter;
import microautomation.attendencesystem.MainActivity;
import microautomation.attendencesystem.Model.Attendence;
import microautomation.attendencesystem.Model.Students;
import microautomation.attendencesystem.Model.user;
import microautomation.attendencesystem.admin_home_page;
import microautomation.attendencesystem.faculty_home_page;
import microautomation.attendencesystem.guard_home_page;
import microautomation.attendencesystem.scanned_barcode_details;

public class firebase_operations {
    public static void add_students(final Context context, final String name, final String email, final String password, final String id, final List<String> subjects, final String role, final Uri image_uri){
        final AlertDialog dialog=new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Adding Student Data to System");
        dialog.show();
       final StorageReference student_pic_storage_ref= FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        student_pic_storage_ref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              student_pic_storage_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {
                                      Students s=new Students(name, password, id, subjects, email, role, uri.toString());
                                      FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).set(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                              if(task.isSuccessful()){
                                                  dialog.dismiss();
                                                  Toast.makeText(context,"Student is added to the System",Toast.LENGTH_LONG).show();

                                              }
                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@androidx.annotation.NonNull Exception e) {
                                              dialog.dismiss();
                                              Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                          }
                                      });
                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      dialog.dismiss();
                                  }
                              });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }else{
                        dialog.dismiss();
                        Toast.makeText(context,"user info not found",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public static void sign_in(final Context context, String roll_number, final String password){
       final AlertDialog dialog=new SpotsDialog.Builder().setContext(context).build();

        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        //CollectionReference students_ref=FirebaseFirestore.getInstance().collection("Students");
        dialog.setMessage("Please Wait...");
        dialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(roll_number,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        if(user.getUid().equals("1L8mEZj1MDSTndfT7FIk80mmmbM2")){
                            //admin side
                            dialog.dismiss();
                            prefs.edit().putString("admin_info",user.getUid()).apply();
                            Intent i=new Intent(context, admin_home_page.class);
                            context.startActivity(i);
                            ((AppCompatActivity)context).finish();
                        }else{
                            //student side
                            FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    dialog.dismiss();

                                    if(documentSnapshot.exists()) {
                                        user s = documentSnapshot.toObject(user.class);
                                        Log.e("role",s.getRole());
                                        if (s != null) {
                                            if(s.getRole().equals("Faculty Member")) {
                                                prefs.edit().putString("faculty_info", new Gson().toJson(s)).apply();
                                                Intent i = new Intent(context, faculty_home_page.class);
                                                context.startActivity(i);
                                                ((AppCompatActivity) context).finish();
                                            }else if(s.getRole().equals("Students")) {
                                                Students stu = documentSnapshot.toObject(Students.class);
                                                prefs.edit().putString("student_info", new Gson().toJson(stu)).apply();
                                                Intent i = new Intent(context, MainActivity.class);
                                                context.startActivity(i);
                                                ((AppCompatActivity) context).finish();
                                            }else if(s.getRole().equals("Guard")){
                                                prefs.edit().putString("guard_info", new Gson().toJson(s)).apply();
                                                Intent i = new Intent(context, guard_home_page.class);
                                                context.startActivity(i);
                                                ((AppCompatActivity) context).finish();
                                            }

                                        }
                                    }else{
                                        dialog.dismiss();
                                        Toast.makeText(context,"user info is null",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }else{
                        dialog.dismiss();
                         Toast.makeText(context,"user info not found",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
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
        CollectionReference attendence_ref=FirebaseFirestore.getInstance().collection("Users").document(roll_no).collection("Attendence");
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
        CollectionReference attendence_ref=FirebaseFirestore.getInstance().collection("Users").document(roll_no).collection("Attendence");
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
    public static void add_faculty_member_or_guard(final Context context,final String name, final String email, final String password, final String id, final String role, final Uri image_uri){
        final AlertDialog dialog=new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Adding Employee Data to System");
        dialog.show();
        final StorageReference employee_pic_storage_ref= FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        employee_pic_storage_ref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                employee_pic_storage_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        microautomation.attendencesystem.Model.user u=new user(name, password,  id,  email,  role, uri.toString());
                                        FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    dialog.dismiss();
                                                    Toast.makeText(context,"Employee is added to the System",Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                                dialog.dismiss();
                                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }else{
                        dialog.dismiss();
                        Toast.makeText(context,"user info not found",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void view_scanned_barcode_detail(final Context context, String scanned_text){
        final AlertDialog dialog=new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Verifying");
        dialog.show();
        FirebaseFirestore.getInstance().collection("Users").document(scanned_text).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    dialog.dismiss();
                    user u=documentSnapshot.toObject(user.class);
                    Intent i=new Intent(context, scanned_barcode_details.class);
                    i.putExtra("scanned_barcode_details",new Gson().toJson(u));
                    context.startActivity(i);
                    ((AppCompatActivity)context).finish();
                }else{
                    dialog.dismiss();
                    Toast.makeText(context,"Invalid Card Scanned",Toast.LENGTH_LONG).show();
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
