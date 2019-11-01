package memory.cacheMappingStrategy;

import memory.Cache;
import memory.Memory;
import transformer.Transformer;

import java.util.Arrays;

public class AssociativeMapping extends MappingStrategy {  // 全相联映射

    /**
     * @param blockNO 内存数据块的块号
     * @return cache数据块号 22-bits  [前22位有效]
     */
    @Override
    public char[] getTag(int blockNO) {
        return new Transformer().intToBinary(String.valueOf(blockNO)).substring(10,32).toCharArray();
    }

    @Override
    public int map(int blockNO) {
        return super.replacementStrategy.isHit(0,(Cache.CACHE_SIZE_B/Cache.LINE_SIZE_B)-1,getTag(blockNO));
    }

    @Override
    public int writeCache(int blockNO) {
        Memory memoryInstance=Memory.getMemory();
        String eip=new Transformer().intToBinary(String.valueOf(blockNO)).substring(10,32)+"0000000000";
        char[] data=memoryInstance.read(eip,Cache.LINE_SIZE_B);
        return super.replacementStrategy.writeCache(0,(Cache.CACHE_SIZE_B/Cache.LINE_SIZE_B)-1,getTag(blockNO),data);
    }
}
