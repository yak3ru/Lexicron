package main.entities;

import main.game.*;


import java.awt.*;

public class SpawnWord {

    public String text;
    public int x, y;
    private int fallSpeed = 5;

    private long spawnTime;
    private long pausedAt = 0;
    private long pausedDuration = 0;
    private boolean isPaused = false;
    GameDifficulty diff = GetDifficulty.getDifficulty();

    private long lifetime = 0; // miliseconds duration

    public SpawnWord(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.spawnTime = System.currentTimeMillis();

        switch (diff) {
            case EASY:
            lifetime = 5000;
            break;
            case MEDIUM:
            lifetime = 4000;
            break;
            case CODING:
            lifetime = 4500;
            break;
        }
    }

    public void update() {
        if (!isPaused) {
            y += fallSpeed;
        }
    }

    public void pause() {
        if (!isPaused) {
            isPaused = true;
            pausedAt = System.currentTimeMillis();
        }
    }

    public void resume() {
        if (isPaused) {
            isPaused = false;
            pausedDuration += (System.currentTimeMillis() - pausedAt);
        }
    }

    private long getElapsed() {
        if (isPaused) {
            return pausedAt - spawnTime - pausedDuration;
        } else {
            return System.currentTimeMillis() - spawnTime - pausedDuration;
        }
    }

    public boolean isExpired() {
        return getElapsed() >= lifetime;
    }

    public Color getColor() {
        long elapsed = getElapsed();

        if (elapsed < lifetime * 0.5) {
            return Color.WHITE;
        }
        if (elapsed < lifetime * 0.8) {
            return Color.YELLOW;
        }
        return Color.RED;
    }
}
