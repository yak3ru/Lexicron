package main.game.screens;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import main.game.*;

public class PlayScreen extends JPanel {

    private Rectangle easyBox = new Rectangle();
    private Rectangle mediumBox = new Rectangle();
    private Rectangle codeBox = new Rectangle();
    private Rectangle backBox = new Rectangle();

    private MouseAdapter mouseAdapter;
    private GameLoop game;

    private BufferedImage[] bgFrames = new BufferedImage[6];
    private BufferedImage logo2d;
    private int frameCounter = 0;

    private Font customFont;

    public PlayScreen(GameLoop game) {
        this.game = game;

        loadImages();
        loadFont();

        // switch frames
        new Timer(200, e -> {
            frameCounter = (frameCounter + 1) % 6;
            repaint();
        }).start();

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        };
        System.out.println("Play Screen Active");
    }

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

    private void handleClick(int x, int y) {
        if (easyBox.contains(x, y)) {
            GetDifficulty.setDifficulty(GameDifficulty.EASY);
            game.switchState(GameState.PLAY_GAME);
        }

        if (mediumBox.contains(x, y)) {
            GetDifficulty.setDifficulty(GameDifficulty.MEDIUM);
            game.switchState(GameState.PLAY_GAME);
        }

        if (codeBox.contains(x, y)) {
            GetDifficulty.setDifficulty(GameDifficulty.CODING);
            game.switchState(GameState.PLAY_GAME);
        }

        if (backBox.contains(x, y)) {
            game.switchState(GameState.MENU);
        }
    }

    private void loadImages() {
        bgFrames = AssetsLoader.loadFrames("/main/assets/images/background_menu/bg_frame", 6);
        logo2d = AssetsLoader.loadImage("/main/assets/images/logos/2d_logo.png");
    }

    private void loadFont() {
        customFont = AssetsLoader.loadFont("/main/assets/fonts/DePixelHalbfett.ttf", 25);
    }

    public void draw(Graphics g) {

        // draw sprites
        g.drawImage(bgFrames[frameCounter], 0, 0, 1280, 720, null);

        // draw logo
        g.drawImage(logo2d, 100, -100, 500, 500 ,null);
        g.setColor(Color.WHITE);

        g.setFont(customFont);
        FontMetrics fontm = g.getFontMetrics();

        String easy = "Easy";
        String medium = "Normal";
        String coding = "Coding";
        String back = "Back";

        int easyX = (1280 - fontm.stringWidth(easy)) / 8;
        int mediumX = (1320 - fontm.stringWidth(medium)) / 8;
        int codeX = (1280 - fontm.stringWidth(coding)) / 8;
        int backX = (1280 - fontm.stringWidth(back)) / 8;

        int easyY = 350;
        int mediumY = 450 - 30;
        int codeY = 550 - 60;
        int backY = 650 - 50;

        g.drawString(easy, easyX, easyY);
        g.drawString(medium, mediumX, mediumY);
        g.drawString(coding, codeX, codeY);
        g.drawString(back, backX, backY);

        easyBox.setBounds(easyX, easyY - fontm.getAscent(), fontm.stringWidth(easy), fontm.getHeight());
        mediumBox.setBounds(mediumX, mediumY - fontm.getAscent(), fontm.stringWidth(medium), fontm.getHeight());
        codeBox.setBounds(codeX, codeY - fontm.getAscent(), fontm.stringWidth(coding), fontm.getHeight());
        backBox.setBounds(backX, backY - fontm.getHeight(), fontm.stringWidth(back), fontm.getHeight());

        g.setFont(customFont.deriveFont(5f));
        g.drawString("Music Credits: xDeviruchi, MB Music", 1130, 700);
    }
}