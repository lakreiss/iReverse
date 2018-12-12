package gameplay;

import players.HardComputer;
import players.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class GeneticLearning {
    private PrintStream ps;
    private Random r;
    private int numberOfComputers;
    private int numberOfRounds;

    public GeneticLearning(int numberOfComputers, int numberOfRounds) throws FileNotFoundException {
        this.ps = new PrintStream(new File("round_robin/" + System.currentTimeMillis()));
        this.r = new Random();
        this.numberOfComputers = numberOfComputers;
        this.numberOfRounds = numberOfRounds;

        runNewLearningSet();
    }

    //Genetic Learning where we begin with two sets of weights
    public GeneticLearning(int numberOfComputers, int numberOfRounds, double[][] weights) throws FileNotFoundException {
        this.ps = new PrintStream(new File("round_robin/" + System.currentTimeMillis()));
        this.r = new Random();
        this.numberOfComputers = numberOfComputers;
        this.numberOfRounds = numberOfRounds;

        runNewLearningSet(weights);
    }

    private void runNewLearningSet() {
        double[][] computerWeights = makeComputerWeights(r,numberOfComputers);
        int counter = 0;

        while (counter < numberOfRounds) {
            counter++;
            double[] results = getResults(computerWeights);

            System.out.println();
            for (int i = 0; i < results.length; i++) {
                System.out.println("Computer " + i + ": " + results[i] + " points");
            }

            double bestResultScore = -1, secondBestResultScore = -1;
            int bestResult = -1, secondBestResult = -1;
            for (int i = 0; i < results.length; i++) {
                if (results[i] > bestResultScore) {
                    secondBestResultScore = bestResultScore;
                    secondBestResult = bestResult;
                    bestResult = i;
                    bestResultScore = results[i];
                } else if (results[i] > secondBestResultScore) {
                    secondBestResultScore = results[i];
                    secondBestResult = i;
                }
            }

            double[] bestResultWeights = computerWeights[bestResult];
            double[] secondBestResultWeights = computerWeights[secondBestResult];

            ps.println();

            ps.println("Computer 1: " + weightsToString(bestResultWeights));
            ps.println("Computer 2: " + weightsToString(secondBestResultWeights));

            computerWeights[0] = bestResultWeights;
            computerWeights[1] = secondBestResultWeights;

            for (int i = 2; i < numberOfComputers - 5; i++) {
                if (i % 2 == 0) {
                    computerWeights[i] = modify(bestResultWeights, r);
                } else {
                    computerWeights[i] = modify(secondBestResultWeights, r);
                }
            }

            double[][] newComputerWeights = makeComputerWeights(r, 5);

            for (int i = 0; i < 5; i++) {
                computerWeights[i + numberOfComputers - 5] = newComputerWeights[i];
            }
        }
    }

    private void runNewLearningSet(double[][] startingWeights) {
        double[][] computerWeights = new double[numberOfComputers][30];
        computerWeights[0] = startingWeights[0];
        computerWeights[1] = startingWeights[1];

        for (int i = 2; i < numberOfComputers - 5; i++) {
            if (i % 2 == 0) {
                computerWeights[i] = modify(startingWeights[0], r);
            } else {
                computerWeights[i] = modify(startingWeights[1], r);
            }
        }

        double[][] newComputerWeights = makeComputerWeights(r, 5);

        for (int i = 0; i < 5; i++) {
            computerWeights[i + numberOfComputers - 5] = newComputerWeights[i];
        }
        int counter = 0;

        while (counter < numberOfRounds) {
            counter++;
            double[] results = getResults(computerWeights);

            System.out.println();
            for (int i = 0; i < results.length; i++) {
                System.out.println("Computer " + i + ": " + results[i] + " points");
            }

            double bestResultScore = -1, secondBestResultScore = -1;
            int bestResult = -1, secondBestResult = -1;
            for (int i = 0; i < results.length; i++) {
                if (results[i] > bestResultScore) {
                    secondBestResultScore = bestResultScore;
                    secondBestResult = bestResult;
                    bestResult = i;
                    bestResultScore = results[i];
                } else if (results[i] > secondBestResultScore) {
                    secondBestResultScore = results[i];
                    secondBestResult = i;
                }
            }

            double[] bestResultWeights = computerWeights[bestResult];
            double[] secondBestResultWeights = computerWeights[secondBestResult];

            ps.println();

            ps.println("Computer 1: " + weightsToString(bestResultWeights));
            ps.println("Computer 2: " + weightsToString(secondBestResultWeights));

            computerWeights[0] = bestResultWeights;
            computerWeights[1] = secondBestResultWeights;

            for (int i = 2; i < numberOfComputers - 5; i++) {
                if (i % 2 == 0) {
                    computerWeights[i] = modify(bestResultWeights, r);
                } else {
                    computerWeights[i] = modify(secondBestResultWeights, r);
                }
            }

            newComputerWeights = makeComputerWeights(r, 5);

            for (int i = 0; i < 5; i++) {
                computerWeights[i + numberOfComputers - 5] = newComputerWeights[i];
            }
        }
    }

    private String weightsToString(double[] resultWeights) {
        String output = "[";
        output += String.format("%+.3f", resultWeights[0]);
        for (int i = 1; i < resultWeights.length; i++) {
            output += String.format(", %+.3f", resultWeights[i]);
        }
        output += "]";
        return output;
    }

    private double[] modify(double[] bestResultWeight, Random r) {
        double[] modifiedWeights = new double[bestResultWeight.length];
        for (int i = 0; i < bestResultWeight.length; i++) {
            if (r.nextDouble() < 0.25) {
                modifiedWeights[i] = bestResultWeight[i] + ((r.nextDouble() / 2) - 0.25);
            } else {
                modifiedWeights[i] = bestResultWeight[i];
            }
        }
        return modifiedWeights;
    }

    private double[][] makeComputerWeights(Random r, int n) {
        double[][] computerWeights = new double[n][30];
        for (double[] player : computerWeights) {
            for (int i = 0; i < 30; i++) {
                player[i] = (r.nextDouble() - 0.5) * 2;
            }
        }
        return computerWeights;
    }

    private double[] getResults(double[][] computerWeights) {
        double[] results = new double[computerWeights.length];
        for (int i = 0; i < computerWeights.length; i++) {
            for (int j = 0; j < computerWeights.length; j++) {
                if (i != j) {
                    Player p1 = new HardComputer(true, computerWeights[i]);
                    Player p2 = new HardComputer(false, computerWeights[j]);
                    Game game = new Game(GameType.AI_GAME, p1, p2);
                    Player winner = game.startGame();
                    if (winner == null) {
                        results[i] += 0.5;
                        results[j] += 0.5;
                    } else {
                        if (winner.equals(p1)) {
                            results[i] += 1;
                        } else {
                            results[j] += 1;
                        }
                    }
                }
            }
        }
        return results;
    }
}
