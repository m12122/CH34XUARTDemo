package cn.wch.ch34xuartdemo;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        byte ctrlout=0x30;
        ctrlout &=~0x10;
        System.out.println(String.format("%08x",ctrlout));
    }
}