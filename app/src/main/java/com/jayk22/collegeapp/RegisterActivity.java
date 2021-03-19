package com.jayk22.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView whyClgEmail;
    private TextView alreadySignIn;
    private EditText username;
    private EditText userEmailId;
    private EditText userPasswd;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private Button rBtn;

    private FirebaseFirestore firebaseFirestore;

    private FirebaseUser currentUser;
    Context context;

    public static final String SHARED_PREFS="sharedPrefs";
    public static final String COLLEGE_EMAIL="text";
    public static final String CURRENT_USERID="userId";
    //public static final String



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        whyClgEmail=findViewById(R.id.r_why_clg_email);
        alreadySignIn=findViewById(R.id.r_already_signin);

        username=findViewById(R.id.r_clg_username);
        userEmailId=findViewById(R.id.r_clg_email);
        userPasswd=findViewById(R.id.r_clg_passwd);
        rBtn=findViewById(R.id.r_btn_signUp);
        progressBar=findViewById(R.id.r_prg_bar);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        context=this;


        String text = "<font color=#e3342f>Why</font> <font color=#000000>College Email?</font>";
        whyClgEmail.setText(Html.fromHtml(text));

        String text1 = "<font color=#000000>Already on App?</font> <font color=#e3342f>Sign In</font>";
        alreadySignIn.setText(Html.fromHtml(text1));

        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AccountRegister();
            }
        });

        alreadySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private boolean CheckEmail(String email)
    {
        int n=email.length();
        String s="ni.ca";
        StringBuffer che = new StringBuffer();


        Log.e("swdwedwef",s.toString());

        int i=0;
        while(i<5)
        {
          che.insert(i,email.charAt(n-i-1));
            i++;
        }
        String str=che.toString();

        Log.e("Maind2eugd2",che.toString());




        return s.equals(str);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    private String ReturnClg(String email)
    {
        int n=email.length();
        int i=0;
        while(email.charAt(i)!='@')
        {
            i++;
        }

        i++;
        StringBuffer che = new StringBuffer();
        int k=0;
        while(i<n)
        {
            che.insert(k,email.charAt(i));
            k++;
            i++;
        }

        return che.toString();


    }

    private void AccountRegister()
    {
        String email=userEmailId.getText().toString().trim();
        String password=userPasswd.getText().toString().trim();

        String userName=username.getText().toString().trim();

        if(TextUtils.isEmpty(userName))
        {
            username.setError("Username Required");
            return;
        }

        if(TextUtils.isEmpty(email))
        {
            userEmailId.setError("Email is Required");
            return;
        }

        if(email.length()<6)
        {
            userEmailId.setError("Enter Valid Email");
            return;
        }



        boolean emailRight=CheckEmail(email);
        Log.e("Email", String.valueOf(emailRight));

        if(emailRight==false)
        {
            userEmailId.setError("Enter Valid Email");
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            userPasswd.setError("Password is Required");
            return;
        }

        if(password.length()<6)
        {
            userPasswd.setError("Password should be atleast 6 characters long");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String collegeEmail=ReturnClg(email);

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=saved_values.edit();
        editor.putString(COLLEGE_EMAIL,collegeEmail);
        editor.commit();

       /* SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(COLLEGE_EMAIL,collegeEmail );

        editor.apply();*/




        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    currentUser=firebaseAuth.getCurrentUser();

                    SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=saved_values.edit();
                    editor.putString(CURRENT_USERID,currentUser.getUid());


                    editor.commit();




                    Log.e("RegisterACttttttt", collegeEmail);


                    DocumentReference documentReference=firebaseFirestore.collection(collegeEmail).document("users").collection("AllUsers")
                            .document(currentUser.getUid());

                    HashMap<String,Object> user=new HashMap<>();
                    user.put("name",userName);
                    user.put("email",email);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("checkk", "onSuccess: "+currentUser.getUid());
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });




                    Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                    //finish();
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error Creating user", e.getMessage());
            }
        });
    }


}