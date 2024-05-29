package com.flappybird.ui;

import com.flappybird.game.GameManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverScreen {

    public static void display(Stage primaryStage, GameManager gameManager) {
        Pane gameOverPane = new Pane();

        // 添加贴图
        Image gameOverImage = new Image("/images/gameover.png");
        ImageView gameOverImageView = new ImageView(gameOverImage);
        gameOverImageView.setFitWidth(800);
        gameOverImageView.setFitHeight(600);
        gameOverPane.getChildren().add(gameOverImageView);

        Text highScoreText = new Text("最高分数为: " + gameManager.getHighScore());
        highScoreText.setX(320); // 可能需要调整位置以适应游戏界面
        highScoreText.setY(350);
        highScoreText.setStyle("-fx-fill: red; -fx-font-size: 20px;"); // 设置字体颜色和大小

        // 创建显示分数的文本
        Text scoreText = new Text("本次的分数为: " + gameManager.getScore());
        scoreText.setLayoutX(320); // 调整X坐标
        scoreText.setLayoutY(400); // 在“Game Over”文本下方显示分数
        scoreText.setStyle("-fx-fill: orange; -fx-font-size: 20px;"); // 设置字体颜色和大小

        Button restartButton = new Button("Restart Game");
        restartButton.setLayoutX(350); // 根据界面需要调整位置
        restartButton.setLayoutY(450);
        restartButton.setOnAction(e -> {
            gameManager.startNewGame();
            GameScene.showGameScene();  // 使用 GameScene 提供的方法重启游戏
        });

        Button backToMenuButton = new Button("Back to Main Menu");
        backToMenuButton.setLayoutX(330); // 调整位置
        backToMenuButton.setLayoutY(500); // 调整位置，确保不与其他按钮重叠
        backToMenuButton.setOnAction(e -> {
            StartScreen.display(primaryStage, gameManager);
        });

        gameOverPane.getChildren().addAll(highScoreText, scoreText, restartButton, backToMenuButton);

        Scene gameOverScene = new Scene(gameOverPane, 800, 600);
        Platform.runLater(() -> primaryStage.setScene(gameOverScene));
    }

}