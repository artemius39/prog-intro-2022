package expression.parser;

import expression.*;

public class ExpressionParser implements TripleParser {
    @Override
    public TripleExpression parse(final String expression) {
        return new Parser(new StringSource(expression)).parseExpression();
    }

    private static class Parser extends BaseParser {
        private BinaryOperator previouslyReadBinaryOperator;

        private Parser(final CharSource source) {
            super(source);
            previouslyReadBinaryOperator = null;
        }

        private TripleExpression parseExpression() {
            return parseExpression(Integer.MIN_VALUE);
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

            final String operator = parseIdentifier();
            if (operator.equals("set")) {
                return BinaryOperator.SET;
            } else if (operator.equals("clear")) {
                return BinaryOperator.CLEAR;
            } else {
                return null;
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

            String primary = getPrimary();
            if (primary.isEmpty()) {
                throw error("expression expected");
            }

            switch (primary) {
                case "(" -> {
                    final TripleExpression result = parseExpression();
                    skipWhitespace();
                    expect(')');
                    return result;
                }
                case "-" -> {
                    return new Negate(parsePrimary());
                }
                case "count" -> {
                    return new Count(parsePrimary());
                }
                case "x", "y", "z" -> {
                    return new Variable(primary);
                }
                default -> {
                    try {
                        return new Const(Integer.parseInt(primary));
                    } catch (final NumberFormatException e) {
                        throw error("invalid expression start: " + primary);
                    }
                }
            }
        }
    }
}
