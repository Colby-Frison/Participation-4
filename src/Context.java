/**
 * Context.java
 * 
 * Context - stores and manages data for interpretation.
 * This class holds the state of all conditions that can be evaluated.
 */
import java.util.HashMap;
import java.util.Map;

public class Context {
    private Map<String, Boolean> conditions;
    
    /**
     * Creates a new Context with the given condition values.
     * 
     * @param projectActive Whether the project is active
     * @param taskOverdue Whether any task is overdue
     * @param kpiBreach Whether there is a KPI breach
     * @param dependencyBlocked Whether any dependency is blocked
     */
    public Context(boolean projectActive, boolean taskOverdue, boolean kpiBreach, boolean dependencyBlocked) {
        conditions = new HashMap<>();
        conditions.put("projectActive", projectActive);
        conditions.put("taskOverdue", taskOverdue);
        conditions.put("kpiBreach", kpiBreach);
        conditions.put("dependencyBlocked", dependencyBlocked);
    }
    
    /**
     * Gets the value of a condition by name.
     * 
     * @param conditionName The name of the condition to retrieve
     * @return The boolean value of the condition
     */
    public boolean getCondition(String conditionName) {
        return conditions.getOrDefault(conditionName, false);
    }
    
    /**
     * Sets the value of a condition.
     * 
     * @param conditionName The name of the condition
     * @param value The boolean value to set
     */
    public void setCondition(String conditionName, boolean value) {
        conditions.put(conditionName, value);
    }
}

