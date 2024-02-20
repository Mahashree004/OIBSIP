import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TrainReservationSystem extends Application {

    // Sample user credentials
    private static final Map<String, String> userCredentials = new HashMap<>();
    static {
        userCredentials.put("user1", "password1");
        userCredentials.put("user2", "password2");
        // Add more users as needed
    }

    private static final double TICKET_PRICE = 10.0; // Sample ticket price

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Train Reservation System");

        // Set default font size
        String defaultFontSize = "20px";

        // Set background color to light blue
        Color backgroundColor = Color.LIGHTBLUE;

        // Create UI components for login screen
        Label usernameLabel = new Label("Username:");
        TextField usernameTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        // Layout for login screen
        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(10, 10, 10, 10));
        loginGrid.setVgap(5);
        loginGrid.setHgap(5);
        loginGrid.add(usernameLabel, 0, 0);
        loginGrid.add(usernameTextField, 1, 0);
        loginGrid.add(passwordLabel, 0, 1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(loginButton, 1, 2);

        // Set style for login screen
        loginGrid.setStyle("-fx-font-size: " + defaultFontSize + "; -fx-background-color: #" + backgroundColor.toString().substring(2, 8));

        // Set scene for login screen
        Scene loginScene = new Scene(loginGrid, 400, 250);

        // Create UI components for reservation screen
        Label trainNumberLabel = new Label("Train Number:");
        TextField trainNumberTextField = new TextField();
        Label trainNameLabel = new Label("Train Name:");
        TextField trainNameTextField = new TextField();
        Label classTypeLabel = new Label("Class Type:");
        ComboBox<String> classTypeComboBox = new ComboBox<>();
        classTypeComboBox.getItems().addAll("First Class", "Second Class", "Sleeper");
        Label dateLabel = new Label("Date of Journey:");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        Label fromLabel = new Label("From:");
        TextField fromTextField = new TextField();
        Label toLabel = new Label("To:");
        TextField toTextField = new TextField();
        Label numberOfTicketsLabel = new Label("Number of Tickets:");
        Spinner<Integer> numberOfTicketsSpinner = new Spinner<>(1, 10, 1); // Spinner for number of tickets
        Label amountLabel = new Label("Amount:");
        Label amountValueLabel = new Label(); // Label to display amount
        Button bookTicketButton = new Button("Book Ticket");

        // Layout for reservation screen
        GridPane reservationGrid = new GridPane();
        reservationGrid.setPadding(new Insets(10, 10, 10, 10));
        reservationGrid.setVgap(5);
        reservationGrid.setHgap(5);
        reservationGrid.add(trainNumberLabel, 0, 0);
        reservationGrid.add(trainNumberTextField, 1, 0);
        reservationGrid.add(trainNameLabel, 0, 1);
        reservationGrid.add(trainNameTextField, 1, 1);
        reservationGrid.add(classTypeLabel, 0, 2);
        reservationGrid.add(classTypeComboBox, 1, 2);
        reservationGrid.add(dateLabel, 0, 3);
        reservationGrid.add(datePicker, 1, 3);
        reservationGrid.add(fromLabel, 0, 4);
        reservationGrid.add(fromTextField, 1, 4);
        reservationGrid.add(toLabel, 0, 5);
        reservationGrid.add(toTextField, 1, 5);
        reservationGrid.add(numberOfTicketsLabel, 0, 6);
        reservationGrid.add(numberOfTicketsSpinner, 1, 6);
        reservationGrid.add(amountLabel, 0, 7);
        reservationGrid.add(amountValueLabel, 1, 7);
        reservationGrid.add(bookTicketButton, 1, 8);

        // Set style for reservation screen
        reservationGrid.setStyle("-fx-font-size: " + defaultFontSize + "; -fx-background-color: #" + backgroundColor.toString().substring(2, 8));

        // Set scene for reservation screen
        Scene reservationScene = new Scene(reservationGrid, 800, 600);

        // Create UI components for payment screen
        Label cardNumberLabel = new Label("Card Number:");
        TextField cardNumberTextField = new TextField();
        Label paymentAmountLabel = new Label("Amount:");
        Label paymentAmountValueLabel = new Label(); // Label to display payment amount
        Button payButton = new Button("Pay");

        // Layout for payment screen
        GridPane paymentGrid = new GridPane();
        paymentGrid.setPadding(new Insets(10, 10, 10, 10));
        paymentGrid.setVgap(5);
        paymentGrid.setHgap(5);
        paymentGrid.add(cardNumberLabel, 0, 0);
        paymentGrid.add(cardNumberTextField, 1, 0);
        paymentGrid.add(paymentAmountLabel, 0, 1);
        paymentGrid.add(paymentAmountValueLabel, 1, 1);
        paymentGrid.add(payButton, 1, 2);

        // Set style for payment screen
        paymentGrid.setStyle("-fx-font-size: " + defaultFontSize + "; -fx-background-color: #" + backgroundColor.toString().substring(2, 8));

        // Set scene for payment screen
        Scene paymentScene = new Scene(paymentGrid, 600, 400);

        // Event handling for login button
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            if (isValidCredentials(username, password)) {
                primaryStage.setScene(reservationScene);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password. Please try again.");
            }
        });

        // Event handling for book ticket button in reservation screen
        bookTicketButton.setOnAction(e -> {
            // Calculate amount based on number of tickets and ticket price
            int numberOfTickets = numberOfTicketsSpinner.getValue();
            double amount = numberOfTickets * TICKET_PRICE;
            amountValueLabel.setText("$" + amount);
            paymentAmountValueLabel.setText("$" + amount);

            primaryStage.setScene(paymentScene);
        });

        // Event handling for pay button in payment screen
        payButton.setOnAction(e -> {
            // Perform payment validation
            if (validatePayment(cardNumberTextField.getText())) {
                showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "Payment successful!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Payment Error", "Invalid card number. Please try again.");
            }
        });

        // Set the styles for all screens
        String style = "-fx-font-size: " + defaultFontSize + "; -fx-background-color: #" + backgroundColor.toString().substring(2, 8);
        loginScene.getRoot().setStyle(style);
        reservationScene.getRoot().setStyle(style);
        paymentScene.getRoot().setStyle(style);

        // Set the login screen as the initial scene
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private boolean isValidCredentials(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private boolean validatePayment(String cardNumber) {
        // Perform payment validation logic here
        // For demonstration, just check if the card number is not empty
        return !cardNumber.isEmpty();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}