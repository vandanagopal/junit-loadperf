junit-loadperf
==============

Enhancements on the existing JunitPerf by adding annotated tests and Spring autowiring, etc.

Features of the project -
This project has several improvements over the existing JunitPerf in that -
1. Annotated Performance tests are now possible.
2. Annotation to trigger Staggered performance tests are now available
3. It is now possible to autowire Spring Dependencies in your test classes
4. The time taken per concurrent user is written to a file.


Example

A Load test class that has two types of load tests  - a normal load test foo() and a staggered load test fooStaggered()

@RunWith(LoadRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class LoadTest extends TestCase {

    @Autowired
    private DummyClass dummy;

    public LoadTest(String name) {
        super(name);
    }

    //SetUp1 that can run a number of times in parallel
    @LoadPerfBefore(priority = 1,concurrentUsers = 5)
    public void setUp1(){

    }

    //SetUp2 that can run a number of times in parallel
    @LoadPerfBefore(priority = 2,concurrentUsers = 7)
    public void setUp2(){

    }

    @LoadPerf(concurrentUsers = 10)
    public void foo(){

    }

    @LoadPerfStaggered(totalNumberOfUsers = 10,minMaxRandomBatchSizes = {"2","4"}, minDelayBetweenBatchesInMillis = 20,delayVariation = 10)
    public void fooStaggered(){

    }

}

This class has spring autowiring set up as specified by ContextConfiguration annotation. The object dummy is autowired.
The first test foo() runs 10 times in parallel. The two functions setup1 and setup2 are run before foo
for any set up that needs to be done  -this is similar to @Before but can run a number of times in parallel.

The function fooStaggered() runs a total of 10 times. However not all 10 instances run in parallel - the minimum number
of instances that run in parallel are 2 and the maximum is 4 as specified by minMaxRandomBatchSizes.
The minimum delay between each batch can be adjusted and here is 20 millis. This delay can vary from a minimum of 20 ms
to a maximum of 20 + delay variation. So the delay between each batch is a random value chosen from this range.


