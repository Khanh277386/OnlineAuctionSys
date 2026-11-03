package com.auction.exception;

public class AuthenticationException extends AuctionException {
    private static final long serialVersionUID = 1L;
    public AuthenticationException(String message) {
        super(message);
    }
}