package ia2.winnow.complex;


import ia2.winnow.WinnowUtil;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NoSupportForMissingValuesException;
import weka.core.UnsupportedAttributeTypeException;
import weka.core.UnsupportedClassTypeException;
import static ia2.winnow.Constants.POSITIVE;
import static ia2.winnow.Constants.NEGATIVE;

public class WinnowClassifierIntelli extends Classifier {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 91692613691906964L;
	
	private Map<Integer,SpecialistCollectionIntelli> class2SpecialistCollection; 
	
	public WinnowClassifierIntelli() {
		this.class2SpecialistCollection = new HashMap<Integer, SpecialistCollectionIntelli> ();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildClassifier(Instances dataSet) throws Exception {
//		this.checkIntegrity(data);
		dataSet = new Instances(dataSet);
		dataSet.deleteWithMissingClass();
		Enumeration<Instance> enumeration = dataSet.enumerateInstances();
		while (enumeration.hasMoreElements())
			buildClassifier(enumeration.nextElement());	
	}
		
	public void buildClassifier(Instance instance) {
		int classValue = (int) instance.classValue();
//		SpecialistCollection specialistCollection = this.getSpecialistCollectionForClass(classValue);
		int[] notZeroAttributeIndex = WinnowUtil.getNotZeroAttributeIndex(instance);
		double[] classificationResult = distributionForInstance(instance,notZeroAttributeIndex);//classifico l'instance: da questo array so chi sbaglia e chi non sbaglia
		this.trainSpecialists(classValue, classificationResult, notZeroAttributeIndex);
	}

	private double[] distributionForInstance(Instance instance, int[] notZeroAttributeIndex) {
		int numClasses = instance.numClasses();
		double[] a = new double[numClasses]; 
		for (int classValue=0; classValue<numClasses; classValue++)//lo faccio per ogni classe, ma potrei anche fare in modo di fermarmi al primo 1
			a[classValue] = classifyInstanceForClass(classValue, notZeroAttributeIndex);
//		a[0]=0;
		return a;
	}
	
	private double classifyInstanceForClass(int classValue, int[] notZeroAttributeIndex){
		SpecialistCollectionIntelli specialistCollection =  this.getSpecialistCollectionForClass(classValue);//non invoco il metodo getSpecialist perchè non devo creare una nuova categoria qui
		return specialistCollection.getPrediction(notZeroAttributeIndex);
	}
	
	

	private void trainSpecialists(int classValue, double[] classificationResults, int[] specialistIndex) {
//		int tmp =0;
//		for(int i=1; i<classificationResults.length; i++) {			
//			if (classificationResults[i]>classificationResults[tmp])
//				tmp = i;
//		}
//		classificationResults = new double[6];
//		classificationResults[tmp]=1;
		
		for(int i=0; i<classificationResults.length; i++) {
			double classificationResult = classificationResults[i];
			
//			if(classificationResult == POSITIVE && classValue == i
//					||														// condizione che corrisponde a quando ho predetto correttamente lascio per completezza
//			classificationResult == NEGATIVE && classValue != i)
			if(classificationResult >= POSITIVE && classValue != i) // predico positivo quando non lo è
				this.class2SpecialistCollection.get(i).decreaseWeight(specialistIndex);
			else if(classificationResult < POSITIVE && classValue == i)	///predico negativo quando non lo è
				this.class2SpecialistCollection.get(i).raiseWeight(specialistIndex);
		/* normalizzato ma illeggibile decommentare in produzione :)*/
//			if(classValue==i) {
//				if(classificationResult == NEGATIVE)
//					this.class2SpecialistCollection.get(i).premia(specialistIndex);
//			} else if (classificationResult == POSITIVE)
//				this.class2SpecialistCollection.get(i).penalizza(specialistIndex);
		}
	}

	private SpecialistCollectionIntelli getSpecialistCollectionForClass(int classValue) {
		SpecialistCollectionIntelli specialistCollection = this.class2SpecialistCollection.get(classValue);
		if(specialistCollection == null){
			specialistCollection = new SpecialistCollectionIntelli();
			class2SpecialistCollection.put(classValue, specialistCollection);
		}
		return specialistCollection;
	}

	@Override
	public double[] distributionForInstance(Instance instance){
		int numClasses = instance.numClasses();
		double[] a = new double[numClasses]; 
		for (int classValue=0; classValue<numClasses; classValue++)//lo faccio per ogni classe, ma potrei anche fare in modo di fermarmi al primo 1
			a[classValue] = classifyInstanceForClass(instance, classValue);
//		a[0]=0;
		return a;
	}
	
	private double classifyInstanceForClass(Instance instance, int classValue){
		SpecialistCollectionIntelli specialistCollection =  this.getSpecialistCollectionForClass(classValue);//non invoco il metodo getSpecialist perchè non devo creare una nuova categoria qui
		return specialistCollection.getPrediction(instance);
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
	
}
