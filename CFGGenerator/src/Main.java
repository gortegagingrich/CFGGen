import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.PersistentVector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // set up instaparse wrapper
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("instaparse-wrapper.core"));

        // read grammar
        IFn fn = Clojure.var("instaparse-wrapper.core", "parse-grammar");
        String grammar = readGrammar("grammar.txt");

        testAndPrint(fn,grammar,"tests/test0.java");
        testAndPrint(fn,grammar,"tests/test1.java");
        testAndPrint(fn,grammar,"tests/test2.java");
        testAndPrint(fn,grammar,"tests/test3.java");
    }

    private static void testAndPrint(IFn fn, String grammar, String test) {
        System.out.printf("Testing %s\n", test);
        try {
            test = readTest(test);
        } catch (Exception e) {
            // test is not a file name
        }
        long startTime = System.currentTimeMillis();
        PersistentVector vec = (PersistentVector)fn.invoke(grammar, test);
        long endTime = System.currentTimeMillis();

        new Structure(vec);

        System.out.printf("Time elaplsed: %d\n\n", endTime - startTime);
    }

    private static String readGrammar(String fn) {
        File file = new File(fn);
        String grammar = "";

        FileReader fr;
        try {
            fr = new FileReader(file);
            Scanner scan = new Scanner(fr);

            while (scan.hasNextLine()) {
                grammar += scan.nextLine() + "\n";
            }

            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return grammar;
    }

    private static String readTest(String fn) throws FileNotFoundException {
        String out = "";

        FileReader fr = new FileReader(new File(fn));
        Scanner scan = new Scanner(fr);

        while (scan.hasNextLine()) {
            out += scan.nextLine() + "\n";
        }

        scan.close();

        return String.format("{%s}", out);
    }
}
