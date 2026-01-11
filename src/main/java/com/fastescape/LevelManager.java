package com.fastescape;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class LevelManager {
    private int currentLevel;
    private int score;
    private int highScore;
    private final int LEVEL_THRESHOLD = 500; // New level every 500 distance

    public LevelManager() {
        this.currentLevel = 1;
        this.score = 0;
        loadHighScore();
    }

    private void loadHighScore() {
        try {
            java.io.File file = new java.io.File("highscore.txt");
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                if (scanner.hasNextInt()) {
                    this.highScore = scanner.nextInt();
                }
                scanner.close();
            } else {
                this.highScore = 0;
            }
        } catch (Exception e) {
            System.err.println("Failed to load high score: " + e.getMessage());
            this.highScore = 0;
        }
    }

    public void saveHighScore() {
        if (score > highScore) {
            highScore = score;
            try {
                java.io.PrintWriter writer = new java.io.PrintWriter("highscore.txt");
                writer.println(highScore);
                writer.close();
            } catch (Exception e) {
                System.err.println("Failed to save high score: " + e.getMessage());
            }
        }
    }

    public void update(int speed) {
        score += speed;
        currentLevel = 1 + (score / LEVEL_THRESHOLD);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return Math.max(score, highScore);
    }

    public void draw(Graphics g, int width) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Level: " + currentLevel, 20, 30);
        g.drawString("Score: " + score, 20, 60);
        g.drawString("High Score: " + getHighScore(), width - 200, 30);
    }

    public void reset() {
        saveHighScore(); // Try saving on reset just in case
        currentLevel = 1;
        score = 0;
        loadHighScore(); // Refresh from file
    }
}
