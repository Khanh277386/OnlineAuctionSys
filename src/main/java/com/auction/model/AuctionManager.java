package com.auction.model;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuctionManager {
    private static AuctionManager instance;
    private Map<String,Auction> auctions;
    private AuctionManager(){
        auctions = new HashMap<>();
    }
    public static synchronized AuctionManager getInstance(){
        if (instance == null){
            instance = new AuctionManager();
        }
        return instance;
    }
    public Auction creatAuction(Item item,int DurationMinutes){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(DurationMinutes);
        String auctionId = "AUC_"+System.currentTimeMillis();
        Auction auction = new Auction(auctionId,item,startTime,endTime);\
        this.auctions.put(auctionId,auction);
        return auction;
    }
    public Auction getAuctionById(String id){
        if (auctions.containsKey(id) == false){
            System.out.println("ERROR: Phiên đấu giá này không tồn tại!");
        }
        return auctions.get(id);
    }
}
