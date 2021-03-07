package me.sup2is.exception;

public class ConvertedUrlNotFoundException extends RuntimeException {

    public ConvertedUrlNotFoundException() {
    }

    public ConvertedUrlNotFoundException(String message) {
        super(message);
    }
}
