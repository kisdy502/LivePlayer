package com.fm.lvplay;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    /***
     * method1 为什么比 method2 快
     * 因为 method1 顺序读取内存，
     * method2 属于随机读取内存
     */
    @Test
    public void method1() {
        long[][] array = new long[10240][10240];
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10240; i++) {
            for (int j = 0; j < 10240; j++) {
                array[i][j] = i + 2 * j;
            }
        }

        long end = System.currentTimeMillis();
        long coust = end - start;
        System.out.println("coust1:"+coust);

    }

    @Test
    public void method2() {
        long[][] array = new long[10240][10240];
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10240; i++) {
            for (int j = 0; j < 10240; j++) {
                array[j][i] = i + 2 * j;
            }
        }

        long end = System.currentTimeMillis();
        long coust = end - start;
        System.out.println("coust2:"+coust);

    }
}