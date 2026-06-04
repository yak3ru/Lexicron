package main.game.screens;

import java.awt.*;
import java.awt.event.MouseAdapter;
import javax.sound.sampled.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.game.*;
import main.entities.*;
import main.words.WordLoader;

public class PlayGame {
    GameDifficulty diff = GetDifficulty.getDifficulty();
    private GameLoop game;
    private Player player;
    private BossAttributes boss;

    // gameplay stuff
    private ArrayList<SpawnWord> chosenWords = new ArrayList<>();
    private Timer spawnTimer;
    private String typedText = "";
    private int maxLetters = 10;
    private boolean isPaused = false; // pauseing

    private Rectangle menuBtn = new Rectangle((1280 - 500) / 2, 340, 500, 50);

    private BufferedImage gameBackground;
    private Image heart;
    private Font customFontInput;
    private Font customFontWords;
    private Font customFontStats;

    // for wpm
    private long strtTme;
    private long pausedTime = 0;
    private long pauseStartTime = 0;
    private int wrdsTypd = 0;
    private int AvrgeWpm;

    // music and sfx
    private Clip bgMusic;
    private Clip attackFx0;
    private Clip attackFx1;
    private Clip attackFx2;
    private Clip hitFx0;
    private Clip hitFx1;
    private Clip hitFx2;
    private Clip hitFx3;

