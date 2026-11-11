package com.auction.model;

import java.time.LocalDateTime;

public class AuctionItem {
  private final double startPrice;
  private final double bidIncrement;
  private final LocalDateTime startTime;
  private volatile LocalDateTime endTime;
  private final Item item;

  private static final long serialVersionUID = 1L;
  public AuctionItem(
      Item item,
      double startPrice,
      double bidIncrement,
      LocalDateTime startTime,
      LocalDateTime endTime) {
    if (item == null) {
      throw new IllegalArgumentException("Yêu cầu sản phẩm");
    }
    if (startPrice <= 0 || bidIncrement <= 0) {
      throw new IllegalArgumentException("Giá khởi điểm và bước giá phải lớn hơn 0");
    }
    if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
      throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu");
    }
    this.item = item;
    this.startPrice = startPrice;
    this.bidIncrement = bidIncrement;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public void extendEndTime(long seconds) {
      this.endTime = this.endTime.plusSeconds(seconds);
  }

  public double getStartPrice() {
    return startPrice;
  }

  public double getIncrement() {
    return bidIncrement;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public Item getItem() {
    return item;
  }
}
