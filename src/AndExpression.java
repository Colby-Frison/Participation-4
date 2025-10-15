/**
 * AndExpression.java
 * 
 * NonTerminalExpression - represents the logical AND operator.
 * Evaluates to true only if both sub-expressions are true.
 */
public class AndExpression implements Expression {
    private Expression left;
    private Expression right;
    
    /**
     * Creates an AND expression with two sub-expressions.
     * 
     * @param left The left operand expression
     * @param right The right operand expression
     */
    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    /**
     * Evaluates both sub-expressions and returns true only if both are true.
     * 
     * @param context The context for evaluation
     * @return true if both left AND right evaluate to true
     */
    @Override
    public boolean interpret(Context context) {
        return left.interpret(context) && right.interpret(context);
    }
    
    @Override
    public String toString() {
        return "(" + left + " AND " + right + ")";
    }
}

