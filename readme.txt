# Flappy Bird 项目

## 项目简介
这是一个基于Java和JavaFX的Flappy Bird游戏。该项目包括基本的游戏功能、碰撞检测、得分系统、音效处理以及用户界面的设计。

## 项目结构
C:.
├───src
│ ├───com
│ │ └───flappybird
│ │ ├───game
│ │ │ Bird.java
│ │ │ GameManager.java
│ │ │ Obstacle.java
│ │ │
│ │ ├───ui
│ │ │ GameOverScreen.java
│ │ │ GameScene.java
│ │ │ PauseMenu.java
│ │ │ StartScreen.java
│ │ │
│ │ └───util
│ │ AudioManager.java
│ │ FileUtil.java
│ │
│ └───main.java
└───resources
├───images
│ gameover.png
│ pause.png
│ start.png
│
└───sounds
background1.mp3
background2.mp3
die.mp3
jump.mp3
point.mp3

## 使用方法
1. 克隆或下载本项目。
2. 使用IDE（例如IntelliJ IDEA）打开项目。
3. 确保已安装JDK 17和JavaFX 21.0.2。
4. 配置项目以包含JavaFX库。请参考以下VM选项：
--module-path /path/to/javafx-sdk-21.0.2/lib --add-modules javafx.controls,javafx.fxml,javafx.media

5. 运行 `com.flappybird.Main` 类启动游戏。

## 功能和特点
- 障碍物生成与管理：自动生成障碍物，并进行管理。
- 碰撞检测：实现小鸟与障碍物的碰撞检测。
- 得分系统：实时更新得分。
- 音效处理：包括背景音乐和游戏音效。
- 用户界面：包含开始界面、游戏界面、暂停界面和结束界面。

## 创新之处
- 实现了小鸟队列机制，使得游戏更具挑战性。
- 引入了多种背景音轨，增强了游戏的体验。

## 待完善部分
- 增加更多的游戏关卡和障碍物类型。
- 优化游戏性能，确保在更多设备上流畅运行。

## 贡献
欢迎提交Pull Request以改进本项目。如果你发现任何问题，请提交Issue。

## 许可证
本项目基于MIT许可证。