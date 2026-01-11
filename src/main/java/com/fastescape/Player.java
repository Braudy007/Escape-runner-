package com.fastescape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player {
    private int x, y;
    private int width, height;
    private int yVelocity;
    private boolean isJumping;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -18; // Negative because Y decreases upwards
    private int groundY;

    public Player(int startX) {
        this.x = startX;
        this.width = 40;
        this.height = 60;
        this.y = 0; // Will be set in first update
        this.yVelocity = 0;
        this.isJumping = false;
    }

    public void update(int groundY) {
        this.groundY = groundY;

        // Init y if first run (or if fell through floor due to resize debug)
        if (y == 0)
            y = groundY - height;

        // Apply gravity
        if (y < groundY - height || yVelocity < 0) {
            yVelocity += GRAVITY;
            y += yVelocity;
        }

        // Check ground collision
        if (y >= groundY - height) {
            y = groundY - height;
            yVelocity = 0;
            isJumping = false;
        }
    }

    public void jump() {
        if (!isJumping) {
            yVelocity = JUMP_STRENGTH;
            isJumping = true;
        }
    }

    public void draw(Graphics g) {
        // Character Design: "Hollow Knight"-ish style
        // White Head with Horns
        g.setColor(Color.WHITE);
        g.fillOval(x, y, 40, 40); // Head circle

        // Horns (Polygon)
        int[] leftHornX = { x + 5, x + 5, x + 15 };
        int[] leftHornY = { y + 10, y - 20, y + 5 };
        g.fillPolygon(leftHornX, leftHornY, 3);

        int[] rightHornX = { x + 35, x + 35, x + 25 };
        int[] rightHornY = { y + 10, y - 20, y + 5 };
        g.fillPolygon(rightHornX, rightHornY, 3);

        // Eyes (Black Void)
        g.setColor(Color.BLACK);
        g.fillOval(x + 10, y + 15, 8, 12); // Left Eye
        g.fillOval(x + 22, y + 15, 8, 12); // Right Eye

        // Body (Black)
        g.setColor(Color.BLACK);
        g.fillOval(x + 5, y + 35, 30, 25); // Body

        // Red Shorts/Pants
        g.setColor(Color.RED);
        g.fillRect(x + 5, y + 50, 30, 10);

        // Arms/Legs (Simple lines)
        g.setColor(Color.BLACK);
        // Legs & Boots
        g.drawLine(x + 10, y + 60, x + 10, y + 70);
        g.drawLine(x + 30, y + 60, x + 30, y + 70);

        // Brown Boots
        g.setColor(new Color(139, 69, 19));
        g.fillOval(x + 5, y + 68, 12, 8);
        g.fillOval(x + 25, y + 68, 12, 8);

        // Sword (Yellow/Gold) in hand
        g.setColor(new Color(255, 215, 0)); // Gold
        // Handle
        g.fillRect(x + 35, y + 45, 20, 5); // Guard
        g.fillRect(x + 42, y + 45, 5, 35); // Blade pointing down (or up? let's make it up/diagonal)

        // Let's make a generic sword held out
        // Redrawing sword simpler:
        // Handle
        g.setColor(new Color(139, 69, 19)); // Brown Handle
        g.fillRect(x + 32, y + 45, 5, 10);

        g.setColor(new Color(255, 215, 0)); // Gold Blade
        g.fillRect(x + 30, y + 25, 9, 20); // Blade vertical
        g.fillRect(x + 28, y + 42, 13, 3); // Guard
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
