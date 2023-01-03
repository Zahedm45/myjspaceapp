package com.mycompany.myjspaceapp.exercises.exer3_distributedP.mergeSort;


import org.jspace.*;
import java.io.IOException;



class Conquer {
    private static final String SIZE_LEFT_DIVIDE = "sizeLeftToDivide";
    private static final String LOCK_CONQUER = "lockConquer";
    private static final String SORTED = "sorted";
    private static final String DONE_CONQUER = "doneConquer";





    public static void main(String[] args) {
        Conquer conquer = new Conquer();
        conquer.doWork();
        System.out.println("I am done.");
        System.exit(0);
    }

    void doWork() {
        int port = 31145;
        //String host = "10.209.95.114";
        String host = "192.168.1.136";
        String uri = "tcp://" + host + ":" + port + "/space?keep";

        try {
            Space space = new RemoteSpace(uri);
            Object[] arrLenObj = space.query(new ActualField("arrayLengthConquer"), new FormalField(Integer.class));
            int arrLen =(int) arrLenObj[1];

            while (true) {
                space.get(new ActualField(LOCK_CONQUER));
                Object[] obj1 = space.get(new ActualField(SORTED), new FormalField(Object.class));
                if (isConqueringDone(obj1, space, arrLen)) return;
                Object[] obj2 = space.get(new ActualField(SORTED), new FormalField(Object.class));
                space.put(LOCK_CONQUER);

                Data data = new Data();
                data.arr1 = (Integer[]) obj1[1];
                data.arr2 = (Integer[]) obj2[1];
                data.result = new Integer[data.arr1.length + data.arr2.length];
                merge(data, 0, 0, 0);
                space.put(SORTED, data.result);
            }

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    void merge(Data data, int i1, int i2, int i3) {
        if (i1 >= data.arr1.length) {
            addToAnArray(data.arr2, data.result, i2, i3);
            return;
        }

        if (i2 >= data.arr2.length) {
            addToAnArray(data.arr1, data.result, i1, i3);
            return;
        }


        if (data.arr1[i1] < data.arr2[i2]) {
            data.result[i3] = data.arr1[i1];
            merge(data, i1+1, i2, i3+1);

        } else {
            data.result[i3] = data.arr2[i2];
            merge(data, i1, i2+1, i3+1);
        }

    }
    void addToAnArray(Integer[] arr, Integer[] result, int i1, int i2) {
        for (int i = i1; i < arr.length; i++) {
            result[i2] = arr[i];
            i2++;
        }
    }

    boolean isConqueringDone(Object[] obj1, Space space, int arrayLength) throws InterruptedException {
        Integer[] arr1 = (Integer[]) obj1[1];
        if (arr1.length == arrayLength) {
            space.put(SORTED, arr1);
            space.put(LOCK_CONQUER);
            space.put(DONE_CONQUER);
            return true;
        }
        return false;
    }


    class Data {
        Integer[] arr1;
        Integer[] arr2;
        Integer[] result;
    }

}
