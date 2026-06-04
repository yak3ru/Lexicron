package main.game.screens;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class AssetsLoader {

    // load sprites
    public static BufferedImage[] loadFrames(String path, int index) {
        BufferedImage[] frames = new BufferedImage[index];

        try {
            for (int i = 0; i < index; i++) {
                frames[i] = ImageIO.read(AssetsLoader.class.getResource(path + i + ".png"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return frames;
    }

    // load single image
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(AssetsLoader.class.getResource(path));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static Font loadFont(String path, float size) {
        try {
            InputStream is = AssetsLoader.class.getResourceAsStream(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (Exception e) {
            System.out.println("Font Load Error: " + e.getMessage());
            return new Font("Arial", Font.PLAIN, 30);
        }
    }

    // sfx
    public static Clip loadSfx(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(AssetsLoader.class.getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (Exception e) {
            System.out.println("SFX Load Error: " + e.getMessage());
            return null;
        }
    }

    // music
    public static Clip loadMusic(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(
                    AssetsLoader.class.getResource(path)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (Exception e) {
            System.out.println("Music Load Error: " + e.getMessage());
            return null;
        }
    }
}
