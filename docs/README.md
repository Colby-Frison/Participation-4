# Participation 4 - Interpreter Pattern
## Daily Operations Dashboard - Group H

**Implementation of the Interpreter Pattern** transforming hardcoded decision logic into a flexible, dynamic rule engine.

---

## Project Structure

```
Participation 4/
├── src/                    # Java source files
│   ├── Expression.java
│   ├── Context.java
│   ├── TerminalExpression.java
│   ├── AndExpression.java
│   ├── OrExpression.java
│   ├── NotExpression.java
│   ├── Rule.java
│   ├── RuleEngine.java
│   ├── InterpreterDashboard.java
│   ├── DailyOperationsDashboard.java
│   └── RuleApiServer.java  # NEW: HTTP API for live rule adding
├── build/                  # Compiled class files
├── docs/                   # Documentation
│   ├── IMPLEMENTATION.md
│   ├── PRESENTATION_GUIDE.md
│   ├── API.md              # NEW: API documentation
│   ├── Instructions.md
│   └── Section 5-10.md
├── scripts/                # Build and run scripts
│   ├── build-and-run-interpreter.bat
│   └── demo.sh             # NEW: Interactive API demo script
└── README.md              # This file
```

---

## Quick Start

### Option 1: Use the Build Script (Recommended)
```bash
# From project root
scripts\build-and-run-interpreter.bat
```

### Option 2: Manual Compilation
```bash
# Compile
javac -d build src\*.java

# Run
java -cp build InterpreterDashboard
```

### Option 3: Live Rule Adding API Demo (NEW!)
```bash
# Compile and start the API server
cd src
javac *.java
java RuleApiServer

# In another terminal, run the interactive demo
cd scripts
./demo.sh
```

See **[API.md](API.md)** for complete API documentation.

---

## What We Built

**Interpreter Pattern Implementation**
- AbstractExpression interface
- Terminal expressions (4 conditions)
- Non-terminal expressions (AND, OR, NOT operators)
- Context for state management
- Rule engine for dynamic evaluation

**Complete Documentation**
- Technical implementation guide
- 4-minute presentation script
- Code examples and diagrams

---

## Documentation

All documentation is in the **`docs/`** directory:

- **[IMPLEMENTATION.md](IMPLEMENTATION.md)** - Complete technical documentation
  - Architecture overview
  - Pattern components explained
  - Expression tree examples
  - Usage examples

- **[API.md](API.md)** - **NEW: Live Rule Adding API Documentation**
  - HTTP API endpoints
  - How to add rules at runtime
  - Complete examples and demo walkthrough
  - Demonstrates the power of the Interpreter Pattern

- **[Instructions.md](Instructions.md)** - Original assignment requirements

- **[Section 5-10.md](Section%205-10.md)** - Textbook notes on Interpreter Pattern

---

## Decision Table - Business Logic

The Daily Operations Dashboard implements the following business rules through the Interpreter Pattern:

### Conditions (Input Variables)
- **Project Active?** (Y/N) - Whether the project is currently active
- **Any Task Overdue?** (Y/N) - Whether any task has passed its deadline
- **KPI Breach?** (Y/N) - Whether key performance indicators are violated
- **Dependency Blocked?** (Y/N) - Whether any project dependency is blocked

### Actions (Output Variables)
- **Flag Project as AtRisk** - Mark project for executive attention
- **Notify Manager** - Send alert to project manager
- **Escalate to Executive** - Send alert to executive team
- **Send Reminder to Assignee(s)** - Notify task assignees
- **Create Dependency Alert** - Generate dependency resolution alert
- **Log Event & Notify Project Owner** - Record event and notify owner

### Decision Table

|                                      | 1   | 2   | 3   | 4   | 5   | 6   |
| ------------------------------------ | --- | --- | --- | --- | --- | --- |
| **Project Active?**                  | N   | Y   | Y   | Y   | Y   | Y   |
| **Any Task Overdue?**                | -   | -   | Y   | N   | Y   | N   |
| **KPI Breach?**                      | -   | Y   | N   | N   | N   | N   |
| **Dependency Blocked?**              | -   | -   | N   | Y   | Y   | N   |
| **Rule Count**                       | 8   | 4   | 1   | 1   | 1   | 1   |
| **Flag Project as AtRisk**           |     | X   |     |     |     |     |
| **Notify Manager**                   |     | X   | X   | X   | X   |     |
| **Escalate to Executive**            |     | X   |     |     |     |     |
| **Send Reminder to Assignee(s)**     |     |     | X   |     | X   |     |
| **Create Dependency Alert**          |     |     |     | X   | X   |     |
| **Log Event & Notify Project Owner** | X   |     |     |     |     |     |
| **No Action – Continue Monitoring**  |     |     |     |     |     | X   |

**Legend:** Y = Yes, N = No, - = Don't Care, X = Action Performed

---

## From Decision Table to Class Structure

### Step 1: Identify Conditions as Terminal Expressions
Each condition in the decision table becomes a `TerminalExpression`:

```java
Expression projectActive = new TerminalExpression("projectActive");
Expression taskOverdue = new TerminalExpression("taskOverdue");
Expression kpiBreach = new TerminalExpression("kpiBreach");
Expression dependencyBlocked = new TerminalExpression("dependencyBlocked");
```

### Step 2: Build Complex Conditions as Non-Terminal Expressions
Each rule's condition becomes a composed expression using logical operators:

**Rule 2 Example:** `Project Active AND KPI Breach`
```java
Expression rule2Condition = new AndExpression(
    new TerminalExpression("projectActive"),
    new TerminalExpression("kpiBreach")
);
```

