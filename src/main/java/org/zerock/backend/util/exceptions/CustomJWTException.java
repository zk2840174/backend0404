package org.zerock.backend.util.exceptions;

public class CustomJWTException extends RuntimeException {

    public CustomJWTException(String msg){
        super(msg);
    }

}
