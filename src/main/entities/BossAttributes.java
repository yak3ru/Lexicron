package main.entities;

import main.game.screens.AssetsLoader;

import javax.swing.*;
import java.awt.*;

public class BossAttributes {

    //  base boss attributes
    private int health;
    int damage = 10;
    private double critChance = 0.3;

    // ANIMATIONS
        // idle animation
    private Image boss_idle, boss_idle1;
    private int idleFrame = 0;
    private int idleCounter = 0;

        // attack animation
    private Image[] bossAttackArr = new Image[6];
    private boolean bossAttacking = false;
    private int attackFrame = 0;
    private int attackCounter = 0;

    public BossAttributes() {
        this.health = 250;
        loadImages();
    }

    public int normalAttack() {
        // attack anim
        bossAttacking = true;
        attackFrame = 0;
        attackCounter = 0;

        if (Math.random() < critChance) {
            damage *= 1.2;
            System.out.println("Boss gives Critical hit");
        }
        return damage;
    }

    // get health
    public int getHealth() {
        return health;
    }

    // damage health
    public void setHealth(int hp) {
        health = hp;
    }

    public Image getBossFrames() {
        // idle
        idleCounter++;
        if (idleCounter >= 50) { // speed
            idleFrame = 1 - idleFrame;
            idleCounter = 0;
        }

        // atack
        if (bossAttacking) {
            attackCounter++;

            if (attackCounter >= 3) {   // control speed
                attackFrame++;
                attackCounter = 0;
            }

            // Finished all frames?
            if (attackFrame >= bossAttackArr.length) {
                bossAttacking = false;
                attackFrame = 0;
                return boss_idle;   // go back to idle
            }
            return bossAttackArr[attackFrame];
        }

        if (idleFrame == 0) {
            return boss_idle;
        } else {
            return boss_idle1;
        }
    }

    private void loadImages() {
        boss_idle = AssetsLoader.loadImage("/main/assets/images/boss/boss_idle/boss_idle0.png");
        boss_idle1 = AssetsLoader.loadImage("/main/assets/images/boss/boss_idle/boss_idle1.png");
        bossAttackArr = AssetsLoader.loadFrames("/main/assets/images/boss/boss_attack/bossAttack", 6);
    }
}
