package ia2.freezedclassifierAndTools;

import ia2.preprocess.Preprocessor;
import ia2.preprocess.PreprocessorException;
import ia2.preprocess.filter.TrecFilter;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class TrecPreprocessor_80_6 implements Preprocessor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see preprocess.Preprocessor#convert(weka.core.Instances)
	 */
	@Override
	public Instances convert(Instances inputDataSet)
			throws PreprocessorException {
		try {
			return execute(inputDataSet);
		} catch (Exception e) {
			throw new PreprocessorException("Preprocessor panic!", e);
		}
	}

	private Instances execute(Instances inputDataSet) throws Exception {

		// TrecWekaFilter trecFilter = new TrecWekaFilter();
		// trecFilter.setInputFormat(inputDataSet);
		// inputDataSet = Filter.useFilter(inputDataSet, trecFilter);
		TrecFilter trecFilter = new FullOfThingTrecFilter();
		inputDataSet = trecFilter.filter(inputDataSet);

		// TODO: filtro coppia++
		StringToWordVector filter = new StringToWordVector();
		// filter.setTokenizer(new FirstTwoWordTokenizer());
		// filter.setLowerCaseTokens(true);
		// filter.setStemmer(new SnowballStemmer("english"));
		filter.setInputFormat(inputDataSet);
		inputDataSet = Filter.useFilter(inputDataSet, filter);

		Reorder order = new Reorder();
		order.setInputFormat(inputDataSet);
		String[] options = { "-R", "2-last,1" };
		order.setOptions(options);
		// return Filter.useFilter(inputDataSet, order);

		NumericToNominal filter2 = new NumericToNominal();
		filter2.setInputFormat(inputDataSet);

		return Filter.useFilter(inputDataSet, filter2);

	}

}