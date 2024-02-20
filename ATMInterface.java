import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMInterface extends Application {

    private Map<String, String> users;
    private User currentUser;
    private List<Transaction> transactionHistory;

    @Override
    public void start(Stage primaryStage) {
        users = new HashMap<>();
        users.put("user123", "1234");
        users.put("user456", "5678");

        primaryStage.setTitle("ATM Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userIdLabel = new Label("User ID:");
        grid.add(userIdLabel, 0, 0);

        TextField userIdField = new TextField();
        grid.add(userIdField, 1, 0);

        Label pinLabel = new Label("PIN:");
        grid.add(pinLabel, 0, 1);

        PasswordField pinField = new PasswordField();
        grid.add(pinField, 1, 1);

        Button loginBtn = new Button("Login");
        grid.add(loginBtn, 1, 2);

        Label statusLabel = new Label();
        grid.add(statusLabel, 1, 3);

        loginBtn.setOnAction(e -> {
            String userId = userIdField.getText();
            String pin = pinField.getText();

            if (users.containsKey(userId) && users.get(userId).equals(pin)) {
                currentUser = new User(userId);
                transactionHistory = new ArrayList<>();
                openATM(primaryStage);
            } else {
                statusLabel.setText("Invalid User ID or PIN.");
            }
        });

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openATM(Stage primaryStage) {
        primaryStage.close();

        Stage atmStage = new Stage();
        atmStage.setTitle("ATM - " + currentUser.getUserId());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label titleLabel = new Label("Welcome, " + currentUser.getUserId() + "!");
        grid.add(titleLabel, 0, 0, 2, 1);

        Button transactionHistoryBtn = new Button("Transaction History");
        grid.add(transactionHistoryBtn, 0, 1);

        Button withdrawBtn = new Button("Withdraw");
        grid.add(withdrawBtn, 1, 1);

        Button depositBtn = new Button("Deposit");
        grid.add(depositBtn, 0, 2);

        Button transferBtn = new Button("Transfer");
        grid.add(transferBtn, 1, 2);

        Button quitBtn = new Button("Quit");
        grid.add(quitBtn, 0, 3, 2, 1);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        grid.add(outputArea, 0, 4, 2, 1);

        transactionHistoryBtn.setOnAction(e -> displayTransactionHistory(outputArea));

        withdrawBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Withdraw");
            dialog.setHeaderText("Enter amount to withdraw:");
            dialog.showAndWait().ifPresent(amount -> {
                int withdrawAmount = Integer.parseInt(amount);
                withdraw(withdrawAmount, outputArea);
            });
        });

        depositBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Deposit");
            dialog.setHeaderText("Enter amount to deposit:");
            dialog.showAndWait().ifPresent(amount -> {
                int depositAmount = Integer.parseInt(amount);
                deposit(depositAmount, outputArea);
            });
        });

        transferBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Transfer");
            dialog.setHeaderText("Enter recipient's User ID:");
            dialog.showAndWait().ifPresent(recipientId -> {
                TextInputDialog amountDialog = new TextInputDialog();
                amountDialog.setTitle("Transfer");
                amountDialog.setHeaderText("Enter amount to transfer:");
                amountDialog.showAndWait().ifPresent(amount -> {
                    int transferAmount = Integer.parseInt(amount);
                    transfer(recipientId, transferAmount, outputArea);
                });
            });
        });

        quitBtn.setOnAction(e -> atmStage.close());

        Scene atmScene = new Scene(grid, 400, 300);
        atmStage.setScene(atmScene);
        atmStage.show();
    }

    private void displayTransactionHistory(TextArea outputArea) {
        outputArea.clear();
        outputArea.appendText("Transaction History:\n");
        for (Transaction transaction : transactionHistory) {
            outputArea.appendText(transaction.toString() + "\n");
        }
    }

    private void withdraw(int amount, TextArea outputArea) {
        int currentBalance = calculateBalance();
        if (amount <= 0) {
            outputArea.appendText("Invalid amount.\n");
        } else if (amount > currentBalance) {
            outputArea.appendText("Insufficient funds.\n");
        } else {
            transactionHistory.add(new Transaction(TransactionType.WITHDRAW, amount));
            outputArea.appendText("Rs." + amount + " withdrawn successfully.\n");
        }
    }

    private void deposit(int amount, TextArea outputArea) {
        if (amount <= 0) {
            outputArea.appendText("Invalid amount.\n");
        } else {
            transactionHistory.add(new Transaction(TransactionType.DEPOSIT, amount));
            outputArea.appendText("Rs." + amount + " deposited successfully.\n");
        }
    }

    private void transfer(String recipientId, int amount, TextArea outputArea) {
        if (amount <= 0) {
            outputArea.appendText("Invalid amount.\n");
            return;
        }
        transactionHistory.add(new Transaction(TransactionType.TRANSFER, amount, recipientId));
        outputArea.appendText("Rs." + amount + " transferred to user " + recipientId + " successfully.\n");
    }

    private int calculateBalance() {
        int balance = 0;
        for (Transaction transaction : transactionHistory) {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                balance += transaction.getAmount();
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                balance -= transaction.getAmount();
            } else if (transaction.getType() == TransactionType.TRANSFER) {
                if (transaction.getRecipientId().equals(currentUser.getUserId())) {
                    balance += transaction.getAmount();
                } else {
                    balance -= transaction.getAmount();
                }
            }
        }
        return balance;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class User {
    private String userId;

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}

class Transaction {
    private TransactionType type;
    private int amount;
    private String recipientId;

    public Transaction(TransactionType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public Transaction(TransactionType type, int amount, String recipientId) {
        this.type = type;
        this.amount = amount;
        this.recipientId = recipientId;
    }

    public TransactionType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getRecipientId() {
        return recipientId;
    }

    @Override
    public String toString() {
        if (type == TransactionType.TRANSFER) {
            return "Type: " + type + ", Amount: Rs." + amount + ", Recipient ID: " + recipientId;
        } else {
            return "Type: " + type + ", Amount: Rs." + amount;
        }
    }
}

enum TransactionType {
    WITHDRAW,
    DEPOSIT,
    TRANSFER
}