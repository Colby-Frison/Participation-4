# Interpreter Pattern Implementation
## Daily Operations Dashboard - Group H

### Overview
This implementation transforms the hardcoded conditional logic from `DailyOperationsDashboard.java` into a flexible, extensible rule engine using the **Interpreter Pattern**.

---

## Architecture

### Pattern Components

#### 1. **AbstractExpression** (`Expression.java`)
- Common interface for all expressions
- Defines `interpret(Context)` method
- All concrete expressions implement this interface

#### 2. **TerminalExpression** (`TerminalExpression.java`)
- Represents basic conditions (leaf nodes)
- Examples: `projectActive`, `taskOverdue`, `kpiBreach`, `dependencyBlocked`
- Looks up condition values from Context

#### 3. **NonTerminalExpression** (Operator Classes)
- **`AndExpression.java`**: Logical AND operator
- **`OrExpression.java`**: Logical OR operator  
- **`NotExpression.java`**: Logical NOT operator
- Compose sub-expressions into complex conditions

#### 4. **Context** (`Context.java`)
- Hash table storing condition values
- Manages state for expression evaluation
- Provides getter/setter for conditions

#### 5. **Rule** (`Rule.java`)
- Combines a condition (Expression) with actions
- When condition evaluates to true, returns associated actions
- Represents business rules

#### 6. **RuleEngine** (`RuleEngine.java`)
- Manages collection of rules
- Evaluates rules in order
- Supports first-match or all-matches evaluation

---

## Expression Tree Examples

### Example 1: KPI Breach Rule
**Condition:** `projectActive AND kpiBreach`

```
        AND
       /   \
projectActive  kpiBreach
```

**Actions:**
- Flag Project as AtRisk
- Notify Manager
- Escalate to Executive

### Example 2: Multiple Operational Issues
**Condition:** `projectActive AND taskOverdue AND dependencyBlocked AND NOT kpiBreach`

```
                AND
               /   \
            AND     NOT
           /   \      \
        AND   dependencyBlocked  kpiBreach
       /   \
projectActive  taskOverdue
```

**Actions:**
- Notify Manager
- Send Reminder to Assignee(s)
- Create Dependency Alert

---

## Key Benefits

### 1. **Dynamic Rule Modification**
Rules can be added, removed, or modified at runtime  
No recompilation required  
Business logic separated from code

### 2. **Extensibility**
Easy to add new operators (XOR, NAND, etc.)  
New conditions can be added to Context  
Actions can be modified independently

### 3. **Maintainability**
Clear separation of concerns  
Each expression is a single, focused class  
Easy to understand and test

### 4. **Flexibility**
Same grammar supports different semantics  
Rules can be loaded from configuration files  
Support for complex nested conditions

---

## Comparison: Before vs After

### Before (Hardcoded)
```java
if (!projectActive) {
    actions.add("Log Event & Notify Project Owner");
    return actions;
}

if (kpiBreach) {
    actions.add("Flag Project as AtRisk");
    actions.add("Notify Manager");
    actions.add("Escalate to Executive");
    return actions;
}
// ... more nested if-else statements
```

**Problems:**
- Requires recompilation for rule changes
- Difficult to modify without introducing bugs
- Hard to test individual conditions
- Business logic mixed with implementation

### After (Interpreter Pattern)
```java
Expression condition = new AndExpression(
    projectActive, 
    kpiBreach
);

Rule rule = new Rule(
    "KPI Breach - Critical",
    condition,
    "Flag Project as AtRisk",
    "Notify Manager",
    "Escalate to Executive"
);

ruleEngine.addRule(rule);
```

**Benefits:**
- Rules are data, not code
- Can be modified at runtime
- Easy to test each component
- Clear business logic representation

---

## Usage Example

```java
// Create dashboard with rules
InterpreterDashboard dashboard = new InterpreterDashboard();

// Process a decision
List<String> actions = dashboard.processProjectDecision(
    true,  // projectActive
    false, // taskOverdue
    true,  // kpiBreach
    false  // dependencyBlocked
);

System.out.println(actions);
// Output: [Flag Project as AtRisk, Notify Manager, Escalate to Executive]
```

---

## Dynamic Rule Update Example

```java
// Get the rule engine
RuleEngine engine = dashboard.getRuleEngine();

// Add a new custom rule
Expression newCondition = new AndExpression(
    new TerminalExpression("projectActive"),
    new TerminalExpression("customCondition")
);

Rule newRule = new Rule(
    "Custom Rule",
    newCondition,
    "Custom Action 1",
    "Custom Action 2"
);

engine.addRule(newRule);
```

---

## Testing

### Running the Implementation
```bash
# Compile all files
javac *.java

# Run the interpreter dashboard
java InterpreterDashboard
```

### Expected Output
```
=== Daily Operations Dashboard - Interpreter Pattern ===

Active with KPI Breach:
 -> [Flag Project as AtRisk, Notify Manager, Escalate to Executive]

Active with Task Overdue:
 -> [Notify Manager, Send Reminder to Assignee(s)]

Active with Dependency Blocked:
 -> [Notify Manager, Create Dependency Alert]

...
```

---

## Design Decisions

### 1. First-Match vs All-Matches
- Implemented both strategies in `RuleEngine`
- Dashboard uses first-match for deterministic behavior
- Prioritizes rules by adding them in order of importance

### 2. Terminal Expression Design
- Uses string-based condition names for flexibility
- Context acts as symbol table
- Easy to extend with new condition types

### 3. Rule Evaluation Order
1. Inactive projects (return immediately)
2. KPI breach (highest priority)
3. Operational issues (task overdue, dependencies)
4. Normal operation (fallback)

---

## Future Enhancements

### Potential Improvements
1. **Persistence**: Load/save rules from JSON/XML
2. **GUI Editor**: Visual rule builder interface
3. **Rule Priority**: Explicit priority levels
4. **Conflict Resolution**: Handle overlapping rules
5. **Performance**: Optimize expression evaluation
6. **Validation**: Check for contradictory rules

---

## Pattern Relationships

- **Composite Pattern**: Expression tree structure
- **Strategy Pattern**: Different evaluation strategies
- **Chain of Responsibility**: Sequential rule evaluation
- **Command Pattern**: Actions as commands

---

## Conclusion

The Interpreter Pattern successfully transforms rigid conditional logic into a flexible, maintainable rule engine. This implementation demonstrates:

- Clean separation of business logic from implementation
- Easy extensibility for new conditions and actions
- Runtime rule modification capabilities
- Clear, understandable code structure

This approach is ideal for systems where business rules change frequently and need to be modified by non-programmers through configuration rather than code changes.

