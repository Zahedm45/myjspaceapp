package com.mycompany.myjspaceapp.exercises.exer3_distributedP.mergeSort;

import org.jspace.*;

import java.util.List;
import java.util.Random;


public class MergeSort_server {
    public static final int N = 10;
    public static final String THREADS_NAME_CONQUER = "threadsConquer";
    public static final String DONE_CONQUER = "doneConquer";
    public static final String LOCK_CONQUER = "lockConquer";
    public static final String SORTED = "sorted";
    public static final String DIVIDE = "divide";
    public static final String LOCK_DIVIDE = "lockDivide";
    public static final String THREADS_NAME_DIVIDE = "threadsDivide";
    public static final String SIZE_LEFT_DIVIDE = "sizeLeftToDivide";
    public static final String DONE_DIVIDE = "doneDivide";


    public static void main(String[] args) {
        int port = 31145;
        //String host = "10.209.95.114";'
        String host = "192.168.1.136";
        String uri = "tcp://" + host + ":" + port + "/?keep";
        SpaceRepository spaceRepository = new SpaceRepository();
        spaceRepository.addGate(uri);
        Space space = new SequentialSpace();
        spaceRepository.add("space", space);

        //Integer[] arr = {6, 5, 4, 3, 2, 1, 5, 10};
        int arraySize = 9000;
        Integer[] arr = new Integer[arraySize];
        for (int i = 0; i < arraySize; i++) {
            arr[i] = new Random().nextInt(100);
        }


        try {

            space.put(DIVIDE, arr);
            space.put("arrayLengthConquer", arr.length);
            space.put(SIZE_LEFT_DIVIDE, arr.length);
            space.put(LOCK_DIVIDE);
            space.put(LOCK_CONQUER);


            space.get(new ActualField(DONE_DIVIDE));
            System.out.println("Divide: done");

            //List<Object[]> objList = space.queryAll(new ActualField(SORTED), new FormalField(Object.class));




            //waitForCompletion(space, DONE_CONQUER, "Conquer: done");
            space.get(new ActualField(DONE_CONQUER));
            printArray(space);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }



    static void waitForCompletion(Space space, String tag, String msg) throws InterruptedException {
        for (int i = 0; i < N; i++) {
            space.get(new ActualField(tag));
        }
        System.out.println(msg);
    }

    static void printArray(Space space) throws InterruptedException {
        Object[] obj = space.get(new ActualField(SORTED), new FormalField(Object.class));
        Integer[] objArr = (Integer[]) obj[1];
        System.out.print("Sorted Array: ");
        for (Integer integer : objArr) {
            System.out.print(integer + " ");
        }
        System.out.println();
    }
}



class Data {
    Integer[] arr1;
    Integer[] arr2;
    Integer[] result;
}