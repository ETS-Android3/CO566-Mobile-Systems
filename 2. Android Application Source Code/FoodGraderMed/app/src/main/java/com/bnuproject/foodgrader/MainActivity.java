package com.bnuproject.foodgrader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private CodeScanner mCodeScanner;
    Button btn;
    EditText editTextBarcode;
    Vibrator myVib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        if(!checkPermissions()){
            Intent intent = new Intent (getBaseContext(), PermissionActivity.class);
            startActivity (intent);
        }

        codeScanner();

        editTextBarcode = (EditText) findViewById(R.id.editText_barcode);
        btn = findViewById(R.id.btnGo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                getData(editTextBarcode.getText().toString());
            }
        });
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
                        getData(result.getText());
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

    public void getData(String barcodeNumber) {
        // Create a String request using Volley Library
        String myUrl = "https://world.openfoodfacts.org/api/v2/product/" + barcodeNumber;
        final String[] responseString = {null};
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            //TODO find way to implement jason inheritance
                            //Create a JSON object containing information from the API.
                            String jsonData = response.toString();
                            JSONObject myJsonObject = new JSONObject(jsonData);
                            if(myJsonObject.getString("status_verbose" ) != "product not found"){
                                if(!myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("salt_100g").equals("")){

                                    Intent intent = new Intent (getBaseContext(), SecondaryActivity.class);
                                    intent.putExtra("json_value", jsonData);
                                    startActivity (intent);

                                }
                                else{
                                    //error
                                    Log.d("RESULT:", "res:e1");
                                }
                            }
                            else{
                                // error
                                Log.d("RESULT:", "res:e2");
                            }
                        } catch (JSONException e) {
                            // error
                            Log.d("RESULT:", "res:e3, " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse( VolleyError volleyError ) {
                        // error
                        Log.d("RESULT:", "res:e4, " + volleyError);
                    }
                }
        ){
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("User-Agent", "FoodGrader-Android-V1.0");
            return params;
        }
        };
        RequestQueue requestQueue
                = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);
    }



    private boolean checkPermissions()
    {
        Context context = this;
        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(
                Manifest.permission.CAMERA,
                context.getPackageName());
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else{
            return false;
        }
    }
}