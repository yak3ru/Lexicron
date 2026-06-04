package main.game.screens;

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.Clip;
import javax.swing.*;

import main.game.GameLoop;

public class GameOverScreen{

    private GameLoop game;
    private boolean win = false; // win lose

    // loaders
    private Font customFont;
    private Image bossDed;
    private Image playerDed;

    // strings
    private String winner = "YOU WIN!";
    private String loser = "YOU LOSE!";
    private boolean showText = true;
    private String menu = "Press spacebar to return to main menu";

    private int averageWPM;

    private Clip winMusic;
    private Clip loseMusic;

    public GameOverScreen(GameLoop game) {

        System.out.println("Gameover Screen Active");

        loadFont();
        loadImage();

        // blinking logic
        new Timer(500, e -> {
            showText = !showText;
        }).start();

        winMusic = AssetsLoader.loadMusic("/main/assets/sounds/music/xDeviruchi - Take some rest and eat some food!.wav");
        loseMusic = AssetsLoader.loadMusic("/main/assets/sounds/music/Game Over (8-Bit Music).wav");
    }

    public void playerWin() {
        win = true;
    }
    public void update() {}

    private void loadFont() {
        customFont = AssetsLoader.loadFont("/main/assets/fonts/DePixelHalbfett.ttf", 50);
    }

    private void loadImage() {
        bossDed = AssetsLoader.loadImage("/main/assets/images/boss/boss_ded/boss_ded.png");
        playerDed = AssetsLoader.loadImage("/main/assets/images/player/player_ded/player_ded.png");
    }

    public void setAverageWPM(int wpm) {
        this.averageWPM = wpm;
    }

    public void stopMusic() {
        winMusic.stop();
        loseMusic.stop();
    }

    public void draw(Graphics g) {
        g.setFont(customFont);
        g.setColor(Color.WHITE);

        FontMetrics fm = g.getFontMetrics();

        if (win) {
            if (winMusic != null) {
                winMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
            g.drawString(winner, (1280 - fm.stringWidth(winner)) / 2, 200 );
            g.drawImage(bossDed, (1280 - 400) / 2, 100, 400, 400, null);
        }
        else {
            if (loseMusic != null) {
                loseMusic.start();
            }
            g.drawString(loser, (1280 - fm.stringWidth(loser)) / 2, 200 );
            g.drawImage(playerDed, (1280 - 400) / 2, 100, 400, 400, null);
        }

        g.setFont(customFont.deriveFont(15f));
        FontMetrics fm2 = g.getFontMetrics();
        if (showText) {
            g.drawString(menu, (1280 - fm2.stringWidth(menu)) / 2, 550);
        }

        String average = "Average WPM: " + averageWPM;
        g.setColor(Color.YELLOW);
        g.drawString(average, (1280 - fm2.stringWidth(average)) / 2, 230 );
    }
}
