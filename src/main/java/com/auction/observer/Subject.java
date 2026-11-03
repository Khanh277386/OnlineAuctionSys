package com.auction.observer;
import com.auction.model.Bid;

public interface Subject{
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Bid bid);
}