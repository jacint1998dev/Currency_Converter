package com.sda.currency_converter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConverter {

    public static void main(String[] args) throws IOException {

        HashMap<Integer,String> currencyCodes = new HashMap<>();

        //Add currency codes
        currencyCodes.put(1,"USD");
        currencyCodes.put(2,"EUR");
        currencyCodes.put(3,"CAD");
        currencyCodes.put(4,"HKD");
        currencyCodes.put(5,"IRN");

        String fromCode, toCode;
        double amount;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the currency converter!");
        System.out.println("Currency converting FROM?");
        System.out.println(
                "1:USD (US Dollar) \t 2:EUR (Euro) \t 3:CAD (Canadian dollar)\t " +
                        "4:HKD (Hong Kong Dollar) \t 5:IRN (Iranian Rupee)"
        );

        fromCode = currencyCodes.get(scanner.nextInt());
        System.out.println("Currency converting TO?");
        System.out.println(
                "1:USD (US Dollar) \t 2:EUR (Euro) \t 3:CAD (Canadian dollar)\t " +
                        "4:HKD (Hong Kong Dollar) \t 5:IRN (Iranian Rupee)"
        );
        toCode=currencyCodes.get(scanner.nextInt());

        System.out.println("Amount you wish to convert?");
        amount = scanner.nextFloat();

        sendHttpGETRequest(fromCode,toCode,amount);

        System.out.println("Thank you for using currency converter!");

    }

    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException {

        DecimalFormat f = new DecimalFormat("00.00");

        String GET_URL= "http://api.exchangeratesapi.io/v1/latest?access_key=YOUR_ACCESS_KEY&base=" + toCode + "&symbols=" + fromCode;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode== HttpURLConnection.HTTP_OK){ //success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();

            JSONObject object = new JSONObject(response.toString());
            Double exchangeRate = object.getJSONObject("rates").getDouble(fromCode);
            System.out.println(object.getJSONObject("rates"));
            System.out.println(exchangeRate);//keep for debugging
            System.out.println();
            System.out.println(f.format(amount) + fromCode+ " = " + f.format(amount/exchangeRate) + toCode);
        }
        else {
            System.out.println("GET request failed!!");
        }
    }
}
