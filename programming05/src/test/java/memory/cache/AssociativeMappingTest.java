package memory.cache;


import memory.Cache;
import memory.Memory;
import memory.cacheMappingStrategy.AssociativeMapping;
import memory.cacheReplacementStrategy.FIFOReplacement;
import memory.cacheReplacementStrategy.LFUReplacement;
import memory.cacheReplacementStrategy.LRUReplacement;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class AssociativeMappingTest {
    @Test
    public void test01() {
        Memory memory = Memory.getMemory();
        Cache cache = Cache.getCache();
        cache.setStrategy(new AssociativeMapping(), new LFUReplacement());
        char[] input = new char[1024 * 1024];
        char[] second = new char[1024];
        char[] third = new char[1024];
        Arrays.fill(input, (char) 0b11111111);
        Arrays.fill(second, (char) 0b10001111);
        Arrays.fill(third, (char) 0b10001100);
        String eipBase = "00000000000000000000000000000000";
        String eipSec = "00000000000000000000010000000000";
        String eipTrd = "00000111000000000000000000000000";
		//write First
        memory.write(eipBase, input.length, input);
        char[] dataRead = cache.read(eipBase, input.length);
        assertTrue(Arrays.equals(input, dataRead));
		assertTrue(cache.checkStatus(new int[]{0}, new boolean[]{true}, new char[][]{"0000000000000000000000".toCharArray()}));
        //write Sec
        memory.write(eipSec, second.length, second);
        dataRead = cache.read(eipSec, second.length);
        assertTrue(Arrays.equals(second, dataRead));
		//write Third
		memory.write(eipTrd, third.length, third);
		dataRead = cache.read(eipTrd, third.length);
		assertTrue(Arrays.equals(third, dataRead));
		assertTrue(cache.checkStatus(new int[]{1, 0}, new boolean[]{true, true}, new char[][]{"0000000000000000000001".toCharArray(), "0000011100000000000000".toCharArray()}));
        cache.clear();
    }

    @Test
    public void test02() {
        Memory memory = Memory.getMemory();
        Cache cache = Cache.getCache();
        cache.setStrategy(new AssociativeMapping(), new LRUReplacement());
        char[] input1 = new char[1024 * 1024];
        char[] input2 = new char[1024];
        Arrays.fill(input1, (char) 0b10001011);
        Arrays.fill(input2, (char) 0b10101101);
        String eipBase = "00000000000000000000000000000000";
        String eip2 = "00000000000000000001110000000000";
        memory.write(eipBase, input1.length, input1);
        char[] dataRead = cache.read(eipBase, input1.length);
        assertTrue(Arrays.equals(input1, dataRead));
        //eip2
        memory.write(eip2, input2.length, input2);
        dataRead = cache.read(eip2, input2.length);
        assertTrue(Arrays.equals(input2, dataRead));
		assertTrue(cache.checkStatus(new int[]{0}, new boolean[]{true}, new char[][]{"0000000000000000000000".toCharArray()}));
        cache.clear();
    }

    @Test
    public void test03() {
        Memory memory = Memory.getMemory();
        Cache cache = Cache.getCache();
        cache.setStrategy(new AssociativeMapping(), new FIFOReplacement());

        char[] input1 = new char[1024 * 1024];
        char[] input2 = new char[1024];
        char[] input3 = new char[1024];
        Arrays.fill(input1, (char)0b11111111);
        Arrays.fill(input2, (char)0b01010101);
        Arrays.fill(input3, (char)0b01110111);
        String eip1 = "00000000000000000000000000000000";
        String eip2 = "00000010101000000000010000000000";
        String eip3 = "00000010011100000000010000000001";

        memory.write(eip1, input1.length, input1);
        // cache里现在应该全是 0b11111111
        char[] dataRead = cache.read(eip1, 1024 * 1024);
        assertTrue(Arrays.equals(input1, dataRead));

        memory.write(eip2, input2.length, input2);
        // cache中第一个块应该被替换，相应的tag需要改动
        dataRead = cache.read(eip2, 1024);
        assertTrue(Arrays.equals(input2, dataRead));
        cache.checkStatus(new int[]{0}, new boolean[]{true}, new char[][]{"0000001010100000000001".toCharArray()});

        // cache中的第二个块和第三个块应该被替换，相应的tag需要改动
        memory.write(eip3, input3.length, input3);
        dataRead = cache.read(eip3, 1024);
        assertTrue(Arrays.equals(input3, dataRead));
        assertTrue(cache.checkStatus(new int[]{1, 2}, new boolean[]{true, true}, new char[][]{"0000001001110000000001".toCharArray(), "0000001001110000000010".toCharArray()}));
		cache.clear();
    }
}
