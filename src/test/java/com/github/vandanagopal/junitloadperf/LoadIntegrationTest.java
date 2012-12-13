package com.github.vandanagopal.junitloadperf;


import com.github.vandanagopal.junitloadperf.annotations.LoadPerf;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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
