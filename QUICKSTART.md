# Quick Start Guide for Live Rule Adding Demo

## 🎯 What This Demo Shows

This demonstrates the **Interpreter Pattern's** power to add business rules at runtime without restarting or recompiling the application.

## 🚀 Getting Started (2 Steps!)

### Step 1: Start the Server
```bash
cd src
javac *.java
java RuleApiServer
```

You'll see:
```
╔════════════════════════════════════════════════════════════╗
║  Rule Engine API Server - Live Rule Adding Demo           ║
╚════════════════════════════════════════════════════════════╝

Server started on http://localhost:8080
```

**Keep this terminal open!** You'll see live updates as rules are added.

### Step 2: Test the API

#### Option A: Run the Demo Script (Easiest)
```bash
# In a new terminal
cd scripts
./demo.sh
```

The script will walk you through an interactive demonstration.

#### Option B: Manual Testing (For Customization)
```bash
# List current rules
curl http://localhost:8080/api/rules

# Add a new rule
curl -X POST http://localhost:8080/api/rules/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Custom Rule",
    "condition": "projectActive AND kpiBreach",
    "actions": ["Alert Team", "Create Ticket"]
  }'

# Test with conditions
curl -X POST http://localhost:8080/api/rules/test \
  -H "Content-Type: application/json" \
  -d '{
    "projectActive": true,
    "taskOverdue": false,
    "kpiBreach": true,
    "dependencyBlocked": false
  }'
```

## 📝 Key Points to Highlight in Presentation

1. **No Restart Required**: Server stays running while you add rules
2. **No Recompilation**: Rules are added dynamically, not hardcoded
3. **Immediate Effect**: New rules work instantly after adding
4. **Console Feedback**: Watch the server console for live updates

## 🎤 Presentation Flow (2-3 minutes)

1. **Start server** - Show it's running with 6 default rules
2. **Test a scenario** - Show how existing rules work
3. **Add a new rule** - Watch the console update in real-time
4. **Test again** - Show the new rule is immediately active
5. **Summarize benefits** - Dynamic, flexible, maintainable

## 📚 For More Details

- **Full API Documentation**: `docs/API.md`
- **Implementation Details**: `docs/IMPLEMENTATION.md`
- **Project README**: `docs/README.md`

## 💡 Example Rules to Add During Demo

### Simple Rule
```json
{
  "name": "Active Project Check",
  "condition": "projectActive",
  "actions": ["Log Activity"]
}
```

### Emergency Rule
```json
{
  "name": "Critical Situation",
  "condition": "projectActive AND kpiBreach AND taskOverdue AND dependencyBlocked",
  "actions": ["Emergency Alert", "Notify Everyone", "Escalate Now"]
}
```

### Weekend Rule
```json
{
  "name": "Weekend Status",
  "condition": "projectActive AND NOT kpiBreach",
  "actions": ["All Good", "Enjoy Weekend"]
}
```

## 🔧 Troubleshooting

**Port 8080 already in use?**
```bash
# Kill any existing servers
pkill -f "java RuleApiServer"
```

**curl not found?**
- Windows: Use Git Bash or PowerShell with `Invoke-WebRequest`
- Mac/Linux: curl should be pre-installed
- Alternative: Open http://localhost:8080 in your browser

## ✅ Success Checklist

- [ ] Server starts without errors
- [ ] Can view rules at http://localhost:8080/api/rules
- [ ] Can add a rule successfully
- [ ] Server console shows the new rule
- [ ] Can test rules with different conditions
- [ ] Understand how this demonstrates the Interpreter Pattern

---

**Time Required**: 5 minutes to setup, 2-3 minutes to demo

**Difficulty**: Easy - just run two commands!

**Impact**: High - clearly shows the pattern's power
