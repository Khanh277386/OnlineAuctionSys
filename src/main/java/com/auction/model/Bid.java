package com.auction.model;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Bid implements Entity, Comparable<Bid> {
    private final String id;
    private final String auctionId;
    private final Bidder bidder;
    private final double amount;
    private final LocalDateTime timestamp;

    public Bid(String auctionId, Bidder bidder, double amount){
        if (amount < 0) throw new IllegalArgumentException("Số tiền phải lớn hơn 0");
        this.id = UUID.randomUUID().toString();
        this.auctionId = auctionId;
        this.bidder = bidder;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    public String getId() {
        return id;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int compareTo(Bid bid) {
        int byAmount = Double.compare(this.amount, bid.amount);
        if (byAmount != 0) return byAmount;
        return this.timestamp.compareTo(other.timestamp);
    }

    private toString(){
        return bidder.getName() + " đặt " + String.format("%,.0f", amount) + " lúc " + timestamp;
    }
}