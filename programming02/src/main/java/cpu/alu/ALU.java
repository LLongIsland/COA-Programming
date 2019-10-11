package cpu.alu;

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

    //add two integer
    String add(String src, String dest) {
        ans=new StringBuilder();
        int c=0,s=0;
        for(int i=dest.length()-1;i>=0;i--){
            int x=src.charAt(i)-'0',y=dest.charAt(i)-'0';
            s=x^y^c;
            c=(x&c)|(y&c)|(x&y);
            ans.append(s);
        }
        CF=""+c;
        OF=(src.charAt(0)==dest.charAt(0))&&(src.charAt(0)!=ans.charAt(0))?"1":"0";
		return ans.reverse().toString();
    }

    //sub two integer
    // dest - src
    String sub(String src, String dest) {
        ans=new StringBuilder();
        src=getComplement(src);
		return add(src,dest);
	}

    //signed integer mod
    String imod(String src, String dest) {
        int a=0,b=0;
        if(src.charAt(0)=='1'){
            a=1;
            src=getComplement(src);
        }
        if(dest.charAt(0)=='1'){
            b=1;
            dest=getComplement(dest);
        }
        while(dest.charAt(0)!='1'){
            dest=sub(src,dest);
        }
        dest=add(src,dest);
        if((a^b)==1) {
            if (b == 1) return getComplement(dest);
            else return dest;
        }
        return dest;
    }

    String and(String src, String dest) {
        for (int i=0;i<32;i++)
            ans.append(dest.charAt(i)==src.charAt(i)&&dest.charAt(i)=='1'?'1':'0');
		return ans.toString();
    }

    String or(String src, String dest) {
        for (int i=0;i<32;i++)
            ans.append(dest.charAt(i)==src.charAt(i)&&dest.charAt(i)=='0'?'0':'1');
		return ans.toString();
    }

    String xor(String src, String dest) {
        for (int i=0;i<32;i++)
            ans.append(dest.charAt(i)!=src.charAt(i)?'1':'0');
		return ans.toString();
    }

    String shl(String src, String dest) {
        int count=Integer.parseInt(src,2);
        if(count>=32)
            return "00000000000000000000000000000000";
        ans=new StringBuilder(dest);
        while(count>0){
            ans.deleteCharAt(0);
            ans.append('0');
            count--;
        }
		return ans.toString();
    }

    String shr(String src, String dest) {
        int count=Integer.parseInt(src,2);
        if(count>=32)
            return "00000000000000000000000000000000";
        ans=new StringBuilder(dest);
        while(count>0){
            ans.deleteCharAt(ans.length()-1);
            ans.insert(0,'0');
            count--;
        }
        return ans.toString();
    }

    String sal(String src, String dest) {
		return shl(src,dest);
    }

    String sar(String src, String dest) {
        ans=new StringBuilder(shr(src,dest));
        if(dest.charAt(0)=='1'){
            for(int i=0;i<ans.length();i++){
                if(ans.charAt(i)=='1')break;
                ans.replace(i,i+1,"1");
            }
        }
		return ans.toString();
    }

    String rol(String src, String dest) {
        int count=Integer.parseInt(src,2);
        count%=32;
        char temp;
        ans=new StringBuilder(dest);
        while(count>0){
            temp=ans.charAt(0);
            ans.deleteCharAt(0);
            ans.append(temp);
            count--;
        }
		return ans.toString();
    }

    String ror(String src, String dest) {
        int count=Integer.parseInt(src);
        count%=32;
        char temp;
        ans=new StringBuilder(dest);
        while(count>0){
            temp=ans.charAt(ans.length()-1);
            ans.deleteCharAt(ans.length()-1);
            ans.insert(0,temp);
            count--;
        }
		return ans.toString();
    }

}
