package com.example.phoneauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class newDetails extends AppCompatActivity {
    EditText first, last, mail;
    Button save;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        first = findViewById(R.id.firstName);
        last = findViewById(R.id.lastName);
        mail = findViewById(R.id.emailAddress);
        save = findViewById(R.id.saveBtn);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        uid = fauth.getCurrentUser().getUid();
        final DocumentReference docref = fstore.collection("Users").document(uid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!first.getText().toString().isEmpty() && !last.getText().toString().isEmpty() && !mail.getText().toString().isEmpty()){
                    String firstName = first.getText().toString();
                    String lastName = last.getText().toString();
                    String email = mail.getText().toString();

                    Map<String,Object> user = new HashMap<>();
                    user.put("fName",firstName);
                    user.put("lName",lastName);
                    user.put("uMail",email);

                    docref.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(newDetails.this, "Data insertion failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(newDetails.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
