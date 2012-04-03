import sun.misc.Regexp;
import sun.org.mozilla.javascript.internal.regexp.RegExpImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Michael Gerasimenko
 * Date: 11.03.12
 * Time: 8:17
 */
public class RegexpTest {

    public static void main(String[] args) {
        String fileName = "E:\\!Fun\\NLP\\homework\\src\\homework1\\pa1-spamlord-v2\\java\\src\\input.txt";

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
//            Regexp regexp = Regexp.c
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replaceAll("[\\s]*(?:\\s|\\()(?:DOT|dot|DOM|dom)(?:\\s|\\))[\\s]*", ".");
                line = line.replaceAll("[\\s]*(?:\\s|\\()(?:at|AT|WHERE)(?:\\s|\\))[\\s]*", "@");
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
