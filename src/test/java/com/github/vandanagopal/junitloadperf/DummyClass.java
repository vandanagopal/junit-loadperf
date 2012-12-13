package com.github.vandanagopal.junitloadperf;

import org.springframework.stereotype.Component;

@Component
public class DummyClass {

    private int foo=1;

    public int getFoo() {
        return foo;
    }
}
