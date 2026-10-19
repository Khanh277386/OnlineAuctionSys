classDiagram

%% Giao tiếp cơ sở
class Entity {
    <<Interface>>
    +getId() String
}

%% Cụm Người dùng (User Hierarchy)
class User {
    <<abstract>>
    -String id
    -String username
    -String password
    -String role
    +printInfo()* void
    +getId() String
    +getUsername() String
    +getRole() String
}

class Bidder {
    -double maxAutoBid
    +printInfo() void
    +getMaxAutoBid() double
}

class Admin {
    +printInfo() void
}

class Seller {
    -double rating
    +printInfo() void
    +getRating() double
}

Entity <|.. User : implements
User <|-- Bidder : extends
User <|-- Admin : extends
User <|-- Seller : extends

%% Cụm Sản phẩm & Factory (Item Hierarchy)
class Item {
    <<abstract>>
    -String id
    -String title
    -String description
    -double startingPrice
    -String sellerId
    +getCategory()* String
    +printDetails()* void
    +getId() String
    +getStartingPrice() double
}

class Electronics {
    -String brand
    -int warrantyMonths
    +getCategory() String
    +printDetails() void
}

class Art {
    -String artist
    -int yearCreated
    +getCategory() String
    +printDetails() void
}

class Vehicle {
    -int modelYear
    -double mileage
    +getCategory() String
    +printDetails() void
}

class ItemFactory {
    +createItem(String type, String id, String title, double price) Item
}

Entity <|.. Item : implements
Item <|-- Electronics : extends
Item <|-- Art : extends
Item <|-- Vehicle : extends
ItemFactory ..> Item : creates

%% Cụm Nghiệp vụ Đấu giá & Quản lý
class Bid {
    -String bidId
    -String auctionId
    -String bidderId
    -double amount
    -LocalDateTime timestamp
    +getAmount() double
    +getBidderId() String
    +getTimestamp() LocalDateTime
}

class Auction {
    -String auctionId
    -Item item
    -double currentPrice
    -String status
    -LocalDateTime startTime
    -LocalDateTime endTime
    -List~Bid~ bids
    -Bidder highestBidder
    +placeBid(Bidder bidder, double amount) boolean
    +closeAuction() void
    +getWinningBidder() Bidder
    +getItem() Item
    +getCurrentPrice() double
}

class AuctionManager {
    -AuctionManager instance$
    -Map~String, Auction~ auctions
    -AuctionManager()
    +getInstance()$ AuctionManager
    +createAuction(Item item, int durationMinutes) Auction
    +getAuctionById(String id) Auction
}

AuctionManager "1" o-- "0..*" Auction : manages
Auction "1" *-- "1" Item : contains
Auction "1" *-- "0..*" Bid : contains
Auction ..> Bidder : uses
Bid ..> Bidder : references
