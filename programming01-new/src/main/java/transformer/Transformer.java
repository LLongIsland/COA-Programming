package transformer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transformer {
    public static String getComplement(String tar) {
        tar = tar.replace("0", "2").replace("1", "0").replace("2", "1");
        char[] status = tar.toCharArray();
        for (int i = tar.length() - 1, jud = 1; i >= 0; i--) {
            status[i] = (char) ((jud ^ (tar.charAt(i) - '0')) + '0');
            jud = ((tar.charAt(i) - '0') & jud);
        }
        return Arrays.toString(status).replaceAll("[\\[\\]\\s,]", "");
    }

    /**
     * Integer to binaryString
     *
     * @param numStr to be converted
     * @return result
     */
    public String intToBinary(String numStr) {
        boolean flag = (numStr.charAt(0) == '-');
        StringBuilder tmp = new StringBuilder();
        String ret = "";
        String ad = "";
        BigDecimal ans = new BigDecimal(numStr.replace("-", ""));
        while (ans.compareTo(new BigDecimal("0")) == 1) {
            tmp.append(ans.remainder(new BigDecimal("2")).toString());
            ans = ans.divideToIntegralValue(new BigDecimal("2"));
        }
        tmp = tmp.reverse();
        ret = tmp.toString();
        if (flag) {
            ret = tmp.insert(0, "00").toString();
            ret = getComplement(ret);
        }
        if (ret.length() <= 32) {
            for (int i = 1; i <= 32 - ret.length(); i++)
                ad = ad.replaceFirst("", flag ? "1" : "0");
        }
        return ad + ret;
    }

    /**
     * BinaryString to Integer
     *
     * @param binStr : Binary string in 2's complement
     * @return :result
     */
    public String binaryToInt(String binStr) {
        boolean flag = (binStr.charAt(0) == '1');
        if (flag) {
            binStr = '1' + binStr;
            binStr = getComplement(binStr);
        }
        int dec = Integer.parseInt(binStr, 2);
        if (flag) {
            dec = -dec;
        }
        return Integer.toString(dec);
    }

    /**
     * Float true value to binaryString
     *
     * @param floatStr : The string of the float true value
     */
    public String floatToBinary(String floatStr) {
        boolean flag = (floatStr.charAt(0) == '-');
        if(flag)
            floatStr = floatStr.replaceFirst("-", "");
        BigDecimal tmp=new BigDecimal(floatStr);
        if(tmp.compareTo(new BigDecimal(Float.MAX_VALUE))!=-1)
            return flag?"-Inf":"+Inf";
        StringBuilder ret = new StringBuilder(flag ? "1" : "0");
        if (Pattern.compile("E").matcher(floatStr).find()) {
            String[] parts=floatStr.split("E");
            int e=Integer.parseInt(parts[1].replace("-",""));
            if(parts[1].charAt(0)=='-'){
                e=e-1+parts[0].length();
                floatStr=String.format("%"+e+"s",parts[0]).replace(" ","0").replace(".","");
                floatStr="0."+floatStr;
            }else{
                String[] latter=parts[0].split("\\.");
                floatStr=String.format("%-"+e+"s",latter[1]).replace(" ","0");
                floatStr=latter[0]+floatStr+".0";
            }
        }
        String[] parts = floatStr.split("\\.");
        BigDecimal prev=new BigDecimal(parts[0]);
        StringBuilder code=new StringBuilder();
        while (prev.compareTo(new BigDecimal("0")) == 1) {
            code.append(prev.remainder(new BigDecimal("2")).toString());
            prev = prev.divideToIntegralValue(new BigDecimal("2"));
        }
        code=code.reverse();
        if(code.length()==0)
            code.append("0");
        int dotloc = (code.charAt(0) == '0' ? 0 : code.length());
        BigDecimal remain =new BigDecimal("0." + parts[1]);
        while (remain.compareTo(BigDecimal.ZERO)!=0) {
            remain =remain.multiply(new BigDecimal("2"));
            if (remain.subtract(BigDecimal.ONE).abs().compareTo(new BigDecimal("1E-6"))==-1) {
                code.append("1");
                break;
            }else if(remain.compareTo(BigDecimal.ONE)==1){
                code.append("1");
                remain=remain.subtract(BigDecimal.ONE);
            } else {
                code.append("0");
            }
        }
        if (dotloc == 0) {
            dotloc = 127 - (code.indexOf("1") == -1 ? 127 :code.indexOf("1"));
            while(code.charAt(0)=='0')
                code.delete(0,1);
            if (dotloc < 0) dotloc = 0;
        } else {
            dotloc = 126 + dotloc;
        }
        ret.append(String.format("%8s", Integer.toBinaryString(dotloc)).replace(" ", "0"));
        ret.append(String.format("%-23s", code.substring(dotloc==0?0:1, code.length() > 24 ? 24 : code.length())).replace(" ", "0"));
        return ret.toString();
    }

    /**
     * Binary code to its float true value
     */
    public String binaryToFloat(String binStr) {
        if(binStr.substring(1,9).equals("11111111"))
            return binStr.charAt(0)=='1'?"-Inf":"+Inf";
        BigDecimal num=BigDecimal.ZERO;
        float addpart=1;
        int e=Integer.parseInt(binStr.substring(1,9),2);
        e-=127;
        if(!binStr.substring(1,9).equals("00000000")) {
            num = num.add(BigDecimal.ONE);
        }else{
            e+=1;
        }
        for(int i=9;i<32;i++){
            addpart*=0.5;
            if(binStr.charAt(i)=='1') {
                num = num.add(new BigDecimal("" + addpart));
            }
        }
        num=num.multiply(new BigDecimal(""+Math.pow(2,e)));
        String[] prevdeal=num.toPlainString().split("\\.");
        StringBuilder ret=new StringBuilder();
        if(Pattern.matches("0*",prevdeal[1])){
            ret.append(prevdeal[0]).append(".0");
        }else{
            e=0;
            ret.append(prevdeal[1]);
            while(ret.charAt(0)=='0') {
                ret.delete(0, 1);
                e++;
            }
            e=-e-1;
            ret.insert(1,".");
            while(ret.length()>18){
                ret.delete(ret.length()-2,ret.length());
            }
            if(ret.charAt(ret.length()-1)=='0')
                ret.delete(ret.length()-1,ret.length());
            ret.append("E").append(e);
        }
        return (binStr.charAt(0)=='1'?"-":"")+ret.toString();
    }

    /**
     * The decimal number to its NBCD code
     */
    public String decimalToNBCD(String decimal) {
        StringBuilder ret = new StringBuilder(decimal.charAt(0) == '-' ? "1101" : "1100");
        int i = 0;
        decimal = decimal.replace("-", "");
        if (decimal.length() > 7) {
            decimal = decimal.substring(decimal.length() - 7, decimal.length());
        }
        for (i = 0; i < 7 - decimal.length(); i++)
            ret.append("0000");
        i = 0;
        while (i < decimal.length()) {
            ret.append(String.format("%4s", Integer.toBinaryString(decimal.charAt(i++) - '0')).replace(" ", "0"));
        }
        return ret.toString();
    }

    /**
     * NBCD code to its decimal number
     */
    public String NBCDToDecimal(String NBCDStr) {
        String ret = "";
        if (Pattern.matches("^1101[0-9]*", NBCDStr)) {
            ret = "-";
        }
        boolean flag = false;
        Pattern p = Pattern.compile("\\d{4}");
        Matcher m = p.matcher(NBCDStr.substring(4, NBCDStr.length()));
        for (int i = 0, j = 0; i < 7; i++) {
            if (m.find())
                j = Integer.parseInt(m.group(), 2);
            if (j == 0) {
                if (!flag) {
                    continue;
                }
            } else {
                flag = true;
            }
            ret = ret + String.valueOf(j);
        }
        return flag ? ret : ret + "0";
    }

    public static void main(String[] args) {
        Transformer t = new Transformer();
        System.out.println(t.binaryToFloat("00000000011000000000000000000000"));
    }

}
