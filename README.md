classDiagram
    %% Thực thể Người dùng
    class User {
        <<abstract>>
    }
    User <|-- Bidder : Kế thừa
    User <|-- Seller : Kế thừa
    User <|-- Admin : Kế thừa

    %% Thực thể Sản phẩm & Factory Method
    class Item {
        <<abstract>>
    }
    Item <|-- Electronics : Kế thừa
    Item <|-- Art : Kế thừa
    Item <|-- Vehicle : Kế thừa

    class ItemFactory
    ItemFactory ..> Item : Tạo ra (Factory Method)

    %% Nghiệp vụ Đấu giá
    class Auction {
        -status: AuctionStatus
    }
    class AuctionStatus {
        <<enumeration>>
        OPEN
        RUNNING
        FINISHED
        PAID
        CANCELED
    }
    class Bid

    Auction "1" *-- "*" Bid : Chứa
    Auction --> AuctionStatus : Trạng thái

    %% Observer Pattern (Realtime Update)
    class AuctionSubject {
        <<interface>>
    }
    class AuctionObserver {
        <<interface>>
        +update()
    }
    AuctionSubject --> AuctionObserver : Báo giá realtime
    AuctionManager ..|> AuctionSubject : Cài đặt (Implements)

    %% Singleton & Data Access
    class AuctionManager {
        <<Singleton>>
        +getInstance()
    }
    class UserDAO
    class AuctionDAO
    class DataStorage
    
    UserDAO --> DataStorage
    AuctionDAO --> DataStorage
