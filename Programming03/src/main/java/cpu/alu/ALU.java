package cpu.alu;

import transformer.Transformer;

import java.util.Arrays;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 加减乘除
 */
public class ALU {
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
		return (src.charAt(0)=='1'?"1":"0")+src.substring(0,src.length()-1);
	}
	/**
	 * 返回两个二进制整数的乘积(结果直接截取后32位)
     * 要求使用布斯乘法计算
	 * @param src 32-bits
	 * @param dest 32-bits
	 * @return 32-bits
	 */
	String mul (String src, String dest,String...args){
	    dest="00000000000000000000000000000000"+dest+"0";
	    String rev=getComplement(src);
	    src=src+"000000000000000000000000000000000";
	    rev=rev+"000000000000000000000000000000000";//扩展到65位
		for(int i=0;i<32;i++){
			if(dest.charAt(64)-dest.charAt(63)==1)
				dest=add(src,dest);
			else if(dest.charAt(64)-dest.charAt(63)==-1)
				dest=add(rev,dest);
			dest=shift(dest);
		}
		if(args.length==0)
        	return dest.substring(32,64);
		else return dest;
    }

}
