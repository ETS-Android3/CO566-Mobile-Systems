package com.bnuproject.foodgrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class SecondaryActivity extends AppCompatActivity {

    JSONObject myJsonObject;
    Button btnBack;
    TextView
            viewTitle,
            viewKcal,
            viewKj,
            viewCarbsGrams,
            viewFatGrams,
            viewProteinGrams,
            viewIngredients,
            viewAllergens,
            viewFoodgrade,
            viewCarbsGramsP,
            viewFatGramsP,
            viewProteinGramsP;
    CardView
            cardViewFoodGrade,
            cardViewAllergens,
            cardViewCarbs,
            cardViewFat,
            cardViewProtein;
    ImageView viewProductImage;


    double
            kcal,
            kj,
            carbsGrams,
            fatGrams,
            proteinGrams,
            sugars,
            salt,
            fibre,
            fatSat;

    String
            title,
            imageURL,
            allergens,
            ingredients;

    int
            highRisk = Color.rgb(193, 62, 62),
            medRisk = Color.rgb(193, 138, 62),
            lowRisk = Color.rgb(80, 193, 62),
            unknownRisk = Color.rgb(114, 62, 193),
            nova;

    DecimalFormat
            dp2 = new DecimalFormat("#.##"),
            dp1 = new DecimalFormat("#");

    ArrayList<Integer> totalGrade = new ArrayList<>();

    HashMap<String, String[]> macroData = new HashMap<String, String[]>() {{
        put("salt", new String[]{"L", "0.3,1.5"});
        put("sugar", new String[]{"L", "5,22.5"});
        put("fat", new String[]{"L", "3,17.5"});
        put("fatSat", new String[]{"L", "1.5,5"});

        put("fibre", new String[]{"H", "3,6"});
        put("calcium", new String[]{"H", "100,180"}); // mg
        put("vitaminA", new String[]{"H", "400,600"}); // ug
        put("vitaminC", new String[]{"H", "20,60"}); // mg
        put("vitaminD", new String[]{"H", "1,8"}); //ug

        put("protein", new String[]{"H", "0.2,0.4"}); //percentage
    }};

    //TODO Implement quick notes
    HashMap<Integer, String[]> riskData = new HashMap<Integer, String[]>() {{
        put(1, new String[]{String.valueOf(lowRisk), "Healthy Amount of "});
        put(2, new String[]{String.valueOf(medRisk), "Average Amount of "});
        put(3, new String[]{String.valueOf(highRisk), "Unhealthy Amount of "});
        put(4, new String[]{String.valueOf(unknownRisk), "Unknown Value of "});
    }};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent (getBaseContext(), MainActivity.class);
            startActivity (intent);
        });

        try {
            myJsonObject = new JSONObject(getIntent().getStringExtra("json_value"));

            title = myJsonObject.getJSONObject("product").getString("product_name");
            kcal = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("energy-kcal_100g"));
            kj = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("energy-kj_100g"));
            carbsGrams = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("carbohydrates_100g"));
            fatGrams = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("fat_100g"));
            proteinGrams = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("proteins_100g"));
            ingredients = myJsonObject.getJSONObject("product").getString("ingredients_text");
            imageURL = myJsonObject.getJSONObject("product").getString("image_front_small_url");
            allergens = myJsonObject.getJSONObject("product").getString("allergens");

            sugars = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("sugars_100g"));
            salt = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("salt_100g"));
            fibre = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("fiber_100g"));
            fatSat = Double.parseDouble(myJsonObject.getJSONObject("product").getJSONObject("nutriments").getString("saturated-fat_100g"));

            nova = Integer.parseInt(myJsonObject.getJSONObject("product").getString("nova_group"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewTitle = findViewById(R.id.textView_Title);
        viewKcal = findViewById(R.id.textView_kcal);
        viewKj = findViewById(R.id.textView_kj);
        viewCarbsGrams = findViewById(R.id.textView_carbsGrams);
        viewFatGrams = findViewById(R.id.textView_fatGrams);
        viewProteinGrams = findViewById(R.id.textView_proteinGrams);
        viewIngredients = (findViewById(R.id.textView_ingredients));
        viewCarbsGramsP = findViewById(R.id.textView_carbsPercentage);
        viewFatGramsP = findViewById(R.id.textView_fatPercentage);
        viewProteinGramsP = findViewById(R.id.textView_proteinPercentage);
        viewAllergens = findViewById(R.id.textView_allergens);
        viewProductImage = findViewById(R.id.imageView_product);
        viewFoodgrade = findViewById(R.id.textView_foodGrade);

        cardViewFoodGrade = findViewById(R.id.cardView_notes);
        cardViewAllergens = findViewById(R.id.cardView_allergens);
        cardViewCarbs = findViewById(R.id.cardView_carbs);
        cardViewFat = findViewById(R.id.cardView_fat);
        cardViewProtein = findViewById(R.id.cardView_protein);


        if(imageURL == null){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewTitle.getLayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(0);
            }
            viewTitle.setLayoutParams(params);
            viewProductImage.setVisibility(View.INVISIBLE);
        }
        else{
            Picasso.get().load(imageURL).into(viewProductImage);
        }


        // local conversion as a backup
        if(kj == 0 && kcal > 0){
            kj = kcal*4.184;
        }
        StringBuilder allergenListBuilder = new StringBuilder();
        String[] allergensArray = allergens.split(",");
        if (allergensArray != null && allergensArray.length > 1) {
            for(String allergen : allergensArray) {
                allergenListBuilder.append(allergen.substring(3)).append(", ");
            }
            allergens = allergenListBuilder.toString();
            allergens = allergens.substring(0, allergens.length() - 2);
            cardViewAllergens.setCardBackgroundColor(highRisk);
        }
        else{
            allergens = "No Allergens Found";
        }


        viewTitle.setText(title);
        viewKcal.setText(dp1.format(kcal) + "kcal");
        viewKj.setText(dp1.format(kj) + "kJ");
        viewCarbsGrams.setText(dp2.format(carbsGrams) + "g");
        viewFatGrams.setText(dp2.format(fatGrams) + "g");
        viewProteinGrams.setText(dp2.format(proteinGrams) + "g");

        viewCarbsGramsP.setText("Sugar: " + dp2.format(sugars) + "g");
        viewFatGramsP.setText("Saturated: " + dp2.format(sugars) + "g");
        viewProteinGramsP.setText("Daily: " + dp2.format((proteinGrams/56)*100) + "%");
        viewIngredients.setText(ingredients);
        viewAllergens.setText(allergens);

        setFactors();

        int totalPoints = 0;
        for(int i = 0; i < totalGrade.size(); i++){
            totalPoints += totalGrade.get(i);
        }

        viewFoodgrade.setText(Integer.toString(totalPoints));
        int totalSize = (totalGrade.size()*2)+3;
        int foodGradePercentage = totalPoints*100/totalSize;
        // debug Toast.makeText(SecondaryActivity.this, "TOTALSIZE: " + totalSize + ", POINTS: " + totalPoints + "PERCENTAGE: " + foodGradePercentage, Toast.LENGTH_LONG).show();
        viewFoodgrade.setText(alphabeticalGrader(foodGradePercentage));
    }

    private String alphabeticalGrader(int foodGradePercentage){
        if(foodGradePercentage <= 25){
            //D
            cardViewFoodGrade.setCardBackgroundColor(highRisk);
            return "D";
        }
        else if(foodGradePercentage <= 50){
            //C
            cardViewFoodGrade.setCardBackgroundColor(medRisk);
            return "C";
        }
        else if(foodGradePercentage <= 75){
            //B
            cardViewFoodGrade.setCardBackgroundColor(lowRisk);
            return "B";
        }
        else if(foodGradePercentage <= 100){
            //A
            cardViewFoodGrade.setCardBackgroundColor(lowRisk);
            return "A";
        }
        else{
            //ERROR
            return "ERROR";
        }
    }

    private void setFactors(){
        totalGrade.clear();


        totalGrade.add(macroGrader("sugar", sugars));
        totalGrade.add(macroGrader("fatSat", fatSat));
        totalGrade.add(macroGrader("protein", proteinGrams));

        totalGrade.add(macroGrader("salt", salt));
        totalGrade.add(macroGrader("fibre", fibre));

        switch(nova) {
            case 1:
                totalGrade.add(3);
                break;
            case 2:
                totalGrade.add(2);
                break;
            case 3:
                totalGrade.add(1);
                break;
            case 4:
                totalGrade.add(0);
                break;
        }
        totalGrade.removeAll(Collections.singleton(-1));
    }

    private int macroGrader(String macroQuery, double macroValue){
        String[] dataMain = riskEvaluator(macroQuery, macroValue);
        int riskCode =  Integer.parseInt(dataMain[0]);
        int cardColour =  Integer.parseInt(dataMain[1]);
        // String description = dataMain[2];
        if(riskCode != 4){
            switch (macroQuery) {
                case "sugar":
                    cardViewCarbs.setCardBackgroundColor(cardColour);
                    break;
                case "fat":
                case "fatSat":
                    cardViewFat.setCardBackgroundColor(cardColour);
                    break;
                case "protein":
                    cardViewProtein.setCardBackgroundColor(cardColour);
                    break;
            }
        }

        //foodgrade points
        switch (riskCode) {
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 0;
            default:
                return -1;
        }
    }

    private String[] riskEvaluator(String macroQuery, double macroValue){
        int returnCode = 4;
        int cardColour;
        String description;
        String[] dataMain = macroData.get(macroQuery);
        String[] dataSecondary = dataMain[1].split(",");
        double lowVal = Double.valueOf(dataSecondary[0]);
        double highVal = Double.valueOf(dataSecondary[1]);

        if(macroValue != 0){

            if(macroQuery.equals("protein")){
                macroValue = (macroValue*10)/kcal;
            }
            if(macroValue <= lowVal){
                returnCode = 1;
            }
            else if(macroValue > lowVal && macroValue <= highVal){
                returnCode = 2;
            }
            else if(macroValue > highVal){
                returnCode = 3;
            }

            if(dataMain[0].equals("H")){
                switch (returnCode) {
                    case 1:
                        returnCode = 3;
                        break;
                    case 3:
                        returnCode = 1;
                        break;
                }
            }
        }
        String[] dataRisk = riskData.get(returnCode);
        cardColour = Integer.parseInt(dataRisk[0]);
        description = dataRisk[1];
        return new String[] { Integer.toString(returnCode), Integer.toString(cardColour), description};
    }
}