package ia2.util;

import ia2.parse.Parser;
import ia2.parse.ParserCoupled;
import ia2.parse.TrecParser;
import ia2.preprocess.Preprocessor;
import ia2.preprocess.SimplePreprocessor;
import ia2.preprocess.TrecPreprocessor;

import java.io.FileReader;
import java.io.FileWriter;

import weka.core.Instances;

public class TrecParseAndConvert {

	private Instances trainDataSet;
	private Instances testDataSet;

	public void parseTrain(int numberOfInstance) throws Exception {
		Parser parser = new TrecParser(new FileReader("src/train_5500.label"));
		trainDataSet = parser.getDataSet(numberOfInstance);
		
		
		Preprocessor preprocessor = new TrecPreprocessor();
		trainDataSet = preprocessor.convert(trainDataSet);		
		System.out.println(trainDataSet);
		
//		FileWriter fw = new FileWriter("src/train_" + numberOfInstance + ".arff");
//		fw.write(trainDataSet.toString());
//		fw.close();		
	}
	
	public void parseTest() throws Exception {
		Parser parser = new ParserCoupled(new FileReader("src/TREC_10.label"));
		testDataSet = parser.getDataSet();
		System.out.println(testDataSet);
		
		Preprocessor simplePreprocessor = new SimplePreprocessor();
		testDataSet = simplePreprocessor.convert(testDataSet);		
		
		deleteMissingAttribute();
		
		FileWriter fw = new FileWriter("src/TREC_10.arff");
		fw.write(testDataSet.toString());
		fw.close();	
	}

	private void deleteMissingAttribute() {
		Instances structure = trainDataSet.stringFreeStructure();
		Instances instance;
		//TODO
		
	}
	
}
