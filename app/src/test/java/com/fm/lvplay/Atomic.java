package com.fm.lvplay;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

public class Atomic {
    private static AtomicLong atomicLong = new AtomicLong();

    private static Integer[] arrayOne = new Integer[]{0, 1, 2, 3, 0, 5, 6, 0, 56, 0};
    private static Integer[] arrayTwo = new Integer[]{0, 1, 2, 3, 0, 5, 6, 0, 56, 0};

    @Test
    public void testAtoLong() throws InterruptedException {
        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                int len = arrayOne.length;
                for (int i = 0; i < len; i++) {
                    if (arrayOne[i].intValue() == 0) {
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });

        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                int len = arrayTwo.length;
                for (int i = 0; i < len; i++) {
                    if (arrayTwo[i].intValue() == 0) {
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });

        threadOne.start();
        threadTwo.start();


        threadOne.join();
        threadTwo.join();

        System.out.println("count:" + atomicLong.get());

    }
}
