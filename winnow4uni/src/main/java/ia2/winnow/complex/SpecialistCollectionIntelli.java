package ia2.winnow.complex;

import static ia2.winnow.Constants.NEGATIVE;
import static ia2.winnow.Constants.POSITIVE;
import ia2.winnow.WinnowUtil;

import java.util.HashMap;
import java.util.Map;

import weka.core.Instance;

public class SpecialistCollectionIntelli {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5029288927793573758L;

	private int numberOfError;
	
	private final Map<Integer, SpecialistLazy> index2Specialist;

		
	public SpecialistCollectionIntelli() {
		this.index2Specialist = new HashMap<Integer, SpecialistLazy>();
	}
	
	public SpecialistLazy getSpecialist(int index) {
		SpecialistLazy specialist = this.index2Specialist.get(index);
		if(specialist == null) {
			specialist = new SpecialistLazy(index);
			this.index2Specialist.put(index, specialist);
		}
		return specialist; 
	}
	
	public int getPrediction(int[] specialistIndexes) {
		double totalWeight = 0;
		for(int i : specialistIndexes)
			totalWeight += this.getSpecialist(i).getWeigth();
		if (totalWeight > specialistIndexes.length) // sarebbe la lunghezza degli elementi
			return POSITIVE;	// Si - appartiene alla classe
		return NEGATIVE;	// No - non appartiene alla classe
	}
	
	private double getPredictionN(int[] specialistIndexes) {
		double totalWeight = 0;
		for(int i : specialistIndexes)
			totalWeight += this.getSpecialist(i).getWeigth();
		return totalWeight/specialistIndexes.length;
	}
	
	public void raiseWeight(int[] specialistIndexes) {
		double value = 1-this.getPredictionN(specialistIndexes);
		for(int i : specialistIndexes)
			value += raiseWeight(i, value);
	}
	
	private double raiseWeight(int specialistIndex, double value) {
		SpecialistLazy specialist = this.getSpecialist(specialistIndex);
		return specialist.raiseWeight(value);
	}
	
	public void decreaseWeight(int[] specialistIndexes) {
		double value = this.getPredictionN(specialistIndexes);
		for(int i : specialistIndexes)
			value -= decreaseWeight(i, value);
	}

	private double decreaseWeight(int specialistIndex, double value) {
		SpecialistLazy specialist = this.getSpecialist(specialistIndex);
		return specialist.decreaseWeight(value);
	}

	public double getPrediction(Instance instance) {
		return this.getPrediction(WinnowUtil.getNotZeroAttributeIndex(instance));
	}
	
}
