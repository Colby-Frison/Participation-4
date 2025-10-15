/**
 * Expression.java
 * 
 * AbstractExpression - the common interface for all expressions in the Interpreter Pattern.
 * All concrete expressions must implement the interpret() method.
 */
public interface Expression {
    /**
     * Evaluates this expression given the current context.
     * 
     * @param context The context containing the state/data for evaluation
     * @return true if the expression evaluates to true, false otherwise
     */
    boolean interpret(Context context);
}

