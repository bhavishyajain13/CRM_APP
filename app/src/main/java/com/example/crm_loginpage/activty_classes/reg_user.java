package com.example.crm_loginpage.activty_classes;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.users_firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class reg_user extends AppCompatActivity {
    TextView textView_log;
    EditText username_reg, password_reg, username_reg_phoneno, username_reg_name;
    FirebaseAuth mAuth;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user);
        mAuth = FirebaseAuth.getInstance();
        username_reg = findViewById(R.id.username_reg);
        password_reg = findViewById(R.id.password_reg);
        username_reg_phoneno = findViewById(R.id.username_reg_phoneno);
        username_reg_name = findViewById(R.id.username_reg_name);
        textView_log = findViewById(R.id.log_text_but);

        textView_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(reg_user.this, MainActivity.class));
            }
        });


        register_button = findViewById(R.id.register_button);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    String email = username_reg.getText().toString();
                    String pass = password_reg.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String uuid = currentFirebaseUser.getUid();
                                        users_firebase ub = new users_firebase(username_reg_name.getText().toString(), username_reg_phoneno.getText().toString(), email);
                                        if (currentFirebaseUser != null)
                                            uuid = currentFirebaseUser.getUid();
                                        FirebaseDatabase.getInstance().getReference("users_db").child(uuid).setValue(ub);
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(reg_user.this, "Account Created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(reg_user.this, navcon.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(reg_user.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }

    public boolean validation() {
        String email = username_reg.getText().toString();
        String pass = password_reg.getText().toString();
        String nm = username_reg_name.getText().toString();
        String ph = username_reg_phoneno.getText().toString();

        if (
                email.equalsIgnoreCase("") ||
                        pass.equalsIgnoreCase("") ||
                        nm.equalsIgnoreCase("") ||
                        ph.equalsIgnoreCase("")
        ) return false;
        else if (!ph.matches("^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$")) {
            Toast.makeText(this, "Phone Number Wrong", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Toast.makeText(this, "Invalid Mail ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}