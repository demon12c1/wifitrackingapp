package com.tj.tri.trafficjamtrack.datahelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tri on 2/9/15.
 */
public class Helper {
    public static boolean checkIsJsonArrayOrNot(String jsonData){
        char first = jsonData.charAt(0);
        String letter=(""+first).toLowerCase();
        if(letter.equals("{"))
            return false;
        else
            return true;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
