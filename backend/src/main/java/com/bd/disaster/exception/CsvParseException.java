package com.bd.disaster.exception;
public class CsvParseException extends DisasterDataException {
    public CsvParseException(String message) { super(message); }
    public CsvParseException(String message, Throwable cause) { super(message, cause); }
}
