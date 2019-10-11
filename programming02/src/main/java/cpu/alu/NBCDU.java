package cpu.alu;

public class NBCDU {

	// 模拟寄存器中的进位标志位
	private String CF = "0";

	// 模拟寄存器中的溢出标志位
	private String OF = "0";
	private static String bitAdd(String...args){
		ALU alu=new ALU();
		StringBuilder ret=new StringBuilder(alu.add("0"+args[0],"0"+args[1]));//防止溢出
		if(args.length==4)return ret.toString();
		else if(args.length==3)
			if(args[2].equals("1"))
				ret=new StringBuilder(alu.add(ret.toString(),"00001"));
		int x=ret.charAt(1)-'0',y=ret.charAt(2)-'0',z=ret.charAt(3)-'0';
		if(ret.charAt(0)=='1'){
			return alu.add(ret.toString(),"00110");//1直接保留
		}
		else if((x&(y|z))==1){
			return "1"+alu.add(ret.toString().substring(1,5),"0110"); //说明进位
		}
		return ret.toString();
	}
	private static String reverse(String tar,int bits){
		tar=tar.substring(32-bits*4,32);//取bits位
		StringBuilder reverse=new StringBuilder();
		for(int i=0;i<=tar.length()-4;i+=4){
			String tmp=bitAdd(tar.substring(i,i+4),"0110","0","not_carry")
					.substring(1,5)
					.replaceAll("1","2").replaceAll("0","1").replaceAll("2","0");
			if(i==tar.length()-4)
				tmp=bitAdd(tmp,"0001","0","not_carry").substring(1,5);
			reverse.append(tmp);
			//翻转时不用处理进位,not_carry
		}
		return "1100"+ String.format("%28s",reverse.toString()).replaceAll(" ","0");  //返回正数化的负数
	}
	private static int getBit(String tar){
		int cnt=0;
		for(int i=4;i<28;i+=4){
			if(tar.substring(i,i+4).equals("0000")) {
				cnt++;
				continue;
			}
			break;
		}
		return 7-cnt;
	}//算位数

	/**
	 *
	 * @param a A 32-bits NBCD String
	 * @param b A 32-bits NBCD String
	 * @return a + b
	 */
	String add(String a, String b) {
		StringBuilder ret=new StringBuilder();
		if(a.substring(0,4).equals(b.substring(0,4))){  //同号
			String c="0";
			for(int i=31;i>3;i-=4){
				String temp=bitAdd(a.substring(i-3,i+1),b.substring(i-3,i+1),c);
				if(temp.charAt(0)=='1')
					c="1";
				else c="0";
				ret.insert(0,temp.substring(1,5));
			}
			return ret.insert(0,a.substring(0,4)).toString();
		}
		else{
			String temp;
			if(a.substring(0,4).equals("1100")){
				temp=a;
				a=b;
				b=temp;
			}
			int bits=Math.max(getBit(a),getBit(b));
			temp=add(reverse(a,bits),b);
			if(temp.substring(28-4*bits,32-4*bits).equals("0001"))
				return temp.replaceFirst("0001","0000");
			else
				return reverse(temp,bits).replaceFirst("1100","1101");
		}
	}

	/***
	 *
	 * @param a A 32-bits NBCD String
	 * @param b A 32-bits NBCD String
	 * @return b - a
	 */
	String sub(String a, String b) {
		return add(a.substring(0,4).equals("1100")?"1101"+a.substring(4,32):"1100"+a.substring(4,32),b);
	}

}
