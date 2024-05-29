package com.flappybird.game;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bird {
    private double velocity = 0;
    private final double gravity = 0.35;
    private final Pane gamePane;
    private ImageView birdView;


    public Bird(Pane gamePane) {
        this.gamePane = gamePane;
        Image birdImage = new Image(getClass().getResourceAsStream("/images/1.png"));
        if (birdImage.isError()) {
            System.out.println("Error loading bird image: " + birdImage.getException());
            return;
        }
        birdView = new ImageView(birdImage);
        
        birdView.setFitWidth(40);
        birdView.setFitHeight(40);
        birdView.setX(250);
        birdView.setY(300);
        this.gamePane.getChildren().add(birdView);
    }
    public void jump() {
        velocity = -5.0;
    }
    public void slightRise() {
        // 根据需要调整上升速度和幅度
        double riseAmount = -3.5; // 轻微上升的速度值
        double newY = birdView.getY() + riseAmount; // 使用ImageView的getY()方法获取当前Y坐标
        newY = Math.max(newY, 0); // 防止小鸟飞出屏幕顶部，假设顶部Y坐标为0
        birdView.setY(newY); // 设置ImageView的新Y坐标
    }

    public void update() {
        velocity += gravity; // 重力影响下降速度
        double newY = birdView.getY() + velocity;

        // 防止小鸟飞出屏幕顶部或底部
        if (newY < 0) {
            newY = 0;
        } else if (newY + birdView.getFitHeight() > gamePane.getHeight()) {
            newY = gamePane.getHeight() - birdView.getFitHeight();
        }

        birdView.setY(newY);
    }
    public void reset() {
        birdView.setY(300); // 重置到初始位置
        birdView.setX(250);
        velocity = 0; // 速度重置
    }
    public ImageView getBirdView() {
        return birdView; // 返回小鸟的ImageView，可能用于其他操作或检查
    }
}