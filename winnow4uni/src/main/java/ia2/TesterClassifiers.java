package ia2;
import ia2.winnow.WinnowClassifier;
import ia2.winnow.weka.WinnowCollector;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;



public class TesterClassifiers {
	
//	private static int[] numberOfInstance = new int[]{50,100,200,300,500,1000,1500,2000,2500,3000,3500,4000,4500,5000,5500};
	private static int[] numberOfInstance = new int[]{100,500,1000,2000,3000,4000,5000,5500};
	private static int[] numberOfInstanceForWinnowWeka = new int[]{100,500,1000,2000,3000,3500};

	public static void main(String[] args) throws Exception {
		XYSeries series = testSingleClassifier(new SMO(),numberOfInstance);
		XYSeries series2 = testSingleClassifier(new WinnowClassifier(),numberOfInstance);
		XYSeries series3 = testSingleClassifier(new WinnowCollector(),numberOfInstanceForWinnowWeka);
		

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        JFreeChart chart = ChartFactory.createXYLineChart("Trec Question Classification","number of instance","precision %",dataset,PlotOrientation.VERTICAL,true,true,false);      
    	chart.setBackgroundPaint(Color.white);
        ChartUtilities.saveChartAsPNG(new File("chart.jpg"), chart, 1000,700);
            
      
	}

	private static XYSeries testSingleClassifier(Classifier c,int[] numberOfInstance)throws FileNotFoundException, Exception {
		XYSeries series = new XYSeries(c.getClass().getSimpleName());
       
		for (int i = 0; i < numberOfInstance.length; i++) {
			try{
				double result = TestTrecClassifier.executeTrecTest(numberOfInstance[i], c.getClass().newInstance());
				series.add(numberOfInstance[i],result);
			}catch (Exception e){
				break;
			}			
		}
		return series;
	}
	
}
