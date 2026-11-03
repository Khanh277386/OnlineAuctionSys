# Hệ thống đấu giá trực tuyến (phần logic)

Code cho các lớp trong sơ đồ lớp: model, Observer, ItemFactory, 3 Manager (Singleton), ngoại lệ.
Chưa có GUI (JavaFX), Socket và các tính năng nâng cao.

## Chạy
- Chạy demo: mở `src/main/java/com/auction/Main.java` trong IntelliJ và Run.
- Chạy test: `mvn test`

## Cấu trúc
```
com.auction
├── Main.java                 demo luồng nghiệp vụ trên console
├── exception/                AuctionException và 4 lớp con
├── observer/                 Observer, Subject
├── factory/                  ItemFactory
├── manager/                  UserManager, ItemManager, AuctionManager (Singleton)
└── model/                    Entity, User/Bidder/Seller/Admin, Item/Electronic/Art/Vehicle,
                              ItemType, AuctionItem, Auction, AuctionStatus, Bid
```
