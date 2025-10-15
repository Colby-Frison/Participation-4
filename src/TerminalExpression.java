/**
 * TerminalExpression.java
 * 
 * TerminalExpression - represents constants or basic conditions.
 * These are the leaf nodes in the expression tree.
 */
public class TerminalExpression implements Expression {
    private String conditionName;
    
    /**
     * Creates a terminal expression for a specific condition.
     * 
     * @param conditionName The name of the condition to evaluate
     */
    public TerminalExpression(String conditionName) {
        this.conditionName = conditionName;
    }
    
    /**
     * Evaluates the condition by looking it up in the context.
     * 
     * @param context The context containing the condition values
     * @return The boolean value of the condition
     */
    @Override
    public boolean interpret(Context context) {
        return context.getCondition(conditionName);
    }
    
    @Override
    public String toString() {
        return conditionName;
    }
}

