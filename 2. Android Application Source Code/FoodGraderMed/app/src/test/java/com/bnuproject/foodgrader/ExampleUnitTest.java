package com.bnuproject.foodgrader;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest{


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void connectionTest() {
        String[] testData = {
                "5000328422633",
                "3229820019307",
                "7622210449283",
                "8000500310427",
                "7300400481588"};
        long startTime = System.nanoTime();
        for (String barcode : testData) {
            // Create a String request using Volley Library



        }
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / (double) 1000000;
        System.out.println("Time: " + String.format("%.7f", timeTaken) + "ms"); //divide by 1000000 to get milliseconds.
    }
}