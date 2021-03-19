package com.jayk22.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPasswd;
    private Button loginBtn;
    private TextView accSignUp;
    private TextView frgtPasswd;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail=findViewById(R.id.l_clg_email);
        loginPasswd=findViewById(R.id.l_clg_passwd);
        progressBar=findViewById(R.id.l_prg_bar);
        accSignUp=findViewById(R.id.l_already_signup);
        loginBtn=findViewById(R.id.l_btn_signin);
        frgtPasswd=findViewById(R.id.l_frgt_passwd);

        firebaseAuth=FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AccountLogin();

            }
        });

        accSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        frgtPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail=new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your College Email to receive reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail=resetMail.getText().toString();
                        if(TextUtils.isEmpty(mail))
                        {
                            Toast.makeText(LoginActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error : Link Not Send"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                    }
                });

                passwordResetDialog.create().show();

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

    private void AccountLogin()
    {
        String email=loginEmail.getText().toString().trim();
        String password=loginPasswd.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            loginEmail.setError("Email is Required");
            return;
        }

        if(email.length()<6)
        {
            loginEmail.setError("Enter Valid Email");
            return;
        }

        boolean emailRight=CheckEmail(email);
        if(!emailRight)
        {
            loginEmail.setError("Enter Valid Email");
            return;
        }


        if(TextUtils.isEmpty(password))
        {
            loginPasswd.setError("Email is Required");
            return;
        }

        if(password.length()<6)
        {
            loginPasswd.setError("Password should be atleast 6 characters long");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}