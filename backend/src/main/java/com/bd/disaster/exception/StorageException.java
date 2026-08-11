package com.bd.disaster.exception;
public class StorageException extends DisasterDataException {
    public StorageException(String message) { super(message); }
    public StorageException(String message, Throwable cause) { super(message, cause); }
}
