package com.mycompany.myjspaceapp.exercises.exer2.mergeSort;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import java.util.Random;

public class MergeSort {
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


    public static void main(String[] args) throws InterruptedException {

        Space space = new SequentialSpace();
        //Integer[] arr = {6, 5, 4, 3, 2, 1, 5, 10};
        int arraySize = 20;
        Integer[] arr = new Integer[arraySize];
        for (int i = 0; i < arraySize; i++) {
            arr[i] = new Random().nextInt(100);
        }


        space.put(DIVIDE, arr);
        space.put(SIZE_LEFT_DIVIDE, arr.length);
        space.put(LOCK_DIVIDE);
        space.put(LOCK_CONQUER);



        for (int i = 0; i < N; i++) {
           Thread thread = new Thread(new Divide(space, arr.length));
           thread.start();
        }

        space.get(new ActualField(DONE_DIVIDE));
        System.out.println("Divide: done");

        for (int i = 0; i < N; i++) {
            Thread thread = new Thread(new Conquer(space, arr.length));
            thread.start();
        }

        waitForCompletion(space, DONE_CONQUER, "Conquer: done");
        printArray(space);


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