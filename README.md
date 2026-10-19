classDiagram
    %% Giao diện cơ sở
    class Entity {
        <<interface>>
        +getId() String
    }

    %% Cụm Người dùng (User Hierarchy)
    class User {
        <<abstract>>
        -String id
        -String username
        -String password
        -String role
        +printInfo() void
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
        +getCategory() String
        +printDetails() void
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
        -double currentPrice
        -String status
        -LocalDateTime startTime
        -LocalDateTime endTime
        +placeBid(Bidder bidder, double amount) boolean
        +closeAuction() void
        +getWinningBidder() Bidder
    }

    class AuctionManager {
        -AuctionManager instance$
        -Map~String, Auction~ auctions
        -AuctionManager()
        +getInstance()$ AuctionManager
        +createAuction(Item item, int durationMinutes) Auction
        +getAuctionById(String id) Auction
    }

    Auction "1" *-- "0..*" Bid : history
    Auction "1" --> "1" Item : item
    AuctionManager "1" o-- "0..*" Auction : manages
    Bid ..> Bidder : references
