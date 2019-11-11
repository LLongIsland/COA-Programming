package memory.cacheReplacementStrategy;

import memory.Cache;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;

/**
 * 先进先出算法
 */
public class FIFOReplacement extends ReplacementStrategy {

    //倒序的链表，方便从前往后遍历找到最早入队的
    LinkedList<Integer> queue = new LinkedList<>();

    @Override
    public int isHit(int start, int end, char[] addrTag) {
        Cache cacheInstance = Cache.getCache();
        for (int i = start; i <= end; i++) {
            if (cacheInstance.getCacheLineValidBit(i))
                if (Arrays.equals(addrTag, cacheInstance.getCacheLineTag(i))) {
                    return i;
                }
        }
        return -1;
    }

    @Override
    public int writeCache(int start, int end, char[] addrTag, char[] input) {
        Cache cacheInstance=Cache.getCache();
        int pointer = -1,addup=0,invalid=-1;
        for (int element : queue) {
            if (element >= start && element <= end) {
                if(!cacheInstance.getCacheLineValidBit(element)){
                    invalid=element;
                    break;
                }
                else{
                    pointer = element;
                    addup++;
                }
            }
        }
        if(pointer==-1||addup<(end-start+1)){ //区间未满
            int lineN0=(invalid==-1?start+addup:Math.min(invalid,start+addup));
            cacheInstance.setCacheLine(lineN0,addrTag,input);
            queue.add(0,lineN0);
            return lineN0;
        }else{
            cacheInstance.setCacheLine(pointer,addrTag,input);
            queue.remove((Integer) pointer);
            queue.add(0,pointer);
            return pointer;
        }
    }


}
