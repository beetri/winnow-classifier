package ia2.trash;

import ia2.util.TrecParseAndConvert;



public class TestParseAndFilter {

	public static void main(String[] args) throws Exception {
		int numberOfInstance = 10;
		TrecParseAndConvert parseAndConvert = new TrecParseAndConvert();
		parseAndConvert.parseTrain(numberOfInstance);
	}
	
}
