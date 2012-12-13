package com.github.vandanagopal.junitloadperf;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.RandomTimer;
import com.github.vandanagopal.junitloadperf.annotations.LoadPerfStaggered;
import junit.framework.Test;
import org.joda.time.DateTime;
import com.github.vandanagopal.junitloadperf.outputWriter.IndividualTestTimeResultWriter;
import org.junit.runners.model.FrameworkMethod;

public class LoadTestFactory {


    public static Test createLoadTest(int maxUsers, FrameworkMethod method) {
        Test customTest = getCustomTest(method.getMethod().getDeclaringClass(), method.getMethod().getName(),true);
        return new LoadTest(customTest, maxUsers);
    }

    public static Test createSetUpTest(int maxUsers, FrameworkMethod method) {
        Test customTest = getCustomTest(method.getMethod().getDeclaringClass(), method.getMethod().getName(),false);
        return new LoadTest(customTest, maxUsers);
    }

    public static Test createStaggeredLoadTest(LoadPerfStaggered loadPerfStaggered, FrameworkMethod method) {
        Test customTest = getCustomTest(method.getMethod().getDeclaringClass(), method.getMethod().getName(),true);
        int variation = loadPerfStaggered.delayVariation();
        RandomTimer randomTimer = new RandomTimer(loadPerfStaggered.minDelayBetweenBatchesInMillis(), variation);

        String[] batchSizes = loadPerfStaggered.minMaxRandomBatchSizes();
        int minBatchSize = Integer.parseInt(batchSizes[0]);
        int maxBatchSize = Integer.parseInt(batchSizes[1]);
        return new StaggeredLoadTest(customTest, loadPerfStaggered.totalNumberOfUsers(), randomTimer, minBatchSize, maxBatchSize);
    }

    private static Test getCustomTest(Class testClass, String methodName, boolean shouldWriteOutputToFile) {
        Test testCase = new SpringTestMethodFactory(testClass, methodName);
        IndividualTestTimeResultWriter resultWriter = null;
        if (shouldWriteOutputToFile) {
            resultWriter = new IndividualTestTimeResultWriter(methodName + "-PerformanceTestLog" + DateTime.now() + ".csv");
        }
        return new CustomTest(testCase, resultWriter);
    }
}
