package ia2.preprocess;

import weka.core.Instances;

public interface Preprocessor {

	/**
	 * 
	 * @param inputDataSet
	 * @return segreto
	 * @throws PreprocessorException
	 */
	public Instances convert(Instances inputDataSet) throws PreprocessorException;
	
}
