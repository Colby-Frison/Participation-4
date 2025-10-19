# Rule Engine API Documentation

## 🚀 Quick Start Guide

This HTTP API demonstrates the power of the **Interpreter Pattern** by allowing you to add, modify, and test business rules in real-time without restarting the application.

### Starting the Server

```bash
# Navigate to the src directory
cd src

# Compile the API server (if not already compiled)
javac RuleApiServer.java

# Start the server
java RuleApiServer
```

The server will start on **http://localhost:8081**

You should see output like:
```
╔════════════════════════════════════════════════════════════╗
║  Rule Engine API Server - Live Rule Adding Demo           ║
╚════════════════════════════════════════════════════════════╝

Server started on http://localhost:8081

Available endpoints:
  GET  /                    - API documentation
  GET  /api/rules           - List all rules
  POST /api/rules/add       - Add a new rule
  POST /api/rules/test      - Test rules with conditions
  POST /api/rules/clear     - Clear all rules
```

---

## 📋 API Endpoints

### 1. View API Documentation (Browser)

**Endpoint:** `GET /`

Open in your browser: **http://localhost:8081/**

This provides a nice HTML interface with all API documentation.

---

### 2. List All Rules

**Endpoint:** `GET /api/rules`

Lists all currently loaded rules in the engine.

**Example Request:**
```bash
curl http://localhost:8081/api/rules
```

**Example Response:**
```json
{
  "count": 6,
  "rules": [
    {
      "index": 0,
      "name": "Inactive Project",
      "condition": "(NOT projectActive)"
    },
    {
      "index": 1,
      "name": "KPI Breach - Critical",
      "condition": "(projectActive AND kpiBreach)"
    },
    ...
  ]
}
```

---

### 3. Add a New Rule (Live!)

**Endpoint:** `POST /api/rules/add`

Add a new rule to the engine **while it's running**. This demonstrates the key benefit of the Interpreter Pattern!

**Request Body:**
```json
{
  "name": "Rule Name",
  "condition": "condition expression",
  "actions": ["Action 1", "Action 2", "Action 3"]
}
```

**Condition Expression Syntax:**
- Use `AND`, `OR`, `NOT` operators
- Available conditions: `projectActive`, `taskOverdue`, `kpiBreach`, `dependencyBlocked`
- Use parentheses for complex expressions (optional, handled automatically)

**Examples:**

#### Simple Rule
```bash
curl -X POST http://localhost:8081/api/rules/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Simple Active Check",
    "condition": "projectActive",
    "actions": ["Log Project Active"]
  }'
```

#### Rule with AND
```bash
curl -X POST http://localhost:8081/api/rules/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Emergency Situation",
    "condition": "projectActive AND kpiBreach AND taskOverdue",
    "actions": [
      "Emergency Alert",
      "Notify CEO",
      "Escalate Immediately"
    ]
  }'
```

#### Rule with NOT
```bash
curl -X POST http://localhost:8081/api/rules/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Healthy Project",
    "condition": "projectActive AND NOT kpiBreach AND NOT taskOverdue",
    "actions": ["Log Success"]
  }'
```

#### Complex Rule with Multiple Operators
```bash
curl -X POST http://localhost:8081/api/rules/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Critical Issues",
    "condition": "projectActive AND (kpiBreach OR (taskOverdue AND dependencyBlocked))",
    "actions": [
      "Flag as Critical",
      "Notify Management",
      "Create Incident Report"
    ]
  }'
```

**Success Response:**
```json
{
  "success": true,
  "message": "Rule 'Emergency Situation' added successfully",
  "totalRules": 7
}
```

**Console Output:**
After adding a rule, you'll see in the server console:
```
[RULE ADDED]
  Name: Emergency Situation
  Condition: projectActive AND kpiBreach AND taskOverdue
  Actions: Emergency Alert, Notify CEO, Escalate Immediately

─────────────────────────────────────────────────────────────
Current Rules in Engine: 7
─────────────────────────────────────────────────────────────
1. Inactive Project
2. KPI Breach - Critical
...
7. Emergency Situation
─────────────────────────────────────────────────────────────
```

---

### 4. Test Rules with Conditions

**Endpoint:** `POST /api/rules/test`

Test how the rules evaluate with specific conditions. The engine will return actions from the first matching rule.

**Request Body:**
```json
{
  "projectActive": true,
  "taskOverdue": false,
  "kpiBreach": true,
  "dependencyBlocked": false
}
```

**Example Request:**
```bash
curl -X POST http://localhost:8081/api/rules/test \
  -H "Content-Type: application/json" \
  -d '{
    "projectActive": true,
    "taskOverdue": false,
    "kpiBreach": true,
    "dependencyBlocked": false
  }'
```

**Example Response:**
```json
{
  "conditions": {
    "projectActive": true,
    "taskOverdue": false,
    "kpiBreach": true,
    "dependencyBlocked": false
  },
  "actions": [
    "Flag Project as AtRisk",
    "Notify Manager",
    "Escalate to Executive"
  ]
}
```

**Console Output:**
```
[RULE EVALUATION]
  Conditions:
    projectActive: true
    taskOverdue: false
    kpiBreach: true
    dependencyBlocked: false
  Result: [Flag Project as AtRisk, Notify Manager, Escalate to Executive]
```

---

### 5. Clear All Rules

**Endpoint:** `POST /api/rules/clear`

Removes all rules from the engine. Use with caution!

**Example Request:**
```bash
curl -X POST http://localhost:8081/api/rules/clear
```

