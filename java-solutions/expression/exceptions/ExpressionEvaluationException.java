package expression.exceptions;

public class ExpressionEvaluationException extends ExpressionException {
    public ExpressionEvaluationException(String message) {
        super(message);
    }

    public ExpressionEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionEvaluationException(Throwable cause) {
        super(cause);
    }
}
