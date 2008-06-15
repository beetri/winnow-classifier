package ia2.winnow;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NoSupportForMissingValuesException;
import weka.core.UnsupportedAttributeTypeException;
import weka.core.UnsupportedClassTypeException;

public class MockClassifier extends Classifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8418072165471394579L;

	@Override
	public void buildClassifier(Instances data) throws Exception {
//		this.checkIntegrity(data);
		data = new Instances(data);
		data.deleteWithMissingClass();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private void checkIntegrity(Instances data) throws UnsupportedClassTypeException, UnsupportedAttributeTypeException, NoSupportForMissingValuesException {
		if (!data.classAttribute().isNominal())
			throw new UnsupportedClassTypeException("Le classi devono essere nominali.");
		/*	Controlla i tipi degli attributi	*/
		Enumeration<Attribute> attributeEnumeration = data.enumerateAttributes();
		while (attributeEnumeration.hasMoreElements())
			if (!attributeEnumeration.nextElement().isNominal())
				throw new UnsupportedAttributeTypeException("Sono accettati solo attributi nominali.");
		/*	Controlla i valori delle righe	*/
		Enumeration<Instance> instanceEnumeration = data.enumerateInstances();
		while (instanceEnumeration.hasMoreElements())
			if (instanceEnumeration.nextElement().hasMissingValue())
				throw new NoSupportForMissingValuesException("Valori mancanti!");
	}

	private static final Random r = new Random(new GregorianCalendar().getTimeInMillis());
	
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		int numClasses = instance.numClasses();
		double[] a = new double[numClasses]; 
		a[r.nextInt(numClasses)] = 1;
//		a[1] = a[2] = 1;
		return a;
	}
		
}
