package ia2;

import ia2.parse.Parser;
import ia2.parse.TestFilter;
import ia2.parse.TrecParser;
import ia2.preprocess.TrecPreprocessor;
import ia2.util.Resource;
import ia2.winnow.WinnowClassifier;

import java.awt.Color;
import java.io.FileReader;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class WinnowClassifierBench {
	
	private static final String Y_AXIS_LABEL = "% value";
	private static final String X_AXIS_LABEL = "number of instance";
	private static final String CHART_NAME = "Winnow Classifier";
	private static final String TEST_DATASET = "TREC_10.label";
	private static final String TRAIN_DATASET = "train_5500.label";
	private static final String CHART_FILE = "WinnowClassifier.png";
	private static int NUMBER_OF_INSTANCE = 5500;
	private static int NUM_FOLDS = 100;

	public static void main(String[] args) throws Exception {
		XYSeries correctlyClassifiedSeries = new XYSeries("Correctly Classified");
		XYSeries incorrectlyClassifiedSeries = new XYSeries("Incorrectly Classified");
		XYSeries unClassifiedInstancesSeries = new XYSeries("UnClassified Instances");
		executeTest(correctlyClassifiedSeries,incorrectlyClassifiedSeries,unClassifiedInstancesSeries);			
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		xySeriesCollection.addSeries(correctlyClassifiedSeries);
		xySeriesCollection.addSeries(incorrectlyClassifiedSeries);
		xySeriesCollection.addSeries(unClassifiedInstancesSeries);
		JFreeChart chart = ChartFactory.createXYLineChart(CHART_NAME,X_AXIS_LABEL,Y_AXIS_LABEL,xySeriesCollection,PlotOrientation.VERTICAL,true,true,false);      
    	chart.setBackgroundPaint(Color.white);
        ChartUtilities.saveChartAsPNG(Resource.getNewFile(CHART_FILE), chart, 1000,700);
	}

	private static void executeTest(XYSeries correctlyClassifiedSeries, XYSeries incorrectlyClassifiedSeries, XYSeries unClassifiedInstancesSeries) throws Exception {
		Classifier classifier = new WinnowClassifier();		
		Parser trainParser = new TrecParser(new FileReader(Resource.getResourceFile(TRAIN_DATASET)));			
		Instances dataSet = trainParser.getDataSet(NUMBER_OF_INSTANCE );
		Instances trainDataSet = new TrecPreprocessor().convert(dataSet);			
		TrecParser testParser = new TrecParser(new FileReader(Resource.getResourceFile(TEST_DATASET)));
		Instances testDataSet = testParser.getDataSet();
		Instances filteredTestDataSet = new TrecPreprocessor().convert(testDataSet);
		Instances testInstances = new TestFilter(trainDataSet).revertInstances(filteredTestDataSet);
		Evaluation e = null;
		for (int i = 0; i <= NUM_FOLDS ; i++) {
			classifier.buildClassifier(trainDataSet.trainCV(NUM_FOLDS, i));
			e = new Evaluation(testInstances);
			e.evaluateModel(classifier, testInstances);
			double correctlyClassified = (e.correct()*100)/e.numInstances();
			double incorrectlyClassified = (e.incorrect()*100)/e.numInstances();
			double unClassifiedInstances = (e.unclassified()*100)/e.numInstances();
			int numInstance = (i+1)*(trainDataSet.numInstances()/NUM_FOLDS);
			correctlyClassifiedSeries.add(numInstance, correctlyClassified);
			incorrectlyClassifiedSeries.add(numInstance,incorrectlyClassified);
			unClassifiedInstancesSeries.add(numInstance,unClassifiedInstances);
//			printResult(e, "TEST VALIDATION: number of instances >>> "+numInstance+" <<<");
		}
		printResult(e, "TEST VALIDATION: number of instances >>> "+trainDataSet.numInstances()+" <<<");
	}
	
	private static void printResult(Evaluation e,String title) throws Exception {
		System.out.println("RISULTATO "+title);
		System.out.println(e.toSummaryString());	
		System.out.println(e.toMatrixString(title));
		System.out.println(e.toCumulativeMarginDistributionString());
		System.out.println(e.toClassDetailsString());		
	}

}
