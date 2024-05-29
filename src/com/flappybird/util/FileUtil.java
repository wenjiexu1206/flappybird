package com.flappybird.util;

import java.io.*;

public class FileUtil {
    private static final String HIGH_SCORE_FILE = "highscore.txt";

    public static int loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("High score file not found. Assuming no high score.");
            saveHighScore(0);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Failed to read high score. Assuming no high score.");
        }
        return 0;
    }

    public static void saveHighScore(int highScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.out.println("Failed to save high score.");
        }
    }
}