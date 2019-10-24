package cpu.alu;

import transformer.Transformer;
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
     * compute the float mul of a / b
     */
    String div(String a, String b) {
        char flag=a.charAt(0)==b.charAt(0)?'0':'1';
        if(Pattern.matches("[01]0{31}",b))
            if(Pattern.matches("[01]0{31}",a)) return IEEE754Float.NaN;
            else throw new ArithmeticException();
        if(Pattern.matches("1{8}0{23}",a.substring(1,a.length()))||Pattern.matches("[01]0{31}",a))
            return flag+a.substring(1,a.length());
        ALU alu=new ALU();
        String ex=alu.add(a.substring(1,9),alu.add(ALU.getComplement(b.substring(1,9)),"01111111"));
        String sig=alu.div("00001"+a.substring(9,32)+"0000","00001"+b.substring(9,32)+"0000");
        sig=sig.charAt(32)+sig.substring(38);
        for(int i=0;i<sig.indexOf('1');i++)
            ex=alu.add(ex,"11111111");
        return flag+ex+sig.substring(sig.indexOf('1')+1,sig.indexOf('1')+24);
    }

}
