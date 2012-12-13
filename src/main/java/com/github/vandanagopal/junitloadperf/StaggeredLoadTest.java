package com.github.vandanagopal.junitloadperf;

import com.clarkware.junitperf.ThreadBarrier;
import com.clarkware.junitperf.ThreadedTest;
import com.clarkware.junitperf.ThreadedTestGroup;
import com.clarkware.junitperf.Timer;
import junit.framework.Test;
import junit.framework.TestResult;
import org.apache.commons.lang.math.RandomUtils;

/*Modified from the junit perf LoadTest*/
public class StaggeredLoadTest implements Test {

    private final int totalUsers;
    private final Timer timer;
    private final ThreadedTest test;
    private final ThreadedTestGroup group;
    private final ThreadBarrier barrier;
    private boolean enforceTestAtomicity;
    private int minBatchSize;
    private int maxBatchSize;

    public StaggeredLoadTest(Test test, int totalUsers, Timer timer, int minBatchSize, int maxBatchSize) {
        this.minBatchSize = minBatchSize;
        this.maxBatchSize = maxBatchSize;

        if (totalUsers < 1) {
            throw new IllegalArgumentException("Number of totalUsers must be > 0");
        } else if (timer == null) {
            throw new IllegalArgumentException("Delay timer is null");
        } else if (test == null) {
            throw new IllegalArgumentException("Decorated test is null");
        }

        this.totalUsers = totalUsers;
        this.timer = timer;
        setEnforceTestAtomicity(false);
        this.barrier = new ThreadBarrier(totalUsers);
        this.group = new ThreadedTestGroup(this);
        this.test = new ThreadedTest(test, group, barrier);
    }


    /**
     * Indicates whether test atomicity should be enforced.
     * <p/>
     * If threads are integral to the successful completion of
     * a decorated test, meaning that the decorated test should not be
     * treated as complete until all of its threads complete, then
     * <code>setEnforceTestAtomicity(true)</code> should be invoked to
     * enforce test atomicity.  This effectively causes the load test to
     * wait for the completion of all threads belonging to the same
     * <code>ThreadGroup</code> as the thread running the decorated test.
     *
     * @param isAtomic <code>true</code> to enforce test atomicity;
     *                 <code>false</code> otherwise.
     */
    public void setEnforceTestAtomicity(boolean isAtomic) {
        enforceTestAtomicity = isAtomic;
    }

    /**
     * Returns the number of tests in this load test.
     *
     * @return Number of tests.
     */
    public int countTestCases() {
        return test.countTestCases() * totalUsers;
    }

    /**
     * Runs the test.
     *
     * @param result Test result.
     */
    public void run(TestResult result) {

        group.setTestResult(result);
        int testCount = 0;

        while (testCount < totalUsers) {
            int randomBatchSize = getRandom(minBatchSize, maxBatchSize);
            int remainingIterationsToBePerformed = totalUsers - testCount;
            int batchSize = randomBatchSize > remainingIterationsToBePerformed ? remainingIterationsToBePerformed : randomBatchSize;

            for (int batchIndex = 0; batchIndex < batchSize; batchIndex++) {

                if (result.shouldStop()) {
                    barrier.cancelThreads(remainingIterationsToBePerformed);
                    break;
                }

                testCount++;
                test.run(result);

            }

            sleep(getDelay());

        }

        waitForTestCompletion();

        cleanup();
    }


    protected void waitForTestCompletion() {
        //
        // TODO: May require a strategy pattern
        //       if other algorithms emerge.
        //
        if (enforceTestAtomicity) {
            waitForAllThreadsToComplete();
        } else {
            waitForThreadedTestThreadsToComplete();
        }
    }

    protected void waitForThreadedTestThreadsToComplete() {
        while (!barrier.isReached()) {
            sleep(50);
        }
    }

    protected void waitForAllThreadsToComplete() {
        while (group.activeCount() > 0) {
            sleep(50);
        }
    }

    protected void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ignored) {
        }
    }

    protected void cleanup() {
        try {
            group.destroy();
        } catch (Throwable ignored) {
        }
    }

    public String toString() {
        if (enforceTestAtomicity) {
            return "LoadTest (ATOMIC): " + test.toString();
        } else {
            return "LoadTest (NON-ATOMIC): " + test.toString();
        }
    }

    protected long getDelay() {
        return timer.getDelay();
    }

    private static int getRandom(int min, int max) {
        int n = max - min;
        if (n <= 0) n = 1;
        return min + RandomUtils.nextInt(n);
    }
}
