/**
 * NotExpression.java
 * 
 * NonTerminalExpression - represents the logical NOT operator.
 * Negates the result of the sub-expression.
 */
public class NotExpression implements Expression {
    private Expression expression;
    
    /**
     * Creates a NOT expression with a sub-expression to negate.
     * 
     * @param expression The expression to negate
     */
    public NotExpression(Expression expression) {
        this.expression = expression;
    }
    
    /**
     * Evaluates the sub-expression and returns its negation.
     * 
     * @param context The context for evaluation
     * @return true if the expression evaluates to false, and vice versa
     */
    @Override
    public boolean interpret(Context context) {
        return !expression.interpret(context);
    }
    
    @Override
    public String toString() {
        return "(NOT " + expression + ")";
    }
}

