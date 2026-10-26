package auction.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuctionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private double starterPrice;
    private double currentPrice;
    private double bidIncrement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Item item;

    public AuctionItem(Item item, double starterPrice, double bidIncrement,
                       LocalDateTime startTime, LocalDateTime endTime) {
        if (item == null) {
            throw new IllegalArgumentException("Sản phẩm (Item) không được để trống.");
        }
        if (starterPrice < 0 || bidIncrement <= 0) {
            throw new IllegalArgumentException("Giá khởi điểm không âm và bước giá phải lớn hơn 0.");
        }
        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu.");
        }
        this.item = item;
        this.starterPrice = starterPrice;
        this.currentPrice = starterPrice;
        this.bidIncrement = bidIncrement;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public double getStarterPrice() {
        return starterPrice;
    }

    public void setStarterPrice(double starterPrice) {
        this.starterPrice = starterPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(double bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void extendEndTime(long extraSeconds) {
        if (extraSeconds > 0) {
            this.endTime = this.endTime.plusSeconds(extraSeconds);
        }
    }
}