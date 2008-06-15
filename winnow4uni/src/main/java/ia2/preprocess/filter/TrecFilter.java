package ia2.preprocess.filter;

import static ia2.util.TrecCommons.createInstance;
import static ia2.util.TrecCommons.getAttributesFastVector;

import java.util.Enumeration;
import java.util.StringTokenizer;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;

public class TrecFilter {
	
	public Instances filter(Instances inputDataSet) {
		Instances resultDataSet = new Instances("TrecFilter",getAttributesFastVector(),inputDataSet.numInstances());
		resultDataSet.setClassIndex(0);
		Enumeration<Instance> instances = inputDataSet.enumerateInstances();
		while (instances.hasMoreElements()) {
			Instance instance = (Instance) instances.nextElement();
			resultDataSet.add(filter(instance,resultDataSet));
		}
		return resultDataSet;
	}
	
	private Instance filter(Instance instance, Instances resultDataSet) {
		String nameCategory = instance.stringValue(instance.classIndex());			
		String question = instance.stringValue(instance.numAttributes()-1-instance.classIndex());		
		StringTokenizer stringTokenizer = new StringTokenizer(question, " ");
		
		StringBuilder stringBuilder = new StringBuilder();		//il numero di elementi rimanenti
		for(int i=0; stringTokenizer.hasMoreTokens(); i++)
			stringBuilder.append(stringTokenizer.nextToken()).append(" ");
		question = stringBuilder.toString();
		question = this.modifyString(question);
		return createInstance(nameCategory,question,resultDataSet);		
	} 

	private Stemmer stemmer;
	
	
	public TrecFilter() throws Exception {
//		snowballStemmer = new englishStemmer();
//		Class.forName("net.sf.snowball.ext.PorterStemmer");
		stemmer = new SnowballStemmer();
	}
	
	private String modifyString(String question) {
		StringTokenizer tokenizer = new StringTokenizer(question," ");
		String firstWord = tokenizer.nextToken();
		String secondWord = tokenizer.nextToken();
		
		String firstWordS = stem(firstWord);
		String secondWordS = stem(secondWord);
		return firstWord + "_" + secondWord + " " +firstWordS + "_" + secondWordS + " " + first4Char(question);
	}
	
	private String stem(String toStem) {		
		return stemmer.stem(toStem);
	}

	 private String first4Char(String question) {
		StringBuilder result = new StringBuilder();
		//  StringBuffer result = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			result.append(word).append(" ");
		
			int length = word.length();
			if (length > 3) {
				String end = word.substring(2, length);
				result.append(end).append(" ");
				result.append(stem(end)).append(" ");
			}
			if (length > 6) {
				String start = word.substring(0, length - 4);
				result.append(start).append(" ");
				result.append(stem(start)).append(" ");
			}
			result.append(word);
			result.append(stem(word));

		}
		return result.toString();// +" "+question;
	}
	
}
