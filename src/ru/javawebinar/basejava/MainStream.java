package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainStream {

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(array));

        List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        oddOrEven(integers).forEach(System.out::println);

        oddOrEvenStream(integers).forEach(System.out::println);
    }

    public static int minValue(int[] array) {
        return Arrays.stream(array)
                .distinct()
                .sorted()
                .reduce((a, b) -> b = b + a * 10)
                .orElse(-1);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = 0;
        List<Integer> odds = new ArrayList<>();
        for (Integer i : integers) {
            if (i % 2 != 0) {
                odds.add(i);
                sum += i;
            }
        }
        if (sum % 2 == 0) {
            return odds;
        } else {
            List<Integer> result = new ArrayList<>(integers);
            result.removeAll(odds);
            return result;
        }
    }

    public static List<Integer> oddOrEvenStream(List<Integer> integers) {
        AtomicInteger sum = new AtomicInteger(0);
        return integers
                .stream()
                .peek(i -> sum.accumulateAndGet(i, Integer::sum))
                .collect(Collectors.partitioningBy(i -> i % 2 == 0))
                .get(sum.get() % 2 != 0);
    }
}
