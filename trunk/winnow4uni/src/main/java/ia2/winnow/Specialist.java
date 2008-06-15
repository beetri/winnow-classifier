package ia2.winnow;

import java.io.Serializable;


public class Specialist implements Serializable {
	
	private double weigth;
	
	public Specialist(int numValues) {
		this.weigth = 1;
	}
	
	public double getWeigth(){
		return this.weigth;
	}
	
	protected void setWeigth(double weigth) {
		this.weigth = weigth;
	}

	public void raiseWeight() {
		this.weigth *= 2;
	}

	public void decreaseWeight() {
		this.weigth /= 2;
	}

}
