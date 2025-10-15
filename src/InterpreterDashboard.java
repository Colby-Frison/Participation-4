/**
 * InterpreterDashboard.java
 *
 * Reimplementation of the Daily Operations Dashboard using the Interpreter Pattern.
 * This version replaces hardcoded if-else logic with a flexible, dynamic rule engine.
 * 
 * Key improvements:
 * - Rules can be modified without recompiling code
 * - New conditions and actions can be added dynamically
 * - Business logic is separated from implementation
 * - Easy to understand and maintain
 */
import java.util.List;

public class InterpreterDashboard {
    
    private RuleEngine ruleEngine;
    
    /**
     * Initializes the dashboard with the business rules.
     */
    public InterpreterDashboard() {
        ruleEngine = new RuleEngine();
        initializeRules();
    }
    
    /**
     * Initializes all business rules using the Interpreter Pattern.
     * Rules are evaluated in the order they are added.
     */
    private void initializeRules() {
        // Terminal expressions for each condition
        Expression projectActive = new TerminalExpression("projectActive");
        Expression taskOverdue = new TerminalExpression("taskOverdue");
        Expression kpiBreach = new TerminalExpression("kpiBreach");
        Expression dependencyBlocked = new TerminalExpression("dependencyBlocked");
        
        // Negated expressions
        Expression projectNotActive = new NotExpression(projectActive);
        
        // Rule 1: Project not active - just log
        // Condition: NOT projectActive
        ruleEngine.addRule(new Rule(
            "Inactive Project",
            projectNotActive,
            "Log Event & Notify Project Owner"
        ));
        
        // Rule 2: KPI Breach (highest priority for active projects)
        // Condition: projectActive AND kpiBreach
        ruleEngine.addRule(new Rule(
            "KPI Breach - Critical",
            new AndExpression(projectActive, kpiBreach),
            "Flag Project as AtRisk",
            "Notify Manager",
            "Escalate to Executive"
        ));
        
        // Rule 3: Task Overdue but no KPI breach
        // Condition: projectActive AND taskOverdue AND NOT kpiBreach
        ruleEngine.addRule(new Rule(
            "Task Overdue",
            new AndExpression(
                new AndExpression(projectActive, taskOverdue),
                new NotExpression(kpiBreach)
            ),
            "Notify Manager",
            "Send Reminder to Assignee(s)"
        ));
        
        // Rule 4: Dependency Blocked but no KPI breach or task overdue
        // Condition: projectActive AND dependencyBlocked AND NOT kpiBreach AND NOT taskOverdue
        ruleEngine.addRule(new Rule(
            "Dependency Blocked",
            new AndExpression(
                new AndExpression(
                    new AndExpression(projectActive, dependencyBlocked),
                    new NotExpression(kpiBreach)
                ),
                new NotExpression(taskOverdue)
            ),
            "Notify Manager",
            "Create Dependency Alert"
        ));
        
        // Rule 5: Task Overdue AND Dependency Blocked (but no KPI breach)
        // Condition: projectActive AND taskOverdue AND dependencyBlocked AND NOT kpiBreach
        ruleEngine.addRule(new Rule(
            "Multiple Operational Issues",
            new AndExpression(
                new AndExpression(
                    new AndExpression(projectActive, taskOverdue),
                    dependencyBlocked
                ),
                new NotExpression(kpiBreach)
            ),
            "Notify Manager",
            "Send Reminder to Assignee(s)",
            "Create Dependency Alert"
        ));
        
        // Rule 6: Active project with no issues
        // Condition: projectActive AND NOT taskOverdue AND NOT kpiBreach AND NOT dependencyBlocked
        ruleEngine.addRule(new Rule(
            "Normal Operation",
            new AndExpression(
                new AndExpression(
                    new AndExpression(projectActive, new NotExpression(taskOverdue)),
                    new NotExpression(kpiBreach)
                ),
                new NotExpression(dependencyBlocked)
            ),
            "No Action – Continue Monitoring"
        ));
    }
    
    /**
     * Processes a project decision using the rule engine.
     * 
     * @param projectActive Whether the project is active
     * @param taskOverdue Whether any task is overdue
     * @param kpiBreach Whether there is a KPI breach
     * @param dependencyBlocked Whether any dependency is blocked
     * @return List of actions to perform
     */
    public List<String> processProjectDecision(boolean projectActive, boolean taskOverdue, 
                                                boolean kpiBreach, boolean dependencyBlocked) {
        // Create context with current conditions
        Context context = new Context(projectActive, taskOverdue, kpiBreach, dependencyBlocked);
        
        // Evaluate rules and return actions from first matching rule
        return ruleEngine.evaluateFirstMatch(context);
    }
    
    /**
     * Gets the rule engine for dynamic rule modification.
     * 
     * @return The rule engine
     */
    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }
    
    /**
     * Demonstration of the Interpreter Pattern implementation.
     */
    public static void main(String[] args) {
        InterpreterDashboard dashboard = new InterpreterDashboard();
        
        // Test cases
        TestCase[] testCases = {
            new TestCase("Active with KPI Breach", true, false, true, false),
            new TestCase("Active with Task Overdue", true, true, false, false),
            new TestCase("Active with Dependency Blocked", true, false, false, true),
            new TestCase("Active with Multiple Issues", true, true, false, true),
            new TestCase("Active and Nominal", true, false, false, false),
            new TestCase("Project Not Active", false, true, false, true)
        };
        
        System.out.println("=== Daily Operations Dashboard - Interpreter Pattern ===\n");
        
        for (TestCase test : testCases) {
            List<String> result = dashboard.processProjectDecision(
                test.projectActive,
                test.taskOverdue,
                test.kpiBreach,
                test.dependencyBlocked
            );
            
            System.out.println(test.description + ":");
            System.out.println(" -> " + result);
            System.out.println();
        }
        
        // Demonstrate dynamic rule inspection
        System.out.println("\n=== Current Business Rules ===");
        List<Rule> rules = dashboard.getRuleEngine().getRules();
        for (int i = 0; i < rules.size(); i++) {
            System.out.println((i + 1) + ". " + rules.get(i));
        }
    }
    
    /**
     * Simple test case structure.
     */
    static class TestCase {
        String description;
        boolean projectActive;
        boolean taskOverdue;
        boolean kpiBreach;
        boolean dependencyBlocked;
        
        TestCase(String description, boolean projectActive, boolean taskOverdue,
                boolean kpiBreach, boolean dependencyBlocked) {
            this.description = description;
            this.projectActive = projectActive;
            this.taskOverdue = taskOverdue;
            this.kpiBreach = kpiBreach;
            this.dependencyBlocked = dependencyBlocked;
        }
    }
}

