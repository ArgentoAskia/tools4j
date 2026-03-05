package cn.argento.askia.utilities.algorithms;

import cn.argento.askia.utilities.ArrayUtility;
import cn.argento.askia.utilities.AssertionUtility;

import java.lang.reflect.Array;
import java.util.Objects;

public class ArraySortAlgorithmsUtility {

    public static void bubbleSort(Object arrayObj){

    }


    public static boolean isSorted(Object arrayObj){
        AssertionUtility.requireObjectParamAsArrayType(arrayObj);
        AssertionUtility.requireComparableVariable(arrayObj);
        final int length = ArrayUtility.getLength(arrayObj);
        for (int i = 1; i < length; i++) {
        }
        return true;
    }
}
