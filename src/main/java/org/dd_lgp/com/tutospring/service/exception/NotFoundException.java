package org.dd_lgp.com.tutospring.service.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) { super(message);}
    public NotFoundException(Exception message) { super(message);}
}
