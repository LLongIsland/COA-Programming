package cpu.alu;

import util.IEEE754Float;

import java.util.regex.Pattern;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用4位保护位进行计算，计算完毕直接舍去保护位
 * TODO: 浮点数运算
 */
public class FPU {

    /**
     * compute the float mul of a * b
     * 分数部分(23 bits)的计算结果直接截取前23位
     */
    String mul(String a, String b) {
        char flag=a.charAt(0)==b.charAt(0)?'0':'1';
        if(Pattern.matches("0{31}",a.substring(1,32)))
            if(Pattern.matches("1{8}",b.substring(1,9)))
                return "(0|1){1}1{8}(0+1+|1+0+)(0|1)*";
            else return flag+a.substring(1,32);
        ALU alu=new ALU();
        String ex=alu.add(a.substring(1,9),alu.add(b.substring(1,9),"10000001"));
        String sig=alu.mul("000000001"+a.substring(9,32),"000000001"+b.substring(9,32),"not cut");
        return flag+ex+sig.substring(sig.indexOf('1')+1,sig.indexOf('1')+24);
    }

}
