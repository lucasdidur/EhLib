package com.ehaqui.lib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextUtils
{
    /**
     * Returns a string containing the tokens joined by delimiters.
     * 
     * @param tokens
     *            an array objects to be joined. Strings will be formed from the objects by calling object.toString().
     */
    public static String join(CharSequence delimiter, Object[] tokens)
    {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens)
        {
            if (firstTime)
            {
                firstTime = false;
            }
            else
            {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     * 
     * @param tokens
     *            an array objects to be joined. Strings will be formed from the objects by calling object.toString().
     */
    public static String join(CharSequence delimiter, Iterable<?> tokens)
    {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens)
        {
            if (firstTime)
            {
                firstTime = false;
            }
            else
            {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }
    
    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
