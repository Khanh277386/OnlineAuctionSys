package com.auction;

import com.auction.exception.AuctionException;
import com.auction.manager.AuctionManager;
import com.auction.manager.ItemManager;
import com.auction.manager.UserManager;
import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.Bidder;
import com.auction.model.Item;
import com.auction.model.ItemType;
import com.auction.model.Seller;
import com.auction.model.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Chương trình chạy thử toàn bộ luồng nghiệp vụ trên console (chưa có GUI/Socket).
 */
public class Main {

    public static void main(String[] args) throws Exception {
        UserManager userManager = UserManager.getInstance();
        ItemManager itemManager = ItemManager.getInstance();
        AuctionManager auctionManager = AuctionManager.getInstance();

        // 1. Đăng ký và đăng nhập
        Seller seller = (Seller) userManager.register("seller01", "123", User.ROLE_SELLER);
        final Bidder alice = (Bidder) userManager.register("alice", "123", User.ROLE_BIDDER);
        final Bidder bob = (Bidder) userManager.register("bob", "123", User.ROLE_BIDDER);
        alice.addFund(50_000_000);
        bob.addFund(50_000_000);

        try {
            userManager.login("alice", "sai-mat-khau");
        } catch (AuctionException e) {
            System.out.println("Đăng nhập thất bại như mong đợi: " + e.getMessage());
        }

        // 2. Seller thêm sản phẩm qua ItemManager (bên trong dùng ItemFactory)
        Map<String, String> attrs = new HashMap<>();
        attrs.put("warranty", "12");
        attrs.put("durability", "95");
        Item laptop = itemManager.addItem(seller, ItemType.ELECTRONIC,
                "MacBook Air M3", "16GB RAM, 512GB SSD", attrs);
        laptop.printInfo();

        // 3. Tạo phiên: bắt đầu ngay, kéo dài 4 giây
        LocalDateTime now = LocalDateTime.now();
        final Auction auction = auctionManager.createAuction(seller, laptop,
                20_000_000, 500_000, now, now.plusSeconds(4));
        auction.attach(alice);
        auction.attach(bob);
        auctionManager.startScheduler();
        Thread.sleep(1500); // chờ scheduler chuyển phiên sang RUNNING
        System.out.println("Trạng thái phiên: " + auction.getStatus());

        // 4. Hai người đặt giá cùng lúc trên hai luồng
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                tryBid(auction, alice, 21_000_000);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                tryBid(auction, bob, 21_000_000);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Giá quá thấp → InvalidBidException
        tryBid(auction, alice, 100);

        // 5. Chờ hết giờ để scheduler tự đóng phiên
        Thread.sleep(4000);
        System.out.println("Trạng thái phiên: " + auction.getStatus());
        System.out.println("Người thắng: " + auction.getLeading().getName()
                + " với giá " + String.format("%,.0f", auction.getCurrentPrice()));

        // Đặt giá sau khi đóng → AuctionClosedException
        tryBid(auction, bob, 30_000_000);

        // 6. Thanh toán
        auction.markPaid();
        System.out.println("Trạng thái phiên: " + auction.getStatus());
        alice.printInfo();
        bob.printInfo();
        seller.printInfo();

        System.out.println("Lịch sử đặt giá:");
        for (Bid bid : auction.getBidHistory()) {
            System.out.println("  " + bid);
        }

        auctionManager.shutdown();
    }

    /** Đặt giá và in kết quả, kể cả khi bị từ chối. */
    private static void tryBid(Auction auction, Bidder bidder, double amount) {
        try {
            auction.placeBid(bidder, amount);
            System.out.println(bidder.getName() + " đặt " + String.format("%,.0f", amount) + ": THÀNH CÔNG");
        } catch (AuctionException e) {
            System.out.println(bidder.getName() + " đặt " + String.format("%,.0f", amount) + ": BỊ TỪ CHỐI - "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
