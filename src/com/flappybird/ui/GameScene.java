package com.flappybird.ui;

import com.flappybird.game.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static com.flappybird.game.GameManager.audioManager;

public class GameScene extends Application {
    private static GameManager gameManager;
    private static Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        GameScene.primaryStage = primaryStage;
        primaryStage.setTitle("Flappy Bird");
        gameManager = new GameManager(new Pane());

        // 设置游戏结束处理
        gameManager.setGameOverHandler(() -> {
            System.out.println("Game Over");
            GameOverScreen.display(primaryStage, gameManager);
        });

        // 显示开始界面
        StartScreen.display(primaryStage, gameManager);
    }
    public static void showGameScene() {
        gameManager.resetGame();  // 在显示场景前重置游戏状态
        Scene gameScene = createGameScene(gameManager);
        primaryStage.setScene(gameScene);
        primaryStage.show();
        gameManager.startGame();
    }
    private static Scene createGameScene(GameManager gameManager) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        // 设置键盘事件监听
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.SPACE && !gameManager.isPaused()) {
                gameManager.jumpBird();
            } else if (code == KeyCode.P) {
                if (gameManager.isPaused()) {
                    gameManager.resumeGame();
                } else {
                    gameManager.pauseGame();
                    PauseMenu.display(primaryStage, gameManager, audioManager);
                }
            }
        });
        gameManager.setGamePane(root);
        return scene;

    }

    public static Scene getGameScene(Stage primaryStage, GameManager gameManager) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Flappy Bird");

        // 设置键盘事件监听
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.SPACE) {
                if (!gameManager.isPaused()) {
                    gameManager.jumpBird();
                }
            } else if (code == KeyCode.P) {
                if (gameManager.isPaused()) {
                    gameManager.resumeGame(); // 如果游戏已暂停，恢复游戏
                } else {
                    gameManager.pauseGame(); // 如果游戏未暂停，暂停游戏
                    PauseMenu.display(primaryStage, gameManager, audioManager); // 显示暂停菜单
                }
            }
        });

        gameManager.setGamePane(root);
        return scene;
    }

}