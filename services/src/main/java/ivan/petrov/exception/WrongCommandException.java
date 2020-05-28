package ivan.petrov.exception;

public class WrongCommandException extends RuntimeException {

	private static final long serialVersionUID = 5824353225898606758L;

	public WrongCommandException(String message) {
		super(message);
	}
}
