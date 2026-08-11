package com.bd.disaster.exception;
public class DisasterDataException extends RuntimeException {
    public DisasterDataException(String message) { super(message); }
    public DisasterDataException(String message, Throwable cause) { super(message, cause); }
}
