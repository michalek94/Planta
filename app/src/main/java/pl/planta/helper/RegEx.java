package pl.planta.helper;

import java.util.regex.Pattern;

public class RegEx {

    public RegEx() {
    }

    private final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                    "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    public boolean checkEmail(String email){
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
