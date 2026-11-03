package com.auction.exception;

public class UserAlreadyExistsException extends AuctionException {
    private static final long serialVersionUID = 1L;
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}