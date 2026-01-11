package com.fastescape;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private ObstacleManager obstacleManager;
    private LevelManager levelManager;

    private enum GameState {
        MENU, PLAYING, OPTIONS, CREDITS, GAME_OVER
    };

    private GameState currentState;
    private int menuSelection = 0; // 0: Play, 1: Options, 2: Credits
    private boolean soundEnabled = true; // Placeholder for sound option
    private boolean isPaused;

    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.player = new Player(100);
        this.obstacleManager = new ObstacleManager();
        this.levelManager = new LevelManager();

        this.currentState = GameState.MENU;
        this.isPaused = false;

        // Timer for game loop (approx 60 FPS)
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        if (width == 0)
            width = GAME_WIDTH;
        if (height == 0)
            height = GAME_HEIGHT;

        int groundY = height - 100;

        // Draw Background
        g.setColor(new Color(135, 206, 235)); // Sky Blue
        g.fillRect(0, 0, width, height);

        // Draw Ground
        g.setColor(new Color(34, 139, 34)); // Forest Green
        g.fillRect(0, groundY, width, 100);

        // State Machine Drawing
        switch (currentState) {
            case MENU:
                drawMenu(g, width, height);
                break;
            case OPTIONS:
                drawOptions(g, width, height);
                break;
            case CREDITS:
                drawCredits(g, width, height);
                break;
            case PLAYING:
            case GAME_OVER:
                // Draw game world
                player.draw(g);
                obstacleManager.draw(g);
                levelManager.draw(g, width);

                if (currentState == GameState.GAME_OVER) {
                    drawCenteredString(g, "GAME OVER - Press R to Restart", width, height);
                } else if (isPaused) {
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(0, 0, width, height);
                    g.setColor(Color.WHITE);
                    drawCenteredString(g, "PAUSED", width, height);
                }
                break;
        }
    }

    private void drawMenu(Graphics g, int width, int height) {
        // Title
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
        String title = "FAST ESCAPE";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - titleWidth) / 2, height / 2 - 100);

        // Menu Options
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
        String[] options = { "JOUER", "OPTIONS", "CRÉDITS" };

        for (int i = 0; i < options.length; i++) {
            if (i == menuSelection) {
                g.setColor(Color.RED); // Selected
                g.drawString("> " + options[i] + " <",
                        (width - g.getFontMetrics().stringWidth("> " + options[i] + " <")) / 2, height / 2 + (i * 50));
            } else {
                g.setColor(Color.BLACK);
                g.drawString(options[i], (width - g.getFontMetrics().stringWidth(options[i])) / 2,
                        height / 2 + (i * 50));
            }
        }
    }

    private void drawOptions(Graphics g, int width, int height) {
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));
        String title = "OPTIONS";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title)) / 2, 100);

        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 30));
        String soundText = "Son: " + (soundEnabled ? "ON" : "OFF");
        if (menuSelection == 0)
            g.setColor(Color.RED);
        else
            g.setColor(Color.BLACK);

        g.drawString(soundText, (width - g.getFontMetrics().stringWidth(soundText)) / 2, height / 2);

        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 20));
        String back = "Appuyez sur ESPACE pour changer, ECHAP pour retour";
        g.drawString(back, (width - g.getFontMetrics().stringWidth(back)) / 2, height - 100);
    }

    private void drawCredits(Graphics g, int width, int height) {
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));
        String title = "CRÉDITS";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title)) / 2, 100);

        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 25));
        String creator = "Créateur : Braud Bryan";
        g.drawString(creator, (width - g.getFontMetrics().stringWidth(creator)) / 2, height / 2);

        String ai = "Assistant AI : Antigravity";
        g.drawString(ai, (width - g.getFontMetrics().stringWidth(ai)) / 2, height / 2 + 40);

        String back = "Appuyez sur ECHAP pour retour";
        g.drawString(back, (width - g.getFontMetrics().stringWidth(back)) / 2, height - 100);
    }

    private void drawCenteredString(Graphics g, String text, int width, int height) {
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
        int stringWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (width - stringWidth) / 2, height / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState != GameState.PLAYING) {
            repaint();
            return;
        }

        if (currentState == GameState.PLAYING && !isPaused) {
            int width = getWidth();
            if (width == 0)
                width = GAME_WIDTH;
            int height = getHeight();
            if (height == 0)
                height = GAME_HEIGHT;
            int groundY = height - 100;

            // Logic Update
            player.update(groundY);

            int level = levelManager.getCurrentLevel();
            int currentSpeed = (level == 1) ? 6 : 6 + (level - 1);

            levelManager.update(currentSpeed);
            obstacleManager.update(currentSpeed, level, groundY, width);

            if (obstacleManager.checkCollision(player.getBounds())) {
                currentState = GameState.GAME_OVER;
                levelManager.saveHighScore();
            }
        }

        repaint();
    }

    private void restartGame() {
        currentState = GameState.PLAYING;
        isPaused = false;
        player = new Player(100);
        obstacleManager.reset();
        levelManager.reset();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (currentState == GameState.MENU) {
                if (key == KeyEvent.VK_DOWN) {
                    menuSelection++;
                    if (menuSelection > 2)
                        menuSelection = 0;
                }
                if (key == KeyEvent.VK_UP) {
                    menuSelection--;
                    if (menuSelection < 0)
                        menuSelection = 2;
                }
                if (key == KeyEvent.VK_ENTER) {
                    if (menuSelection == 0) {
                        currentState = GameState.PLAYING;
                    } else if (menuSelection == 1) {
                        currentState = GameState.OPTIONS; // Changed state
                        menuSelection = 0;
                    } else if (menuSelection == 2) {
                        currentState = GameState.CREDITS; // Changed state
                    }
                }
                return;
            }

            if (currentState == GameState.OPTIONS) {
                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
                    soundEnabled = !soundEnabled; // Toggle sound
                }
                if (key == KeyEvent.VK_ESCAPE) {
                    currentState = GameState.MENU;
                    menuSelection = 1;
                }
                return;
            }

            if (currentState == GameState.CREDITS) {
                if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_ENTER) {
                    currentState = GameState.MENU;
                    menuSelection = 2;
                }
                return;
            }

            if (currentState == GameState.PLAYING || currentState == GameState.GAME_OVER) {
                if (key == KeyEvent.VK_P) {
                    if (currentState == GameState.PLAYING) {
                        isPaused = !isPaused;
                    }
                }

                if (key == KeyEvent.VK_SPACE) {
                    if (currentState == GameState.PLAYING && !isPaused) {
                        player.jump();
                    }
                }

                if (key == KeyEvent.VK_R) {
                    if (currentState == GameState.GAME_OVER) {
                        restartGame();
                    }
                }

                if (key == KeyEvent.VK_ESCAPE) {
                    currentState = GameState.MENU;
                    isPaused = true;
                    menuSelection = 0;
                }
            }
        }
    }
}
