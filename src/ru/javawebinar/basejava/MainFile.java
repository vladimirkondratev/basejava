package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("--------------------------------------");
        printFileNameRecursionWithParagraphs(new File("./src/ru/javawebinar/basejava"), "");
    }

    private static Comparator<Path> pathComparator = (o1, o2) -> {
        if (o1.toFile().isDirectory() && o2.toFile().isDirectory()) {
            return o1.getFileName().compareTo(o2.getFileName());
        }
        if (o1.toFile().isFile() && o2.toFile().isFile()) {
            return o1.getFileName().compareTo(o2.getFileName());
        }
        if (o1.toFile().isDirectory() && o2.toFile().isFile()) {
            return -1;
        }
        if (o1.toFile().isFile() && o2.toFile().isDirectory()) {
            return 1;
        }
        return 0;
    };

    private static File[] getSortedFilesAndFolders(File directory) {
        try {
            Path[] paths = Files.list(Paths.get(directory.getAbsolutePath()))
                    .sorted(pathComparator).toArray(Path[]::new);
            File[] files = new File[paths.length];
            for (int i = 0; i < paths.length; i++) {
                files[i] = paths[i].toFile();
            }
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void printFileNameRecursionWithParagraphs(File directory, String paragraph) {
        File[] files = getSortedFilesAndFolders(directory);
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(paragraph + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println(paragraph + file.getName());
                    printFileNameRecursionWithParagraphs(file, paragraph.concat("|  "));
                }
            }
        }
    }

    private static void printFileNameRecursion(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("File: " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println("Directory: " + file.getName());
                    printFileNameRecursion(file);
                }
            }
        }
    }
}