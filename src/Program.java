import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var chessboard = new Chessboard();
        var factory = new EightQueenAlgorithmFactory(chessboard);

        EightQueenAlgorithm algorithmExecuter;
        String algorithm;

        do {
            System.out.println("Which algorithm do you want to solve the eightQueenProblem with?");
            algorithm = scanner.nextLine();
            algorithmExecuter = factory.getAlgorithm(algorithm);
            if (algorithmExecuter == null) System.out.println("Invalid algorithm.");
        } while (algorithmExecuter == null);

        System.out.println("Cooking...");

        chessboard = algorithmExecuter.solve();

        System.out.println(chessboard);

        if (chessboard.isSolved()) {
            System.out.println(algorithm + " took " + algorithmExecuter.getElapsedTime() + "ms and attempts to solve the eight queen problem.");
        } else {
            System.out.println(algorithm + " took " + algorithmExecuter.getElapsedTime() + "ms and did not solve the eight queen problem.");
        }
    }
}
