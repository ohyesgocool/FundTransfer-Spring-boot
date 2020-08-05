package com.expleo.users.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception {
	
	   public HttpStatus code;
	   public ApiExceptionBody body;
	   
	    public ApiException(final HttpStatus code, final ApiExceptionBody body) {
	        this.body = body;
	        this.code = code;
	    }
	
}
