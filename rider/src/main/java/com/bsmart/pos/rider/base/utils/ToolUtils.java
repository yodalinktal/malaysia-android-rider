package com.bsmart.pos.rider.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: yoda
 * DateTime: 2020/3/25 23:19
 */
public class ToolUtils {

    public static boolean isEmailValid(String email)
    {
        String regExp =
                "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExp,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }



}
