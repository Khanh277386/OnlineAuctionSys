import java.util.ArrayList;
public class void Auction extend Item, User{
    private String acutionId;
    private double curentPrice;
    private Stirng status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Bid> bid = new ArrayList<>();
    private Item item;

    public void Auction(String acutionId, Item item, int durationMinute){
        this.auctionId = auctionId;
        this.item = item;
        this.currentPrice = item.getStartingPrice();
        this.status = "OPEN";
        this.startTime = LocalDateTime.now();
        this.endTime = this.startTime.plusMinutes(durationMinutes);
        this.bids = new ArrayList<>();
    }

    public synchronized boolean placeBid(Bidder bidder, double amount){
        LocalDateTime now = LocalDateTime.now();

        if (!"OPEN".equals(this.status) || now.isAfter(this.endTime)) {
            this.status = "CLOSED";
            return false;
        }

        if (amount <= this.currentPrice) {
            return false;
        }

        String newBidId = "BID-" + UUID.randomUUID().toString().substring(0, 6);
        Bid newBid = new Bid(newBidId, this.auctionId, bidder.getId(), amount, now);

        this.bids.add(newBid);
        this.currentPrice = amount;
        this.currentWinningBidder = bidder;
        return true;
    }
    public void closeAuction(){
        this.status = "CLOSED"
    }
    public Bidder getWinningBidder(){
        return null;
    }
}