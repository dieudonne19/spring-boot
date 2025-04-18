package org.dd_lgp.com.tutospring.service.exception;

public class ServerException extends RuntimeException{
    public ServerException(String message) { super(message);}
    public ServerException(Exception message) { super(message);}
}
