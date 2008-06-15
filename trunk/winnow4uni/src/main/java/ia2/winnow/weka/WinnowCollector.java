package ia2.winnow.weka;

import ia2.util.Profiler;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.classifiers.functions.Winnow;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class WinnowCollector extends Classifier {

	private Map<Integer, Winnow> class2Winnow = new HashMap<Integer, Winnow>();

	private Profiler profiler = new Profiler();
	
	@Override
	public void buildClassifier(Instances dataSet) throws Exception {
		dataSet = new Instances(dataSet);
		dataSet.deleteWithMissingClass();
		int numClasses = dataSet.numClasses();
		for (int i = 1; i <= numClasses; i++) {// verificare gli indici
			long startWhile = System.currentTimeMillis();
			long start = System.currentTimeMillis();
			Instances dataSetByClass = createDataSet(dataSet,i);
//			System.out.println("Ho finito di creare l'Instances per il classificatore numero "+i+" - ho impiegato "+(System.currentTimeMillis()-start)+" millisecondi");
			Winnow winnow = getWinnowByClass(i);
			start = System.currentTimeMillis();
			winnow.buildClassifier(dataSetByClass);
//			System.out.println("Ho finito di buildare il classificatore numero "+i+" - ho impiegato "+(System.currentTimeMillis()-start)+" millisecondi");
//			System.out.println("tutto il ciclo while "+i+" è durato "+(System.currentTimeMillis()-startWhile)+" millisecondi");
			System.gc();
		}
	}

	private Instances createDataSet(Instances dataSet, int i) {
		Instances resultDataSet = new Instances("Categoria: "+i,getAttributeByClass(dataSet,i),5500);		
		resultDataSet.setClassIndex(dataSet.classIndex());
		Enumeration<Instance> instances = dataSet.enumerateInstances();
		while (instances.hasMoreElements()) {
			Instance instance = (Instance) instances.nextElement();
			resultDataSet.add(createInstance(resultDataSet, instance, i));
		}
//		System.out.println(resultDataSet);
		return resultDataSet;
	}

	private Instance createInstance(Instances newDataSet, Instance oldInstance, int i) {
		
		Instance resultInstance = new Instance(oldInstance.numAttributes());//TODO accoppiato con il numero di attributo
		double[] doubleArray = oldInstance.toDoubleArray();
		doubleArray[oldInstance.classIndex()] = 0;
		resultInstance = new Instance(oldInstance.weight(), doubleArray);
		resultInstance.setDataset(newDataSet);
//		double classValue = instance.classValue();
		String classValue = oldInstance.classAttribute().value((int)oldInstance.classValue());
		if(!classValue.equals(newDataSet.classAttribute().value(1)))
			classValue = "FALSE";
		resultInstance.setClassValue(classValue);
		
//		Enumeration<Attribute> attributes = oldInstance.enumerateAttributes();
//		while (attributes.hasMoreElements()) {
//			Attribute attribute = (Attribute) attributes.nextElement();
//			int attributeIndex = attribute.index();
//			resultInstance.setValue(newDataSet.attribute(attributeIndex),oldInstance.value(attributeIndex));
//		}
		return resultInstance;
	}
	
	private FastVector getAttributeByClass(Instances dataSet, int i) {
		FastVector result = new FastVector();
		Enumeration<Attribute> attributes = dataSet.enumerateAttributes();
		int classIndex = dataSet.classIndex();		
		//creiamo l'attributo classe
		result.addElement(new Attribute("Class",getClassValuesFastVector(dataSet.classAttribute(),i)));
		//creiamo gli altri attributi 
		while (attributes.hasMoreElements()) {
			Attribute attribute = (Attribute) attributes.nextElement();			
			Attribute newAttribute = new Attribute(attribute.name(),(FastVector)null);
			result.addElement(attribute);		
		}
		return result;
	}

	private FastVector getClassValuesFastVector(Attribute attribute, int i) {
		FastVector classValues = new FastVector(2);
		classValues.addElement("FALSE");
		classValues.addElement(attribute.value(i-1));
		return classValues;
	}

	private Winnow getWinnowByClass(int classIndex) {
		Winnow winnow = this.class2Winnow .get(classIndex);
		if (winnow == null) {
			winnow = new Winnow();
			class2Winnow.put(classIndex, winnow);
		}
		return winnow;
	}

	
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception{		
		int numClasses = getNumberOfClasses();
		double[] a = new double[numClasses];
		for (int classIndex=1; classIndex<=numClasses; classIndex++) {//lo faccio per ogni classe, ma potrei anche fare in modo di fermarmi al primo 1
			double[] distr = getWinnowByClass(classIndex).distributionForInstance(instance);
			a[classIndex-1] = distr[1];
		}
		return a;
	}
	
	private int getNumberOfClasses() {
		return this.class2Winnow.size();
	}
		

}
