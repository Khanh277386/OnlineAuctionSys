package com.auction.client.store;

import com.auction.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStore {

    // Lưu toàn bộ tài khoản trên RAM (Key: username)
    private static final Map<String, User> USERS = new ConcurrentHashMap<>();

    // Lưu toàn bộ phiên đấu giá trên RAM (Dùng ObservableList để UI tự cập nhật)
    private static final ObservableList<Auction> AUCTIONS = FXCollections.observableArrayList();

    // Lưu tài khoản đang đăng nhập hiện tại
    private static User currentUser;

    // Khởi tạo dữ liệu mẫu sẵn trên RAM mỗi lần bật app
    static {
        seedInitialData();
    }

    private static void seedInitialData() {
        // 1. Tạo sẵn 3 tài khoản mẫu cho 3 vai trò để test nhanh
        Bidder defaultBidder = new Bidder("BID-001", "bidder1", "123");
        defaultBidder.addFund(10_000); // Nạp sẵn $10,000

        Seller defaultSeller = new Seller("SEL-001", "seller1", "123");
        Admin defaultAdmin = new Admin("ADM-001", "admin", "123");

        USERS.put(defaultBidder.getName(), defaultBidder);
        USERS.put(defaultSeller.getName(), defaultSeller);
        USERS.put(defaultAdmin.getName(), defaultAdmin);

        // 2. Tạo sẵn 3 phiên đấu giá mẫu
        Item laptop = new Electronic("ITM-01", "MSI Thin GF63 12VE", "Core i5-12450H, RTX 4050", defaultSeller, 98.5, 24);
        Item painting = new Art("ITM-02", "Old Quarter Lacquer Painting", "Hand-painted wooden frame artwork", defaultSeller, "Lacquer", 35);
        Item motorbike = new Vehicle("ITM-03", "Honda SH 150i ABS", "First-owner scooter, Hanoi plate", defaultSeller, "SH 150i", "Honda", 8500.0);

        LocalDateTime now = LocalDateTime.now();

        Auction auc1 = new Auction(new AuctionItem(laptop, 800, 50, now.minusMinutes(15), now.plusHours(2)));
        auc1.startAuction();

        Auction auc2 = new Auction(new AuctionItem(painting, 300, 20, now.minusMinutes(30), now.plusHours(5)));
        auc2.startAuction();

        Auction auc3 = new Auction(new AuctionItem(motorbike, 3200, 100, now.minusHours(1), now.plusHours(1)));
        auc3.startAuction();

        AUCTIONS.addAll(auc1, auc2, auc3);
    }

    // --- NGHIỆP VỤ TÀI KHOẢN ---

    public static boolean registerUser(String username, String password, String role) {
        if (USERS.containsKey(username)) {
            return false; // Trùng tên đăng nhập
        }

        String id = "USR-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        User newUser;

        if ("Seller".equalsIgnoreCase(role)) {
            newUser = new Seller(id, username, password);
        } else {
            newUser = new Bidder(id, username, password);
        }

        USERS.put(username, newUser);
        return true;
    }

    public static User login(String username, String password) {
        User user = USERS.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Bidder getCurrentBidder() {
        return (currentUser instanceof Bidder b) ? b : null;
    }

    public static Seller getCurrentSeller() {
        return (currentUser instanceof Seller s) ? s : null;
    }

    // --- NGHIỆP VỤ PHIÊN ĐẤU GIÁ ---

    public static ObservableList<Auction> getAuctions() {
        return AUCTIONS;
    }

    public static void addAuction(Auction auction) {
        AUCTIONS.add(auction);
    }
}