package cpu.present.transformers;

import cpu.present.PresentType;

public class Transformer {

	int eLength;

	int sLength;

	String code;                                            //The bin code indicate the two's complement

	PresentType type;

	String[] args = null;

	public Transformer(PresentType codeType, String... args) {
		this.type = codeType;
		this.args = args;
		this.code = args[0];
		if (args.length > 1) {
			eLength = Integer.parseInt(args[1]);
			sLength = Integer.parseInt(args[2]);
		}
	}

	public String to(PresentType type) {
		Transformer tmp=TransformerFactory.getTransformer(this.type,type,code);
		if(tmp.code.length()>32) {
			return tmp.code.substring(tmp.code.length()-32,tmp.code.length());
		} else{
			return tmp.code;
		}
	}

}