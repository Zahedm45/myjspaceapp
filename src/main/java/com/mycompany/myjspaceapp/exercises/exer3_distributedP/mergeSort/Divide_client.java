package com.mycompany.myjspaceapp.exercises.exer3_distributedP.mergeSort;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;
import java.util.Arrays;


class Divide_client {
    public static final String DIVIDE = "divide";
    public static final String SORTED = "sorted";
    public static final String SIZE_LEFT_DIVIDE = "sizeLeftToDivide";
    public static final String DONE_DIVIDE = "doneDivide";

    public static void main(String[] args) {
        doWork();
        System.exit(0);
    }


    static void doWork() {
        int port = 31145;
        //String host = "10.209.95.114";
        String host = "192.168.1.136";
        String uri = "tcp://" + host + ":" + port + "/space?keep";

        try {
            Space space = new RemoteSpace(uri);

            while (true) {
                Object[] obj = space.get(new ActualField(DIVIDE), new FormalField(Object.class));

                Integer[] arr = (Integer[]) obj[1];
                if (arr.length == 0) {          // No more job to be done
                    space.put(obj);
                    System.out.println("hello");
                    return;
                }

                if (arr.length <= 2) System.out.println("Something went wrong!");
                else {
                    int mid = arr.length / 2;
                    Integer[] firstArr = Arrays.copyOfRange(arr, 0, mid);
                    Integer[] secondArr = Arrays.copyOfRange(arr, mid, arr.length);
                    putIntoSpace(firstArr, space);
                    putIntoSpace(secondArr, space);
                }
            }


        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void putIntoSpace(Integer[] arr, Space space) throws InterruptedException {
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
        updateSizeLeftToDivide(i, space);

    }


    static void updateSizeLeftToDivide(int i, Space space) throws InterruptedException {
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
