package ia2.winnow;

public class SpecialistTCP extends Specialist {

	/**
	 * 
	 */
	private static final long serialVersionUID = 715412695655916595L;
	private double slowStartThreshold;

	public SpecialistTCP(int numValues) {
		super(numValues);
		this.slowStartThreshold = Double.MAX_VALUE;
	}

	@Override
	public void decreaseWeight() {
		double weight = this.getWeigth();
		weight /= 2;
		if (this.isInSlowStartMode())
			this.slowStartThreshold = weight;
		else
			this.slowStartThreshold = (this.slowStartThreshold + weight) / 2;
		this.setWeigth(weight);
	}

	@Override
	public void raiseWeight() {
		double weight = this.getWeigth();
		if (this.isInSlowStartMode())
			weight *= 4;
		else
			weight *= 2 + Math.min(this.slowStartThreshold / weight, weight
					/ this.slowStartThreshold);
		this.setWeigth(weight);
	}

	private boolean isInSlowStartMode() {
		return this.getWeigth() < this.slowStartThreshold;
	}

}
