/*
Ryan Carlsmith
TSP
AI
10/30/22
 */

public class Runner {
    public static void main(String[] args) {
        String country = "";
        String alg = "";
        String start = "";
        int rnnGreed = 0;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') {
                arg = arg.substring(1);
                switch (arg) {
                    case "USA":
                        country = "USA";
                        break;
                    case "canada":
                        country = "canada";
                        break;
                    case "nn": //nearest neighbor
                        alg = "nn";
                        break;
                    case "rnn": //random nearest neighbor
                        alg = "rnn";
                        i++;
                        rnnGreed = Integer.parseInt(args[i]);
                        break;
                    case "greedy":
                        alg = "greedy";
                        break;
                    case "insertion":
                        alg = "insertion";
                        break;
                    case "randomRestartNN":
                        alg = "randomRestartNN";
                        break;
                    case "randomRestartRNN":
                        alg = "randomRestartRNN";
                        i++;
                        rnnGreed = Integer.parseInt(args[i]);
                        break;
                }
            } else {
                start = arg; //will first be set to file name, but at the end of arg parsing should be the last term, which is the starting State
            }
        }
        State startingState;
        if (country.equals("USA")) {
            startingState = States.find(start);
        } else {
            startingState = Canada.find(start);
        }
        Solver.solve(alg, country, startingState, rnnGreed);
    }
}
