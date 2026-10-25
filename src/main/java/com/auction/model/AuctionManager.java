package com.auction.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionManager {
    private static AuctionManager instance;
    private final Map<String,Auction> auctionList;
    private final Map<String,User> userList;
    private AuctionManager(){
        auctionList = new ConcurrentHashMap<>();
        userList = new ConcurrentHashMap<>();
    }
    public static synchronized AuctionManager getInstance(){
        if (instance == null){
            instance = new AuctionManager();
        }
        return instance;
    }
    public void addAuction(String auctionId,Auction auction){
        if (auction != null && auctionId != null){
            auctionList.put(auctionId,auction);
        }
    }
    public Auction createAuction(AuctionItem item) {
        String auctionId = "AUC_" + System.currentTimeMillis();
        Auction auction = new Auction(item);
        this.auctionList.put(auctionId, auction);
        return auction;
    }
    public Auction getAuctionById(String id){
        Auction auction = auctionList.get(id);
        if (auction == null) {
            System.err.println("ERROR: Phiên đấu giá không tồn tại: " + id);
        }
        return auction;
    }
    public List<Auction>  getAllAuction(){
        return new ArrayList<>(this.auctionList.values());
    }
    public Auction popAuction(String auctionId) {
        Auction removedAuction = this.auctionList.remove(auctionId);
        if (removedAuction == null) {
            System.err.println("ERROR: Không thể xóa! Phiên đấu giá " + auctionId + " không tồn tại!");
        }
        return removedAuction;
    }
    public void addUser(User user){
        if(user != null){
            userList.put(user.getId(),user);
        }
    }
    public User getUser(String userId) {
        User user = userList.get(userId);
        if (user == null){
            System.err.println("Người dùng không tồn tại : "+userId);
        }
        return user;
    }
}