    public PlayGame(GameLoop game) {
        System.out.println("Play Game Screen Active: " + diff + "\n");
        this.game = game;

        loadImage();
        loadFont();

        // create entities
        player = new Player();
        boss = new BossAttributes();

        // spawn words
        wordSpawn();

        // wpm
        strtTme = System.currentTimeMillis();

        // music and sound effects
        attackFx0 = AssetsLoader.loadSfx("/main/assets/sounds/sfx/swoosh0.wav");
        attackFx1 = AssetsLoader.loadSfx("/main/assets/sounds/sfx/swoosh1.wav");
        attackFx2 = AssetsLoader.loadSfx("/main/assets/sounds/sfx/swoosh2.wav");
        hitFx0 = AssetsLoader.loadSfx("/main/assets/sounds/sfx/hit_0.wav");
        hitFx1 = AssetsLoader.loadSfx("/main/assets/sounds/sfx/hit_0.wav");
        hitFx2 = AssetsLoader.loadSfx("/main/assets/sounds/sfx/hit_0.wav");
        hitFx3 = AssetsLoader.loadSfx("/main/assets/sounds/sfx/hit_0.wav");
        bgMusic = AssetsLoader.loadMusic("/main/assets/sounds/music/xDeviruchi - Prepare for Battle! .wav");

        if (bgMusic != null) {
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isPaused) return;

                int mx = e.getX();
                int my = e.getY();

                if (menuBtn.contains(mx, my)) {
                    game.switchState(GameState.MENU);
                    game.startMusic();
                }
            }
        };
    }

    public KeyAdapter getKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                PlayGame.this.keyPressed(e.getKeyChar());
            }
        };
    }

    // kb input
    public void keyPressed(char c)
    {
        if (!isPaused) {
            if (Character.isLetter(c)) {
                if (typedText.length() < maxLetters) {
                    typedText += c;
                }
            }


            if (c == '\b' && typedText.length() > 0) {
                typedText = typedText.substring(0, typedText.length() - 1);
            }

            // check word
            if (c == '\n') {  // enter
                checkWordMatch();
                typedText = "";
            }
        }

        if (c == 27) {  // esc pause
            togglePause();
        }
    }

    private void wordSpawn() {

        int delay = 0;

        switch (diff) {
            case EASY:
                delay = 1000;
                break;
            case MEDIUM:
                delay = 800;
                break;
            case CODING:
                delay = 800;
                break;
        }

        spawnTimer = new Timer(delay, e -> {

            // 4 words only
            if (chosenWords.size() >= 4) {
                return;
            }

            String word = "";

            switch (diff) {
                case EASY:
                    word = WordLoader.getEasyWord();
                    break;
                case MEDIUM:
                    word = WordLoader.getEasyWord();
                    break;
                case CODING:
                    word = WordLoader.getCodingWord();
                    break;
            }

            int randomX, randomY;

            do {
                if (Math.random() < 0.5) {
                    randomX = (int)(Math.random() * 300) + 60;    // word spawner left
                } else {
                    randomX = (int)(Math.random() * 300) + 750;   // word spawner right
                }

                randomY = (int)(Math.random() * 300) + 120;
            } while (wordBarrier(randomX, randomY));


            chosenWords.add(new SpawnWord(word, randomX, randomY));
        });

        spawnTimer.start();
    }

    private boolean wordBarrier(int x, int y) {
        for (SpawnWord wrd : chosenWords) {
            wrd.update();
            int dstncX = x - wrd.x;
            int dstncY = y - wrd.y;
            int disSquared = dstncX*dstncX + dstncY*dstncY;
            int rSquared = 150 * 150;

            if (disSquared < rSquared) {
                return true;
            }
        }
        return false;
    }

    public void update() {

        // word expiration
        for (int i = chosenWords.size() - 1; i >= 0; i--) {
            if (chosenWords.get(i).isExpired()) {
                chosenWords.remove(i);
                bossAttack();
                hitSfx();
            }
        }

        if (boss.getHealth() <= 0) { // boss ded
            int finalWpm = AverageWPM.getAverageWPM(strtTme + pausedTime, wrdsTypd);

            game.switchState(GameState.GAME_OVER);
            game.getGameOverScreen().setAverageWPM(finalWpm);
            game.getGameOverScreen().playerWin();
            bgMusic.stop();
        }

        if (player.getHealth() <= 0) { // player ded
            int finalWpm = AverageWPM.getAverageWPM(strtTme + pausedTime, wrdsTypd);

            game.switchState(GameState.GAME_OVER);
            game.getGameOverScreen().setAverageWPM(finalWpm);
            bgMusic.stop();
        }

    }

    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            spawnTimer.stop();
            bgMusic.stop();
            for (SpawnWord w : chosenWords) w.pause();
            pauseStartTime = System.currentTimeMillis();
        } else {
            pausedTime += System.currentTimeMillis() - pauseStartTime;
            spawnTimer.start();
            bgMusic.start();
            for (SpawnWord w : chosenWords) w.resume();
        }
    }

    // check word match
    private void checkWordMatch() {

        for (int i = chosenWords.size() - 1; i >= 0; i--) {
            if (chosenWords.get(i).text.equalsIgnoreCase(typedText)) {
                chosenWords.remove(i);
                playerAttack();
                attackSfx();
                wrdsTypd++;
                break;
            }
        }
    }

    public void playerAttack() {
        int playerDmg = player.attack();
        int bossHealth = boss.getHealth();
        boss.setHealth(bossHealth - playerDmg);
    }

    public void bossAttack() {
        int bossDmg = boss.normalAttack();
        int playerHealth = player.getHealth();
        player.setHealth(playerHealth - bossDmg);
    }

    private void loadImage() {
        gameBackground = AssetsLoader.loadImage("/main/assets/images/gameplay/game_bg.png");
        heart = AssetsLoader.loadImage("/main/assets/images/gameplay/heart.png");
    }

    private void loadFont() {
        customFontInput = AssetsLoader.loadFont("/main/assets/fonts/DePixelHalbfett.ttf", 30);
        customFontWords = AssetsLoader.loadFont("/main/assets/fonts/DePixelHalbfett.ttf", 20);
        customFontStats = AssetsLoader.loadFont("/main/assets/fonts/DePixelHalbfett.ttf", 15);
    }

    private void playSfx(Clip c) {
        if (c == null) return;
        c.stop();
        c.setFramePosition(0);
        c.start();
    }

    private void attackSfx() {
        int sfx = (int)(Math.random() * 3);

        if (sfx == 0) {
            playSfx(attackFx0);
        }
        else if (sfx == 1) {
            playSfx(attackFx1);
        }
        else if (sfx == 2) {
            playSfx(attackFx2);
        }
    }

    private void hitSfx() {
        int sfx = (int)(Math.random() * 4);

        if (sfx == 0) {
            playSfx(hitFx0);
        }
        else if (sfx == 1) {
            playSfx(hitFx1);
        }
        else if (sfx == 2) {
            playSfx(hitFx2);
        }
        else {
            playSfx(hitFx3);
        }
    }


    public void draw(Graphics g) {

        g.drawImage(gameBackground, 0, 0, 1280, 720, null);
        g.setFont(customFontStats);
        g.setColor(Color.WHITE);

        // WPM
        AvrgeWpm = AverageWPM.getAverageWPM(strtTme + pausedTime, wrdsTypd);
        if (!isPaused) {
            AvrgeWpm = AverageWPM.getAverageWPM(strtTme + pausedTime, wrdsTypd);
            g.drawString("WPM: " + AvrgeWpm, 1150, 680);
        }

        // boss heart and health
        g.drawImage(heart, (1280 - 90) / 2, 80, 35, 30, null);
        g.drawString("" + boss.getHealth(), 1280 / 2, 100);

        // player heart and health
        g.drawImage(heart, 490, 500, 35, 30, null);
        g.drawString("" + player.getHealth(), (1280 - 220) / 2, 520);

        g.drawImage(boss.getBossFrames(), (1280 - 400) / 2, 60, 400, 400, null);
        g.drawImage(player.getCurrentFrame(), (1280 - 250) / 2, 400, 250, 250, null);

        // custom word gen
        g.setFont(customFontWords);
        for (SpawnWord wrd : chosenWords) {
            g.setColor(wrd.getColor());
            g.drawString(wrd.text.toUpperCase(), wrd.x, wrd.y);
        }

        g.setFont(customFontInput);

        // bottom input box
        g.setColor(Color.WHITE);
        int boxWidth = 500;
        int boxX = (1280 - boxWidth) / 2;
        int boxY = 640;
        g.drawRect(boxX, boxY, boxWidth, 60);
        g.setColor(Color.BLACK);
        g.fillRect(boxX, boxY, boxWidth, 60);
        FontMetrics fm = g.getFontMetrics();

        int textX = boxX + (boxWidth - fm.stringWidth(typedText)) / 2;
        int textY = boxY + ((60 - fm.getHeight()) / 2) + fm.getAscent();
        g.setColor(Color.WHITE);
        g.drawString(typedText.toUpperCase(), textX, textY);

        // back to menu
        if (isPaused) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, 1280, 720);

            g.setColor(Color.WHITE);

            // DRAW MENU BTN
            String menu = "RETURN TO MENU";
            String unpause = "Press ESC to unpause";

            g.setColor(Color.YELLOW);
            g.drawString(menu, (1280 - fm.stringWidth(menu)) / 2, 380);
            g.setFont(customFontWords.deriveFont(12f));
            g.setColor(Color.WHITE);
            FontMetrics fm2 = g.getFontMetrics();
            g.drawString(unpause, (1280 - fm2.stringWidth(unpause)) / 2 , 450);
        }
    }
}
