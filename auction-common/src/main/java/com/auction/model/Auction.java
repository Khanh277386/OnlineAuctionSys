package com.auction.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.auction.exception.AuctionClosedException;
import com.auction.exception.InvalidBidException;
import com.auction.observer.Observer;
import com.auction.observer.Subject;

public class Auction implements Entity, Subject {
  private final String id;
  private final AuctionItem auctionItem;
  private transient final ReentrantLock lock = new ReentrantLock();
  private final List<Bid> bidHistory = new ArrayList<>();
  private transient final List<Observer> observers = new CopyOnWriteArrayList<>();
  private volatile double currentPrice;
  private volatile Bidder leading;
  private volatile AuctionStatus status;

  public Auction(AuctionItem auctionItem) {
    if (auctionItem == null) {
      throw new IllegalArgumentException("Thiếu cấu hình phiên đấu giá");
    }
    this.id = UUID.randomUUID().toString();
    this.auctionItem = auctionItem;
    this.currentPrice = auctionItem.getStartPrice();
    this.leading = null;
    this.status = AuctionStatus.OPEN;
  }

  public void startAuction() {
    lock.lock();
    try {
      if (status != AuctionStatus.OPEN) {
        throw new IllegalStateException("Chỉ được mở phiên đang ở trạng thái OPEN");
      }
      status = AuctionStatus.RUNNING;
    } finally {
      lock.unlock();
    }
  }

  public void closeAuction() {
    lock.lock();
    try {
      if (status != AuctionStatus.RUNNING) {
        throw new IllegalStateException("Chỉ đóng phiên đang RUNNING");
      }
      status = AuctionStatus.FINISHED;
      if (leading == null) {
        status = AuctionStatus.CANCELLED;
      }
    } finally {
      lock.unlock();
    }
  }

  public void markPaid() {
    lock.lock();
    try {
      if (status != AuctionStatus.FINISHED || leading == null) {
        throw new IllegalStateException("Chỉ thanh toán được khi phiên kết thúc và có người thắng");
      }
      if (!leading.deductFund(currentPrice)) {
        throw new IllegalStateException("Người thắng không đủ tiền trả");
      }
      auctionItem.getItem().getSeller().addRevenue(currentPrice);
      status = AuctionStatus.PAID;
    } finally {
      lock.unlock();
    }
  }

  public void cancel() {
    lock.lock();
    try {
      if (status == AuctionStatus.PAID || status == AuctionStatus.CANCELLED) {
        throw new IllegalStateException("Phiên đã kết thúc");
      }
      status = AuctionStatus.CANCELLED;
    } finally {
      lock.unlock();
    }
  }

  public Bid placeBid(Bidder bidder, double amount)
      throws InvalidBidException, AuctionClosedException {
    if (bidder == null) {
      throw new InvalidBidException("Thiếu người");
    }
    Bid newBid;
    lock.lock();
    try {
      if (status != AuctionStatus.RUNNING
          || LocalDateTime.now().isAfter(auctionItem.getEndTime())) {
        throw new AuctionClosedException("Phiên đấu không còn nhận giá");
      }

      if (leading != null && leading.getId().equals(bidder.getId())) {
        throw new InvalidBidException("Bạn đang dẫn đầu");
      }

      double minAllowed;
      if (leading == null) {
        minAllowed = auctionItem.getStartPrice();
      } else {
        minAllowed = currentPrice + auctionItem.getIncrement();
      }
      if (amount < minAllowed) {
        throw new InvalidBidException(
            "Phải đặt từ " + String.format("%,.0f", minAllowed) + " đổ lên");
      }
      if (amount > bidder.getBalance()) {
        throw new InvalidBidException("Số dư không đủ đặt");
      }

      newBid = new Bid(id, bidder, amount);
      bidHistory.add(newBid);
      currentPrice = amount;
      leading = bidder;
    } finally {
      lock.unlock();
    }

    notifyObservers(newBid);
    return newBid;
  }

  public void attach(Observer observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
    }
  }

  public void detach(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers(Bid bid) {
    for (Observer observer : observers) {
      try {
        observer.update(bid);
      } catch (RuntimeException e) {
        System.err.println("Lỗi thông báo: " + e.getMessage());
      }
    }
  }

  public String getId() {
    return id;
  }

  public AuctionItem getAuctionItem() {
    return auctionItem;
  }

  public double getCurrentPrice() {
    return currentPrice;
  }

  public Bidder getLeading() {
    return leading;
  }

  public AuctionStatus getStatus() {
    return status;
  }

  public List<Bid> getBidHistory() {
    lock.lock();
    try {
      return new ArrayList<>(bidHistory);
    } finally {
      lock.unlock();
    }
  }
}
