package com.github.vandanagopal.junitloadperf;

import com.github.vandanagopal.junitloadperf.annotations.LoadPerfStaggered;
import junit.framework.TestSuite;
import org.junit.runners.model.FrameworkMethod;

public class TestSuiteFactory {

    public static TestSuite createTestSuite(int maxUsers, FrameworkMethod method) {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(LoadTestFactory.createLoadTest(maxUsers, method));
        return testSuite;
    }

    public static TestSuite createBeforeAfterTestSuite(int maxUsers, FrameworkMethod method) {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(LoadTestFactory.createSetUpTest(maxUsers, method));
        return testSuite;
    }

    public static TestSuite createStaggeredLoadTestSuite(LoadPerfStaggered loadPerfStaggered, FrameworkMethod method) {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(LoadTestFactory.createStaggeredLoadTest(loadPerfStaggered, method));
        return testSuite;
    }
}

