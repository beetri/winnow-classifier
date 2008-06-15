package ia2.winnow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import weka.core.Instance;
import static ia2.winnow.Constants.POSITIVE;
import static ia2.winnow.Constants.NEGATIVE;

public class SpecialistCollection implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6155382530828865864L;

	private Map<Integer, Specialist> index2Specialist;
	
	private int maxNum;
		
	public SpecialistCollection() {
		this.index2Specialist = new HashMap<Integer, Specialist>();
		this.maxNum = 0;
	}
	
	public Specialist getSpecialist(int index) {
		Specialist specialist = this.index2Specialist.get(index);
		if(specialist == null) {
			specialist = new Specialist(index);
			this.index2Specialist.put(index, specialist);
			this.maxNum = Math.max(index, maxNum);
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
	
	public void raiseWeight(int[] specialistIndexes) {
		for(int i : specialistIndexes)
			raiseWeight(i);
	}
	
	private void raiseWeight(int specialistIndex) {
		Specialist specialist = this.getSpecialist(specialistIndex);
		specialist.raiseWeight();
	}
	
	public void decreaseWeight(int[] specialistIndexes) {
		for(int i : specialistIndexes)
			decreaseWeight(i);
	}

	private void decreaseWeight(int specialistIndex) {
		Specialist specialist = this.getSpecialist(specialistIndex);
		specialist.decreaseWeight();
	}

	@SuppressWarnings("unused")
	private int threshold() {
//		return this.index2Specialist.size();
		return (int) Math.round(Math.log10(this.maxNum/2));
		//TODO: io in seguito lo farei diventare il doppio del massimo numero di specialisti che hanno predetto insieme (sia in positivo che in negativo)
		//A istinto logicamente
	}

	public int getPrediction(Instance instance) {
		return this.getPrediction(WinnowUtil.getNotZeroAttributeIndex(instance));
	}
//	public double getPrediction(Instance instance) {
//		double[] instanceVector = instance.toDoubleArray();
//		double totaleWeigth = 0;
//		int numParole = 0;
//		for(int i=0; i<instanceVector.length; i++){
//			if (instanceVector[i]>0){
//				totaleWeigth += this.getSpecialist(i).getWeigth();
//				numParole++;				
//			}
//		}
////		System.out.println(totaleWeigth+" "+numParole);
//		if (totaleWeigth > numParole)
//			return 1;
//		return 0;
////		return totaleWeigth/(numParole);
//	}
	
}
