import javax.swing.*;

import main.game.GameLoop;
import main.words.WordLoader;

public class LexicronMain {
    public static void main(String[] args) {
        System.out.println();

        WordLoader.loadWords();

        JFrame frame = new JFrame("Lexicron 1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GameLoop game = new GameLoop();
        frame.add(game);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.requestFocusInWindow();

        game.start();

        System.out.println("\nWord Loader Test: " + WordLoader.getEasyWord());

    }
}
