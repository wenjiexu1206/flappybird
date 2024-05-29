package com.flappybird.ui;

import com.flappybird.game.GameManager;
import com.flappybird.util.AudioManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PauseMenu {
    public static void display(Stage primaryStage, GameManager gameManager, AudioManager audioManager) {
        StackPane pausePane = new StackPane();
        Scene pauseScene = new Scene(pausePane, 800, 600);
        // 添加贴图
        Image pauseImage = new Image("/images/pause.png");
        ImageView pauseImageView = new ImageView(pauseImage);
        pauseImageView.setFitWidth(800);
        pauseImageView.setFitHeight(600);
        pausePane.getChildren().add(pauseImageView);

        VBox buttonContainer = new VBox(10);
        buttonContainer.setStyle("-fx-padding: 10; -fx-alignment: center;");

        Text pauseText = new Text("Game Paused. Press 'P' or 'space' to continue.");
        pauseText.setStyle("-fx-font-size: 20; -fx-fill: black;");
        buttonContainer.getChildren().add(pauseText);

        Button resumeButton = new Button("Resume Game");
        resumeButton.setOnAction(e -> {
            primaryStage.setScene(GameScene.getGameScene(primaryStage, gameManager));
            gameManager.resumeGame();
        });
        buttonContainer.getChildren().add(resumeButton);

        Button newGameButton = new Button("Start New Game");
        newGameButton.setOnAction(e -> {
            gameManager.startNewGame();
            GameScene.showGameScene();
        });
        buttonContainer.getChildren().add(newGameButton);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> SettingsDialog.showSettingsDialog(primaryStage, audioManager));
        buttonContainer.getChildren().add(settingsButton);

        Button backToMenuButton = new Button("Back to Main Menu");
        backToMenuButton.setOnAction(e -> {
            StartScreen.display(primaryStage, gameManager);
        });
        buttonContainer.getChildren().add(backToMenuButton);

        pausePane.getChildren().add(buttonContainer);

        pauseScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                primaryStage.setScene(GameScene.getGameScene(primaryStage, gameManager));
                gameManager.resumeGame();
            }
        });

        primaryStage.setScene(pauseScene);
    }
}