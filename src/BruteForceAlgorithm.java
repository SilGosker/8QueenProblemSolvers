import java.util.Arrays;
import java.util.Date;

public class BruteForceAlgorithm implements EightQueenAlgorithm {

    private final Chessboard chessboard;
    private long startTimeMs;

    public BruteForceAlgorithm(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    @Override
    public Chessboard solve() {
        startTimeMs = new Date().getTime();

        int[] queenLocations = new int[8];

        while (!chessboard.isSolved()) {

            for (int i = 0; i < queenLocations.length; i++) {
                var location = queenLocations[i];
                chessboard.tryPlaceQueen(location, i);
            }

            if (!chessboard.isSolved()) {

                for (int i = 0; i < queenLocations.length; i++) {
                    var location = queenLocations[i];
                    chessboard.removeQueen(location, i);
                }

                try {
                    incrementQueenCount(queenLocations);
                } catch (Exception ex) {
                    return chessboard;
                }
            }
        }

        return chessboard;
    }

    private void incrementQueenCount(int[] queenLocations) throws Exception {
        for (int i = queenLocations.length - 1; i >= 0; i--) {
            queenLocations[i]++;

            if (Arrays.stream(queenLocations).allMatch(queenLocation -> queenLocation >= 7)){
                throw new Exception("Maximum increment reached");
            }

            if (queenLocations[i] <= 7) {
                break;
            }

            queenLocations[i] = 0;
        }
    }

    @Override
    public long getElapsedTime() {
        return new Date().getTime() - startTimeMs;
    }
}