**Rule 5 Example:** `Project Active AND Task Overdue AND NOT KPI Breach AND Dependency Blocked`
```java
Expression rule5Condition = new AndExpression(
    new AndExpression(
        new AndExpression(
            new TerminalExpression("projectActive"),
            new TerminalExpression("taskOverdue")
        ),
        new NotExpression(new TerminalExpression("kpiBreach"))
    ),
    new TerminalExpression("dependencyBlocked")
);
```

### Step 3: Create Rules with Conditions and Actions
Each row in the decision table becomes a `Rule` object:

```java
Rule rule2 = new Rule(
    "KPI Breach - Critical",
    rule2Condition,
    "Flag Project as AtRisk",
    "Notify Manager", 
    "Escalate to Executive"
);
```

### Step 4: Organize Rules in Rule Engine
All rules are added to a `RuleEngine` in priority order:

```java
RuleEngine ruleEngine = new RuleEngine();
ruleEngine.addRule(rule1);  // Inactive project (highest priority)
ruleEngine.addRule(rule2);  // KPI breach
// ... add remaining rules
```

### Step 5: Create Context for Data Storage
The `Context` class stores the current state of all conditions:

```java
public class Context {
    private Map<String, Boolean> conditions;
    
    public Context(boolean projectActive, boolean taskOverdue, 
                   boolean kpiBreach, boolean dependencyBlocked) {
        conditions = new HashMap<>();
        conditions.put("projectActive", projectActive);
        conditions.put("taskOverdue", taskOverdue);
        conditions.put("kpiBreach", kpiBreach);
        conditions.put("dependencyBlocked", dependencyBlocked);
    }
}
```

### Mapping Summary

| Decision Table Component | Interpreter Pattern Class | Purpose |
|-------------------------|---------------------------|---------|
| **Conditions** | `TerminalExpression` | Represent basic boolean conditions |
| **Logical Operators** | `AndExpression`, `OrExpression`, `NotExpression` | Compose complex conditions |
| **Rule Conditions** | `Expression` (composed) | Define when rule applies |
| **Rule Actions** | `List<String>` in `Rule` | Define what to do when rule matches |
| **Rule Rows** | `Rule` objects | Combine condition + actions |
| **Input Data** | `Context` | Store current condition values |
| **Rule Evaluation** | `RuleEngine` | Process rules in order |

This mapping transforms static decision table logic into dynamic, extensible business rules that can be modified at runtime without code changes.

---

## Key Features

### Pattern Components

| Component | Location | Description |
|-----------|----------|-------------|
| **AbstractExpression** | `src/Expression.java` | Common interface |
| **TerminalExpression** | `src/TerminalExpression.java` | Leaf nodes |
| **NonTerminalExpression** | `src/AndExpression.java`<br>`src/OrExpression.java`<br>`src/NotExpression.java` | Operators |
| **Context** | `src/Context.java` | State management |
| **Rule** | `src/Rule.java` | Business rules |
| **RuleEngine** | `src/RuleEngine.java` | Rule evaluation |
| **Client** | `src/InterpreterDashboard.java` | Main implementation |
| **API Server** | `src/RuleApiServer.java` | **NEW: HTTP API for live rule adding** |

---

## Example Usage

### Simple Rule
```java
Expression condition = new AndExpression(
    new TerminalExpression("projectActive"),
    new TerminalExpression("kpiBreach")
);

Rule rule = new Rule("KPI Breach", condition, "Escalate to Executive");
```

### Expression Tree Visualization
```
        AND
       /   \
projectActive  kpiBreach
```

---

## Testing

Run the main program to see 6 test cases:

```bash
java -cp build InterpreterDashboard
```

**Expected Output:**
- Active with KPI Breach → 3 actions
- Active with Task Overdue → 2 actions  
- Active with Dependency Blocked → 2 actions
- Active with Multiple Issues → 3 actions
- Active and Nominal → 1 action
- Project Not Active → 1 action

---

## Benefits

| Before (Hardcoded) | After (Interpreter Pattern) |
|-------------------|----------------------------|
| Requires recompilation | Dynamic rule changes |
| Difficult to maintain | Easy to extend |
| Logic mixed with code | Clear separation |
| Hard to test | Simple to test |

### Live Rule Adding Demo (NEW!)

The **RuleApiServer** demonstrates the ultimate power of the Interpreter Pattern:

✅ **Add rules while the application is running**
- No restart required
- No recompilation needed
- Changes take effect immediately

✅ **HTTP API for easy demonstration**
- Simple REST endpoints
- JSON-based rule definitions
- Real-time console feedback

✅ **Perfect for presentations**
- Interactive demo script included
- Shows dynamic rule modification
- Highlights pattern benefits

See **[API.md](API.md)** for complete documentation and examples.

---

## For Presentation

1. Navigate to project root
2. Run: `scripts\build-and-run-interpreter.bat`
3. Show output demonstrating dynamic rule evaluation

---

## Group Information

**Group**: H  
**Course**: CS 4213 - Software Design Patterns  
**Assignment**: M7S3-Participation 4: Implementing the Daily Operations Dashboard Decision Table Using the Interpreter Pattern \
**Semester**: Fall 2025

---

## Learn More

- See **`docs/IMPLEMENTATION.md`** for technical details
- See textbook Section 15.10 on Interpreter Pattern

---

**Status**: Complete and Ready for Presentation

**Last Updated**: October 19, 2025

