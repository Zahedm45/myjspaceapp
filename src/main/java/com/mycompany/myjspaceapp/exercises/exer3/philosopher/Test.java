package com.mycompany.myjspaceapp.exercises.exer3.philosopher;

import org.jspace.ActualField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        int port = 31145;
        String host = "localhost";

        String uri = "tcp://" + host + ":" + port + "/board?conn";
        try {

            while (true) {
                Space board = new RemoteSpace(uri);
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
