package main.game.screens;

import java.awt.*;
import java.awt.event.*;
import main.game.GameLoop;
import main.game.GameState;

import javax.swing.*;

public class TutorialScreen {

    private MouseAdapter mouseAdapter;
    private GameLoop game;
    private Image tutorial;
    private Image tutorial2;
    private Font customFont;
    boolean frameSwap = true;

    private Rectangle backBox = new Rectangle();

    public TutorialScreen(GameLoop game) {
        this.game = game;
        System.out.println("Tutorial Screen Active");

        tutorial = AssetsLoader.loadImage("/main/assets/images/tutorial/tut_frame0.png");
        tutorial2 = AssetsLoader.loadImage("/main/assets/images/tutorial/tut_frame1.png");
        customFont = AssetsLoader.loadFont("/main/assets/fonts/DePixelHalbfett.ttf", 25);

        new Timer(500, e -> {
            frameSwap = !frameSwap;
        }).start();

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        };
    }

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

    private void handleClick(int x, int y) {
        if (backBox.contains(x, y)) {
            game.switchState(GameState.MENU);
        }
    }

    public void update() {

    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(customFont);
        FontMetrics fm = g.getFontMetrics();
        String backText = "BACK";

        if (frameSwap) {
            g.drawImage(tutorial, 0, 0, 1280, 720, null);
        } else {
            g.drawImage(tutorial2, 0, 0, 1280, 720, null);
        }

        g.drawString(backText, 70 , 100);
        backBox.setBounds(70, 100 - fm.getAscent(), fm.stringWidth(backText), fm.getHeight());
    }
}
