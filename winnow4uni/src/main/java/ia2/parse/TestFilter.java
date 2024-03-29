package ia2.parse;

import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class TestFilter {
	
	private final Instances outputFormat;
	
	private final double[] azzeratore;
	
	public TestFilter(Instances dataSet) {
		this.outputFormat = dataSet.stringFreeStructure();	//faccio due volte... forse risprmio memoria
		this.azzeratore = new double[this.outputFormat.numAttributes()];
	}

	@SuppressWarnings("unchecked")
	public Instances revertInstances(Instances testDataSet) {
		Instances resultDataSet = this.outputFormat.stringFreeStructure();
		Enumeration<Instance> instances = testDataSet.enumerateInstances();
		while (instances.hasMoreElements()) {
			Instance instance = (Instance) instances.nextElement();
			resultDataSet.add(createInstance(resultDataSet, instance));
		}
		return resultDataSet;
	}
	
	private Instance createInstance(Instances newDataSet, Instance oldInstance) {
//		Instance resultInstance = new Instance(newDataSet.numAttributes());//TODO accoppiato con il numero di attributo
		Instance resultInstance = new Instance(oldInstance.weight(),new double[newDataSet.numAttributes()]);
		resultInstance.setDataset(newDataSet);
		
		String classValue = oldInstance.classAttribute().value((int)oldInstance.classValue());
		resultInstance.setClassValue(classValue);

		final double[] oldVal = oldInstance.toDoubleArray();
		for(int i=0;i<oldVal.length;i++)
			if(oldVal[i]!=0) {
				Attribute destination = newDataSet.attribute(oldInstance.attribute(i).name());
				if(destination!=null)
					resultInstance.setValue(destination.index(),oldVal[i]);
			}
		resultInstance.replaceMissingValues(this.azzeratore);
		return resultInstance;
	}
	
}
