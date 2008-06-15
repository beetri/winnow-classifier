package ia2;
import ia2.parse.Parser;
import ia2.parse.TestFilter;
import ia2.parse.TrecParser;
import ia2.preprocess.TrecPreprocessor;
import ia2.winnow.WinnowClassifier;

import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;


public class BuildOnline {
	
	private static int numberOfInstance = 100;

	public static void main(String[] args) throws Exception {
		
		Classifier classifier = new WinnowClassifier();
		
		Parser trainParser = new TrecParser(new FileReader("src/train_5500.label"));			
		Instances dataSet = trainParser.getDataSet(numberOfInstance  );
		Instances trainDataSet = new TrecPreprocessor().convert(dataSet);		
		
		TrecParser testParser = new TrecParser(new FileReader("src/TREC_10.label"));
		Instances testDataSet = testParser.getDataSet();
		Instances filteredTestDataSet = new TrecPreprocessor().convert(testDataSet);
		Instances testInstances = new TestFilter(trainDataSet).revertInstances(filteredTestDataSet);
		
//		Instances lol =dataSet.trainCV(10, 0);
//		System.out.println(lol);
//		System.out.println(lol.numInstances());
		int numFolds = 4;
		classifier.buildClassifier(trainDataSet);
		for (int i = 0; i < numFolds; i++) {
			Instances asd = testInstances.testCV(numFolds, i);
			Evaluation e = new Evaluation(asd);
			e.evaluateModel(classifier, asd);
			printResult(e, "asd");
		}

//		Enumeration<Instance> enumeration = trainDataSet.enumerateInstances();
//		
//		
//		for (int i=1; enumeration.hasMoreElements(); i++) {
//			Instance instance = (Instance) enumeration.nextElement();
//			Instances instances = trainDataSet.stringFreeStructure();
//			instances.add(instance);			
//			classifier.buildClassifier(instances);
//			if (i%20 == 0){
//				Evaluation e = new Evaluation(testInstances);
//				e.evaluateModel(classifier, testInstances);
//				printResult(e, "CROSS VALIDATION");
////				System.out.println("Number of instance: "+i+", risultato: "+(e.correct()*100)/e.numInstances());
//			}
//			
//		}
		

	}
	
	private static void printResult(Evaluation e,String title) throws Exception {
		System.out.println("RISULTATO "+title);
		System.out.println(e.toSummaryString());	
		System.out.println(e.toMatrixString(title));
		System.out.println(e.toCumulativeMarginDistributionString());
		System.out.println(e.toClassDetailsString());
		
	}

}
