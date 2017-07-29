package rally;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Brian on 1/28/2017.
 */
public class RallyRunner {
    public static void main(String[] args) {
        RallyRunner rallyRunner = new RallyRunner();
        String key = getFileWithUtil("keys.txt");

        RallyQueries.getProjectTestCases(key);
    }

    private static String getFileWithUtil(String fileName) {

        StringBuilder result = new StringBuilder("");

        ClassLoader classLoader = RallyRunner.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}
