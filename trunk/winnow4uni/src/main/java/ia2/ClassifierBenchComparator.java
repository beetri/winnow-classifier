package ia2;

import ia2.freezedclassifierAndTools.TrecPreprocessor_80_6;
import ia2.parse.Parser;
import ia2.parse.TestFilter;
import ia2.parse.TrecParser;
import ia2.preprocess.Preprocessor;
import ia2.preprocess.TrecPreprocessor;
import ia2.util.Resource;
import ia2.winnow.WinnowClassifier;
import ia2.winnow.weka.WinnowCollector;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;

public class ClassifierBenchComparator {
	
	private static final String Y_AXIS_LABEL = "% value";
	private static final String X_AXIS_LABEL = "number of instance";
	private static final String CHART_NAME = "Classifier Comparison";
	private static final String TEST_DATASET = "TREC_10.label";
	private static final String TRAIN_DATASET = "train_5500.label";
	private static final String CHART_FILE = "ClassifierComparison.png";
	private static int NUMBER_OF_INSTANCE = 100;
	private static int NUM_FOLDS = 2;
	
	private Classifier[] onlineClassifiers = new Classifier[]{new WinnowClassifier(),new WinnowCollector()};
	private Classifier[] offlineClassifiers = new Classifier[]{new SMO()};
	private Preprocessor preprocessor = new TrecPreprocessor_80_6();

	public static void main(String[] args) throws Exception {
		System.setProperty("wordnet.database.dir",Resource.getResourceFile("dict").getAbsolutePath());	
		ClassifierBenchComparator comparator = new ClassifierBenchComparator();
		comparator.execute();
	}	

	private void execute() throws Exception, IOException,URISyntaxException {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		for (Classifier classifier : onlineClassifiers) {
			xySeriesCollection.addSeries(executeOnlineClassifierTest(classifier));	
		}
		for (Classifier classifier : offlineClassifiers) {
			xySeriesCollection.addSeries(executeOfflineClassifierTest(classifier));	
		}	
		JFreeChart chart = ChartFactory.createXYLineChart(CHART_NAME,X_AXIS_LABEL,Y_AXIS_LABEL,xySeriesCollection,PlotOrientation.VERTICAL,true,true,false);      
    	chart.setBackgroundPaint(Color.white);
        ChartUtilities.saveChartAsPNG(Resource.getNewFile(CHART_FILE), chart, 1000,700);
	}

	private XYSeries executeOfflineClassifierTest(Classifier classifier) throws Exception {
		int numberOfInstance = NUMBER_OF_INSTANCE/NUM_FOLDS;
		XYSeries result = new XYSeries(classifier.getClass().getSimpleName()+" corrected");
		for (int i = 1; i <= NUM_FOLDS; i++) {
			Parser trainParser = new TrecParser(new FileReader(Resource.getResourceFile(TRAIN_DATASET)));			
			Instances dataSet = trainParser.getDataSet(numberOfInstance * i);
			Instances trainDataSet = preprocessor .convert(dataSet);			
			TrecParser testParser = new TrecParser(new FileReader(Resource.getResourceFile(TEST_DATASET)));
			Instances testDataSet = testParser.getDataSet();
			Instances filteredTestDataSet = preprocessor.convert(testDataSet);
			Instances testInstances = new TestFilter(trainDataSet).revertInstances(filteredTestDataSet);
			
			classifier.buildClassifier(trainDataSet);
			Evaluation e = new Evaluation(testInstances);
			e.evaluateModel(classifier, testInstances);
			double correctlyClassified = (e.correct()*100)/e.numInstances();
			result.add(trainDataSet.numInstances(),correctlyClassified);
		}	
		return result;
	}

	private XYSeries executeOnlineClassifierTest(Classifier classifier) throws Exception {
		Parser trainParser = new TrecParser(new FileReader(Resource.getResourceFile(TRAIN_DATASET)));			
		Instances dataSet = trainParser.getDataSet(NUMBER_OF_INSTANCE );
		Instances trainDataSet = preprocessor .convert(dataSet);			
		TrecParser testParser = new TrecParser(new FileReader(Resource.getResourceFile(TEST_DATASET)));
		Instances testDataSet = testParser.getDataSet();
		Instances filteredTestDataSet = preprocessor.convert(testDataSet);
		Instances testInstances = new TestFilter(trainDataSet).revertInstances(filteredTestDataSet);
		Evaluation e = null;
		XYSeries result = new XYSeries(classifier.getClass().getSimpleName()+" corrected");
		for (int i = 0; i <= NUM_FOLDS ; i++) {
			classifier.buildClassifier(trainDataSet.trainCV(NUM_FOLDS, i));
			e = new Evaluation(testInstances);
			e.evaluateModel(classifier, testInstances);
			double correctlyClassified = (e.correct()*100)/e.numInstances();
//			double incorrectlyClassified = (e.incorrect()*100)/e.numInstances();
//			double unClassifiedInstances = (e.unclassified()*100)/e.numInstances();
			int numInstance = (i+1)*(trainDataSet.numInstances()/NUM_FOLDS);
			result.add(numInstance,correctlyClassified);
//			printResult(e, "TEST VALIDATION: number of instances >>> "+numInstance+" <<<");
		}
		printResult(e, "TEST VALIDATION: number of instances >>> "+trainDataSet.numInstances()+" <<<");		
		return result;
	}	
	
	private static void printResult(Evaluation e,String title) throws Exception {
		System.out.println("RISULTATO "+title);
		System.out.println(e.toSummaryString());	
		System.out.println(e.toMatrixString(title));
		System.out.println(e.toCumulativeMarginDistributionString());
		System.out.println(e.toClassDetailsString());		
	}

}
