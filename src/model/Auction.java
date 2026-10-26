package auction.model;

import auction.exception.AuctionClosedException;
import auction.exception.InvalidBidException;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lớp quản lý trung tâm của một phiên đấu giá.
 */
public class Auction implements Subject, Entity, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private AuctionItem auctionItem;
    private double currentHighestBid;
    private final ReentrantLock lock;
    private AuctionStatus status;
    private final List<Observer> observers;
    private final List<Bid> bids;
    private Bid winningBid;

    public Auction(String id, AuctionItem auctionItem) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiên đấu giá không được để trống.");
        }
        if (auctionItem == null) {
            throw new IllegalArgumentException("AuctionItem không được null.");
        }
        this.id = id;
        this.auctionItem = auctionItem;
        this.currentHighestBid = auctionItem.getStarterPrice();
        this.lock = new ReentrantLock(true);
        this.status = AuctionStatus.OPEN;
        this.observers = new ArrayList<Observer>();
        this.bids = new ArrayList<Bid>();
        this.winningBid = null;
    }

    public void startAuction() {
        lock.lock();
        try {
            if (this.status != AuctionStatus.OPEN) {
                throw new IllegalStateException(
                        "Chỉ có thể bắt đầu phiên đấu giá khi đang ở trạng thái OPEN.");
            }
            this.status = AuctionStatus.RUNNING;
        } finally {
            lock.unlock();
        }
        notifyObservers();
    }

    public void closeAuction() {
        lock.lock();
        try {
            if (this.status != AuctionStatus.RUNNING && this.status != AuctionStatus.OPEN) {
                return;
            }
            this.status = AuctionStatus.FINISHED;
            if (!this.bids.isEmpty()) {
                this.winningBid = this.bids.get(this.bids.size() - 1);
            }
        } finally {
            lock.unlock();
        }
        notifyObservers();
    }

    public void paidOrCancelled() {
        lock.lock();
        try {
            if (this.status != AuctionStatus.FINISHED) {
                throw new IllegalStateException(
                        "Phiên đấu giá phải ở trạng thái FINISHED trước khi thanh toán hoặc hủy.");
            }
            if (this.winningBid != null && this.winningBid.processPayment()) {
                this.status = AuctionStatus.PAID;
            } else {
                this.status = AuctionStatus.CANCELLED;
            }
        } finally {
            lock.unlock();
        }
        notifyObservers();
    }

    public void placeBid(Bid bid) throws AuctionClosedException, InvalidBidException {
        if (bid == null) {
            throw new InvalidBidException("Thông tin đặt giá không được null.");
        }

        lock.lock();
        try {
            LocalDateTime now = LocalDateTime.now();

            if (now.isAfter(auctionItem.getEndTime())) {
                if (this.status == AuctionStatus.RUNNING) {
                    this.status = AuctionStatus.FINISHED;
                }
                throw new AuctionClosedException("Phiên đấu giá đã hết thời gian.");
            }

            if (this.status != AuctionStatus.RUNNING) {
                throw new AuctionClosedException(
                        "Phiên đấu giá hiện không hoạt động (Trạng thái: " + this.status + ").");
            }

            Bidder bidder = bid.getBidder();
            if (bidder == null) {
                throw new InvalidBidException("Người đặt giá không hợp lệ.");
            }
            Seller seller = auctionItem.getItem().getSeller();
            if (seller != null && seller.getId() != null
                    && seller.getId().equals(bidder.getId())) {
                throw new InvalidBidException("Người bán không được tự đặt giá sản phẩm của mình.");
            }

            double bidAmount = bid.getAmount();
            double minimumRequiredAmount;
            if (this.bids.isEmpty()) {
                minimumRequiredAmount = this.auctionItem.getStarterPrice();
                if (bidAmount < minimumRequiredAmount) {
                    throw new InvalidBidException(
                            "Giá đặt đầu tiên phải từ " + minimumRequiredAmount + " trở lên.");
                }
            } else {
                minimumRequiredAmount = this.currentHighestBid + this.auctionItem.getIncrement();
                if (bidAmount < minimumRequiredAmount) {
                    throw new InvalidBidException(
                            "Giá đặt phải lớn hơn hoặc bằng " + minimumRequiredAmount
                                    + " (Giá hiện tại: " + this.currentHighestBid
                                    + " + Bước giá: " + this.auctionItem.getIncrement() + ").");
                }
            }


            if (bidder.getBalance() < bidAmount) {
                throw new InvalidBidException("Số dư tài khoản không đủ để đặt mức giá này.");
            }


            this.currentHighestBid = bidAmount;
            this.auctionItem.setCurrentPrice(bidAmount);
            this.winningBid = bid;
            this.bids.add(bid);

            if (!this.observers.contains(bidder)) {
                this.observers.add(bidder);
            }


        notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        if (observer == null) {
            return;
        }
        lock.lock();
        try {
            if (!this.observers.contains(observer)) {
                this.observers.add(observer);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observer == null) {
            return;
        }
        lock.lock();
        try {
            this.observers.remove(observer);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void notifyObservers() {
        List<Observer> snapshot;
        lock.lock();
        try {
            snapshot = new ArrayList<Observer>(this.observers);
        } finally {
            lock.unlock();
        }

        for (Observer observer : snapshot) {
            observer.update();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuctionItem getAuctionItem() {
        return auctionItem;
    }

    public double getCurrentHighestBid() {
        lock.lock();
        try {
            return currentHighestBid;
        } finally {
            lock.unlock();
        }
    }

    public AuctionStatus getStatus() {
        lock.lock();
        try {
            return status;
        } finally {
            lock.unlock();
        }
    }

    public Bid getWinningBid() {
        lock.lock();
        try {
            return winningBid;
        } finally {
            lock.unlock();
        }
    }

    public List<Bid> getBids() {
        lock.lock();
        try {
            return Collections.unmodifiableList(new ArrayList<Bid>(this.bids));
        } finally {
            lock.unlock();
        }
    }

    public List<Observer> getObservers() {
        lock.lock();
        try {
            return Collections.unmodifiableList(new ArrayList<Observer>(this.observers));
        } finally {
            lock.unlock();
        }
    }
}