package memory.cacheReplacementStrategy;

public abstract class ReplacementStrategy {

    /**
     * 在start-end范围内查找是否命中
     * @param start 起始行
     * @param end 结束行 闭区间
     */
    public abstract int isHit(int start, int end, char[] addrTag);

    /**
     * 在未命中的情况下将内存中的数写入cache
     * @param start 起始行
     * @param end 结束行 闭区间
     * @param addrTag tag
     * @param input  数据
     * @return
     */
    public abstract int writeCache(int start, int end, char[] addrTag, char[] input);
}
