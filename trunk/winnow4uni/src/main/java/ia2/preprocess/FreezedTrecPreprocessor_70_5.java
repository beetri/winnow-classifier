package ia2.preprocess;

import static ia2.util.TrecCommons.createInstance;
import static ia2.util.TrecCommons.getAttributesFastVector;

import ia2.preprocess.filter.TrecFilter;

import java.util.Enumeration;
import java.util.StringTokenizer;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class FreezedTrecPreprocessor_70_5 extends TrecFilter {
	
	@SuppressWarnings("unchecked")
	@Override
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
	
	private SnowballStemmer wekaStemmer;
	
	private WordNetDatabase wnDB;
	
	public FreezedTrecPreprocessor_70_5() throws Exception {
		wekaStemmer = new SnowballStemmer();
		wnDB = WordNetDatabase.getFileInstance();
	}
	
	private void aggiungiCoppieConTipi(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		Synset[] synSet = wnDB.getSynsets(tokenizer.nextToken());
		while(synSet.length==0 && tokenizer.hasMoreTokens())
			synSet = wnDB.getSynsets(tokenizer.nextToken());
		if(!tokenizer.hasMoreTokens())
			return;
		SynsetType type_1 = synSet[0].getType();
		
		while (tokenizer.hasMoreTokens()) {
			Synset[] synSet2 = wnDB.getSynsets(tokenizer.nextToken());
			if(synSet2.length==0)
				continue;
			SynsetType type_2 = synSet2[0].getType();
			result.append(type_1).append("_").append(type_2).append(" ");
			//XXX: Come sempre le cose funzionano al contrario
//			type_1=type_1;//se correggi peggiora di molto dovrebbe essere type_1=type_2
		}
	}
	
	private void aggiungiTerneConTipi(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		Synset[] synSet = wnDB.getSynsets(tokenizer.nextToken());
		while(synSet.length==0 && tokenizer.hasMoreTokens())
			synSet = wnDB.getSynsets(tokenizer.nextToken());
		if(!tokenizer.hasMoreTokens())
			return;
		SynsetType type_1 = synSet[0].getType();
		while(synSet.length==0 && tokenizer.hasMoreTokens())
			synSet = wnDB.getSynsets(tokenizer.nextToken());
		if(!tokenizer.hasMoreTokens())
			return;
		SynsetType type_2 = synSet[0].getType();
		while (tokenizer.hasMoreTokens()) {
			Synset[] synSet2 = wnDB.getSynsets(tokenizer.nextToken());
			if(synSet2.length==0)
				continue;
			SynsetType type_3 = synSet2[0].getType();
			result.append(type_1).append("_").append(type_2).append("_").append(type_3).append(" ");
			type_1=type_2;
			type_2=type_3;
		}
	}
	
	private String modifyString(String question) {
		StringBuilder result = new StringBuilder(question).append(" ");
		aggiungiCoppieConTipi(result, question);
		aggiungiTerneConTipi(result, question);
		aggiungiDomandaTuttaStemmata(result, question);
//		aggiungiDomandaTuttaStemmataConIndicatoriPosizione(result, question);
		accoppiaPrimeDueParoleStemmate(result, question);
		accoppiaPrimeDueParoleNonStemmate(result, question);
		aggiungiParoleTagliateStemmateENon(result, question);
		accoppiaParoleStemmateDueADue(result, question);
		aggiungiPrimaEUltimaParola(result, question);
		aggiungiTerne(result,question);
//		System.out.println(result);
		return result.toString();
	}
	
	@SuppressWarnings("unused")
	private void aggiungiDomandaTuttaStemmataConIndicatoriPosizione(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		int numberOfWord = tokenizer.countTokens();
		int sogliaInizioCentro = numberOfWord/3;
		int sogliaCentroFine = (numberOfWord*2)/3;
		if(numberOfWord<6)
			while (tokenizer.hasMoreTokens()) {
				String word = tokenizer.nextToken();
				result.append(stem(word)).append(" ");
			}
		else
			for(int i=1;tokenizer.hasMoreTokens();i++) {
				String word = tokenizer.nextToken();
				if(i<sogliaInizioCentro)
					result.append("I_");
				else if(i>sogliaCentroFine)
					result.append("F_");
				else
					result.append("C_");
				result.append(stem(word)).append(" ");
			}
	}

	private void aggiungiTerne(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		String first = tokenizer.nextToken();
		String second = tokenizer.nextToken();
		while (tokenizer.hasMoreTokens()) {
			String third = tokenizer.nextToken();
			result.append(stem(first)).append("_").append(stem(second)).append("_").append(stem(third)).append(" ");
			first=second;
			second=third;
		}
	}

	private void aggiungiDomandaTuttaStemmata(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			result.append(stem(word)).append(" ");
		}
	}
	
	private void aggiungiPrimaEUltimaParola(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		String first = tokenizer.nextToken();
		String last = null; 
		while (tokenizer.hasMoreTokens())
			last = tokenizer.nextToken();
		result.append(first).append("_").append(last).append(" ");
	}

	private void accoppiaParoleStemmateDueADue(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		String first = tokenizer.nextToken();
		while (tokenizer.hasMoreTokens()) {
			String second = tokenizer.nextToken();
			result.append(stem(first)).append("_").append(second).append(" ");
			result.append(stem(second)).append("_").append(stem(first)).append(" ");
			result.append(first).append("_").append(stem(second)).append(" ");
			first = second;
		}
	}
	
	private String cutBegin(String word, int length) {
		return word.substring(2, length);
	}
	
	private String cutEnd(String word, int length) {
		return word.substring(0, length-4);
	}

	private void aggiungiParoleTagliateStemmateENon(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
//			result.append(word).append(" ");
			int l = word.length();
			if (l>3) {
				String end = cutBegin(word, l);
				result.append(end).append(" ");
				String stem = stem(end);
				if(!stem.equals(end));
					result.append(stem).append(" ");;	
				if (l>6){
					String start = cutEnd(word, l);
					stem = stem(start);
					result.append(start).append(" ");
					if(!stem.equals(start));
						result.append(stem).append(" ");
				}
			}
			
		}
	}

	private void accoppiaPrimeDueParoleStemmate(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question," ");
		String firstWord = tokenizer.nextToken();
		String secondWord = tokenizer.nextToken();
		String firstWordS = stem(firstWord);
		String secondWordS = stem(secondWord);
		result.append(firstWordS).append("_").append(secondWordS).append(" ");
	}

	private void accoppiaPrimeDueParoleNonStemmate(StringBuilder result, String question) {
		StringTokenizer tokenizer = new StringTokenizer(question," ");
		String firstWord = tokenizer.nextToken();
		String secondWord = tokenizer.nextToken();
		result.append(firstWord).append("_").append(secondWord).append(" ");
	}



	private String stem(String toStem) {
		return wekaStemmer.stem(toStem);
//		snowballStemmer.setCurrent(toStem);
//		snowballStemmer.stem();
//		return snowballStemmer.getCurrent();
	}

	@SuppressWarnings("unused")
	private String first4Char(String question) {
		StringBuilder result = new StringBuilder();
		StringTokenizer tokenizer = new StringTokenizer(question, " ");
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			result.append(word).append(" ");
			int l = word.length();
			if (l>3) {
				String end = word.substring(2, l);
				result.append(end).append(" ");
				result.append(stem(end)).append(" ");;
			}
			if (l>6){
				String start = word.substring(0, l-4);
				result.append(start).append(" ");
				result.append(stem(start)).append(" ");
			}
			result.append(word);
			result.append(stem(word));
		}
		return result.toString();// +" "+question;
	}
	
}
