package memory.cacheMappingStrategy;

import memory.Cache;
import memory.Memory;
import transformer.Transformer;

/**
 * 4路-组相连映射 n=4,   14位标记 + 8位组号 + 10位块内地址
 * 256个组，每个组4行
 */
public class SetAssociativeMapping extends MappingStrategy{

    /**
     *
     * @param blockNO 内存数据块的块号
     * @return cache数据块号 22-bits  [前14位有效]
     */
    @Override
    public char[] getTag(int blockNO) {
        Transformer t = new Transformer();
        return (t.intToBinary(String.valueOf(blockNO)).substring(10, 24) + "00000000").toCharArray();
    }

    /**
     *
     * @param blockNO 目标数据内存地址前22位int表示
     * @return -1 表示未命中
     */
    @Override
    public int map(int blockNO) {
        return super.replacementStrategy.isHit(Integer.parseInt(getSetBits(blockNO)+"00",2),
                Integer.parseInt(getSetBits(blockNO)+"11",2),getTag(blockNO));
    }

    @Override
    public int writeCache(int blockNO) {
        Memory memoryInstance=Memory.getMemory();
        String eip=new Transformer().intToBinary(String.valueOf(blockNO)).substring(10,32)+"0000000000";
        char[] data=memoryInstance.read(eip,Cache.LINE_SIZE_B);
        return super.replacementStrategy.writeCache(Integer.parseInt(getSetBits(blockNO)+"00",2),
                Integer.parseInt(getSetBits(blockNO)+"11",2),
                getTag(blockNO),data);
    }

    private static String getSetBits(int blockN0){
        Transformer t=new Transformer();
        return "0"+t.intToBinary(String.valueOf(blockN0)).substring(24,32);
    }
}










