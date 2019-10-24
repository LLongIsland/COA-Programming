package cpu.alu;

import transformer.Transformer;
import util.BinaryIntegers;
import util.IEEE754Float;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 加减乘除
 */
public class ALU {

    // 模拟寄存器中的进位标志位
    private String CF = "0";

    // 模拟寄存器中的溢出标志位
    private String OF = "0";

    private StringBuilder ans=new StringBuilder();
    public static String getComplement(String tar) {
        tar = tar.replace("0", "2").replace("1", "0").replace("2", "1");
        char[] status = tar.toCharArray();
        for (int i = tar.length() - 1, jud = 1; i >= 0; i--) {
            status[i] = (char) ((jud ^ (tar.charAt(i) - '0')) + '0');
            jud = ((tar.charAt(i) - '0') & jud);
        }
        return Arrays.toString(status).replaceAll("[\\[\\]\\s,]", "");
    }
    String add(String src, String dest) {
        ans=new StringBuilder();
        int c=0,s=0;
        for(int i=dest.length()-1;i>=0;i--){
            int x=src.charAt(i)-'0',y=dest.charAt(i)-'0';
            s=x^y^c;
            c=(x&c)|(y&c)|(x&y);
            ans.append(s);
        }
        return ans.reverse().toString();
    }
    String shift(String src){
        return src.substring(1)+"0";
    } //特殊的位移一位方法

    /**
     * 返回两个二进制整数的除法结果 operand1 ÷ operand2
     * @param operand1 32-bits
     * @param operand2 32-bits
     * @return 65-bits overflow + quotient + remainder
     */
    String div(String operand1, String operand2) {
        if(Pattern.matches("0{32}",operand2)){
            if(!Pattern.matches("0{32}",operand1))
                throw new ArithmeticException();
            else return BinaryIntegers.NaN;
        }else if(Pattern.matches("0{32}",operand1)) {
            return "0"+BinaryIntegers.ZERO+BinaryIntegers.ZERO;
        }

        char flag1=operand1.charAt(0);
        char flag2=operand2.charAt(0);
        String divisor=operand2+"000000000000000000000000000000000"; //divisor=高32位+低32位0+商末尾补充位0
        String rev=getComplement(operand2)+"000000000000000000000000000000000";// -divisor=高32位补码+低32位0+商末尾补充位0
        String ans=(operand1.charAt(0)=='1'?"11111111111111111111111111111111":"00000000000000000000000000000000")+
                    operand1+"0"; //高32位符号位+低32位被除数+补充位0
        if(flag1==flag2)
            ans=add(ans,rev);
        else ans=add(ans,divisor);
        if(ans.charAt(0)==flag2) {
            ans=ans.substring(0,ans.length()-1)+"1";
        }
        for(int i=0;i<32;i++){
            if(ans.charAt(0)==flag2) {
                ans=shift(ans);
                ans=add(ans,rev);
            }else{
                ans=shift(ans);
                ans=add(ans,divisor);
            }
            if(ans.charAt(0)==flag2)
                ans=ans.substring(0,ans.length()-1)+"1";
        }
        String quotient=ans.substring(32,65);
        String reminder=ans.substring(0,32);
        quotient = shift(quotient).substring(0,32);
        if(quotient.charAt(0)=='1')
            quotient = add(quotient, "00000000000000000000000000000001");
        if(reminder.charAt(0)!=flag1) {
            if (flag1 == flag2)
                reminder = add(reminder, operand2);
            else
                reminder=add(reminder,getComplement(operand2));
        }
        if(reminder.equals(getComplement(operand2))){
            reminder="00000000000000000000000000000000";
            quotient=add(quotient,getComplement("00000000000000000000000000000001"));
        }else if(reminder.equals(operand2)) {
            reminder="00000000000000000000000000000000";
            quotient=add(quotient, "00000000000000000000000000000001");
        }
        return (flag2!='0'&&flag1==flag2&&quotient.charAt(0)==flag1?"1":"0")+quotient+reminder;
    }

}
