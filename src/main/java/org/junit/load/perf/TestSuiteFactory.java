package org.junit.load.perf;

import junit.framework.TestSuite;
import org.junit.load.perf.annotations.LoadPerfStaggered;
import org.junit.runners.model.FrameworkMethod;

public class TestSuiteFactory {

    public static TestSuite createTestSuite(int maxUsers, FrameworkMethod method) {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(LoadTestFactory.createLoadTest(maxUsers, method));
        return testSuite;
    }

    public static TestSuite createStaggeredLoadTestSuite(LoadPerfStaggered loadPerfStaggered, FrameworkMethod method) {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(LoadTestFactory.createStaggeredLoadTest(loadPerfStaggered, method));
        return testSuite;
    }
}

