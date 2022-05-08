package com.bnuproject.foodgrader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.Arrays;

import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private CodeScanner mCodeScanner;
    Button btn;
    EditText editTextBarcode;
    public String previousBarcode;

    Vibrator myVib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        editTextBarcode = (EditText) findViewById(R.id.editText_barcode);
        btn = findViewById(R.id.btnGo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }

        codeScanner();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                barcodeVerifier(editTextBarcode.getText().toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent (getBaseContext(), PermissionActivity.class);
                startActivity (intent);
            }
        }
    }

    private void codeScanner(){
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setFormats(Arrays.asList(
                BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.UPC_EAN_EXTENSION,
                BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13)); // SET CODE THINGS
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myVib.vibrate(200);
                        barcodeVerifier(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void barcodeVerifier(String barcodeQuery){
        if(!barcodeQuery.equals(previousBarcode)){
            OpenFoodFactsWrapper wrapper = new OpenFoodFactsWrapperImpl();
            if (wrapper.fetchProductByCode(barcodeQuery).isStatus()) {
                Intent intent = new Intent (getBaseContext(), SecondaryActivity.class);
                intent.putExtra("string_value", barcodeQuery);
                startActivity (intent);
            }
            else{
                Toast.makeText(MainActivity.this, "Invalid Barcode, Try Again or Enter Below", Toast.LENGTH_SHORT).show();
                onResume();
                previousBarcode = barcodeQuery;
            }
        }
    }
}