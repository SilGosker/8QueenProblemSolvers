import java.util.Date;
import java.util.Random;

public class SimulatedAnnealingAlgorithm implements EightQueenAlgorithm {
    private final Chessboard chessboard;
    private long startTimeMs;

    public SimulatedAnnealingAlgorithm(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    private Chessboard generateNeighbor(Chessboard chessboard, double temperature) {
        var random = new Random();

        int maxRowsMoved = (int) Math.ceil(temperature / 1000);

        for (int col = 0; col < 8; col++) { //each column contains a queen; so loop over them to change the positions of them.

            int oldRow = -1;
            for (int row = 0; row < 8; row++) { // find the queen in the current column.
                if (chessboard.hasQueen(row, col)) {
                    oldRow = row;
                    chessboard.removeQueen(row, col);
                }
            }

            int squaresMoved = random.nextInt(-maxRowsMoved, maxRowsMoved); //-maxRowsMoved to move up, maxRowsMoved to move down.

            int newRow = oldRow + squaresMoved;

            if (newRow > 7) newRow = newRow % 7; // if higher than board size, wrap around and start at bottom of board
            if (newRow < 0) newRow *= -1; // make always positive

            chessboard.tryPlaceQueen(newRow, col);
        }

        return chessboard;
    }

    void placeInitial() {
        var random = new Random();
        for (int col = 0; col < 8; col++) { //place a queen on each column, randomized.
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
            double temperature = 6000;
            double coolingRate = .75;

            for (double t = temperature; t >= 1; t *= coolingRate) {
                var current = best;

                var neighbor = generateNeighbor(best.copy(), t);

                int bestEnergy = best.conflictingQueens();
                int neighborEnergy = neighbor.conflictingQueens();

                if (Math.random() < probability(bestEnergy, neighborEnergy, t)) { // if the temperature is high or if the energy is better
                    current = neighbor;
                }

                if (neighborEnergy < bestEnergy) { // copy the best or the current into the best if the current's energy is better.
                    best = current.copy();
                    System.out.println("Found new best with energy " + bestEnergy + ":");

                    if (!best.isSolved())
                        System.out.println(best);

                }

                if (best.isSolved()) break; //if solved, break and return the best.
            }
        }

        if (tries != 1) System.out.println("Cooked not 1 but " + tries + " times");
        return best;
    }

    public static double probability(double energy1, double energy2, double temp) {
        if (energy2 < energy1) return 1;
        return Math.exp((energy1 - energy2) / temp); //magic math
    }

    @Override
    public long getElapsedTime() {
        return new Date().getTime() - startTimeMs;
    }
}
