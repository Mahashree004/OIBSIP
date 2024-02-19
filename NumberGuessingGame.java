import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Random;

public class NumberGuessingGame extends Application {

    private static final int MAX_ATTEMPTS = 5;
    private static final int NUM_ROUNDS = 3;

    private int randomNumber;
    private int round = 1;
    private int totalScore = 0;

    @Override
    public void start(Stage primaryStage) {
        startNewRound();
    }

    private void startNewRound() {
        if (round <= NUM_ROUNDS) {
            Random random = new Random();
            randomNumber = random.nextInt(100) + 1;
            playGame();
        } else {
            showFinalScore();
        }
    }

    private void playGame() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Number Guessing Game - Round " + round);
        dialog.setHeaderText("Guess a number between 1 and 100 (Attempts left: " + MAX_ATTEMPTS + "):");
        dialog.setContentText("Enter your guess:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(guess -> {
            try {
                int userGuess = Integer.parseInt(guess);
                if (userGuess < 1 || userGuess > 100) {
                    showAlert("Invalid Guess", "Please enter a number between 1 and 100.");
                    playGame();
                } else if (userGuess == randomNumber) {
                    int score = MAX_ATTEMPTS - dialog.getEditor().getText().length();
                    totalScore += score;
                    showAlert("Congratulations!", "You guessed the number in " + score + " attempts! Score for this round: " + score);
                    round++;
                    startNewRound();
                } else if (dialog.getEditor().getText().length() >= MAX_ATTEMPTS) {
                    showAlert("Out of Attempts", "You've used all your attempts. The number was " + randomNumber);
                    round++;
                    startNewRound();
                } else if (userGuess < randomNumber) {
                    showAlert("Too Low", "The number is higher than " + userGuess);
                    playGame();
                } else {
                    showAlert("Too High", "The number is lower than " + userGuess);
                    playGame();
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number.");
                playGame();
            }
        });
    }

    private void showFinalScore() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Final Score");
        alert.setContentText("Your total score after " + (NUM_ROUNDS - 1) + " rounds: " + totalScore);
        alert.showAndWait();
        System.exit(0);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}