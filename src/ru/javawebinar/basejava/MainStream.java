package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainStream {

    public static void main(String[] args) {
        int[] array = {9, 8};
        System.out.println(minValue(array));

        List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        oddOrEven(integers).forEach(System.out::println);

        oddOrEvenStream(integers).forEach(System.out::println);
    }

    public static int minValue(int[] array) {
        List<Integer> items = Arrays.stream(array)
                .distinct()
                .boxed()
                .collect(Collectors.toList());

        return IntStream.range(0, factorial(items.size()))
                .mapToObj(i -> permutation(i, items))
                .mapToInt(p -> {
                    int result = 0;
                    int size = p.size();
                    for (int i = 1; i <= Math.pow(10, size - 1); i *= 10) {
                        result += i * p.get((int) (size - Math.log10(i) - 1));
                    }
                    return result;
                })
                .min()
                .getAsInt();
    }

    private static int factorial(final int num) {
        return IntStream.rangeClosed(2, num).reduce(1, (x, y) -> x * y);
    }

    private static <T> List<T> permutation(final int count, final LinkedList<T> input, final List<T> output) {
        if (input.isEmpty()) {
            return output;
        }
        final int factorial = factorial(input.size() - 1);
        output.add(input.remove(count / factorial));
        return permutation(count % factorial, input, output);
    }

    private static <T> List<T> permutation(final int count, final List<T> items) {
        return permutation(count, new LinkedList<>(items), new ArrayList<>());
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
        return integers
                .stream()
                .filter(integer -> integers
                        .stream()
                        .reduce(0, Integer::sum) % 2 == integer % 2)
                .collect(Collectors.toList());
    }
}
