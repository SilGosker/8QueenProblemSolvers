public class EightQueenAlgorithmFactory {
    private final Chessboard chessboard;

    public EightQueenAlgorithmFactory(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public EightQueenAlgorithm getAlgorithm(String algorithm) {

        return switch (algorithm) {
            case "brute force" -> new BruteForceAlgorithm(chessboard);
            case "simulated annealing" -> new SimulatedAnnealingAlgorithm(chessboard);
            default -> null;
        };

    }
}
