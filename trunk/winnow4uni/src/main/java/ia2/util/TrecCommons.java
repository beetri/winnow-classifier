package ia2.util;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public final class TrecCommons {

	/**
	 * Crea un istanza con "nomeCategoria - domanda".
	 * @param nameCategory - Il nome della categoria
	 * @param question - La domanda
	 * @param dataSet - Il dataset alla quale appartiene la {@link Instance} creata
	 * @return L'instance creata
	 */
	public static Instance createInstance(String nameCategory, String question, Instances dataSet) {
		Instance resultInstance = new Instance(2);//TODO accoppiato con il numero di attributo
		resultInstance.setDataset(dataSet);
		resultInstance.setClassValue(nameCategory);
		resultInstance.setValue(1, question);//TODO stesso problema dell'uso dell'indice della classe
		return resultInstance;
	}
	
	/**
	 * Ritorna il {@link FastVector} con le classi (macro).
	 * @return Il {@link FastVector} delle classi
	 */
	private static FastVector getClassValuesFastVector() {
		FastVector classValues = new FastVector(6);
		classValues.addElement("ABBR");
		classValues.addElement("DESC");
		classValues.addElement("ENTY");		
		classValues.addElement("HUM");
		classValues.addElement("LOC");
		classValues.addElement("NUM");		
		return classValues;
	}
	
	/**
	 * Metodo che ritorna il {@link FastVector} delle {@link Instance}.
	 * @return Il {@link FastVector} con "classe - domanda"
	 */
	public static FastVector getAttributesFastVector(){		
		Attribute questionAttribute = new Attribute("question", (FastVector)null);
		Attribute classAttribute = new Attribute("Class",getClassValuesFastVector());
		FastVector result = new FastVector(2);
		//aggiungere prima la classe se si vuole che abbia indice 0
		result.addElement(classAttribute);
		result.addElement(questionAttribute);		
		return result;
	}
	
}
