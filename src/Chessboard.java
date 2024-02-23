import java.util.ArrayList;
import java.util.HashMap;

public class Chessboard {
    private final HashMap<Integer, ArrayList<Square>> rows;
    private final HashMap<Integer, ArrayList<Square>> columns;
    private int queensPlaced = 0;
    private static int timesCopied;
    private static int timesCheckedForValid;
    private static int timesPlacedQueen;
    private static int timesRemovedQueen;
    private static int timesCheckedForQueenLocation;

    public Chessboard copy() {
        timesCopied++;
        var board = new Chessboard();

        for (var row : rows.values()) {
            for (var square : row) {
                if (square.hasQueen()) board.tryPlaceQueen(square.getX(), square.getY());
            }
        }

        return board;
    }

    public Chessboard() {
        rows = new HashMap<>();
        columns = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            var row = new ArrayList<Square>();

            for (int j = 0; j < 8; j++) {
                row.add(new Square(i, j));
            }

            rows.put(i, row);
        }

        for (int i = 0; i < 8; i++) {
            var column = new ArrayList<Square>();

            for (int j = 0; j < 8; j++) {
                var row = rows.get(j);
                column.add(row.get(i));
            }

            columns.put(i, column);
        }
    }

    private int conflictingQueensInRow(ArrayList<Square> row) {
        Square queen = null;
        int conflictingQueens = 0;

        for (var square : row) {
            if (!square.hasQueen()) continue;

            if (queen == null) {
                queen = square;
                continue;
            }
            if (conflictingQueens == 0) conflictingQueens++;
            else conflictingQueens += conflictingQueens;
        }

        return conflictingQueens;
    }

    public int conflictingQueens() {
        if (queensPlaced == 0 || queensPlaced == 1) return 0;
        int conflictingQueens = 0;

        for (var row : rows.values()) {
            conflictingQueens += conflictingQueensInRow(row);
        }

        for (var column : columns.values()) {
            conflictingQueens += conflictingQueensInRow(column);
        }

        int diagonalConflicts = 0;
        for (int diagonal = 0; diagonal < 8 * 2 - 1; diagonal++) {
            boolean usedDiagonal = false;
            for (int y = 0; y < 8; y++) {
                final int x = diagonal - y;
                if (x >= 0 && x < 8 && rows.get(x).get(y).hasQueen()) {
                    if (usedDiagonal) {
                        if (diagonalConflicts == 0) diagonalConflicts++;
                        else diagonalConflicts += diagonalConflicts;
                    }
                    usedDiagonal = true;
                }
            }
        }

        int reverseDiagonalConflicts = 0;
        // Check if 2 queens are on the same ascending diagonal
        for (int diagonal = 0; diagonal < 8 * 2 - 1; diagonal++) {
            boolean usedDiagonal = false;
            for (int y = 0; y < 8; y++) {
                final int x = diagonal - 8 + 1 + y;
                if (x >= 0 && x < 8 && rows.get(x).get(y).hasQueen()) {
                    if (usedDiagonal) {
                        if (reverseDiagonalConflicts == 0) reverseDiagonalConflicts++;
                        else reverseDiagonalConflicts += reverseDiagonalConflicts;
                    }
                    usedDiagonal = true;
                }
            }
        }

        conflictingQueens += diagonalConflicts + reverseDiagonalConflicts;
        return conflictingQueens;
    }

    public boolean isValidBoard() {
        timesCheckedForValid++;

        if (queensPlaced == 0 || queensPlaced == 1) return true;

        for (var row : rows.values()) {
            if (conflictingQueensInRow(row) > 0) return false;
        }

        for (var column : columns.values()) {
            if (conflictingQueensInRow(column) > 0) return false;
        }

        // Check if 2 queens are on the same descending diagonal
        for (int diagonal = 0; diagonal < 8 * 2 - 1; diagonal++) {
            boolean usedDiagonal = false;
            for (int y = 0; y < 8; y++) {
                final int x = diagonal - y;
                if (x >= 0 && x < 8 && rows.get(x).get(y).hasQueen()) {
                    if (usedDiagonal) {
                        return false;
                    }
                    usedDiagonal = true;
                }
            }
        }

        // Check if 2 queens are on the same ascending diagonal
        for (int diagonal = 0; diagonal < 8 * 2 - 1; diagonal++) {
            boolean usedDiagonal = false;
            for (int y = 0; y < 8; y++) {
                final int x = diagonal - 8 + 1 + y;
                if (x >= 0 && x < 8 && rows.get(x).get(y).hasQueen()) {
                    if (usedDiagonal) {
                        return false;
                    }
                    usedDiagonal = true;
                }
            }
        }

        return true;
    }

    public boolean tryPlaceQueen(int x, int y) {
        timesPlacedQueen++;
        var row = rows.get(x);
        var succeeded = row.get(y).tryPlaceQueen();

        if (succeeded) queensPlaced++;

        return succeeded;
    }

    public boolean isSolved() {
        return queensPlaced == 8 && isValidBoard();
    }

    public boolean hasQueen(int x, int y) {
        timesCheckedForQueenLocation++;
        var row = rows.get(x);
        return row.get(y).hasQueen();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Times copied: ").append(timesCopied).append('\n');
        sb.append("Times queen placed: ").append(timesPlacedQueen).append('\n');
        sb.append("Times queen removed: ").append(timesRemovedQueen).append('\n');
        sb.append("Times checked for board state: ").append(timesCheckedForValid).append('\n');
        sb.append("Times checked for queen location: ").append(timesCheckedForQueenLocation).append('\n');

        for (var row : rows.values()) {
            for (var square : row) {
                if (square.hasQueen()) {
                    sb.append("[X] ");
                } else {
                    sb.append("[ ] ");
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public void removeQueen(int x, int y) {
        timesRemovedQueen++;
        var row = rows.get(x);
        var square = row.get(y);
        if (square.hasQueen()) queensPlaced--;
        square.removeQueen();
    }
}
