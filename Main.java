import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import leetcodeHot100.stack;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.test();
    }

    private void test() {
        AtomicInteger arg = new AtomicInteger(0);
        incr(arg);
        System.out.println(arg.get());
    }

    private void incr(AtomicInteger a) {
        a.incrementAndGet();
    }
}