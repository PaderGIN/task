//Написать консольную программу, которая сортировала построчный текст, расположенный в файле по одному из критериев:

//        1. По алфавиту.
//        2. По количеству символов в строке.
//        3. По слову в строке заданному аргументом программы в виде порядкового номера.
//        Отсортированный текст должен быть сохранен в другом файле.
//        Каждая строка в исходном файле должна быть дополнена числом отображающим кол-во повторений данной строки в исходном файле.
//        Программа должна иметь краткую инструкцию по использованию.
//        Пример для 3-го варианта сортировки:
//
//        1. Предположим, что исходный файл содержит строки расположенные в следующем порядке:
//
//        •	Кошка убегает от собаки
//        •	Собака пытается догнать кошку
//        •	Мышка спокойно сидит в своей норе
//        •	Собака пытается догнать кошку
//
//        2. В данном примере, в файле содержащем обработанную информацию в случае если указана сортировка по второму слову, строки должны быть расположены в следующем порядке:
//
//        •	Собака пытается догнать кошку 2
//        •	Собака пытается догнать кошку 2
//        •	Мышка спокойно сидит в своей норе 1
//        •	Кошка убегает от собаки 1

//TODO:
//2. покрыть тестами


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TextSorter {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        greetings();
        doTask(getChoice());
        scanner.close();
    }

    private static void doTask(int choice) {
        List<String> stringsToSort = readText();
        switch (choice) {
            case 1:
                sortByAlphabet(stringsToSort);
                break;
            case 2:
                sortByLength(stringsToSort);
                break;
            case 3:
                sortByWord(stringsToSort);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private static List<String> readText() {
        List<String> stringsToSort = new ArrayList<>();
        String filePath = System.getProperty("user.dir") + "/resources/input.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringsToSort.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringsToSort;
    }

    private static void writeStringsToFile(List<String> strings) {
        String filePath = System.getProperty("user.dir") + "/resources/output.txt";
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (String s : strings) {
                writer.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getChoice() {
        int choice = -1;

        while (choice < 0) {
            System.out.println("Enter your choice:");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter an integer.");
                scanner.nextLine();
            }
        }

        return choice;
    }

    private static void greetings() {
        System.out.println("""
                Hello!
                This program sorts your text with certain way that can be chosen:
                Type 1 for sort by line by line text
                Type 2 for sort by number of characters per line
                Type 3 for sort by the word in the string specified by the program argument as a serial number
                """);
    }

    private static void sortByAlphabet(List<String> stringsToSort) {
        Collections.sort(stringsToSort, Comparator.naturalOrder());
        writeStringsToFile(countSameStrings(stringsToSort));
    }

    private static void sortByLength(List<String> stringsToSort) {
        Collections.sort(stringsToSort, Comparator.comparingInt(String::length));
        writeStringsToFile(countSameStrings(stringsToSort));
    }

    private static void sortByWord(List<String> stringsToSort) {
        int position = getPosition(stringsToSort);
        Collections.sort(stringsToSort, Comparator.comparing(s -> getWord(s, position)));
        writeStringsToFile(countSameStrings(stringsToSort));
    }

    private static List<String> countSameStrings(List<String> stringsToSort) {
        Map<String, Integer> stringCounts = new HashMap<>();
        for (String str : stringsToSort) {
            if (stringCounts.containsKey(str)) {
                int count = stringCounts.get(str);
                stringCounts.put(str, ++count);
            } else {
                stringCounts.put(str, 1);
            }
        }
        List<String> result = new ArrayList<>();
        for (String str : stringsToSort) {
            int count = stringCounts.get(str);
            String resultStr = String.format("%s %d", str, count);
            result.add(resultStr);
        }
        return result;
    }

    private static String getWord(String s, int position) {
        String[] words = s.split("\\s+");
        return position < words.length ? words[position] : "";
    }

    private static int getPosition(List<String> stringsToSort) {
        int maxPosition = getMinWords(stringsToSort);
        System.out.println(maxPosition);
        int position = -1;
        while (position < 0 || position > maxPosition) {
            System.out.println("Enter position (between 1 and " + maxPosition + "):");
            try {
                position = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter an integer.");
                scanner.nextLine();
            }
        }
        return --position;
    }

    private static int getMinWords(List<String> stringsToSort) {
        int minWords = Integer.MAX_VALUE;
        for (String str : stringsToSort) {
            String[] words = str.split("\\s+");
            int numWords = words.length;
            if (numWords < minWords) minWords = numWords;
        }
        return minWords;
    }

}
