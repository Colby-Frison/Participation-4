/**
 * Rule.java
 * 
 * Represents a business rule consisting of a condition (expression) and actions.
 * When the condition evaluates to true, all associated actions are executed.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rule {
    private String name;
    private Expression condition;
    private List<String> actions;
    
    /**
     * Creates a new rule with a name, condition, and actions.
     * 
     * @param name The name/description of this rule
     * @param condition The expression that must be true for actions to execute
     * @param actions The list of actions to perform when condition is true
     */
    public Rule(String name, Expression condition, String... actions) {
        this.name = name;
        this.condition = condition;
        this.actions = new ArrayList<>(Arrays.asList(actions));
    }
    
    /**
     * Evaluates the rule's condition and returns actions if true.
     * 
     * @param context The context for evaluating the condition
     * @return List of actions if condition is true, empty list otherwise
     */
    public List<String> evaluate(Context context) {
        if (condition.interpret(context)) {
            return new ArrayList<>(actions);
        }
        return new ArrayList<>();
    }
    
    /**
     * Gets the name of this rule.
     * 
     * @return The rule name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the condition expression.
     * 
     * @return The condition expression
     */
    public Expression getCondition() {
        return condition;
    }
    
    @Override
    public String toString() {
        return "Rule: " + name + " | Condition: " + condition + " | Actions: " + actions;
    }
}

