package com.mycompany.myjspaceapp.exercises.exer2.philosopher;

// This is a model of the classic problem of the dining philosophers.
// The solution is based on using tickets to limit concurrency.
import org.jspace.ActualField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Exercise_2_1 {
    // N defines the number of philosophers.
    public static final int N = 3;

    public static void main(String[] args) throws InterruptedException {
        Space board = new SequentialSpace();
        new Thread(new Waiter(board)).start();

        for (int i = 0; i < N -1; i++) {
            board.put("lock");
        }

        for (int i = 0; i < N; i ++) {
            new Thread(new Philosopher(board, i)).start();
        }

        try {
            board.query(new ActualField("done")); // will never succeed
        } catch (InterruptedException e) {}
    }
}

// waiter prepares the board with forks.
class Waiter implements Runnable {
    private Space board;

    public Waiter(Space space) {
        this.board = space;
    }

    public void run() {
        System.out.println("Waiter putting forks on the table...");

        for (int i = 0; i < Exercise_2_1.N; i ++) {
            try {
                board.put("fork", i);
                System.out.println("Waiter put fork " + i + " on the table.");
            } catch (InterruptedException e) {}
        }

        System.out.println("Waiter done.");
    }
}



// philosopher defines the behaviour of a philosopher.
class Philosopher implements Runnable {
    private Space board;
    private int me, left, right;

    public Philosopher(Space space, int me) {
        this.board = space;

        // We define variables to identify the left and right forks.
        this.me = me;
        this.left = me;
        this.right = (me + 1) % Exercise_2_1.N;
    }


    public void run() {

        // The philosopher enters his endless life cycle.
        while (true) {
            try {

                board.get(new ActualField("lock"));

                // Wait until the left fork is ready (get the corresponding tuple).
                board.get(new ActualField("fork"), new ActualField(left));
                System.out.println("Philosopher " + me + " got left fork");

                // Wait until the right fork is ready (get the corresponding tuple).
                board.get(new ActualField("fork"), new ActualField(right));
                System.out.println("Philosopher " + me + " got right fork");

                // Lunch time.
                System.out.println("Philosopher " + me + " is eating...");

                // Return the forks (put the corresponding tuples).
                board.put("fork", left);
                board.put("fork", right);
                System.out.println("Philosopher " + me + " put both forks on the table");
                board.put("lock");

            } catch (InterruptedException e) {}
        }
    }
}
