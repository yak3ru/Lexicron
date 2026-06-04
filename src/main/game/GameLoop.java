package main.game;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import main.game.screens.*;
import main.words.WordLoader;

public class GameLoop extends JPanel {

    private MenuScreen menuScreen;
    private PlayScreen playScreen;
    private TutorialScreen tutorialScreen;
    private GameOverScreen gameOverScreen = null;
    private PlayGame playGameScreen = null;

    private MouseAdapter currentListener;
    private KeyListener currentKeyListener;

    private Clip menuMusic;

    public GameLoop() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.BLACK);

        System.out.println("Game Loop Active");

        WordLoader.loadWords();

        // create screens
        menuScreen = new MenuScreen(this);
        playScreen = new PlayScreen(this);
        tutorialScreen = new TutorialScreen(this);

        // start in menu screen
        switchState(GameState.MENU);

        menuMusic = AssetsLoader.loadMusic("/main/assets/sounds/music/xDeviruchi - The Final of The Fantasy.wav");

        if (menuMusic != null) {
            menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // loop start
    public void start() {
        new Timer(1000 / 60, e -> {
            update();
            repaint();
        }).start();
    }

    public void startMusic() {
        menuMusic.setMicrosecondPosition(0);
        menuMusic.start();
    }

    // switch screen and mouse listeners
    public void switchState(GameState newState) {

        // remove old listener
        if (currentListener != null) {
            removeMouseListener(currentListener);
        }

        if (currentKeyListener != null) {
            removeKeyListener(currentKeyListener);
        }

        GetSetState.setState(newState);

        switch (newState) {

            case MENU:
                currentListener = menuScreen.getMouseAdapter();
                break;

            case PLAY:
                currentListener = playScreen.getMouseAdapter();
                break;

            case PLAY_GAME:
                menuMusic.stop();
                playGameScreen = new PlayGame(this);

                currentListener = playGameScreen.getMouseAdapter();
                currentKeyListener = playGameScreen.getKeyListener();
                addKeyListener(currentKeyListener);
                break;

            case TUTORIAL:
                currentListener = tutorialScreen.getMouseAdapter();
                break;

            case GAME_OVER:
                gameOverScreen = new GameOverScreen(this);

                // spacebar
                currentKeyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        switchState(GameState.MENU);
                        gameOverScreen.stopMusic();
                        startMusic();
                        }
                    }
                };
                addKeyListener(currentKeyListener);
                break;
        }

        // new listener
        if (currentListener != null) {
            addMouseListener(currentListener);
        }
    }

    // gameover getter
    public GameOverScreen getGameOverScreen() {
        return gameOverScreen;
    }


    // screen update
    private void update() {
        switch (GetSetState.getState()) {
            case PLAY_GAME:
                playGameScreen.update();
                break;
            case TUTORIAL:
                tutorialScreen.update();
                break;
            case GAME_OVER:
                gameOverScreen.update();
                break;
        }
    }

    // screen drawing
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (GetSetState.getState()) {
            case MENU:
                menuScreen.draw(g);
                break;
            case PLAY:
                playScreen.draw(g);
                break;
            case PLAY_GAME:
                playGameScreen.draw(g);
                break;
            case TUTORIAL:
                tutorialScreen.draw(g);
                break;
            case GAME_OVER:
                gameOverScreen.draw(g);
                break;
        }
    }

}
