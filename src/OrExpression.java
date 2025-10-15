/**
 * OrExpression.java
 * 
 * NonTerminalExpression - represents the logical OR operator.
 * Evaluates to true if either sub-expression is true.
 */
public class OrExpression implements Expression {
    private Expression left;
    private Expression right;
    
    /**
     * Creates an OR expression with two sub-expressions.
     * 
     * @param left The left operand expression
     * @param right The right operand expression
     */
    public OrExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    /**
     * Evaluates both sub-expressions and returns true if either is true.
     * 
     * @param context The context for evaluation
     * @return true if either left OR right evaluates to true
     */
    @Override
    public boolean interpret(Context context) {
        return left.interpret(context) || right.interpret(context);
    }
    
    @Override
    public String toString() {
        return "(" + left + " OR " + right + ")";
    }
}

