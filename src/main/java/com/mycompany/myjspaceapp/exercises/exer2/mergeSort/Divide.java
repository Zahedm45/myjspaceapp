package com.mycompany.myjspaceapp.exercises.exer2.mergeSort;


import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import java.util.Arrays;

class Divide implements Runnable {
    Space space;
    int arrayLen;

    public Divide(Space space, int arrayLen) {
        this.space = space;
        this.arrayLen = arrayLen;
    }

    @Override
    public void run() {

        try {

            while (true) {
                Object[] obj = space.get(new ActualField("divide"), new FormalField(Object.class));

/*                if (obj == null) {
                    Object[] ob = space.query(new ActualField("sizeLeftToDivide"), new FormalField(Integer.class));
                    int sizeLeft =(int) ob[1];
                    if (sizeLeft == 0) {
                        space.put("divideDone");
                        return;
                    }

                    obj = space.getp(new ActualField("divide"), new FormalField(Object.class));

                }*/

                Integer[] arr = (Integer[]) obj[1];
                if (arr.length <= 2) System.out.println("Something went wrong!");
                else {
                    int mid = arr.length / 2;
                    Integer[] firstArr = Arrays.copyOfRange(arr, 0, mid);
                    Integer[] secondArr = Arrays.copyOfRange(arr, mid, arr.length);
                    putIntoSpace(firstArr);
                    putIntoSpace(secondArr);
                }
            }


        } catch (InterruptedException e) {
        }
    }

    void putIntoSpace(Integer[] arr) throws InterruptedException {
        if (arr.length > 2) {
            space.put("divide", arr);
            return;
        }

        int i = 1;
        if (arr.length == 2) {
            if (arr[1] < arr[0]) {
                int temp = arr[0];
                arr[0] = arr[1];
                arr[1] = temp;
            }
            i++;
        }
        space.put("sorted", arr);

        //space.get(new ActualField("lockDivide"));
        Object[] ob = space.get(new ActualField("sizeLeftToDivide"), new FormalField(Integer.class));
        int integer =(int) ob[1];
        integer -= i;
        space.put("sizeLeftToDivide", integer);
        if (integer == 0) stopAllThreads();
        //space.put("lockDivide");
    }

    void stopAllThreads() throws InterruptedException {
        Object[] obj = space.get(new ActualField("threadsDivide"), new FormalField(Object.class));
        Thread[] threads = (Thread[])obj[1];

        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
        space.put("divideDone");
    }
}
