package org.junit.load.perf;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.RandomTimer;
import junit.framework.Test;
import org.joda.time.DateTime;
import org.junit.load.perf.annotations.LoadPerfStaggered;
import org.junit.load.perf.outputWriter.IndividualTestTimeResultWriter;
import org.junit.runners.model.FrameworkMethod;

public class LoadTestFactory {


    public static Test createLoadTest(int maxUsers, FrameworkMethod method)  {
        CustomTest customTest = getCustomTest(method.getMethod().getDeclaringClass(), method.getMethod().getName());
        return new LoadTest(customTest, maxUsers);
    }

    public static Test createStaggeredLoadTest(LoadPerfStaggered loadPerfStaggered, FrameworkMethod method){
        CustomTest customTest = getCustomTest(method.getMethod().getDeclaringClass(), method.getMethod().getName());
        int variation = loadPerfStaggered.delayVariation();
        RandomTimer randomTimer = new RandomTimer(loadPerfStaggered.minDelayBetweenBatchesInMillis(), variation);

        String[] batchSizes = loadPerfStaggered.minMaxRandomBatchSizes();
        int minBatchSize = Integer.parseInt(batchSizes[0]);
        int maxBatchSize = Integer.parseInt(batchSizes[1]);
        return new StaggeredLoadTest(customTest, loadPerfStaggered.totalNumberOfUsers(),randomTimer,minBatchSize,maxBatchSize);
    }

    private static CustomTest getCustomTest(Class testClass, String methodName) {
        Test testCase = new SpringTestMethodFactory(testClass, methodName);
        IndividualTestTimeResultWriter resultWriter = new IndividualTestTimeResultWriter(methodName+"-PerformanceTestLog" + DateTime.now() + ".csv");
        CustomTest customTest = new CustomTest(testCase, resultWriter);
        return customTest;
    }
}
