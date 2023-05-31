package expression.exceptions;

public class DivisionByZeroException extends ExpressionEvaluationException{
    public DivisionByZeroException(String message) {
        super(message);
    }
}
