package com.flappybird.game;

import com.flappybird.util.AudioManager;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Iterator;
import com.flappybird.util.FileUtil;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameManager {
    private Text scoreText;
    private Text birdCountText;  // 用于显示小鸟数量的文本
    private Bird bird;
    private Pane gamePane;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private AnimationTimer gameLoop;
    private double timeSinceLastObstacle = 0;
    private int score = 0;
    private boolean isPaused = false; // 游戏是否暂停的标志
    private boolean inInitialFlight = true;
    private long initialFlightStartTime = 0;
    private static final long INITIAL_FLIGHT_DURATION = 1_000_000_000; // 1秒，单位纳秒
    private GameOverHandler gameOverHandler;
    private int highScore;  // 添加变量来存储最高分
    private static final double OBSTACLE_FREQUENCY = 1.5;  // 固定生成障碍物的时间间隔
    private double obstacleSpeed = 2.0;  // 障碍物的初始移动速度
    private static final double SPEED_INCREMENT = 0.1;  // 每次速度增加的量
    private int lastSpeedIncreaseScore = 0;  // 上次增速时的分数
    private ArrayList<ImageView> birdQueue = new ArrayList<>(); // 存储小鸟队列
    private ArrayList<ImageView> newBirds = new ArrayList<>();
    private double timeSinceLastNewBird = 0;
    private static final double NEW_BIRD_INTERVAL = 7.0; // 每x秒生成一个新小鸟
    private boolean newBirdsEnabled = false;
    private double timeSinceGameStart = 0;
    private int birdCount = 0;  // 新增小鸟计数器
    private boolean gameInvincible = false; // 添加无敌时间标志
    private long lastCollisionTime = 0; // 上次碰撞时间
    private ImageView background1, background2;
    private static double BACKGROUND_SPEED = 1.0;  // 背景滚动速度
    public static AudioManager audioManager = new AudioManager();

    public void startNewGame() {
        startGame();  // 直接使用 startGame 来启动游戏
    }

    public GameManager(Pane gamePane) {
        this.gamePane = gamePane;
        highScore = FileUtil.loadHighScore();  // 加载最高分
        initializeGame();
    }
    public int getHighScore() {
        return highScore;
    }

    public boolean isPaused() {
        return isPaused;
    }
    public interface GameOverHandler {
        void onGameOver();
    }
    // 获取当前游戏分数的方法
    public int getScore() {
        return score;
    }

    public void setGameOverHandler(GameOverHandler handler) {
        this.gameOverHandler = handler;
    }

    public void startGame() {
        audioManager.playBackgroundMusic();
        inInitialFlight = true;
        initialFlightStartTime = 0;
        resetGame(); // 重置游戏状态
    }
    public void pauseGame() {
        if (!isPaused) {
            audioManager.pauseBackgroundMusic(); // 暂停音乐
            isPaused = true;
            gameLoop.stop(); // 停止游戏循环
        }
    }
    public void resumeGame() {
        if (isPaused) {
            audioManager.playBackgroundMusic(); // 恢复音乐
            isPaused = false;
            // 确保背景图像在恢复时重新添加
            if (!gamePane.getChildren().contains(background1)) {
                gamePane.getChildren().add(0, background1); // 确保背景始终在最底层
            }
            if (!gamePane.getChildren().contains(background2)) {
                gamePane.getChildren().add(1, background2); // 确保第二个背景图像紧随其后
            }

            // 重新添加所有被清除的元素
            gamePane.getChildren().addAll(newBirds);
            gamePane.getChildren().addAll(birdQueue);
            gamePane.getChildren().add(birdCountText);
            if (gameLoop != null) {
                gameLoop.start();  // 重新启动游戏循环
            }
        }
    }


    public void resetGame() {
        // 停止当前游戏循环（如果正在运行）
        if (gameLoop != null) {
            gameLoop.stop();
        }
        score = 0;
        obstacles.clear();
        gamePane.getChildren().clear();
        birdQueue.clear();  // 清空小鸟队列
        newBirds.clear();   // 清除新生成的小鸟列表
        birdCount = 1;  // 开始游戏时重置 birdCount 为1，因为场上有一只初始小鸟
        obstacleSpeed = 2.0;  // 重置障碍物速度为初始值
        lastSpeedIncreaseScore = 0;  // 重置速度增加的分数计数器
        // 重置新小鸟生成的计时器
        timeSinceLastNewBird = 0;
        timeSinceGameStart = 0;
        newBirdsEnabled = false; // 重新启动前不启用新小鸟生成
        timeSinceLastObstacle = 0;  // 重置障碍物生成计时
        initializeGame(); // 重新初始化游戏组件
        bird.reset(); // 重置小鸟的位置和速度
        inInitialFlight = true;
        initialFlightStartTime = System.nanoTime();  // 重新设置初始飞行的开始时间
        isPaused = false; // 确保游戏不在暂停状态
        setupGameLoop(); // 可能不需要重新设置，确保逻辑正确
        gameLoop.start();
    }
    public void setGamePane(Pane gamePane) {
        this.gamePane = gamePane;
        // 由于游戏面板被更新，我们可能需要重新添加所有的游戏元素到新的面板中
        gamePane.getChildren().clear(); // 清除新面板上的所有元素
        gamePane.getChildren().add(scoreText); // 重新添加分数文本到面板
        gamePane.getChildren().add(bird.getBirdView()); // 重新添加小鸟到面板
        // 对于障碍物，我们需要确保它们也被重新添加到面板上，这可能需要遍历现有障碍物列表
        for (Obstacle obstacle : obstacles) {
            gamePane.getChildren().addAll(obstacle.getTopObstacle(), obstacle.getBottomObstacle());
        }
    }
    private void initializeGame() {
        // 加载背景图像
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.png"));
        background1 = new ImageView(backgroundImage);
        background2 = new ImageView(backgroundImage);

        // 设置图像视图的大小以匹配游戏窗口的宽度和高度
        background1.setFitWidth(gamePane.getWidth());
        background1.setFitHeight(gamePane.getHeight());
        background2.setFitWidth(gamePane.getWidth());
        background2.setFitHeight(gamePane.getHeight());

        // 保持图像纵横比
        background1.setPreserveRatio(true);
        background2.setPreserveRatio(true);

        // 设置初始位置
        background1.setX(0);
        background1.setY(0);
        background2.setX(background1.getLayoutBounds().getWidth());  // 紧接在第一个背景的右侧
        background2.setY(0);

        // 添加到游戏面板
        gamePane.getChildren().addAll(background1, background2);
        scoreText = new Text("Score: 0");
        scoreText.setX(10);
        scoreText.setY(20);
        scoreText.setFill(Color.RED);
        scoreText.setFont(new Font(20));
        gamePane.getChildren().add(scoreText);
        birdCountText = new Text("Birds: " + (birdQueue.size()+1));  // 初始化文本
        birdCountText.setX(gamePane.getWidth() - 100);  // 设置位置为右上角
        birdCountText.setY(20);  // 顶部边距
        birdCountText.setFill(Color.RED);
        birdCountText.setFont(new Font(20));
        //birdCountText.setFill(Color.WHITE);  // 设置字体颜色
        gamePane.getChildren().add(birdCountText);  // 添加到游戏面板
        bird = new Bird(gamePane);
    }

    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now){
                if (!isPaused) {
                    updateGame();  // 调用updateGame方法来更新游戏状态
                    if (checkCollision() && !gameInvincible) {
                        handleCollision();
                    }
                }
            }
        };
        gameLoop.start();
    }

    private void updateGame() {
        double deltaTime = 0.016; // 假设每帧约16ms
        timeSinceGameStart += deltaTime;
        if (timeSinceGameStart > 10 && !newBirdsEnabled) {
            newBirdsEnabled = true;
            timeSinceLastNewBird = NEW_BIRD_INTERVAL; // 立即生成第一个新小鸟
        }
        if (newBirdsEnabled) {
            timeSinceLastNewBird += deltaTime;
            if (timeSinceLastNewBird >= NEW_BIRD_INTERVAL) {
                generateNewBird();
                timeSinceLastNewBird = 0;
            }
        }

        if (inInitialFlight) {
            if (initialFlightStartTime == 0) {
                initialFlightStartTime = System.nanoTime();
            } else if (System.nanoTime() - initialFlightStartTime > INITIAL_FLIGHT_DURATION) {
                inInitialFlight = false;
            } else {
                bird.slightRise();
            }
        }
        timeSinceLastObstacle += 0.016;
        if (timeSinceLastObstacle >= OBSTACLE_FREQUENCY) {
            obstacles.add(new Obstacle(gamePane, gamePane.getWidth()));
            timeSinceLastObstacle = 0;
        }
        // 更新背景位置
        background1.setX(background1.getX() - BACKGROUND_SPEED);
        background2.setX(background2.getX() - BACKGROUND_SPEED);

        // 重置背景位置
        if (background1.getX() <= -background1.getFitWidth()) {
            background1.setX(background2.getX() + background2.getFitWidth());
        }
        if (background2.getX() <= -background2.getFitWidth()) {
            background2.setX(background1.getX() + background1.getFitWidth());
        }
        updateBirdPositions(); // 更新队列中小鸟的位置
        bird.update();
        updateObstacles();
        updateNewBirds();
        checkForBirdPickup(); // 检查并处理小鸟拾取
        long timeNow = System.nanoTime();
        if (gameInvincible && (timeNow - lastCollisionTime > 1_000_000_000L)) {
            gameInvincible = false; // 1秒后取消无敌状态
        }
    }
    private void attachBirdToQueue(ImageView newBird) {
        //System.out.println("Attaching new bird to queue.");
        double spacing = 30; // 新的间距值
        double lastBirdX = (birdQueue.isEmpty() ? bird.getBirdView().getX() : birdQueue.get(birdQueue.size() - 1).getX()) - spacing;
        double lastBirdY = (birdQueue.isEmpty() ? bird.getBirdView().getY() : birdQueue.get(birdQueue.size() - 1).getY());

        newBird.setX(lastBirdX);
        newBird.setY(lastBirdY);
        birdQueue.add(newBird);
        birdCount++;
        birdCountText.setText("Birds: " + (birdQueue.size() + 1));  // 更新小鸟数量显示

        // 确保 newBird 只在还未添加到 gamePane 时添加
        if (!gamePane.getChildren().contains(newBird)) {
            gamePane.getChildren().add(newBird);
        }
    }
    private void updateBirdPositions() {
        double spacing = 30; // 使用同样的间距值
        if (!birdQueue.isEmpty()) {
            ImageView previousBird = bird.getBirdView();
            for (ImageView birdInQueue : birdQueue) {
                double newX = previousBird.getX() - spacing;
                birdInQueue.setX(newX);
                birdInQueue.setY(previousBird.getY());
                previousBird = birdInQueue;
            }
        }
    }
    private void checkForBirdPickup() {
        Iterator<ImageView> it = newBirds.iterator();
        while (it.hasNext()) {
            ImageView newBird = it.next();
            if (bird.getBirdView().getBoundsInParent().intersects(newBird.getBoundsInParent())) {
                if (!birdQueue.contains(newBird)) { // 只有当newBird不在队列中时才处理拾取
                    attachBirdToQueue(newBird);
                    it.remove();
                }
            }
        }
    }

    private void generateNewBird() {
        double x, y;
        boolean valid;
        do {
            x = gamePane.getWidth() + 10; // 从屏幕右侧边界外生成
            y = Math.random() * gamePane.getHeight();
            valid = isPositionValid(x, y);
        } while (!valid);
        Image birdImage = new Image(getClass().getResourceAsStream("/images/2.png")); // 加载小鸟图像
        ImageView newBird = new ImageView(birdImage);
        newBird.setFitWidth(40);
        newBird.setFitHeight(40);
        newBird.setX(x);
        newBird.setY(y);
        if (isPositionValid(x, y)) {
            gamePane.getChildren().add(newBird);
            newBirds.add(newBird);
        }
    }

    private boolean isPositionValid(double x, double y) {
        Circle testBird = new Circle(x, y, 10);
        for (Obstacle obstacle : obstacles) {
            if (testBird.getBoundsInParent().intersects(obstacle.getTopObstacle().getBoundsInParent()) ||
                    testBird.getBoundsInParent().intersects(obstacle.getBottomObstacle().getBoundsInParent())) {
                return false;
            }
        }
        return true;
    }

    private void updateNewBirds() {
        Iterator<ImageView> it = newBirds.iterator();
        while (it.hasNext()) {
            ImageView bird = it.next();
            bird.setX(bird.getX() - obstacleSpeed); // 障碍物的速度
            if (bird.getX() < -10) { // 小鸟移出屏幕
                gamePane.getChildren().remove(bird);
                it.remove();
            }
        }
    }
    private void updateObstacles() {
        Iterator<Obstacle> it = obstacles.iterator();
        while (it.hasNext()) {
            Obstacle obstacle = it.next();
            obstacle.move(obstacleSpeed);
            if (!obstacle.isPassed() && obstacle.getTopObstacle().getX() + Obstacle.WIDTH < bird.getBirdView().getX()) {
                //score++;
                score += birdCount;  // 根据场上小鸟数量增加分数
                audioManager.playSoundEffect("point.mp3");
                obstacle.setPassed(true);
                scoreText.setText("Score: " + score);
                if (score - lastSpeedIncreaseScore >= 20) {
                    lastSpeedIncreaseScore = score;
                    obstacleSpeed += SPEED_INCREMENT;
                    BACKGROUND_SPEED += SPEED_INCREMENT;  // 同步增加背景速度
                }
                if (score > highScore) {
                    highScore = score;  // 更新最高分
                    FileUtil.saveHighScore(highScore);  // 保存新的最高分
                }
            }
            if (obstacle.getTopObstacle().getX() + Obstacle.WIDTH < 0) {
                gamePane.getChildren().removeAll(obstacle.getTopObstacle(), obstacle.getBottomObstacle());
                it.remove();
            }
        }
    }
    private boolean checkCollision() {
        if (!inInitialFlight) { // 只在初始飞行结束后开始碰撞检测
            double birdY = bird.getBirdView().getY();
            double birdHeight = bird.getBirdView().getFitHeight();
            if (birdY <= 0 || birdY + birdHeight >= gamePane.getHeight()) {
                return true;
            }
            for (Obstacle obstacle : obstacles) {
                if (bird.getBirdView().getBoundsInParent().intersects(obstacle.getTopObstacle().getBoundsInParent()) ||
                        bird.getBirdView().getBoundsInParent().intersects(obstacle.getBottomObstacle().getBoundsInParent())) {
                    return true;
                }
            }
        }
        return false; // 在初始飞行期间不检测碰撞
    }
    private void handleCollision() {
        audioManager.playSoundEffect("die.mp3");
        if (gameInvincible) {
            return; // 如果处于无敌状态，则直接返回，不处理碰撞
        }

        if (!birdQueue.isEmpty()) {
            // 从队列尾部移除小鸟，并更新视图
            ImageView oldBirdCircle = birdQueue.remove(birdQueue.size() - 1);
            birdCount--; // 小鸟计数减一
            gamePane.getChildren().remove(oldBirdCircle);
            birdCountText.setText("Birds: " + (birdQueue.size() + 1));  // 更新小鸟数量显示
            gameInvincible = true; // 启动无敌时间
            lastCollisionTime = System.nanoTime(); // 更新最后碰撞时间
        } else {
            handleGameOver(); // 如果队列为空，结束游戏
        }
    }
    private void handleGameOver() {
        gameLoop.stop(); // 停止游戏循环
        audioManager.stopBackgroundMusic();  // 停止背景音乐
        if (gameOverHandler != null) {
            gameOverHandler.onGameOver(); // 调用游戏结束处理器
        }
    }
    public void jumpBird() {
        audioManager.playSoundEffect("jump.mp3");
        bird.jump();
    }

}
