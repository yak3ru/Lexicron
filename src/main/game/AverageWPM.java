package main.game;

public class AverageWPM {

    public static int getAverageWPM(long startTime, int wordsTyped) {
        long endTime = System.currentTimeMillis();
        long elapsedMillis = endTime - startTime;
        double minutes = elapsedMillis / 60000.0;

        if (minutes == 0) {
            return 0;
        }

        return (int) (wordsTyped / minutes);
    }

}
