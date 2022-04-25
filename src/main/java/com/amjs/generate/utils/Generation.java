package com.amjs.generate.utils;


import com.amjs.generate.Const;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Generation {

    // Метод для генерации последовательности  простых чисел заданной длины
    public static List<int[]> generate(int length) {
        // sequence длиной 5 в котором лежит массив
        // -> в котором находиться простые числа в кол-ве = length
        List<int[]> sequence = new ArrayList<>();
        while (sequence.size() < Const.COUNTSEQUENCE) {
            int[] array = new int[length];
            for (int i = 0; i < length; i++) {
                while (array[i] == 0) {
                    Random random = new Random();
                    int foo = random.nextInt(1000);
                    if (isPrimeBruteForce(foo)) {
                        array[i] = foo;
                    }
                }
            }
            sequence.add(array);
        }
        return sequence;
    }

    public Map<Integer, Set<Integer>> creatGenerateSequence(List<int[]> oldSequence) {
        Map<Integer, Set<Integer>> sequenceGenerate = new HashMap<>();
        int arrayLength = oldSequence.get(0).length;
        while (sequenceGenerate.size() < Const.COUNTSEQUENCE) {
            for (int w = 0; w < Const.COUNTSEQUENCE; w++) {
                // получаем массив из которого будем дергать 6 чисел(по условию у него длина>10)
                int[] arrayForParsing = oldSequence.get(w);
                Set<Integer> unicArray = new HashSet<>();
                // из arrayForParsing берем random числа  и вставляем в Set пока длина <GENERATESEQUENCE
                for (int i = 0; i < Const.GENERATESEQUENCE; i++) {
                    Random random = new Random();
                     do {
                        unicArray.add(arrayForParsing[random.nextInt(arrayLength)]);
                    }
                    while (unicArray.size()==i) ;
                }
                int lengthForView = w + 1;
                sequenceGenerate.put(lengthForView, unicArray);
            }
        }
         return sequenceGenerate;
    }

    public Set<Integer> creatAutoSequence(List<int[]> generateArray) {
        Set<Integer> unicArray = new HashSet<>();
        Random random = new Random();
        int arrayLength = generateArray.get(0).length;
        System.out.println(unicArray.size());
        do {
            unicArray.add(
                    generateArray.get(
                            random.nextInt(Const.COUNTSEQUENCE)
                    )[random.nextInt(arrayLength)]);
        } while (unicArray.size() < Const.COUNTSEQUENCE);
        return unicArray;
    }
    // проверка на простое число
    public static boolean isPrimeBruteForce(int number) {
        if (number < 0) return false;
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
