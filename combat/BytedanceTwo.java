package combat;

import java.util.ArrayDeque;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BytedanceTwo {

    public static void main(String[] args) {
        char[][] rels = new char[][] {
                { 'a', 'b' },
                { 'a', 'c' },
                { 'b', 'd' },
                { 'd', 'a' },
        };
        System.out.println(detectLoop(rels));
    }

    
}
