package com.mycompany.myjspaceapp.exercises.exer2.mergeSort;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class MergeSort {
    public static final int N = 2;
    public static final String THREADS_NAME_CONQUER = "threadsConquer";
    public static final String DONE_CONQUER = "doneConquer";
    public static final String LOCK_CONQUER = "lockConquer";


    public static void main(String[] args) throws InterruptedException {

        Space space = new SequentialSpace();
        Integer[] arr = {6, 5, 4, 3, 2, 1, 5, 10};
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






        space.put(LOCK_CONQUER);
        Thread[] threads2 = new Thread[N];

        for (int i = 0; i < N; i++) {
            Thread thread = new Thread(new Conquer(space, arr.length));
            thread.start();
            threads2[i] = thread;
        }
        space.put(THREADS_NAME_CONQUER, threads2);


        space.get(new ActualField(DONE_CONQUER));
        System.out.println(space);

    }


    static void stopAllThreads(Space space, String threadsName) throws InterruptedException {
        Object[] obj = space.get(new ActualField(threadsName), new FormalField(Object.class));
        Thread[] threads = (Thread[])obj[1];

        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
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