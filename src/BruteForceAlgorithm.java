import java.util.Date;

public class BruteForceAlgorithm implements EightQueenAlgorithm {

    private final Chessboard chessboard;
    private long startTimeMs;

    public BruteForceAlgorithm(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    @Override
    public Chessboard solve() {
        this.startTimeMs = new Date().getTime();
        try {
            solve(0, 0);
        } catch (Exception e) {
            return chessboard;
        }
        return chessboard;
    }

    private int placedQueens = 0;
    private void solve(final int x, final int y) throws Exception {
        // Put a queen on the current position
        if (chessboard.tryPlaceQueen(x, y)) {
            placedQueens++;
        }
        // All queens are sets then a solution may be present
        if (placedQueens >= 8) {
            if (chessboard.isSolved()) {
                throw new Exception();
            }
        } else {

            // Recursive call to the next position
            final int nextX = (x + 1) % 8;
            // Switch to the next line
            if (0 == nextX) {

                // End of the chessboard check
                if (y + 1 < 8) {
                    solve(nextX, y + 1);
                }
            } else {
                solve(nextX, y);
            }
        }

        // Remove the queen on the current position

        chessboard.removeQueen(x, y);
        placedQueens--;

        // Recursive call to the next position
        final int nextX = (x + 1) % 8;
        // Switch to the next line
        if (0 == nextX) {

            // End of the chessboard check
            if (y + 1 < 8) {
                solve(nextX, y + 1);
            }
        } else {
            solve(nextX, y);
        }
    }

    @Override
    public long getElapsedTime() {
        return new Date().getTime() - startTimeMs;
    }
}
