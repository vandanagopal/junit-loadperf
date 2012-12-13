package com.github.vandanagopal.junitloadperf;


import com.github.vandanagopal.junitloadperf.annotations.LoadPerf;
import com.github.vandanagopal.junitloadperf.annotations.LoadPerfBefore;
import junit.framework.TestCase;
import org.junit.After;
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
        assertEquals(5, loadBefore1Count.size());
        assertEquals(7, loadBefore2Count.size());
        assertEquals(10, loadTestCount.size());
    }

}
