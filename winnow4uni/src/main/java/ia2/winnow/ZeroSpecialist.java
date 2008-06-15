package ia2.winnow;

import java.io.Serializable;

public class ZeroSpecialist extends Specialist implements Serializable {

	public ZeroSpecialist(int numValues) {
		super(numValues);
		
	}

	@Override
	public double getWeigth() {
		return 0;
	}
	
}
