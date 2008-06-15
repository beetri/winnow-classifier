package ia2.preprocess;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SimplePreprocessor implements Preprocessor{

	/*
	 * (non-Javadoc)
	 * @see preprocess.Preprocessor#convert(weka.core.Instances)
	 */
	@Override
	public Instances convert(Instances inputDataSet) throws PreprocessorException {
		try {
			return execute(inputDataSet);
		} catch (Exception e) {
			throw new PreprocessorException("Preprocessor panic!",e);
		}		
	}

	private Instances execute(Instances inputDataSet) throws Exception {

		
		
		
		
		//TODO: filtro coppia++
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(inputDataSet);
		inputDataSet = Filter.useFilter(inputDataSet, filter);
		
		Reorder order = new Reorder();
		order.setInputFormat(inputDataSet);
		String[] options = {"-R","2-last,1"};
		order.setOptions(options);
//		return Filter.useFilter(inputDataSet, order);
		
		NumericToNominal filter2 = new NumericToNominal();
		filter2.setInputFormat(inputDataSet);
		
		return Filter.useFilter(inputDataSet, filter2);

		
	}

	
	
}
