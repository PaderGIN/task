import org.junit.Test;


import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class TextSorterTest {

    @Test
    public void testSortByAlphabet() throws IOException {

        File inputFile = createTempInputFile("dog\napple\ncat");

        String input = "1\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        System.setOut(printStream);

        TextSorter.main(new String[]{});

        String expectedOutput = "Hello!\nThis program sorts your text with certain way that can be chosen:\nType 1 for sort by line by line text\nType 2 for sort by number of characters per line\nType 3 for sort by the word in the string specified by the program argument as a serial number\nEnter your choice:\n";
        expectedOutput += "Enter position (between 1 and 2):\n";
        expectedOutput += "Sorted text:\napple 1\ncat 1\ndog 1\n";
        Assert.assertEquals(expectedOutput, out.toString());

        List<String> sortedLines = Files.readAllLines(new File("resources/output.txt").toPath());
        List<String> expectedLines = Arrays.asList("apple 1", "cat 1", "dog 1");
        Assert.assertEquals(expectedLines, sortedLines);

        inputFile.delete();
    }


    @Test
    public void testSortByLength() throws IOException {
        File inputFile = createTempInputFile("cat\napple\ndog");

        TextSorter.main(new String[]{"2"});
        List<String> sortedLines = Files.readAllLines(new File("resources/output.txt").toPath());

        List<String> expectedLines = Arrays.asList("cat 1", "dog 1", "apple 1");
        Assert.assertEquals(expectedLines, sortedLines);

        inputFile.delete();
    }

    @Test
    public void testSortByWord() throws IOException {
        String inputContents = "Кошка убегает от собаки\n" +
                "Собака пытается догнать кошку\n" +
                "Мышка спокойно сидит в своей норе\n" +
                "Собака пытается догнать кошку";
        File inputFile = createTempInputFile(inputContents);

        TextSorter.main(new String[]{"3", "2"});
        List<String> sortedLines = Files.readAllLines(new File("resources/output.txt").toPath());

        List<String> expectedLines = Arrays.asList("Собака пытается догнать кошку 2",
                "Собака пытается догнать кошку 2",
                "Мышка спокойно сидит в своей норе 1",
                "Кошка убегает от собаки 1");
        Assert.assertEquals(expectedLines, sortedLines);

        inputFile.delete();
    }


    private File createTempInputFile(String contents) throws IOException {
        File inputFile = File.createTempFile("input", ".txt");
        Files.write(inputFile.toPath(), contents.getBytes());
        System.setProperty("user.dir", inputFile.getParent());
        return inputFile;
    }
}