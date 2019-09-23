package cpu.present.transformers;

import cpu.present.Number;
import cpu.present.PresentType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformerFactory {

	/**
     * calculate the Binary Code {@link Transformer#code} from possible data-presentation
     * If raw data-type is Integer, the Binary Code should be a Two-Complement Code or a 8421BCD code
     * Else if raw data-type is Float, the Binary Code should be a True-Value Presentation(IEEE 754, default 32 bits with 8-bits length exponents and 23-bits length fraction)
     **/
    public static Transformer getTransformer(PresentType rawType, PresentType storageType, String... args) {
        String code = args[0];
        int eLength;
        int sLength;

        if (args.length > 1) {
            eLength = Integer.parseInt(args[1]);
            sLength = Integer.parseInt(args[2]);
        } else {
            eLength = 0;
            sLength = Number.DATA_SIZE_LIMITATION;
        }

        if (rawType.equals(PresentType.BCD.W8421)) {
            String ret="";
            if(Pattern.matches("^1101[0-9]*",code)){
                ret="-";
            }
            boolean flag=false;
            Pattern p=Pattern.compile("\\d{4}");
            Matcher m=p.matcher(code.substring(4,code.length()));
            for(int i=0,j=0;i<7;i++){
                if(m.find())
                    j=Integer.parseInt(m.group(),2);
                if(j==0) {
                    if (!flag) {
                        continue;
                    }
                }else{
                    flag=true;
                }
                ret = ret + String.valueOf(j);
            }
            return  new Transformer(PresentType.DEC.INTEGER,ret);
            //TODO
        } else if (rawType.equals(PresentType.BIN.TWOS_COMPLEMENT)) {
            // nothing to do
        } else if (rawType.equals(PresentType.DEC.INTEGER)) {
            if (storageType.equals(PresentType.BCD.W8421)) {
                StringBuilder ret=new StringBuilder(code.charAt(0)=='-'?"1101":"1100");
                int i=0;
                code=code.replace("-","");
                if(code.length()>7){
                    code=code.substring(code.length()-7,code.length());
                }
                for(i=0;i<7-code.length();i++)
                    ret.append("0000");
                i=0;
                while(i<code.length()){
                    ret.append(String.format("%4s",Integer.toBinaryString(code.charAt(i++)-'0')).replace(" ","0"));
                }
                return new Transformer(PresentType.BCD.W8421,ret.toString());
            } else if(storageType.equals(PresentType.BIN.TWOS_COMPLEMENT)) {
                boolean flag=(code.charAt(0)=='-');
                StringBuilder tmp=new StringBuilder();
                String ret="";
                String ad="";
                BigDecimal ans=new BigDecimal(code.replace("-",""));
                while(ans.compareTo(new BigDecimal("0"))==1){
                    tmp.append(ans.remainder(new BigDecimal("2")).toString());
                    ans=ans.divideToIntegralValue(new BigDecimal("2"));
                }
                tmp=tmp.reverse();
                ret=tmp.toString();
                if(flag) {
                    ret=tmp.insert(0,"00").toString();
                    ret=ret.replace("0","2").replace("1","0").replace("2","1");
                    char[] status=ret.toCharArray();
                    for(int i=ret.length()-1,jud=1;i>=0;i--){
                        status[i]=(char)((jud^(ret.charAt(i)-'0'))+'0');
                        jud=((ret.charAt(i)-'0')&jud);
                    }
                    ret= Arrays.toString(status).replaceAll("[\\[\\]\\s,]", "");
                }
                if(ret.length()<=32) {
                    for (int i = 1; i <= 32 - ret.length();i++)
                        ad=ad.replaceFirst("",flag?"1":"0");
                } else{
                    ret=ret.substring(ret.length()-32,ret.length());
                }
                return new Transformer(PresentType.BIN.TWOS_COMPLEMENT,ad+ret);
            }else if(storageType.equals(PresentType.DEC.FLOAT)){
                return new Transformer(PresentType.DEC.FLOAT,code+".0");
            }else if(storageType.equals(PresentType.BIN.FLOAT)){
                return TransformerFactory.getTransformer(PresentType.DEC.FLOAT,PresentType.BIN.FLOAT,code+".0","8","23");
            }
        } else if (rawType.equals(PresentType.DEC.FLOAT)) {
            StringBuilder ret=new StringBuilder(code.charAt(0)=='-'?"1":"0");
            String[] parts=code.split("\\.");
            StringBuilder temp=new StringBuilder(Integer.toBinaryString(Integer.valueOf(parts[0].replace("-",""))));
            int dotloc=(temp.charAt(0)=='0'?0:temp.length());
            float remain=Float.valueOf("0."+parts[1]);
            while(remain!=0){
                remain*=2;
                if(remain>=1){
                    temp.append("1");
                    remain-=1;
                } else{
                    temp.append("0");
                }
            }
            if(dotloc==0){
                dotloc=127-(temp.indexOf("1")==-1?temp.indexOf("1"):127);
                if(dotloc<0)dotloc=0;
            }
            else{
                dotloc=126+dotloc;
            }
            ret.append(String.format("%"+eLength+"s",Integer.toBinaryString(dotloc)).replace(" ","0"));
            ret.append(String.format("%-"+sLength+"s",temp.substring(1)).replace(" ","0"));
            return new Transformer(PresentType.BIN.FLOAT,ret.toString());
        } else if (rawType.equals(PresentType.BIN.FLOAT)) {
            // nothing to do
        }

        return new Transformer(storageType, code, String.valueOf(eLength), String.valueOf(sLength));
    }



}