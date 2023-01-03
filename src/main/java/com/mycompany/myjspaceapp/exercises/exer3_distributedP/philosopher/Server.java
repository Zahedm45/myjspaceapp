package com.mycompany.myjspaceapp.exercises.exer3_distributedP.philosopher;

import org.jspace.SequentialSpace;
import org.jspace.Space;
import org.jspace.SpaceRepository;


public class Server {
    public static void main(String[] args) {
        int port = 31145;
        //String host = "localhost";
        String host = "10.209.95.114";
        int philosophers = 2;

        String uri = "tcp://" + host + ":" + port + "/?keep";
        SpaceRepository spaceRepository = new SpaceRepository();
        spaceRepository.addGate(uri);
        Space board = new SequentialSpace();

        spaceRepository.add("board", board);

        try {
            board.put("lock");
            runWaiter(board, philosophers);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public static void runWaiter(Space board, int forkSize) throws InterruptedException {
        System.out.println("Waiter putting forks on the table...");


        for (int i = 0; i < forkSize ; i ++) {
            board.put("fork", i);
            System.out.println("Waiter put fork " + i + " on the table.");
        }

        //board.put("waiterDone");

        System.out.println("Waiter done.");
    }
}
