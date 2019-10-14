package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                .getAsInt();
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
        if (sum % 2 != 0) {
            return odds;
        } else {
            integers.removeAll(odds);
            return integers;
        }
    }

    public static List<Integer> oddOrEvenStream(List<Integer> integers) {
        int[] sum = {0};
        return integers.stream()
                .map(i -> {
                    sum[0] = sum[0] + i;
                    return i;
                }).sorted()
                .filter(i -> i % 2 == sum[0] % 2)
                .collect(Collectors.toList());
    }
}
