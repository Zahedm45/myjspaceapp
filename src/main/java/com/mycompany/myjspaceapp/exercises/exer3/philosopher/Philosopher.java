package com.mycompany.myjspaceapp.exercises.exer3.philosopher;

import org.jspace.ActualField;
import org.jspace.RemoteSpace;
import org.jspace.Space;
import java.io.IOException;

class Philosopher {
    static int numPhilosophers = 0;
    static int me = 0;
    static int port = 31145;
    static String host = "localhost";

    public static void main(String[] args) {

        initialize(args);

        try {
            String uri = "tcp://" + host + ":" + port + "/board?conn";
            Space board = new RemoteSpace(uri);
            run(board, me, numPhilosophers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void run(Space board, int me, int numPhilosophers) {
        int left = me;
        int right = (me+1) % numPhilosophers;
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


    static void initialize(String[] args) {
        if (args.length < 2 || args.length > 4) {
            System.out.println("Wrong number of arguments");
            System.out.println("Usage: java -jar run main.jar <number of philosophers> <my id> [host] [port]");
            return;
        }

        numPhilosophers = Integer.parseInt(args[0]);

        if (numPhilosophers <= 1) {
            System.out.println("Wrong number of philosophers. Must be at least 2.");
            return;
        }

        me = Integer.parseInt(args[1]);
        if (me < 0 || me >= numPhilosophers) {
            System.out.println("Wrong philosopher id. Must be between 0 and " + (numPhilosophers - 1));
            return;
        }
        if (args.length >= 3) {
            host = args[2];
        }
        if (args.length >= 4) {
            port = Integer.parseInt(args[3]);
        }
    }
}