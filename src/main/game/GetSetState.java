package main.game;

public class GetSetState {

    private static GameState currentState = GameState.MENU;

    public static GameState getState() {
        return currentState;
    }

    public static void setState(GameState newState) {
        currentState = newState;
    }
}
