package expression;

import expression.exceptions.ExpressionEvaluationException;

public class Log10 extends UnaryOperation implements TripleExpression    {
    @Override
    protected String getSign() {
        return "log10";
    }

    public Log10(TripleExpression operand) {
        super(operand);
    }

    @Override
    protected int eval(int a) {
        if (a <= 0) {
            throw new ExpressionEvaluationException("non-positive operand");
        }
        int pow = 1;
        int ans = 0;
        while (pow <= a / 10) {
            pow *= 10;
            ans++;
        }
        return ans;
    }
}
