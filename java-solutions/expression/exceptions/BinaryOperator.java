package expression.exceptions;

import expression.*;

public enum BinaryOperator {
    SET(Priority.SET), CLEAR(Priority.SET),
    ADD(Priority.ADD), SUBTRACT(Priority.ADD),
    MULTIPLY(Priority.MULTIPLY), DIVIDE(Priority.MULTIPLY);

    public final int priority;

    BinaryOperator(Priority priority) {
        this.priority = priority.ordinal();
    }

    public TripleExpression applyOperation(TripleExpression left, TripleExpression right) {
        return switch (this) {
            case ADD -> new CheckedAdd(left, right);
            case SUBTRACT -> new CheckedSubtract(left, right);
            case MULTIPLY -> new CheckedMultiply(left, right);
            case DIVIDE -> new CheckedDivide(left, right);
            case SET -> new Set(left, right);
            case CLEAR -> new Clear(left, right);
        };
    }
}
