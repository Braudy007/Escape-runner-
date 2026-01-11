package com.fastescape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ObstacleManager {
    private ArrayList<Rectangle> obstacles;
    private Random rand;
    private int spawnTimer;

    public ObstacleManager() {
        this.obstacles = new ArrayList<>();
        this.rand = new Random();
        this.spawnTimer = 0;
    }

    public void update(int speed, int level, int groundY, int panelWidth) {
        // Move obstacles
        for (Rectangle obs : obstacles) {
            obs.x -= speed;
        }

        // Remove off-screen obstacles
        obstacles.removeIf(obs -> obs.x + obs.width < 0);

        // Spawn new obstacles
        spawnTimer++;

        // Dynamic spawn rate: starts at 100, decreases by 5 per level, capped at
        // minimum 40
        int spawnRate = Math.max(40, 100 - (level * 5));

        if (spawnTimer > spawnRate) {
            if (rand.nextInt(100) < 15) { // Slightly increased chance
                spawnObstacle(level, groundY, panelWidth);
                spawnTimer = 0;
            } else if (spawnTimer > spawnRate + 40) {
                spawnObstacle(level, groundY, panelWidth);
                spawnTimer = 0;
            }
        }
    }

    private void spawnObstacle(int level, int groundY, int panelWidth) {
        int width = 30;
        int height = 40;
        int x = panelWidth + rand.nextInt(200); // Base spawn x, should ideally be passed in or relative to panel width
        // For now, let's assume a minimum width or spawn further out.
        // Better: Spawn at "Screen Width + Buffer".
        // But we don't have screen width here easily without changing signature more.
        // Let's assume 1000 is safe or pass screenWidth too.

        // Reverting to basic logic but using passed groundY

        // Dynamic variation based on level
        if (level > 1) {
            if (rand.nextBoolean()) {
                // Taller or wider based on level RNG
                if (rand.nextBoolean()) {
                    height = 50 + rand.nextInt(Math.min(level * 5, 50)); // Height grows with level
                } else {
                    width = 40 + rand.nextInt(Math.min(level * 5, 40)); // Width grows with level
                }
            }
        }

        // Cap sizes reasonably
        if (height > 90)
            height = 90;
        if (width > 80)
            width = 80;

        int y = groundY - height;
        obstacles.add(new Rectangle(x, y, width, height));
    }

    public boolean checkCollision(Rectangle playerBounds) {
        for (Rectangle obs : obstacles) {
            if (obs.intersects(playerBounds)) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        for (Rectangle obs : obstacles) {
            g.fillRect(obs.x, obs.y, obs.width, obs.height);
        }
    }

    public void reset() {
        obstacles.clear();
        spawnTimer = 0;
    }
}
