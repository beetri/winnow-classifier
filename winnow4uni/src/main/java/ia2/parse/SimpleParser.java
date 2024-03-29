package ia2.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class SimpleParser implements Parser {
		
	private static final int DEFAULT_NUMBER_OF_INSTANCE = 6000;	
	private static final String DEFAULT_DATASET_NAME = "default dataset";
		
	private BufferedReader bufferedReader;
	
	public SimpleParser(InputStream inputStream) {
		this(new InputStreamReader(inputStream));
	}
	
	public SimpleParser(Reader inputStream) {
		this(new BufferedReader(inputStream));
	}
	
	public SimpleParser(BufferedReader inputStream) {
		this.bufferedReader = inputStream;
	}
	
	/*
	 * si potrebbe leggere le classi direttamente dal file, dinamicamente
	 */
	private FastVector getClassValuesFastVector() {
		FastVector classValues = new FastVector(6);
		classValues.addElement("ABBR");
		classValues.addElement("DESC");
		classValues.addElement("ENTY");		
		classValues.addElement("HUM");
		classValues.addElement("LOC");
		classValues.addElement("NUM");		
		return classValues;
	}
	
	private FastVector getAttributesFastVector(){		
		Attribute questionAttribute = new Attribute("question", (FastVector)null);
		Attribute classAttribute = new Attribute("Class",getClassValuesFastVector());
		FastVector result = new FastVector(2);
		result.addElement(questionAttribute);
		result.addElement(classAttribute);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see weka.parser.Parser#getDataSet()
	 */
	@Override
	public Instances getDataSet() {
		Instances resultDataSet = new Instances(DEFAULT_DATASET_NAME,this.getAttributesFastVector(),DEFAULT_NUMBER_OF_INSTANCE);
		resultDataSet.setClassIndex(1);//TODO � troppo accoppiato
		try {
			populateDataSet(resultDataSet,this.bufferedReader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultDataSet;
	}

	private void populateDataSet(Instances dataSet,BufferedReader bufferedReader) throws IOException {
		//legge il fila riga per riga, finch� non finisce (= null)
		for (String newLine = bufferedReader.readLine(); newLine!=null; newLine = bufferedReader.readLine()){
			Instance instance = readInstance(newLine,dataSet);
			dataSet.add(instance);
		}
			
	}

	private Instance readInstance(String line, Instances dataSet) {
		String nameCoarseCategory;		//La classe generica
		@SuppressWarnings("unused")
		String nameFineCategory;		//La classe specifica
		String question;				//La domanda divisa in token
		
		StringTokenizer stringTokenizer = new StringTokenizer(line, ":");
		nameCoarseCategory =	stringTokenizer.nextToken();
		nameFineCategory =		stringTokenizer.nextToken(" ");		//Da questo punto il delimitatore resta o spazio
		StringBuilder stringBuilder = new StringBuilder();		//il numero di elementi rimanenti
		for(int i=0; stringTokenizer.hasMoreTokens(); i++)
			stringBuilder.append(stringTokenizer.nextToken()).append(" ");
		question = stringBuilder.toString();
		return createInstance(nameCoarseCategory,question,dataSet);		
	}

	private Instance createInstance(String nameCoarseCategory, String question,	Instances dataSet) {
		Instance resultInstance = new Instance(2);//TODO accoppiato con il numero di attributo
		resultInstance.setDataset(dataSet);
		resultInstance.setClassValue(nameCoarseCategory);
		resultInstance.setValue(0, question);//TODO stesso problema dell'uso dell'indice della classe
		return resultInstance;
	}

	@Override
	public Instances getDataSet(int numberOfInstanceToRead) {
		Instances resultDataSet = new Instances(DEFAULT_DATASET_NAME,this.getAttributesFastVector(),DEFAULT_NUMBER_OF_INSTANCE);
		resultDataSet.setClassIndex(1);//TODO � troppo accoppiato
		try {
			populateDataSet(resultDataSet,this.bufferedReader,numberOfInstanceToRead);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultDataSet;
	}

	private void populateDataSet(Instances dataSet,BufferedReader bufferedReader, int numberOfInstanceToRead) throws IOException {
		for (String newLine = bufferedReader.readLine(); newLine!=null && numberOfInstanceToRead-- > 0; newLine = bufferedReader.readLine()){
			Instance instance = readInstance(newLine,dataSet);
			dataSet.add(instance);
		}
	}

	

}
