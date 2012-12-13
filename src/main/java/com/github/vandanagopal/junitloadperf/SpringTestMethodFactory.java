package com.github.vandanagopal.junitloadperf;

import com.clarkware.junitperf.TestMethodFactory;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

public class SpringTestMethodFactory extends TestMethodFactory {


    private Class testClass;

    public SpringTestMethodFactory(Class testClass, String testMethodName) {
        super(testClass, testMethodName);
        this.testClass = testClass;
    }

    @Override
    protected TestSuite makeTestSuite() {
        TestSuite testSuite = super.makeTestSuite();
        Test test = testSuite.testAt(0);

        wireUpSpringDependencies(testClass, test);
        return testSuite;
    }


    private void wireUpSpringDependencies(Class testClass, Test customTest) {
        Class<ContextConfiguration> annotationType = ContextConfiguration.class;
        Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, testClass);
        if (declaringClass == null) {
            return;
        }
        TestContextManager testContextManager = new TestContextManager(testClass, null);
        try {
            testContextManager.prepareTestInstance(customTest);
        } catch (Exception e) {
            throw new RuntimeException("Error - cannot autowire dependencies" + e.getStackTrace());
        }

    }
}
