package ia2.winnow;

public class SpecialistCollaboration extends Specialist {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1356476461182567712L;

	private boolean up;
	
	public SpecialistCollaboration(int numValues, SpecialistCollection specialistCollection) {
		super(numValues);
	}

	@Override
	public void raiseWeight() {
		up = true;
		super.raiseWeight();
	}
	
	@Override
	public void decreaseWeight() {
		super.decreaseWeight();
	}

}
