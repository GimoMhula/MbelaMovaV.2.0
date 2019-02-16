package mmconsultoria.co.mz.mbelamova.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordValidator {
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final String EMAIL_PATTERN =
            "[A-Za-z0-9%_.-]{4,30}+@+([a-z]{2,15})+\\.+[.a-zA-Z]{2,20}";
    private static final String NO_WORD_PATTERN =
            "[0-9!@#$%^&*()_+`~<>?,.:\\{}/|=:\"]{0,120}";


    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public static boolean validatePassword(final String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean validateEmail(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();

    }

    public static boolean validateName(final String name) {
        Pattern pattern = Pattern.compile(NO_WORD_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return !matcher.matches();

    }


}
