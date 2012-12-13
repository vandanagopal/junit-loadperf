package com.github.vandanagopal.junitloadperf;

import com.github.vandanagopal.junitloadperf.outputWriter.TestResultWriter;
import junit.extensions.TestDecorator;
import junit.framework.Test;
import junit.framework.TestResult;
import org.joda.time.DateTime;

/**
 * A Decorator that runs a test repeatedly.
 */
public class CustomTest extends TestDecorator {

    private TestResultWriter resultWriter;


    public CustomTest(Test test, TestResultWriter resultWriter) {
        super(test);
        this.resultWriter = resultWriter;

    }

    @Override
    public int countTestCases() {
        return super.countTestCases();
    }

    @Override
    public void run(TestResult result) {
        if (result.shouldStop())
            return;
        DateTime timeBeforeTest = DateTime.now();
        super.run(result);
        DateTime timeAfterTest = DateTime.now();
        if (resultWriter != null)
            resultWriter.writeOutput(timeAfterTest.getMillis() - timeBeforeTest.getMillis());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}