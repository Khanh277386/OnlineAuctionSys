package com.auction.client.controller;

import com.auction.client.App;
import com.auction.client.store.InMemoryStore;
import com.auction.exception.AuctionClosedException;
import com.auction.exception.InvalidBidException;
import com.auction.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BidderMainController {

    // Header
    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;

    // Filter & Auction Table
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilterBox;
    @FXML private TableView<Auction> auctionTableView;
    @FXML private TableColumn<Auction, String> itemCol;
    @FXML private TableColumn<Auction, String> typeCol;
    @FXML private TableColumn<Auction, String> priceCol;
    @FXML private TableColumn<Auction, String> incrementCol;
    @FXML private TableColumn<Auction, String> endTimeCol;
    @FXML private TableColumn<Auction, String> statusCol;

    // Item Details Panel (Polymorphic: Electronic / Art / Vehicle)
    @FXML private Label itemNameLabel;
    @FXML private Label itemDescLabel;
    @FXML private Label sellerLabel;
    @FXML private Label spec1Label;
    @FXML private Label spec2Label;

    // Price & Bid History Panel
    @FXML private Label currentPriceLabel;
    @FXML private Label minBidLabel;
    @FXML private Label leadingBidderLabel;
    @FXML private TableView<Bid> bidHistoryTable;
    @FXML private TableColumn<Bid, String> bidderCol;
    @FXML private TableColumn<Bid, String> bidAmountCol;
    @FXML private TableColumn<Bid, String> bidTimeCol;

    // Actions
    @FXML private TextField bidAmountField;
    @FXML private Button payButton;

    private Bidder currentBidder;
    private FilteredList<Auction> filteredAuctions;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM");

    @FXML
    public void initialize() {
        // 1. Lấy tài khoản Bidder đang đăng nhập từ RAM (InMemoryStore)
        currentBidder = InMemoryStore.getCurrentBidder();
        if (currentBidder == null) {
            currentBidder = new Bidder("BID-GUEST", "Guest Bidder", "123");
            currentBidder.addFund(5_000);
        }
        updateBidderHeader();

        // 2. Cấu hình bộ lọc danh mục
        categoryFilterBox.setItems(FXCollections.observableArrayList("All", "Electronic", "Art", "Vehicle"));
        categoryFilterBox.setValue("All");

        // 3. Ánh xạ cột cho bảng Auction TableView
        itemCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAuctionItem().getItem().getName()));
        typeCol.setCellValueFactory(data ->
                new SimpleStringProperty(getCategoryName(data.getValue().getAuctionItem().getItem())));
        priceCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatCurrency(data.getValue().getCurrentPrice())));
        incrementCol.setCellValueFactory(data ->
                new SimpleStringProperty("+" + formatCurrency(data.getValue().getAuctionItem().getIncrement())));
        endTimeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAuctionItem().getEndTime().format(timeFormatter)));
        statusCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus().name()));

        // 4. Ánh xạ cột cho bảng lịch sử đặt giá Bid History
        bidderCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBidder().getName()));
        bidAmountCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatCurrency(data.getValue().getAmount())));
        bidTimeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

        // 5. Lấy danh sách phiên đấu giá dùng chung từ InMemoryStore
        filteredAuctions = new FilteredList<>(InMemoryStore.getAuctions(), p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        categoryFilterBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        auctionTableView.setItems(filteredAuctions);

        // 6. Lắng nghe sự kiện chọn dòng trên bảng
        auctionTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                displayAuctionDetails(newSel);
            }
        });
    }

    private void displayAuctionDetails(Auction auction) {
        AuctionItem aucItem = auction.getAuctionItem();
        Item item = aucItem.getItem();

        itemNameLabel.setText("Item: " + item.getName());
        itemDescLabel.setText("Description: " + item.getDescription());
        sellerLabel.setText("Seller: " + item.getSeller().getName());

        if (item instanceof Electronic e) {
            spec1Label.setText("Warranty: " + e.getWarrantyInfo() + " months");
            spec2Label.setText("Durability: " + e.getDurabilityInfo() + "%");
        } else if (item instanceof Art a) {
            spec1Label.setText("Style: " + a.getStyle());
            spec2Label.setText("Age: " + a.getAge() + " years");
        } else if (item instanceof Vehicle v) {
            spec1Label.setText("Brand / Model: " + v.getBrand() + " " + v.getModel());
            spec2Label.setText("Mileage: " + String.format("%,.0f", v.getMileage()) + " km");
        } else {
            spec1Label.setText("Specs: N/A");
            spec2Label.setText("");
        }

        double minAllowed = (auction.getLeading() == null)
                ? aucItem.getStartPrice()
                : auction.getCurrentPrice() + aucItem.getIncrement();

        currentPriceLabel.setText("Current Price: " + formatCurrency(auction.getCurrentPrice()));
        minBidLabel.setText("Minimum Bid: " + formatCurrency(minAllowed));
        leadingBidderLabel.setText("Leading Bidder: " +
                (auction.getLeading() != null ? auction.getLeading().getName() : "None"));

        bidAmountField.setText(String.format("%.0f", minAllowed));
        bidHistoryTable.setItems(FXCollections.observableArrayList(auction.getBidHistory()));

        boolean canPay = auction.getStatus() == AuctionStatus.FINISHED
                && auction.getLeading() != null
                && auction.getLeading().getId().equals(currentBidder.getId());
        payButton.setDisable(!canPay);
    }

    @FXML
    public void onPlaceBid(ActionEvent event) {
        Auction selected = auctionTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "NO AUCTION SELECTED", "Please select an auction from the table first!");
            return;
        }

        try {
            double amount = Double.parseDouble(bidAmountField.getText().trim());
            Bid newBid = selected.placeBid(currentBidder, amount);

            updateBidderHeader(); // <-- Thêm dòng này để cập nhật số dư ví ngay trên UI
            auctionTableView.refresh();
            displayAuctionDetails(selected);
            showAlert(Alert.AlertType.INFORMATION, "BID PLACED",
                    "Successfully placed a bid of " + formatCurrency(newBid.getAmount()) + " on " + selected.getAuctionItem().getItem().getName());

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "INVALID FORMAT", "Please enter a valid numeric amount!");
        } catch (InvalidBidException | AuctionClosedException e) {
            showAlert(Alert.AlertType.ERROR, "BID REJECTED", e.getMessage());
        }
    }

    @FXML
    public void onAddFund(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1000");
        dialog.setTitle("Add Funds");
        dialog.setHeaderText("Current Balance: " + formatCurrency(currentBidder.getBalance()));
        dialog.setContentText("Enter deposit amount ($):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                double amount = Double.parseDouble(input.trim());
                currentBidder.addFund(amount);
                updateBidderHeader();
                showAlert(Alert.AlertType.INFORMATION, "DEPOSIT SUCCESSFUL",
                        "Added " + formatCurrency(amount) + " to your wallet.");
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "DEPOSIT FAILED", e.getMessage());
            }
        });
    }

    @FXML
    public void onPayWonAuction(ActionEvent event) {
        Auction selected = auctionTableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            selected.markPaid();
            updateBidderHeader();
            auctionTableView.refresh();
            displayAuctionDetails(selected);
            showAlert(Alert.AlertType.INFORMATION, "PAYMENT SUCCESSFUL",
                    "Payment completed for " + selected.getAuctionItem().getItem().getName());
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "PAYMENT FAILED", e.getMessage());
        }
    }

    private void applyFilters() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String selectedCategory = categoryFilterBox.getValue();

        filteredAuctions.setPredicate(auction -> {
            Item item = auction.getAuctionItem().getItem();
            boolean matchesKeyword = keyword.isEmpty() || item.getName().toLowerCase().contains(keyword);
            boolean matchesCategory = selectedCategory == null
                    || "All".equals(selectedCategory)
                    || getCategoryName(item).equalsIgnoreCase(selectedCategory);
            return matchesKeyword && matchesCategory;
        });
    }

    private void updateBidderHeader() {
        welcomeLabel.setText("Bidder: " + currentBidder.getName());
        balanceLabel.setText("Balance: " + formatCurrency(currentBidder.getBalance()));
    }

    private String getCategoryName(Item item) {
        if (item instanceof Electronic) return "Electronic";
        if (item instanceof Art) return "Art";
        if (item instanceof Vehicle) return "Vehicle";
        return "Other";
    }

    private String formatCurrency(double amount) {
        return String.format("$%,.0f", amount);
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        auctionTableView.refresh();
        Auction selected = auctionTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            displayAuctionDetails(selected);
        }
    }

    @FXML
    public void onLogout(ActionEvent event) {
        InMemoryStore.logout();
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/auction/client/view/login-form-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Auction System - Login");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "NAVIGATION ERROR", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}