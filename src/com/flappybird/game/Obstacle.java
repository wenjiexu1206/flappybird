package com.flappybird.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Obstacle {
    private Rectangle topObstacle, bottomObstacle;
    public static final int WIDTH = 25;
    private static final int GAP_HEIGHT = 150;
    private static final int SCENE_HEIGHT = 600; // 假设场景高度为600
    private boolean passed = false;

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Obstacle(Pane gamePane, double positionX) {
        double heightTop = 50 + Math.random() * 200; // 障碍物上部分的高度
        double heightBottom = SCENE_HEIGHT - (heightTop + GAP_HEIGHT); // 障碍物下部分的高度

        topObstacle = new Rectangle(positionX, 0, WIDTH, heightTop);
        bottomObstacle = new Rectangle(positionX, heightTop + GAP_HEIGHT, WIDTH, heightBottom);

        topObstacle.setFill(Color.GREEN);
        bottomObstacle.setFill(Color.GREEN);

        gamePane.getChildren().addAll(topObstacle, bottomObstacle);
    }

    public void move(double speed) {
        topObstacle.setX(topObstacle.getX() - speed);
        bottomObstacle.setX(bottomObstacle.getX() - speed);
    }

    public Rectangle getTopObstacle() {
        return topObstacle;
    }

    public Rectangle getBottomObstacle() {
        return bottomObstacle;
    }
}
