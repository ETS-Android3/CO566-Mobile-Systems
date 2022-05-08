package com.bnuproject.foodgrader;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import pl.coderion.model.Product;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
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
        for (String barcode: testData) {
            //Do your stuff here
            OpenFoodFactsWrapper wrapper = new OpenFoodFactsWrapperImpl();
            Product product = wrapper.fetchProductByCode(barcode).getProduct();
            double protein = product.getNutriments().getProteins100G();
            System.out.println(protein);
        }
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime)/(double)1000000;
        System.out.println( "Time: " + String.format("%.7f", timeTaken) + "ms"); //divide by 1000000 to get milliseconds.
    }
}