package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by fraps on 03/02/17.
 */

public class StringUtils {

    public static boolean isEmailValid(String text) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }

    public static boolean isNameValid(String text) {
        return text.length()>2;
    }

    public static boolean isPasswordValid(String text) {
        return text.length()>5;
    }
}
