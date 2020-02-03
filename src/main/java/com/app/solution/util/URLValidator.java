package com.app.solution.util;

import org.apache.commons.validator.routines.UrlValidator;

public class URLValidator {
    public static boolean validate(String url) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }
}
