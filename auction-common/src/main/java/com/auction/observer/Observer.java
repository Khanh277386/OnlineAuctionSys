package com.auction.observer;

import com.auction.model.Bid;

public interface Observer {
  void update(Bid bid);
}