**Response:**
```json
{
  "success": true,
  "message": "All rules cleared"
}
```

**Console Output:**
```
[RULES CLEARED]
  All rules have been removed from the engine.
```

---

## 🎯 Live Demonstration Walkthrough

Here's a step-by-step guide for demonstrating the live rule adding feature:

### Step 1: Start the Server
```bash
cd src
java RuleApiServer
```

### Step 2: View Initial Rules
```bash
curl http://localhost:8081/api/rules
```
You'll see 6 default rules loaded.

### Step 3: Test a Scenario
```bash
curl -X POST http://localhost:8081/api/rules/test \
  -H "Content-Type: application/json" \
  -d '{
    "projectActive": true,
    "taskOverdue": true,
    "kpiBreach": false,
    "dependencyBlocked": false
  }'
```

Expected result: Actions for "Task Overdue" rule.

### Step 4: Add a New Rule (While Server is Running!)
```bash
curl -X POST http://localhost:8081/api/rules/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Weekend Alert",
    "condition": "projectActive AND taskOverdue",
    "actions": [
      "Send Weekend Notification",
      "Update Dashboard"
    ]
  }'
```

**Watch the console!** You'll see the rule being added in real-time.

### Step 5: Verify the New Rule
```bash
curl http://localhost:8081/api/rules
```
Now you'll see 7 rules, including your new "Weekend Alert" rule.

### Step 6: Test with the New Rule
The rules are evaluated in order, so depending on where your rule was added, it may or may not be the first match. To ensure your rule matches first, you might want to clear and add rules in a specific order, or add more specific conditions.

---

## 💡 Key Benefits Demonstrated

### 1. **No Recompilation Required**
- Add rules without stopping the server
- No need to modify Java code
- No need to rebuild the application

### 2. **Immediate Effect**
- New rules take effect instantly
- See changes in real-time on the console
- Test immediately after adding

### 3. **Flexible Rule Definition**
- Use simple or complex condition expressions
- Combine multiple operators (AND, OR, NOT)
- Multiple actions per rule

### 4. **Business-Friendly**
- Rules expressed in readable format
- No programming knowledge needed to add rules
- Can be loaded from configuration files, databases, or APIs

---

## 🔧 Advanced Usage

### Using with Tools

#### Postman
1. Import requests to Postman
2. Create a collection for each endpoint
3. Use variables for the base URL

#### HTTPie (more user-friendly than curl)
```bash
# Install httpie
pip install httpie

# List rules
http :8081/api/rules

# Add rule
http POST :8081/api/rules/add \
  name="Test Rule" \
  condition="projectActive" \
  actions:='["Action 1", "Action 2"]'

# Test rules
http POST :8081/api/rules/test \
  projectActive:=true \
  taskOverdue:=false \
  kpiBreach:=false \
  dependencyBlocked:=false
```

### Creating Rules from Files

You can prepare rule definitions in JSON files:

**rule.json:**
```json
{
  "name": "Custom Rule",
  "condition": "projectActive AND kpiBreach",
  "actions": ["Custom Action 1", "Custom Action 2"]
}
```

Then load it:
```bash
curl -X POST http://localhost:8081/api/rules/add \
  -H "Content-Type: application/json" \
  -d @rule.json
```

---

## 🐛 Troubleshooting

### Port Already in Use
If port 8081 is already in use, edit `RuleApiServer.java` and change:
```java
private static final int PORT = 8081;
```
to another port like 8082, then recompile.

### Connection Refused
Make sure the server is running:
```bash
java RuleApiServer
```

### Invalid Condition Syntax
Make sure to use proper operators:
- Use `AND`, `OR`, `NOT` (case-insensitive)
- Use valid condition names: `projectActive`, `taskOverdue`, `kpiBreach`, `dependencyBlocked`

---

## 📝 Example Test Scenarios

### Scenario 1: Project in Crisis
```bash
curl -X POST http://localhost:8081/api/rules/test \
  -H "Content-Type: application/json" \
  -d '{
    "projectActive": true,
    "taskOverdue": true,
    "kpiBreach": true,
    "dependencyBlocked": true
  }'
```

### Scenario 2: Healthy Project
```bash
curl -X POST http://localhost:8081/api/rules/test \
  -H "Content-Type: application/json" \
  -d '{
    "projectActive": true,
    "taskOverdue": false,
    "kpiBreach": false,
    "dependencyBlocked": false
  }'
```

### Scenario 3: Inactive Project
```bash
curl -X POST http://localhost:8081/api/rules/test \
  -H "Content-Type: application/json" \
  -d '{
    "projectActive": false,
    "taskOverdue": true,
    "kpiBreach": false,
    "dependencyBlocked": false
  }'
```

---

## 🎓 Understanding the Interpreter Pattern

This API demonstrates how the **Interpreter Pattern** enables:

1. **Runtime Flexibility**: Rules can be added/modified without code changes
2. **Separation of Concerns**: Business logic (rules) separated from implementation
3. **Extensibility**: Easy to add new operators or conditions
4. **Maintainability**: Rules are data, not hardcoded logic

### Expression Tree Example

The condition `projectActive AND (kpiBreach OR taskOverdue)` creates this tree:

```
           AND
          /   \
    projectActive  OR
                  /  \
           kpiBreach  taskOverdue
```

Each node is evaluated recursively to produce the final result.

---

## 📞 Support

For questions or issues, refer to the main [IMPLEMENTATION.md](IMPLEMENTATION.md) documentation.

---

**Happy rule creating! 🎉**
