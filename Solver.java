/*
Ryan Carlsmith
TSP
AI
10/30/22
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Solver {
    static String map = "";
    static DoublyLinkedList<State> mst;
    static double mstLen;
    static double upperBound;

    public static void solve(String alg, String mp, State start, int rnnGreed) {
        map = mp;
        DoublyLinkedList<State> tour = null;

        switch (alg.toLowerCase()) {
            case "nn":
                tour = nearestNeighbor(start);
                break;
            case "rnn":
                tour = randomNearestNeighbor(start, rnnGreed);
                break;
            case "greedy":
                tour = greedy(start);
                break;
            case "insertion":
                tour = insertion(start);
                break;
            case "randomrestartnn":
                tour = randomRestartNN(start);
                break;
            case "randomrestartrnn":
                tour = randomRestartRNN(start, rnnGreed);
                break;
        }
        if (start == null) {
            if (map.equals("USA")) {
                start = States.Maine;
            } else {
                start = Canada.BC;
            }
        }

//        if (!alg.equalsIgnoreCase("randomrestartnn") || !alg.equalsIgnoreCase("randomrestartrnn")) {
//            printTourAndLength(tour);
//        }
        DoublyLinkedList<State> optimizedTour = twoOpt(tour);
        System.out.println("\nOptimized tour:");
        printTourAndLength(optimizedTour);

        mst = mst(start);

        upperBound = mstLen * 1.5; //includes length back to start, then double
//        mstLen += mst[mst.length-1].capital().distance(start.capital());
        printBounds();
    }

    //starting with furthest insertion
    public static DoublyLinkedList<State> insertion(State start) {
        HashMap<State, Boolean> visited = new HashMap<>();
        visited.put(start, true);

        DoublyLinkedList<State> tour = new DoublyLinkedList<>();
        /*
        if (map.equals("USA")) {
            tour = new State[States.continentalStates.length+1];
        }
        else {
            tour = new State[Canada.provinces.length+1];
        }
        */

        //generate triangle
        tour.append(start);
        State add = getRandomUnseenState(visited, tour);
        visited.put(add, true);
        tour.append(add);
        State add2 = getRandomUnseenState(visited, tour);
        visited.put(add2, true);
        tour.append(add2);
        //start insertion loop
        State next = getRandomUnseenState(visited, tour);
        while (next != null) {

            //find what the random state is closest to
            double bestDist = Double.MAX_VALUE;
            double currentDist = 0;
            State addToEdge = null;
            for (State s : tour) {
                currentDist = s.capital().distance(next.capital());
                if (currentDist < bestDist) {
                    addToEdge = s;
                    bestDist = currentDist;
                }
            }
            tour.insertAfter(tour.locate(addToEdge), next);
            visited.put(next, true);
            next = getRandomUnseenState(visited, tour);
        }
        printTour(tour);
        return tour; //Replace with returning tour when finished

    }

    public static DoublyLinkedList<State> randomRestartNN(State start) {
        DoublyLinkedList<State> bestTour = null;
        double bestTourLen = Double.MAX_VALUE;
        State[] mapStates;
        if (map.equals("USA")) {
            mapStates = States.continentalStates;
        } else {
            mapStates = Canada.provinces;
        }
        DoublyLinkedList<State> tourFromGivenStart = nearestNeighbor(start);
        tourFromGivenStart = twoOpt(tourFromGivenStart);
        System.out.println("\nTour starting from given start state:");
        printTourAndLength(tourFromGivenStart);

        DoublyLinkedList<State> tour;
        for (State s : mapStates) {
            tour = nearestNeighbor(s);
            DoublyLinkedList<State> optimizedTour = twoOpt(tour);
            double tourLen = tourLength(optimizedTour);
            if (tourLen < bestTourLen) {
                bestTourLen = tourLen;
                bestTour = optimizedTour;
            }

        }
        return bestTour;
    }

    public static DoublyLinkedList<State> randomRestartRNN(State start, int howGreedy) {
        DoublyLinkedList<State> bestTour = null;
        double bestTourLen = Double.MAX_VALUE;
        State[] mapStates;
        if (map.equals("USA")) {
            mapStates = States.continentalStates;
        } else {
            mapStates = Canada.provinces;
        }
//        DoublyLinkedList<State> tourFromGivenStart = randomNearestNeighbor(start, howGreedy);
        DoublyLinkedList<State> tourFromGivenStart = insertion(start);
        tourFromGivenStart = twoOpt(tourFromGivenStart);
        System.out.println("Tour starting from given start state:");
        printTourAndLength(tourFromGivenStart);

        DoublyLinkedList<State> tour;
        for (State s : mapStates) {
            tour = nearestNeighbor(s);
            DoublyLinkedList<State> optimizedTour = twoOpt(tour);
            double tourLen = tourLength(optimizedTour);
            if (tourLen < bestTourLen) {
                bestTourLen = tourLen;
                bestTour = optimizedTour;
            }

        }
        return bestTour;
    }

    public static DoublyLinkedList<State> twoOpt(DoublyLinkedList<State> tour) { // TO DO
        //pseduo:
        /*
        switch two edges and reverse other pointers
        check to see if new tour is less than current
        return it
         */
        DoublyLinkedList<State> currentTour = tour;
        DoublyLinkedList<State> bestTour = tour;
        double bestDist = tourLength(tour);
        double currentDist = Double.MAX_VALUE;
        boolean isImprovement = true;
        while (isImprovement) {
            isImprovement = false;
            for (int i = 0; i < tour.size() - 2; i++) {
                for (int j = i + 1; j < tour.size() - 1; j++) {
                    currentTour = twoOptSwap(currentTour, i, j);
                    currentDist = tourLength(currentTour);
                    if (currentDist < bestDist) {
                        bestDist = currentDist;
                        bestTour = currentTour;
                        isImprovement = true;
                    }
                }
            }
        }
        return bestTour;
    }

    // ... i -> i+1 -> ... -> j -> j+1 -> ...
    // ... i -> j -> ... ->  i+1 -> j+1 -> ...

    //...b ->

    public static DoublyLinkedList<State> twoOptSwap(DoublyLinkedList<State> tour, int v1, int v2) {
//        State nodei = tour.get(v1);
        State nodeiPlus1 = tour.get(v1 + 1);

        State nodej = tour.get(v2);
//        State nodejPlus1 = tour.get(v2+1);

        State[] newTour = new State[tour.size()];
        int i = 0;
        for (State s : tour) { // Shallow copy on tour list
            newTour[i++] = s;
        }

//          tour = a b  e  d  c f
        //           v1      v2
        //newTour = a b c d e f
        newTour[v1 + 1] = nodej;
        newTour[v2] = nodeiPlus1;
//        newTour[v2+1] = nodejPlus1;
        DoublyLinkedList<State> temp = new DoublyLinkedList<>(newTour);
        return temp;
    }

    public static State getFarthestUnseenState(State current, HashMap<State, Boolean> visited) {
        double farthest = Double.MIN_VALUE;
        State farthestState = null;
        if (map.equals("USA")) {
            for (State s : States.continentalStates) {
                if ((!current.equals(s)) && (!visited.containsKey(s))) {
                    double dist = s.capital().distance((current.capital()));
                    if (dist > farthest) {
                        farthest = dist;
                        farthestState = s;
                    }
                }
            }
        } else {
            for (State s : Canada.provinces) {
                if ((!current.code().equals(s.code())) && (!visited.containsKey(s))) {
                    double dist = s.capital().distance((current.capital()));
                    if (dist > farthest) {
                        farthest = dist;
                        farthestState = s;
                    }
                }
            }
        }
        return farthestState;
    }

    public static State getNearestUnseenState(State current, HashMap<State, Boolean> visited) {
        double closest = Double.MAX_VALUE;
        State closestState = null;
        if (map.equals("USA")) {
            for (State s : States.continentalStates) {
                if ((!current.equals(s)) && (!visited.containsKey(s))) {
                    double dist = s.capital().distance((current.capital()));
                    if (dist < closest) {
                        closest = dist;
                        closestState = s;
                    }
                }
            }
        } else {
            for (State s : Canada.provinces) {
                if ((!current.code().equals(s.code())) && (!visited.containsKey(s))) {
                    double dist = s.capital().distance((current.capital()));
                    if (dist < closest) {
                        closest = dist;
                        closestState = s;
                    }
                }
            }
        }
        return closestState;
    }

    public static State getRandomUnseenState(HashMap<State, Boolean> seen, DoublyLinkedList<State> tour) {
        /*
        int r = -1;
        if (map.equals("USA")) {
            Random random = ThreadLocalRandom.current();
            r = random.nextInt(47);
            if (seen.containsKey(States.continentalStates[r])) {
                r++;
            }
            return States.continentalStates[r];
        } else {
            Random random = ThreadLocalRandom.current();
            r = random.nextInt(12);
            if (seen.containsKey(Canada.provinces[r])) {
                r++;
            }
            return Canada.provinces[r];
        }
        */
        if (tour.size() == 48){
            return null;
        }
        Random random = ThreadLocalRandom.current();
        int r = random.nextInt(48);
        if (seen.containsKey(States.continentalStates[r])){ //if we've already seen it
            while (true){
                if (seen.containsKey(States.continentalStates[r])){
                    r = random.nextInt(48);
                }
                else{
                    return States.continentalStates[r];
                }
            }
        }
        return States.continentalStates[r];
    }

    public static DoublyLinkedList<State> nearestNeighbor(State start) {
        DoublyLinkedList<State> bestTour = new DoublyLinkedList<>();
        double bestTourLength = Double.MAX_VALUE;
        if (start == null) {
            if (map.equals("USA")) {
                HashMap<State, Boolean> visited = new HashMap<>();

                for (State s : States.continentalStates) {
                    double bestDist = Double.MAX_VALUE;
                    DoublyLinkedList<State> bestPath = new DoublyLinkedList<>();
                    DoublyLinkedList<State> b = nearestNeighborHelper(s, visited);
                    if (tourLength(b) < bestDist) {
                        bestPath = b;
                    }
                    printTour(bestPath);
                }
            } else {
                HashMap<State, Boolean> visited = new HashMap<>();
                for (State s : Canada.provinces) {
                    double bestDist = Double.MAX_VALUE;
                    DoublyLinkedList<State> bestPath = null;
                    DoublyLinkedList<State> b = nearestNeighborHelper(s, visited);
                    if (tourLength(b) < bestDist) {
                        bestPath = b;
                    }
                    printTour(bestPath);
                }

            }
        }
        HashMap<State, Boolean> visited = new HashMap<>(); //effectively clears visited map for each run
        //loop around next line to call for all starting
        DoublyLinkedList<State> tour = nearestNeighborHelper(start, visited);

        double tourLen = tourLength(tour);
        if (tourLen < bestTourLength) {
            bestTour = tour;
            bestTourLength = tourLen;
        }
//        printTour(bestTour);
//        System.out.println();
//        System.out.printf("Tour length = %.1f km\n", bestTourLength);
        return bestTour;
    }

    public static DoublyLinkedList<State> randomNearestNeighbor(State start, int howGreedy) {
        DoublyLinkedList<State> bestTour = new DoublyLinkedList<>();
        double bestTourLength = Double.MAX_VALUE;
        if (start == null) {
            if (map.equals("USA")) {
                //TO Change to call on all 48/Canada number of states as start
                HashMap<State, Boolean> visited = new HashMap<>();

                for (State s : States.continentalStates) {
                    double bestDist = Double.MAX_VALUE;
                    DoublyLinkedList<State> bestPath = new DoublyLinkedList<>();
                    DoublyLinkedList<State> b = randomNearestNeighborHelper(s, visited, howGreedy);
                    if (tourLength(b) < bestDist) {
                        bestPath = b;
                    }
                    printTour(bestPath);
                }
            } else {
                HashMap<State, Boolean> visited = new HashMap<>();
                for (State s : Canada.provinces) {
                    double bestDist = Double.MAX_VALUE;
                    DoublyLinkedList<State> bestPath = null;
                    DoublyLinkedList<State> b = randomNearestNeighborHelper(s, visited, howGreedy);
                    if (tourLength(b) < bestDist) {
                        bestPath = b;
                    }
                    printTour(bestPath);
                }

            }
        }
        HashMap<State, Boolean> visited = new HashMap<>(); //effectively clears visited map for each run
        //loop around next line to call for all starting
        DoublyLinkedList<State> tour = randomNearestNeighborHelper(start, visited, howGreedy);

        double tourLen = tourLength(tour);
        if (tourLen < bestTourLength) {
            bestTour = tour;
            bestTourLength = tourLen;
        }
//        printTour(bestTour);
//        System.out.println();
//        System.out.printf("Tour length = %.1f km\n", bestTourLength);
        return bestTour;
    }

    public static void printTourAndLength(DoublyLinkedList<State> tour) {
        double tourLen = tourLength(tour);
        printTour(tour);
        System.out.println();
        System.out.printf("Tour length = %.1f km\n", tourLen);
    }

    public static DoublyLinkedList<State> randomNearestNeighborHelper(State start, HashMap<State, Boolean> visited, int howGreedy) {
        DoublyLinkedList<State> tour = new DoublyLinkedList<>();
        tour.append(start);
        visited.put(start, true);
        State next = getRandomNearestUnseenState(start, visited, howGreedy);
        while (next != null && tour.size() <= 48) {
            tour.append(next);
            visited.put(next, true);
            next = getRandomNearestUnseenState(next, visited, howGreedy);
        }
        tour.append(start);
        return tour;
    }

    public static State getRandomNearestUnseenState(State current, HashMap<State, Boolean> visited, int howGreedy) {
        //array of distances
        //array of states
        double[] closests = new double[howGreedy];
//      State[] states = new State[howGreedy];
        ArrayList<State> states = new ArrayList<>(); //not "howGreedylong"

        //sets all distances to max
        for (int i = 0; i < closests.length; i++) {
            closests[i] = 999999;
        }

        //start logic
        if (map.equals("USA")) {
            for (State s : States.continentalStates) {
                if ((!current.equals(s)) && (!visited.containsKey(s))) {
                    double dist = s.capital().distance((current.capital()));
                    for (int i = 0; i < closests.length; i++) {
                        if (dist < closests[i]) {
                            closests[i] = dist;
                            states.add(s);
                            break;
                        }
                    }
                }
            }
        } else {
            for (State s : Canada.provinces) {
                if ((!current.code().equals(s.code())) && (!visited.containsKey(s))) {
                    double dist = s.capital().distance((current.capital()));
                    for (int i = 0; i < closests.length; i++) {
                        if (dist < closests[i]) {
                            closests[i] = dist;
                            states.add(s);
                            break;
                        }
                    }
                }
            }
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(howGreedy);
        if (closests[randomIndex] == 999999) {
            return null;
        }
//        for (State s : states){
//            System.out.println(s.name());
//
//        }
//        System.out.println("------------------------------");
        State[] resultStates = new State[states.size()];
        resultStates = states.toArray(resultStates);
        if (resultStates.length == 1) {
            return resultStates[0];
        }
        return resultStates[randomIndex];
    }

    public static void printTour(DoublyLinkedList<State> tour) {
        for (State s : tour) {
            System.out.print(s.code() + " "); //Will result in trailing space, to note if he cares
        }
    }

    public static DoublyLinkedList<State> nearestNeighborHelper(State start, HashMap<State, Boolean> visited) {
        DoublyLinkedList<State> tour = new DoublyLinkedList<>();
        tour.append(start);
        visited.put(start, true);
        State next = getNearestUnseenState(start, visited);
        while (next != null) {
            tour.append(next);
            visited.put(next, true);
            next = getNearestUnseenState(next, visited);
        }
        tour.append(start);
        return tour;
    }

    public static DoublyLinkedList<State> mst(State start) {
        DoublyLinkedList<State> mst = new DoublyLinkedList<>();
        HashMap<State, Boolean> mstSet = new HashMap<>();
        mstSet.put(start, true);
        mst.append(start);
        int mstIdx = 1;
        State next = pickNearestFromVisited(mstSet);
        while (next != null) {
            mstSet.put(next, true);
            mst.append(next);
            next = pickNearestFromVisited(mstSet);
        }
        return mst;
    }

    public static State pickNearestFromVisited(HashMap<State, Boolean> visited) {
        State best = null;
        double minDist = Double.MAX_VALUE;
        State parent = null;
        for (State s : visited.keySet()) {
            State nearestFromS = getNearestUnseenState(s, visited);
            if (nearestFromS == null) {
                break;
            }
            double distFromS = s.capital().distance(nearestFromS.capital());
            if (distFromS < minDist) {
                minDist = distFromS;
                best = nearestFromS;
                parent = s;
            }
        }
        if (best != null) {
            mstLen += parent.capital().distance(best.capital());
        }
        return best;
    }

    public static DoublyLinkedList<State> greedy(State start) {
        return null;

    }

    public static double tourLength(DoublyLinkedList<State> tour) {
        double sum = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            if (tour.get(i) == null) {
                System.out.println("State: " + i + " of tour is null");
            }
            sum += tour.get(i).capital().distance(tour.get(i + 1).capital());
        }
        if (tour.get(tour.size() - 1) == null) {
            System.out.println("Last state of tour (should be start) is null");
        }
        return sum;
    }

    public static void printBounds() {
        System.out.printf("OPT TSP LENGTH >= %.1f km\n", mstLen); //replace with bounds coming from MST
        System.out.printf("OPT TSP LENGTH <= %.1f km\n", upperBound);
    }
}