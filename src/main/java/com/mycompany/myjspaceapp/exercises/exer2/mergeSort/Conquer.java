package com.mycompany.myjspaceapp.exercises.exer2.mergeSort;


import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

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

                Object[] obj1 = space.getp(new ActualField("sorted"), new FormalField(Integer.class));
                Object[] obj2 = space.getp(new ActualField("sorted"), new FormalField(Integer.class));

                if (obj1 == null && obj2 == null) {
                    Object ob = space.queryp(new ActualField("conquerDone"));
                    if (ob != null) break;
                    continue;

                } else if (obj1 == null) {
                    if (isAllDone(obj2)) break;
                    continue;

                } else if (obj2 == null) {
                    if (isAllDone(obj1)) break;
                    continue;
                }


                Data data = new Data();
                data.arr1 = (Integer[]) obj1[1];
                data.arr2 = (Integer[]) obj2[1];
                data.result = new Integer[data.arr1.length + data.arr2.length];
                merge(data, 0, 0, 0);
                space.put("sorted", data.result);
            }


        } catch (InterruptedException e) {
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

    boolean isAllDone(Object[] obj) throws InterruptedException {
        Integer[] arr = (Integer[]) obj[1];
        if (arr.length == arrayLength) {
            space.put("conquerDone");
            return true;
        }
        return false;
    }

}
