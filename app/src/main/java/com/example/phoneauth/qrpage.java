package com.example.phoneauth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrpage extends AppCompatActivity {
    TextView amt;
    ImageView qrImage;
    Spinner from,to;
    Long timeString;
    String id;
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), fromto.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpage);

        qrImage = findViewById(R.id.qrImage);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        amt = findViewById(R.id.amt);
        timeString = Timestamp.now().getSeconds();
        String str = timeString + "-" + id;
//


        QRGEncoder qrgEncoder = new QRGEncoder(str, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap qrBits = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(qrBits);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}

