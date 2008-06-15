package ia2.preprocess;

import weka.core.tokenizers.WordTokenizer;

public class FirstTwoWordTokenizer extends WordTokenizer {
 
	private String firstWord;
	
	private String secondWord;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8054228406079970718L;
	
	private int count;
	
	@Override
	public void tokenize(String s) {
		super.tokenize(s);
		count = 0;
	}
	
	@Override
	public Object nextElement() {
		if(count++==3)
			return firstWord + "_" + secondWord;
		String next = (String) super.nextElement();
		switch (count) {
		case 1:
			firstWord = next;
			break;
		case 2:
			secondWord = next;
			break;
		default:
			break;
		}
		return next;
	}
}
