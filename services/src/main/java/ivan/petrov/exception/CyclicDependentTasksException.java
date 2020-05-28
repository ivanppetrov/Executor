package ivan.petrov.exception;

public class CyclicDependentTasksException extends RuntimeException {

	private static final long serialVersionUID = 4594284029861921717L;

	public CyclicDependentTasksException(String message) {
		super(message);
	}

}
