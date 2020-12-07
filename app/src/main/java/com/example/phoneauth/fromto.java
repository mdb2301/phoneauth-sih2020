package com.example.phoneauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class fromto extends AppCompatActivity {
    FirebaseFirestore fstore;
    Button book;
    Spinner from, to;
    FirebaseAuth fauth;
    String uid,fString,a, id;
    TextView sample;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fromto);
        fstore = FirebaseFirestore.getInstance();
        book = findViewById(R.id.book);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        fauth = FirebaseAuth.getInstance();
        sample = findViewById(R.id.amt);
        activity = this;
        uid = fauth.getCurrentUser().getUid();
        id = uid + randomAlphaNumeric(5);

        final DocumentReference docref = fstore.collection("Users/" + uid + "/trip").document(id);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(from.getSelectedItem().toString() == to.getSelectedItem().toString() ){
                    ((TextView)to.getSelectedView()).setError("Please select different end-point");
                }else{
                    final String dbFrom = from.getSelectedItem().toString();
                    final String dbTo = to.getSelectedItem().toString();
                    final Map<String, Object> user = new HashMap<>();
                    user.put("from",dbFrom);
                    user.put("to",dbTo);
                    user.put("amount", calculateAmount(from,to));
                    fString = dbFrom + dbTo + calculateAmount(from,to);
                    a = Integer.toString(calculateAmount(from,to));





                }
            }
        });
    }


    public int calculateAmount(Spinner from, Spinner to) {
        int amount;
        int start = from.getSelectedItemPosition();
        int end = to.getSelectedItemPosition();
        if(start > end){
            amount = (start - end)*10;
        }else{
            amount = (end - start)*10;
        }
        return amount;
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(),home.class));
        finish();
    }

//        book = findViewById(R.id.book);
//        book.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getApplicationContext(), qrpage.class));
////                finish();
////            }
//        });

}
