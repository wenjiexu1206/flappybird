package com.flappybird.ui;

import com.flappybird.game.GameManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.flappybird.game.GameManager.audioManager;

public class StartScreen {
    public static void display(Stage primaryStage, GameManager gameManager) {
        Pane startPane = new Pane();
        Scene startScene = new Scene(startPane, 800, 600);
        // 添加贴图
        Image startImage = new Image("/images/start.png");
        ImageView startImageView = new ImageView(startImage);
        startImageView.setFitWidth(800);
        startImageView.setFitHeight(600);
        startPane.getChildren().add(startImageView);  // 先添加 ImageView

        Text highScoreText = new Text("High Score: " + gameManager.getHighScore());
        highScoreText.setX(10);  // 在左上角显示
        highScoreText.setY(20);
        highScoreText.setStyle("-fx-font-size: 16px; -fx-fill: white;");


        Button startGameButton = new Button("Start Game");
        startGameButton.setLayoutX(350);
        startGameButton.setLayoutY(250);
        startGameButton.setOnAction(e -> {
            gameManager.startNewGame();  // 重置游戏
            GameScene.showGameScene();  // 显示游戏界面
        });

        //添加"settings"按钮以供后续使用
        Button settingsButton = new Button("Settings");
        settingsButton.setLayoutX(350);
        settingsButton.setLayoutY(300);
        //新的功能
        settingsButton.setOnAction(e -> SettingsDialog.showSettingsDialog(primaryStage, audioManager));
        startPane.getChildren().addAll(startGameButton, settingsButton, highScoreText);

        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}