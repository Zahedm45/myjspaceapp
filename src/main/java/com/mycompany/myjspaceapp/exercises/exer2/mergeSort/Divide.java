package com.mycompany.myjspaceapp.exercises.exer2.mergeSort;


import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import java.util.Arrays;

import static com.mycompany.myjspaceapp.exercises.exer2.mergeSort.MergeSort.*;

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
                Object[] obj = space.get(new ActualField(DIVIDE), new FormalField(Object.class));

                Integer[] arr = (Integer[]) obj[1];
                if (arr.length == 0) {          // No more job to be done
                    space.put(obj);
                    return;
                }

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
            space.put(DIVIDE, arr);
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
        space.put(SORTED, arr);
        updateSizeLeftToDivide(i);

    }


    void updateSizeLeftToDivide(int i) throws InterruptedException {
        Object[] ob = space.get(new ActualField(SIZE_LEFT_DIVIDE), new FormalField(Integer.class));
        int integer =(int) ob[1];
        integer -= i;
        space.put(SIZE_LEFT_DIVIDE, integer);
        if (integer == 0) {
            Integer[] emptyArr = new Integer[0];
            space.put(DIVIDE, emptyArr);
            space.put(DONE_DIVIDE);
        }
    }
}
