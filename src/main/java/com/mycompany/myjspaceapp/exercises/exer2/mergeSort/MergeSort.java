package com.mycompany.myjspaceapp.exercises.exer2.mergeSort;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class MergeSort {
    public static final int N = 3;
    public static void main(String[] args) throws InterruptedException {

        Space space = new SequentialSpace();
        Integer[] arr = {6, 5, 4, 3, 2, 1};
        space.put("divide", arr);
        space.put("sizeLeftToDivide", arr.length);
        space.put("lockDivide");


        Thread[] threads = new Thread[N];

        for (int i = 0; i < N; i++) {
           Thread thread = new Thread(new Divide(space, arr.length));
           thread.start();
           threads[i] = thread;
        }
        space.put("threadsDivide", threads);

        space.get(new ActualField("divideDone"));
        System.out.println(space);







/*        for (int i = 0; i < N; i++) {
            Thread thread = new Thread(new Conquer(space, arr.length));
            thread.start();
        }




        space.get(new ActualField("conquerDone"));
        System.out.println(space);*/




    }
}



class Data {
    Integer[] arr1;
    Integer[] arr2;
    Integer[] result;

    public Data(Integer[] arr1, Integer[] arr2, Integer[] result) {
        this.arr1 = arr1;
        this.arr2 = arr2;
        this.result = result;
    }

    public Data() {
    }
}