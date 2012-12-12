package org.junit.load.perf;


import junit.framework.TestCase;
import org.junit.After;
import org.junit.load.perf.annotations.LoadPerf;
import org.junit.load.perf.annotations.LoadPerfBefore;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(LoadRunner.class)
public class LoadTest extends TestCase {

    private static List loadBefore1Count =new ArrayList<Integer>();
    private static List loadBefore2Count=new ArrayList<Integer>();
    private static List loadTestCount =new ArrayList<Integer>();

    public LoadTest(String name) {
        super(name);
    }

    @LoadPerfBefore(priority = 1,concurrentUsers = 5)
    public void setUp1(){
        loadBefore1Count.add(1);
    }

    @LoadPerfBefore(priority = 2,concurrentUsers = 7)
    public void setUp2(){
        assertEquals(5,loadBefore1Count.size());
        loadBefore2Count.add(1);
    }

    @LoadPerf(concurrentUsers = 10)
    public void shouldRunMultipleTimesWithConcurrentUsersAndWithLoadBefore(){
        loadTestCount.add(1);

    }

    @After
    public void after(){
        assertEquals(5, loadBefore1Count);
        assertEquals(7, loadBefore2Count);
        assertEquals(10, loadTestCount);
    }

}
