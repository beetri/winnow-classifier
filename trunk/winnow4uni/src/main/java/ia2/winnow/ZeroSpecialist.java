package ia2.winnow;

import java.io.Serializable;

public class ZeroSpecialist extends Specialist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 162662559528210649L;

	public ZeroSpecialist(int numValues) {
		super(numValues);
	}

	@Override
	public double getWeigth() {
		return 0;
	}
	
}
