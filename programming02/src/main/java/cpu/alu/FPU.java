package cpu.alu;

import java.util.regex.Pattern;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * TODO: 浮点数运算
 */
public class FPU {

    /**
     * compute the float add of (a + b)
     **/
    String add(String a,String b){
        String ret="";
        if(a.substring(1,32).equals("0000000000000000000000000000000"))
            return b;
        else if(b.substring(1,32).equals("0000000000000000000000000000000"))
            return a;
        if(a.substring(1,9).equals("11111111"))
            return a;
        int e=Integer.parseInt(a.substring(1,9),2)-Integer.parseInt(b.substring(1,9),2);
        if(e<0){
            e=-e;
            String temp=a;
            a=b;
            b=temp;
        }
        boolean flag=false;
        StringBuilder sig=new StringBuilder(b.substring(9,32));
        if(e!=0){
            flag=true;
            sig.insert(0,'1');
            e--; //第一次位移把指数给加到significant里
            sig.append("000");//保护位
            for(int i=1;i<=e;i++){
                sig.deleteCharAt(sig.length()-1);
                sig.insert(0,'0');
                if(sig.toString().equals("00000000000000000000000"))
                    return a;
            }
        }
        else
            sig.append("0000");
        sig.insert(0,flag?'0':'1').insert(0,'0');
        StringBuilder tmp=new StringBuilder(a.substring(9,32)).insert(0,'1').insert(0,'0').append("0000");
        if(a.charAt(0)!=b.charAt(0)){
            if(a.charAt(0)=='1')
                tmp=new StringBuilder(ALU.getComplement(tmp.toString()));
            else sig=new StringBuilder(ALU.getComplement(sig.toString()));
        }
        ALU alu=new ALU();
        StringBuilder after=new StringBuilder(alu.add(tmp.toString(),sig.toString()));
        if(a.charAt(0)!=b.charAt(0)){
            if(after.charAt(0)=='1') {
                ret += "1";
                sig=new StringBuilder(ALU.getComplement(after.toString()));
            }
            else{
                ret+="0";
                sig=after;
            }
        }else{
            ret+=a.charAt(0);
            sig=after;
        }
        if(Pattern.matches("^0*$",sig.toString()))
            return "00000000000000000000000000000000";
        int del=1-sig.indexOf("1");
        e=Integer.parseInt(a.substring(1,9),2);
        e+=del;
        ret+= String.format("%8s",Integer.toBinaryString(e)).replaceAll(" ","0");
        if(ret.equals("011111111")||ret.equals("111111111"))
            return ret.charAt(0)=='1'?"11111111100000000000000000000000":"01111111100000000000000000000000";
        ret+=sig.substring(sig.indexOf("1")+1,sig.indexOf("1")+24);
        return ret;
    }

    /**
     * compute the float add of (a - b)
     **/
    String sub(String a,String b){
		return add(a,b.charAt(0)=='1'?'0'+b.substring(1,32):'1'+b.substring(1,32));
    }

}
