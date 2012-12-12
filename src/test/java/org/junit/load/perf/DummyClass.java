package org.junit.load.perf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DummyClass {

    private int foo=1;

    public int getFoo() {
        return foo;
    }
}
