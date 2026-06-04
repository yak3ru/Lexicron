package main.entities;

import main.game.screens.AssetsLoader;

import javax.swing.*;
import java.awt.*;

public class Player {

    // player attributes
    private int health;
    private int normalAttack = 15;
    private double critChance = 0.2;

    // ANIMATIONS
        // idle
    private Image idle1, idle2;
    private int idleFrame = 0;
    private int frameCounter = 0;

        // attack
    private Image[] attackFramesArr = new Image[6];
    private boolean isAttacking = false;
    private int attackFrame = 0;
    private int attackCounter = 0;

    public Player() {
        this.health = 150;
        loadImages();
    }

    public int attack() {
        isAttacking = true;     // start animation
        attackFrame = 0;        // reset to first frame
        attackCounter = 0;

        int damage = normalAttack;

        if (Math.random() < critChance) {
            damage *= 1.2;
            System.out.println("Player gives Critical hit");
        }
        return damage;
    }

    public Image getCurrentFrame() {

        if (isAttacking) {
            attackCounter++;

            if (attackCounter >= 3) {   // controls speed
                attackFrame++;
                attackCounter = 0;
            }

            // Finished all frames?
            if (attackFrame >= attackFramesArr.length) {
                isAttacking = false;    // go back to idle
                attackFrame = 0;
                return idle1;           // first idle frame
            }

            return attackFramesArr[attackFrame];
        }

        frameCounter++;
        if (frameCounter >= 20) {
            idleFrame = 1 - idleFrame;
            frameCounter = 0;
        }
        if (idleFrame == 0) {
            return idle1;
        } else {
            return idle2;
        }
    }

    private void loadImages() {
        idle1 = AssetsLoader.loadImage("/main/assets/images/player/player_idle/idle_frame0.png");
        idle2 = AssetsLoader.loadImage("/main/assets/images/player/player_idle/idle_frame1.png");
        attackFramesArr = AssetsLoader.loadFrames("/main/assets/images/player/player_attack1/attack_frame", 7);
    }

    // getters
    public int getHealth() {
        return health;
    }

    public void setHealth(int hp) {
        health = hp;
    }
}
