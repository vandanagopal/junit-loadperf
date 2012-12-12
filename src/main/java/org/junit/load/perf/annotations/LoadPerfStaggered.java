package org.junit.load.perf.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadPerfStaggered {
    int totalNumberOfUsers();
    String[] minMaxRandomBatchSizes();
    int minDelayInMillis();
    int delayVariation();
}
