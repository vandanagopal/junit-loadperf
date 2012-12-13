package com.github.vandanagopal.junitloadperf;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.junit.After;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.github.vandanagopal.junitloadperf.annotations.LoadPerfBefore;
import com.github.vandanagopal.junitloadperf.annotations.LoadPerfStaggered;
import com.github.vandanagopal.junitloadperf.annotations.LoadPerf;

import static org.junit.Assert.assertTrue;

public class LoadRunner extends BlockJUnit4ClassRunner {

    public LoadRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public Description getDescription() {
        return super.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                runLoadSetUp();
                TestSuite testSuite = getTestSuite(method);
                TestResult testResult = TestRunner.run(testSuite);
                assertTrue(testResult.wasSuccessful());
                runAfter();
            }
        };
    }

    private void runLoadSetUp() {
        List<FrameworkMethod> beforeMethods = getTestClass().getAnnotatedMethods(LoadPerfBefore.class);
        if (beforeMethods == null) return;
        sortBeforeMethodsBasedOnPriority(beforeMethods);
        for (FrameworkMethod method : beforeMethods) {
            LoadPerfBefore loadPerfBefore = method.getAnnotation(LoadPerfBefore.class);
            TestSuite testSuite = TestSuiteFactory.createBeforeAfterTestSuite(loadPerfBefore.concurrentUsers(), method);
            TestResult testResult = TestRunner.run(testSuite);
            assertTrue(testResult.wasSuccessful());
        }

    }

    private void runAfter() {
        List<FrameworkMethod> afterMethod = getTestClass().getAnnotatedMethods(After.class);
        if (afterMethod == null || afterMethod.size()==0) return;
        TestSuite testSuite = TestSuiteFactory.createBeforeAfterTestSuite(1, afterMethod.get(0));
        TestResult testResult = TestRunner.run(testSuite);
        assertTrue(testResult.wasSuccessful());
    }


    private void sortBeforeMethodsBasedOnPriority(List<FrameworkMethod> beforeMethods) {
        Collections.sort(beforeMethods, new Comparator<FrameworkMethod>() {
            @Override
            public int compare(FrameworkMethod o1, FrameworkMethod o2) {
                LoadPerfBefore loadPerfBefore1 = o1.getAnnotation(LoadPerfBefore.class);
                LoadPerfBefore loadPerfBefore2 = o2.getAnnotation(LoadPerfBefore.class);
                return loadPerfBefore1.priority() - loadPerfBefore2.priority();

            }
        });
    }

    private TestSuite getTestSuite(FrameworkMethod method) {
        LoadPerf loadPerf = method.getAnnotation(LoadPerf.class);
        if (loadPerf != null)
            return TestSuiteFactory.createTestSuite(loadPerf.concurrentUsers(), method);
        else {
            LoadPerfStaggered loadPerfStaggered = method.getAnnotation(LoadPerfStaggered.class);
            return TestSuiteFactory.createStaggeredLoadTestSuite(loadPerfStaggered, method);
        }
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        ArrayList<FrameworkMethod> testMethods = new ArrayList<FrameworkMethod>();
        TestClass testClass = getTestClass();
        addTestMethods(testMethods, testClass, LoadPerf.class);
        addTestMethods(testMethods, testClass, LoadPerfStaggered.class);
        return testMethods;

    }

    private void addTestMethods(ArrayList<FrameworkMethod> testMethods, TestClass testClass, Class annotationClass) {
        List<FrameworkMethod> loadTestMethods = testClass.getAnnotatedMethods(annotationClass);
        if (loadTestMethods != null)
            testMethods.addAll(loadTestMethods);
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(LoadPerf.class, false, errors);
        validatePublicVoidNoArgMethods(LoadPerfStaggered.class, false, errors);
    }

    @Override
    protected void validateConstructor(List<Throwable> errors) {

    }
}
