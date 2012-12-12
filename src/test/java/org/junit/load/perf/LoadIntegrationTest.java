package org.junit.load.perf;


import junit.framework.TestCase;
import org.junit.After;
import org.junit.load.perf.annotations.LoadPerf;
import org.junit.load.perf.annotations.LoadPerfBefore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(LoadRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class LoadIntegrationTest extends TestCase {


    @Autowired
    private DummyClass dummy;

    public LoadIntegrationTest(String name) {
        super(name);
    }

    @LoadPerf(concurrentUsers = 2)
    public void shouldAutowireSpringDependency(){
        assertEquals(1,dummy.getFoo());
    }
}
