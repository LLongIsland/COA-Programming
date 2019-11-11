package memory.cacheReplacementStrategy;

import memory.Cache;

import java.util.Arrays;

/**
 * 最近不经常使用算法
 */
public class LFUReplacement extends ReplacementStrategy {

    @Override
    public int isHit(int start, int end, char[] addrTag) {
        Cache cacheInstance=Cache.getCache();
        for(int i=start;i<=end;i++){
            if(cacheInstance.getCacheLineValidBit(i))
                if(Arrays.equals(cacheInstance.getCacheLineTag(i),addrTag)) {
                    cacheInstance.setCacheVisited(i,cacheInstance.getCacheLineVisited(i)+1);
                    return i;
                }
        }
        return -1;
    }

    @Override
    public int writeCache(int start, int end, char[] addrTag, char[] input) {
        Cache cacheInstance=Cache.getCache();
        int pointer=-1,addup=0,invalid=-1;
        int visited=Integer.MAX_VALUE;
        for(int i=start;i<=end;i++){
            if(!cacheInstance.getCacheLineValidBit(i)){
                invalid=i;
                break;
            }else{
                if(cacheInstance.getCacheLineVisited(i)<visited){
                    visited=cacheInstance.getCacheLineVisited(i);
                    pointer=i;
                }
                addup++;
            }
        }
        if(pointer==-1||addup<(end-start+1)){
            cacheInstance.setCacheLine(invalid,addrTag,input);
            cacheInstance.setCacheVisited(invalid,1);
            return invalid;
        }else{
            cacheInstance.setCacheLine(pointer,addrTag,input);
            cacheInstance.setCacheVisited(pointer,1);
            return pointer;
        }
    }
}
