package org.junit.load.perf.outputWriter;

import org.joda.time.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IndividualTestTimeResultWriter implements TestResultWriter {

    private File file;

    public IndividualTestTimeResultWriter(String fileName) {
        file = new File(fileName);
        System.out.println("Results outputted to file "+fileName);
        writeToFile("Current Time, Time Taken in ms\n");
    }

    @Override
    public void writeOutput(Object timeBeforeTest) {
        DateTime afterTest = DateTime.now();
        long timeTaken = afterTest.getMillis() - ((DateTime) timeBeforeTest).getMillis();
        System.out.println("Time taken per test - "+timeTaken + " ms");
        String outputString = DateTime.now().toString("dd-MM-yyyy HH:mm-ss") + ", " + timeTaken + "\n";
        writeToFile(outputString);

    }

    public void writeToFile(String outputString) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(outputString);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Could not write output to file.");
            throw new RuntimeException(e);

        }

    }
}
