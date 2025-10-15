/**
 * RuleEngine.java
 * 
 * The rule engine that manages and evaluates all business rules.
 * Rules are evaluated in order until one matches (first-match wins).
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RuleEngine {
    private LinkedList<Rule> rules;
    
    /**
     * Creates a new rule engine with an empty rule set.
     */
    public RuleEngine() {
        rules = new LinkedList<>();
    }
    
    /**
     * Adds a rule to the engine.
     * 
     * @param rule The rule to add
     */
    public void addRule(Rule rule) {
        rules.add(rule);
    }
    
    /**
     * Evaluates all rules against the context and returns the first matching rule's actions.
     * 
     * @param context The context containing condition values
     * @return List of actions from the first matching rule, or empty list if no match
     */
    public List<String> evaluateFirstMatch(Context context) {
        for (Rule rule : rules) {
            List<String> actions = rule.evaluate(context);
            if (!actions.isEmpty()) {
                return actions;
            }
        }
        return new ArrayList<>();
    }
    
    /**
     * Evaluates all rules and collects actions from all matching rules.
     * 
     * @param context The context containing condition values
     * @return List of all actions from all matching rules
     */
    public List<String> evaluateAllMatches(Context context) {
        List<String> allActions = new ArrayList<>();
        for (Rule rule : rules) {
            List<String> actions = rule.evaluate(context);
            allActions.addAll(actions);
        }
        return allActions;
    }
    
    /**
     * Gets all rules in the engine.
     * 
     * @return List of all rules
     */
    public List<Rule> getRules() {
        return new ArrayList<>(rules);
    }
    
    /**
     * Removes all rules from the engine.
     */
    public void clearRules() {
        rules.clear();
    }
}

