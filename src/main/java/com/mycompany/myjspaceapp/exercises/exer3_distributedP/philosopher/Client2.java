package com.mycompany.myjspaceapp.exercises.exer3_distributedP.philosopher;

import org.jspace.ActualField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class Client2 {
    public static void main(String[] args) {
        int port = 31145;
        String host = "localhost";

        String uri = "tcp://" + host + ":" + port + "/board?conn";
        try {
            Space board = new RemoteSpace(uri);

            while (true) {
                board.get(new ActualField("lock"));
                System.out.println("lock received");
                board.put("lock");
                System.out.println("lock released");
            }


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
