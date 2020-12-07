package com.example.phoneauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class register extends AppCompatActivity {
    FirebaseAuth fauth;
    EditText phn, code;
    Button next;
    ProgressBar pgb;
    TextView state;
    CountryCodePicker country;
    String verID;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean inProgress = false;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fauth = FirebaseAuth.getInstance();
        phn = findViewById(R.id.phone);
        code = findViewById(R.id.codeEnter);
        next = findViewById(R.id.nextBtn);
        pgb = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);
        country = findViewById(R.id.ccp);
        fStore = FirebaseFirestore.getInstance();

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!inProgress){
                    if(!phn.getText().toString().isEmpty() && phn.getText().toString().length() == 10){
                        String num = "+" + country.getSelectedCountryCode() + phn.getText().toString();
                        pgb.setVisibility(View.VISIBLE);
                        state.setText("Sending OTP...");
                        state.setVisibility(View.VISIBLE);
                        requestOTP(num);
                    }else{
                        phn.setError("Please enter a valid phone number");
                    }
                }else{
                    String otp = code.getText().toString();
                    if(!otp.isEmpty() && otp.length() == 6){
                        PhoneAuthCredential cred = PhoneAuthProvider.getCredential(verID, otp);
                        verifyAuth(cred);
                    }else{
                        code.setError("Enter valid OTP");
                    }
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(fauth.getCurrentUser() != null){
            pgb.setVisibility(View.VISIBLE);
            state.setText("Checking");
            state.setVisibility(View.VISIBLE);
            checkProfile();
        }
    }

    private void checkProfile() {
        DocumentReference docref = fStore.collection("Users").document(fauth.getCurrentUser().getUid());
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(), home.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(), newDetails.class));
                    finish();
                }
            }
        });
    }

    private void verifyAuth(PhoneAuthCredential cred) {
        fauth.signInWithCredential(cred).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkProfile();
                }else{
                    code.setError("Incorrect OTP");
                    Toast.makeText(register.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestOTP(String num) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(num, 120L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                pgb.setVisibility(View.INVISIBLE);
                state.setVisibility(View.INVISIBLE);
                code.setVisibility(View.VISIBLE);
                next.setText("Verify");
                verID = s;
                token = forceResendingToken;
                inProgress = true;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(register.this, "OTP expired. Try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                verifyAuth(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(register.this, "Cannot create account" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
