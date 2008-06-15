package ia2;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class TestJAWS {

	/**
	 * Main entry point. The command-line arguments are concatenated together
	 * (separated by spaces) and used as the word form to look up.
	 */
	public static void main(String[] args) {
		System.setProperty("wordnet.database.dir", "C:\\Users\\error0\\Documents\\universita\\ia2\\WordNet-3.0\\dict");
		String wordForm = "bike";

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(wordForm);
		// Display the word forms and definitions for synsets retrieved
		if (synsets.length > 0) {
			System.out.println("The following synsets contain '" + wordForm
					+ "' or a possible base form of that text:");
			for (int i = 0; i < synsets.length; i++) {
				System.out.println("");
				String[] wordForms = synsets[i].getWordForms();
				System.out.println(synsets[i].getTagCount(wordForm));
				System.out.println(synsets[i].getType());

				for (int j = 0; j < wordForms.length; j++) {
					System.out.print((j > 0 ? ", " : "") + wordForms[j]);
				}
				System.out.println(": " + synsets[i].getDefinition());
			}
		} else {
			System.err.println("No synsets exist that contain " + "the word form '" + wordForm + "'");
		}

	}

}
