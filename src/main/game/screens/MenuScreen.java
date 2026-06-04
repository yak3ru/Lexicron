package main.game.screens;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import main.game.*;

public class MenuScreen extends JPanel {

    private Rectangle playBox = new Rectangle();
    private Rectangle settingsBox = new Rectangle();
    private Rectangle quitBox = new Rectangle();
    private MouseAdapter mouseAdapter;
    private GameLoop game;
    private BufferedImage[] bgFrames = new BufferedImage[6];
    private BufferedImage logo2d;
    private int frameCounter = 0;
    private Font customFont;

    public MenuScreen(GameLoop game) {
        this.game = game;

        // loaders
        loadImages();
        loadFont();

        // switch background frames
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

        System.out.println("Menu Screen Active");
    }

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

    private void handleClick(int x, int y) {

        if (playBox.contains(x, y)) {
            game.switchState(GameState.PLAY);
        }

        if (settingsBox.contains(x, y)) {
            game.switchState(GameState.TUTORIAL);
        }

        if (quitBox.contains(x, y)) {
            System.exit(0);
        }
    }

    // load all images
    private void loadImages() {
            bgFrames = AssetsLoader.loadFrames("/main/assets/images/background_menu/bg_frame", 6);
            logo2d = AssetsLoader.loadImage("/main/assets/images/logos/2d_logo.png");
        }

    private void loadFont() {
        customFont = AssetsLoader.loadFont("/main/assets/fonts/DePixelHalbfett.ttf", 25);
        }

    public void draw(Graphics g) {

        // draw animated background
        g.drawImage(bgFrames[frameCounter], 0, 0, 1280, 720, null);

        // draw logo
        g.drawImage(logo2d, 100, -100, 500, 500 ,null);

        g.setFont(customFont);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();

        String play = "Play";
        String settings = "Tutorial";
        String quit = "Quit";

        int playX = (1280 - fm.stringWidth(play)) / 8;
        int settingsX = (1320 - fm.stringWidth(settings)) / 8;
        int quitX = (1280 - fm.stringWidth(quit)) / 8;

        int playY = 350;
        int settingsY = 450;
        int quitY = 550;

        g.drawString(play, playX, playY);
        g.drawString(settings, settingsX, settingsY);
        g.drawString(quit, quitX, quitY);

        playBox.setBounds(playX, playY - fm.getAscent(), fm.stringWidth(play), fm.getHeight());
        settingsBox.setBounds(settingsX, settingsY - fm.getAscent(), fm.stringWidth(settings), fm.getHeight());
        quitBox.setBounds(quitX, quitY - fm.getAscent(), fm.stringWidth(quit), fm.getHeight());

        g.setFont(customFont.deriveFont(5f));
        g.drawString("Music Credits: xDeviruchi, MB Music", 1130, 700);
    }
}

