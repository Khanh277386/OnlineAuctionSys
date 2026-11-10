package com.auction.manager;

import com.auction.exception.AuthenticationException;
import com.auction.model.Admin;
import com.auction.model.Auction;
import com.auction.model.AuctionItem;
import com.auction.model.AuctionStatus;
import com.auction.model.Item;
import com.auction.model.Seller;
import com.auction.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class AuctionManager {

  private static final AuctionManager INSTANCE = new AuctionManager();

  private final ConcurrentHashMap<String, Auction> auctionList = new ConcurrentHashMap<>();

  private ScheduledExecutorService scheduler;

  private AuctionManager() {}

  public static AuctionManager getInstance() {
    return INSTANCE;
  }

  public Auction createAuction(
      Seller seller,
      Item item,
      double startPrice,
      double bidIncrement,
      LocalDateTime startTime,
      LocalDateTime endTime)
      throws AuthenticationException {
    if (seller == null || item == null || !item.getSeller().getId().equals(seller.getId())) {
      throw new AuthenticationException("Chỉ người bán sở hữu sản phẩm mới tạo được phiên");
    }
    AuctionItem config = new AuctionItem(item, startPrice, bidIncrement, startTime, endTime);
    Auction auction = new Auction(config);
    auctionList.put(auction.getId(), auction);
    return auction;
  }

  public void removeAuction(String auctionId, User requester) throws AuthenticationException {
    Auction auction = getAuction(auctionId);
    Seller owner = auction.getAuctionItem().getItem().getSeller();
    boolean isOwner = requester != null && owner.getId().equals(requester.getId());
    boolean isAdmin = requester instanceof Admin;
    if (!isOwner && !isAdmin) {
      throw new AuthenticationException("Bạn không có quyền xóa phiên này");
    }
    if (auction.getStatus() != AuctionStatus.OPEN) {
      throw new IllegalStateException("Chỉ xóa được phiên chưa bắt đầu");
    }
    auctionList.remove(auctionId);
  }

  public Auction getAuction(String id) {
    Auction auction = auctionList.get(id);
    if (auction == null) {
      throw new NoSuchElementException("Không tìm thấy phiên: " + id);
    }
    return auction;
  }

  public List<Auction> getAllAuctions() {
    return new ArrayList<>(auctionList.values());
  }

  public List<Auction> getAuctionsByStatus(AuctionStatus status) {
    List<Auction> result = new ArrayList<>();
    for (Auction auction : auctionList.values()) {
      if (auction.getStatus() == status) {
        result.add(auction);
      }
    }
    return result;
  }

  public List<Auction> getAuctionsBySeller(String sellerId) {
    List<Auction> result = new ArrayList<>();
    for (Auction auction : auctionList.values()) {
      if (auction.getAuctionItem().getItem().getSeller().getId().equals(sellerId)) {
        result.add(auction);
      }
    }
    return result;
  }

  public synchronized void startScheduler() {
    if (scheduler != null) {
      return;
    }
    scheduler =
        Executors.newSingleThreadScheduledExecutor(
            new ThreadFactory() {
              @Override
              public Thread newThread(Runnable task) {
                Thread thread = new Thread(task, "auction-scheduler");
                thread.setDaemon(true);
                return thread;
              }
            });
    scheduler.scheduleAtFixedRate(
        new Runnable() {
          @Override
          public void run() {
            checkAuctions();
          }
        },
        0,
        1,
        TimeUnit.SECONDS);
  }

  public synchronized void shutdown() {
    if (scheduler != null) {
      scheduler.shutdown();
      scheduler = null;
    }
  }

  private void checkAuctions() {
    LocalDateTime now = LocalDateTime.now();
    for (Auction auction : auctionList.values()) {
      try {
        AuctionItem config = auction.getAuctionItem();
        if (auction.getStatus() == AuctionStatus.OPEN && !now.isBefore(config.getStartTime())) {
          auction.startAuction();
        }
        if (auction.getStatus() == AuctionStatus.RUNNING && !now.isBefore(config.getEndTime())) {
          auction.closeAuction();
        }
      } catch (RuntimeException e) {
        System.err.println("Lỗi khi cập nhật phiên " + auction.getId() + ": " + e.getMessage());
      }
    }
  }
}
