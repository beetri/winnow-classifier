package ia2;
import ia2.parse.Parser;
import ia2.parse.TestFilter;
import ia2.parse.TrecParser;
import ia2.preprocess.TrecPreprocessor;
import ia2.winnow.WinnowClassifier;

import java.io.FileNotFoundException;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;



public class TestTrecClassifier {
	
	public static void main(String[] args) throws Exception {	
		int numberOfInstance = 2000;
		Classifier classifier = new WinnowClassifier();
		double precision = executeTrecTest(numberOfInstance,classifier);
		System.out.println(precision);
	}

	public static double executeTrecTest(int numberOfInstance,Classifier classifier)throws FileNotFoundException, Exception {
		Parser trainParser = new TrecParser(new FileReader("src/train_5500.label"));			
		Instances dataSet = trainParser.getDataSet(numberOfInstance );
		Instances trainDataSet = new TrecPreprocessor().convert(dataSet);		
		
		TrecParser testParser = new TrecParser(new FileReader("src/TREC_10.label"));
		Instances testDataSet = testParser.getDataSet();
		Instances filteredTestDataSet = new TrecPreprocessor().convert(testDataSet);
		Instances testInstances = new TestFilter(trainDataSet).revertInstances(filteredTestDataSet);

		Evaluation e = new Evaluation(trainDataSet);		
		classifier.buildClassifier(trainDataSet);
		e.evaluateModel(classifier, trainDataSet);
		printResult(e,"CROSS VALIDATION");
		e = new Evaluation(testInstances);
		e.evaluateModel(classifier, testInstances);
		printResult(e,"TEST VALIDATION");
		return (e.correct()*100)/e.numInstances();
	}
	
	private static void printResult(Evaluation e,String title) throws Exception {
		System.out.println("RISULTATO "+title);
		System.out.println(e.toSummaryString());	
		System.out.println(e.toMatrixString(title));
		System.out.println(e.toCumulativeMarginDistributionString());
		System.out.println(e.toClassDetailsString());
		
	}

}
