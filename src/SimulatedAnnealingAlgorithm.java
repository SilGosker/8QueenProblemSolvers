import java.util.Date;
import java.util.Random;

public class SimulatedAnnealingAlgorithm implements EightQueenAlgorithm {
    private Chessboard chessboard;
    private long startTimeMs;

    public SimulatedAnnealingAlgorithm(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    private Chessboard generateNeighbor(Chessboard chessboard, double temperature) {
        var random = new Random();

        int maxRowsMoved = (int) Math.ceil(temperature / 1000);
        for (int col = 0; col < 8; col++) {
            int oldRow = -1;
            for (int row = 0; row < 8; row++) {
                if (chessboard.hasQueen(row, col)) {
                    oldRow = row;
                    chessboard.removeQueen(row, col);
                }
            }

            int squaresMoved = random.nextInt(-maxRowsMoved, maxRowsMoved);

            int newRow = oldRow + squaresMoved;
            if (newRow < 0) newRow = 0;
            if (newRow > 7) newRow = 7;

            chessboard.tryPlaceQueen(newRow, col);
        }

        return chessboard;
    }

    void placeInitial() {
        var random = new Random();
        for (int col = 0; col < 8; col++) {
            chessboard.tryPlaceQueen(random.nextInt(8), col);
        }
    }

    @Override
    public Chessboard solve() {
        startTimeMs = new Date().getTime();

        placeInitial();
        var best = chessboard.copy();
        int tries = 0;

        while (!best.isSolved()) {
            tries++;
            double temperature = 4000;
            double coolingRate = .99;

            var current = best;

            for (double t = temperature; t >= 1; t *= coolingRate) {
                var neighbor = generateNeighbor(best.copy(), t);

                int bestEnergy = best.conflictingQueens();
                int neighborEnergy = neighbor.conflictingQueens();

                if (Math.random() < probability(bestEnergy, neighborEnergy, t)) {
                    current = neighbor;
                }

                if (neighborEnergy < bestEnergy) {
                    best = current.copy();
                    System.out.println("Found new best with energy " + bestEnergy + ": ");
                    System.out.println(best);
                }

                if (best.isSolved()) break;
            }
        }

        if (tries != 1) System.out.println("Cooked not 1 but " + tries + " times");
        return best;
    }

    public static double probability(double f1, double f2, double temp) {
        if (f2 < f1) return 1;
        return Math.exp((f1 - f2) / temp);
    }

    @Override
    public long getElapsedTime() {
        return new Date().getTime() - startTimeMs;
    }
}
