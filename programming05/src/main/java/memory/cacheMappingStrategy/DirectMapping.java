package memory.cacheMappingStrategy;

import memory.Cache;
import memory.Memory;
import transformer.Transformer;

import java.util.Arrays;

/**
 * 直接映射 12位标记 + 10位块号 + 10位块内地址
 */
public class DirectMapping extends MappingStrategy {

    /**
     * @param blockNO 内存数据块的块号
     * @return cache数据块号 22-bits  [前12位有效]
     */
    @Override
    public char[] getTag(int blockNO) {
        Transformer t = new Transformer();
        return (t.intToBinary(String.valueOf(blockNO)).substring(10, 22) + "0000000000").toCharArray();
    }


    /**
     * 根据内存地址找到对应的行是否命中，直接映射不需要用到替换策略
     *
     * @param blockNO
     * @return -1 表示未命中
     */
    @Override
    public int map(int blockNO) {
        Transformer t = new Transformer();
        int lineN0 = Integer.parseInt("0" + t.intToBinary(String.valueOf(blockNO)).substring(22, 32),2);
        Cache cacheInstance = Cache.getCache();
        if (cacheInstance.getCacheLineValidBit(lineN0))
            if (Arrays.equals(cacheInstance.getCacheLineTag(lineN0), getTag(blockNO)))
                return lineN0;
        return -1;
    }

    /**
     * 在未命中情况下重写cache，直接映射不需要用到替换策略
     *
     * @param blockNO
     * @return
     */
    @Override
    public int writeCache(int blockNO) {
        Cache cacheInstance=Cache.getCache();
        Memory memory=Memory.getMemory();
        int lineN0=Integer.parseInt("0" + new Transformer().intToBinary(String.valueOf(blockNO)).substring(22, 32),2);
        String eip=new Transformer().intToBinary(String.valueOf(blockNO)).substring(10,32)+"0000000000";
        cacheInstance.setCacheLine(lineN0,getTag(blockNO),memory.read(eip,Cache.LINE_SIZE_B));
        return lineN0;
    }


}
