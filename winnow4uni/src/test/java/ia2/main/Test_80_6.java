package ia2.main;

import static ia2.util.Resource.getResourceAsStream;
import ia2.freezedclassifierAndTools.TrecPreprocessor_80_6;
import ia2.parse.Parser;
import ia2.parse.TestFilter;
import ia2.parse.TrecParser;
import ia2.util.Resource;
import ia2.winnow.WinnowClassifier;

import java.io.File;
import java.io.InputStreamReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Test_80_6 {
	public static void main(String[] args) throws Exception {
		try {
			System.setProperty("wordnet.database.dir",Resource.getResourceFile("dict").getAbsolutePath());
		} catch (IllegalArgumentException e ) {
			System.setProperty("wordnet.database.dir",new File("").getAbsolutePath() + File.separatorChar + "dict");
		}
		Parser parser = new TrecParser(new InputStreamReader(getResourceAsStream("train_5500.label")));
		Instances dataSet = parser.getDataSet();
		Instances filteredDataSet = new TrecPreprocessor_80_6().convert(dataSet);
		Instances testInstances = new TestFilter(filteredDataSet).revertInstances(new TrecPreprocessor_80_6().convert(
										new TrecParser(
												new InputStreamReader(getResourceAsStream("TREC_10.label"))
												).getDataSet()
										)
									);
		Evaluation e = new Evaluation(filteredDataSet);
		Classifier classifier = new WinnowClassifier();
		classifier.buildClassifier(filteredDataSet);
		e.evaluateModel(classifier, filteredDataSet);
		printResult(e,"CROSS VALIDATION");
		e = new Evaluation(testInstances);
		e.evaluateModel(classifier, testInstances);
		printResult(e,"TEST VALIDATION");
	}
	
	private static void printResult(Evaluation e,String title) throws Exception {
		System.out.println("RISULTATO "+title);
		System.out.println(e.toSummaryString());	
		System.out.println(e.toMatrixString(title));
		System.out.println(e.toCumulativeMarginDistributionString());
		System.out.println(e.toClassDetailsString());
		
	}
}
