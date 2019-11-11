package memory.cacheReplacementStrategy;

import memory.Cache;

import java.util.Arrays;

/**
 * 最近最少用算法
 */
public class LRUReplacement extends ReplacementStrategy {

    long timeStamp=0l;

    /**
     *
     * @param start 起始位置
     * @param end 结束位置 闭区间
     */
    @Override
    public int isHit(int start, int end,char[] addrTag) {
        Cache cacheInstance=Cache.getCache();
        for(int i=start;i<=end;i++){
            if(cacheInstance.getCacheLineValidBit(i))
                if(Arrays.equals(cacheInstance.getCacheLineTag(i),addrTag)) {
                    cacheInstance.setCacheTimeStamp(i,++timeStamp);
                    return i;
                }
        }
        return -1;
    }


    /**
     * 找到最小时间戳的行，替换
     * @param start 起始行
     * @param end 结束行 闭区间
     * @param addrTag tag
     * @param input  数据
     * @return
     */
    @Override
    public int writeCache(int start, int end, char[] addrTag, char[] input) {
        Cache cacheInstance=Cache.getCache();
        int pointer=-1,addup=0,invalid=-1;
        long time=Long.MAX_VALUE;
        for(int i=start;i<=end;i++){
            if(!cacheInstance.getCacheLineValidBit(i)){
                invalid=i;
                break;
            }else{
                if(cacheInstance.getCacheLineTimeStamp(i)<time){
                    time=cacheInstance.getCacheLineTimeStamp(i);
                    pointer=i;
                }
                addup++;
            }
        }
        if(pointer==-1||addup<(end-start+1)){
            cacheInstance.setCacheLine(invalid,addrTag,input);
            cacheInstance.setCacheTimeStamp(invalid,++timeStamp);
            return invalid;
        }else{
            cacheInstance.setCacheLine(pointer,addrTag,input);
            cacheInstance.setCacheTimeStamp(pointer,++timeStamp);
            return pointer;
        }
    }

}





























