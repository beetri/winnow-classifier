package ia2.parse;

import weka.core.Instances;

public interface Parser {

	public abstract Instances getDataSet();

	public abstract Instances getDataSet(int numberOfInstanceToRead);

}