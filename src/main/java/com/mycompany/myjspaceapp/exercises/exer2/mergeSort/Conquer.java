package com.mycompany.myjspaceapp.exercises.exer2.mergeSort;


import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import static com.mycompany.myjspaceapp.exercises.exer2.mergeSort.MergeSort.*;

class Conquer implements Runnable {
    Space space;
    int arrayLength;


    public Conquer(Space space, int arrayLength) {
        this.space = space;
        this.arrayLength = arrayLength;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Data data = new Data();

                space.get(new ActualField(LOCK_CONQUER));

                Object[] obj1 = space.get(new ActualField(SORTED), new FormalField(Object.class));
                data.arr1 = (Integer[]) obj1[1];
                if (data.arr1.length == arrayLength) {
                    MergeSort.stopAllThreads(space, THREADS_NAME_CONQUER);
                    space.put(obj1);
                    space.put(DONE_CONQUER);
                    return;
                }


                Object[] obj2 = space.get(new ActualField(SORTED), new FormalField(Object.class));
                space.put(LOCK_CONQUER);

                data.arr2 = (Integer[]) obj2[1];
                data.result = new Integer[data.arr1.length + data.arr2.length];
                merge(data, 0, 0, 0);
                space.put(SORTED, data.result);

            }


        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
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


}
