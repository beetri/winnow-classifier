package ia2.preprocess;

public class PreprocessorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 262855552896358381L;

	/**
	 * Questa è un eccezione
	 * @param message
	 * @param e
	 */
	public PreprocessorException(String message, Throwable e) {
		super(message,e);
	}

}
