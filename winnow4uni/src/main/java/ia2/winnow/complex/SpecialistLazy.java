package ia2.winnow.complex;

import java.lang.Math;

public class SpecialistLazy {

	private double weigth;
	
	public SpecialistLazy(int numValues) {
		this.weigth = 1;
	}
	
	public double getWeigth(){
		return this.weigth;
	}
	
	protected void setWeigth(double weigth) {
		this.weigth = weigth;
	}

	public double raiseWeight(double value) {
		final double raiseFactor = this.weigth>value?value:this.weigth;
		
		this.weigth+=raiseFactor;
		return 0;
//		double oldWeigth = this.weigth;
//		double min = Math.min(value, this.weigth);
//		double newWeigth = oldWeigth + min;
//		this.weigth = newWeigth;
//		this.weigth *= 2;
//		return newWeigth-oldWeigth;
	}

	public double decreaseWeight(double value) {
		this.weigth/=2;
		return 0;
//		final double decreaseFactor = (this.weigth + value)/2;
//		if(decreaseFactor>=this.weigth)
//			this.weigth /= 2;
//		else
//			this.weigth -= decreaseFactor;
//		return decreaseFactor/4;
	}
}
