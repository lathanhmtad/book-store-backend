package com.book.constant;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

public class AppConstant extends CsrfTokenRequestAttributeHandler {
    public static final String JWT_HEADER = "Authorization";
}
