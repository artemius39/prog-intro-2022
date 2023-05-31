package expression.exceptions;

import expression.*;
import expression.parser.*;

public class ExpressionParser implements TripleParser {
    @Override
    public TripleExpression parse(String expression) throws ExpressionParsingException {
        if (expression == null) {
            throw new ExpressionParsingException("Expression must be non-null");
        }
        return new Parser(new StringSource(expression)).parse();
    }

    static class Parser extends BaseParser {
        private BinaryOperator previouslyReadBinaryOperator;

        private Parser(CharSource source) {
            super(source);
            previouslyReadBinaryOperator = null;
        }

        @Override
        protected ExpressionParsingException error(String message) {
            return new ExpressionParsingException(getPosition() + ": " + message);
        }

        private ExpressionParsingException error(String message, int pos) {
            return new ExpressionParsingException(pos + ": " + message);
        }

        private TripleExpression parse() {
            TripleExpression result = parseExpression();
            if (eof()) {
                return result;
            }
            throw error("end of input expected");
        }

        private TripleExpression parseExpression() {
            TripleExpression result = parseExpression(Integer.MIN_VALUE);
            if (previouslyReadBinaryOperator == null) {
                return result;
            }
            throw error("operand expected");
        }

        // parses expression as an operand for an operation with given priority
        private TripleExpression parseExpression(final int priority) {
            TripleExpression operand = parsePrimary();
            skipWhitespace();
            while (!eof()) {
                final BinaryOperator binaryOperator = parseBinaryOperator();
                if (binaryOperator == null) {
                    break;
                } else if (binaryOperator.priority <= priority) {
                    previouslyReadBinaryOperator = binaryOperator;
                    break;
                } else {
                    operand = binaryOperator.applyOperation(operand, parseExpression(binaryOperator.priority));
                    skipWhitespace();
                }
            }
            return operand;
        }

        private BinaryOperator parseBinaryOperator() {
            if (previouslyReadBinaryOperator != null) {
                final BinaryOperator result = previouslyReadBinaryOperator;
                previouslyReadBinaryOperator = null;
                return result;
            }

            if (take('+')) {
                return BinaryOperator.ADD;
            } else if (take('-')) {
                return BinaryOperator.SUBTRACT;
            } else if (take('*')) {
                return BinaryOperator.MULTIPLY;
            } else if (take('/')) {
                return BinaryOperator.DIVIDE;
            }

            int identifierStart = getPosition();
            final String identifier = parseIdentifier();
            if (identifier.equals("set")) {
                return BinaryOperator.SET;
            } else if (identifier.equals("clear")) {
                return BinaryOperator.CLEAR;
            } else if (identifier.isEmpty()){
                return null;
            } else {
                throw error("invalid identifier: " + identifier, identifierStart);
            }
        }

        private String parseIdentifier() {
            final StringBuilder sb = new StringBuilder();
            if (Character.isJavaIdentifierStart(ch)) {
                sb.append(take());
                while (!eof() && Character.isJavaIdentifierPart(ch)) {
                    sb.append(take());
                }
            }
            return sb.toString();
        }

        private String getPrimary() {
            if (take('(')) {
                return "(";
            }

            StringBuilder sb = new StringBuilder();
            if (take('-')) {
                sb.append('-');
            }
            while (Character.isDigit(ch)) {
                sb.append(take());
            }

            if (!sb.isEmpty()) {
                return sb.toString();
            } else {
                return parseIdentifier();
            }
        }

        private TripleExpression parsePrimary() {
            skipWhitespace();

            int primaryStart = getPosition();
            String primary = getPrimary();
            if (primary.isEmpty()) {
                throw error("expression expected");
            }

            switch (primary) {
                case "(" -> {
                    final TripleExpression result = parseExpression();
                    skipWhitespace();
                    if (!take(')')) {
                        throw error("')' expected", getPosition() + 1);
                    }
                    return result;
                }
                case "-" -> {
                    return new CheckedNegate(parsePrimary());
                }
                case "count" -> {
                    return new Count(parsePrimary());
                }
                case "pow10" -> {
                    return new Pow10(parsePrimary());
                }
                case "log10" -> {
                    return new Log10(parsePrimary());
                }
                case "x", "y", "z" -> {
                    return new Variable(primary);
                }
                default -> {
                    try {
                        return new Const(Integer.parseInt(primary));
                    } catch (final NumberFormatException e) {
                        throw error("invalid expression start: " + primary, primaryStart);
                    }
                }
            }
        }
    }
}