package com.example.passpringrest.exceptions;

public class ResourceOccupiedException extends RuntimeException {
    public ResourceOccupiedException(String message) {
        super(message);
    }
}
