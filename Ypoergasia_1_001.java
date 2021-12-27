package ypoergasia_1_001;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Ypoergasia_1_001 {

    //Μέθοδος εισόδου τυχαίων αριθμών (0-10)
    private static int randomNumber() {
        int numCas = (int) (Math.random() * 11);
        return numCas;
    }

    // To thread εισάγει έναν πίνακα, ένα διάνυσμα και επιστρέφει τον νέο διάνυσμα
    private static class ProcessThread extends Thread {

        private final int[][] pinA;
        private final int[] vector;
        public ArrayList<Integer> newVector = new ArrayList<>();

        public ProcessThread(int[][] pinA, int[] vector) {
            this.pinA = pinA;
            this.vector = vector;
            System.out.println(this.getName() + " processing an array of " + pinA.length + " Array rows");

        }

        @Override
        public void run() {
            for (int[] pinA1 : pinA) {
                int w = 0;
                for (int j = 0; j < vector.length; j++) {
                    w = w + (pinA1[j] * vector[j]);
                }
                newVector.add(w);

            }
            
        }

        public ArrayList<Integer> getNewVector() {
            return newVector;
        }

    }

    
    //main
    public static void main(String[] args) {
        
        long startTime = System.nanoTime();
        
        

        Scanner sc = new Scanner(System.in);

        //Εισαγωγή κ νημάτων 
        int k = 3;
        while (!(k != 0 && ((k & (k - 1)) == 0))) { //όσο δεν είναι δύναμη του 2 επανέλαβε 
            System.out.println("How many Threads? (power of 2) : ");
            k = sc.nextInt();
        }

        //Εισαγωγή n γραμμών του πίνακα 
        int n = 3;
        while ((!(n != 0 && ((n & (n - 1)) == 0))) || n <= k) { //όσο δεν είναι δύναμη του 2 και δεν είναι μικρότερο από το k επανέλαβε 
            System.out.println("Number of arrays rows? (power of 2 and < of threads) : ");
            n = sc.nextInt();
        }

        //Εισαγωγή m στηλών του πίνακα
        int m = 0;
        while (m < 1) { //όσο είναι μικρότερο του 1 επανέλαβε
            System.out.println("Number of arrays colums : ");
            m = sc.nextInt();
        }

        //δημιουργία των πινάκων 
        int[][] pinA = new int[n][m]; // Ο αρχικός μας πίνακας Α
        int[] vector = new int[m]; // Το διάνυσμά μας
        ArrayList<Integer> newVector = new ArrayList<>();// Το νέο διάνυσμα που θα προκύψει μετά τον πολλαπλασιασμό

        //Γέμισμα του πίνακα Α με τυχαίους αριθμούς
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pinA[i][j] = randomNumber();

            }
        }

        //Γέμισμα του διανύσματος με τυχαίους αριθμούς
        for (int i = 0; i < m; i++) {
            vector[i] = randomNumber();
        }

        //Εκτύπωση του πίνακα Α
        System.out.println("\nThe Array A : ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("[" + pinA[i][j] + "] ");
            }
            System.out.println("");
        }

        //Εκτύπωση του διανύσματος
        System.out.println("\nThe Vector : ");
        for (int i = 0; i < m; i++) {
            System.out.print("[" + vector[i] + "]");
        }

        //Δημιουργία των threads
        ProcessThread[] threads = new ProcessThread[k];
        int batchSize = n / k;
        System.out.println("\n\nTotal Array rows: " + n);
        System.out.println("\nProcess in batches of " + batchSize + " Array rows");

        for (int i = 0; i < threads.length; i++) {
            int start = i * batchSize;
            int end = (i + 1) * batchSize;
            System.out.println("Batch [start, end): [" + start + ", " + end + ")");

            threads[i] = new ProcessThread(Arrays.copyOfRange(pinA, start, end), vector);
            threads[i].start();

        }

        for (ProcessThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Συμπλήρωση του νέου διανυσματος με τις τιμές από τα threads
        for (ProcessThread thread : threads) {
            for (int j = 0; j < thread.getNewVector().size(); j++) {
                newVector.add(thread.getNewVector().get(j));
            }
        }

        //Εκτύπωση νέου διανύσματος
        System.out.println("\nThe new Vector : ");
        System.out.println(newVector);

        double totalTime = System.nanoTime() - startTime;
        System.out.println("\nFor " + k + " threads :");
        System.out.println("Total time: " + totalTime / 1_000_000_000 + " seconds");
    }

}
