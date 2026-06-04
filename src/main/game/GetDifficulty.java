package main.game;

public class GetDifficulty {

    private static GameDifficulty difficulty = GameDifficulty.EASY;

    public static void setDifficulty(GameDifficulty diff) {
        System.out.println("\nSet Difficulty to: " + diff);
        difficulty = diff;
    }

    public static GameDifficulty getDifficulty() {
        return difficulty;
    }
}
