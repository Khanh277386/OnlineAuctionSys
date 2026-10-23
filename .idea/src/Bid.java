public class Bid {
    private String bidId;
    private String acutionId;
    private String bidderId;
    private double amount;
    private LocalDateTime timestamp;

    public double getAmount(){
        return amout;
    }

    public String getBidId(){
        return bidId;
    }

    public LocalDatetime getTimestamp(){
        return timestamp;
    }
}