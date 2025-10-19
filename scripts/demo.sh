#!/bin/bash
# demo.sh - Interactive demonstration script for live rule adding

echo "════════════════════════════════════════════════════════════"
echo "  Rule Engine API - Live Demonstration Script"
echo "════════════════════════════════════════════════════════════"
echo ""
echo "This script will demonstrate the power of the Interpreter Pattern"
echo "by adding rules to a running server without restart or recompilation."
echo ""
echo "Make sure the API server is running in another terminal:"
echo "  cd src && java RuleApiServer"
echo ""
read -p "Press Enter to continue once the server is running..."

BASE_URL="http://localhost:8081"

# Check if server is running
if ! curl -s "$BASE_URL/api/rules" > /dev/null 2>&1; then
    echo "❌ Error: Server is not responding at $BASE_URL"
    echo "Please start the server first: cd src && java RuleApiServer"
    exit 1
fi

echo ""
echo " Server is running!"
echo ""

# Step 1: Show initial rules
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 1: View Initial Rules"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Command: curl $BASE_URL/api/rules"
echo ""
curl -s "$BASE_URL/api/rules" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL/api/rules"
echo ""
read -p "Press Enter to continue..."

# Step 2: Test a scenario
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 2: Test a KPI Breach Scenario"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Conditions: projectActive=true, taskOverdue=false, kpiBreach=true, dependencyBlocked=false"
echo ""
curl -s -X POST "$BASE_URL/api/rules/test" \
  -H "Content-Type: application/json" \
  -d '{"projectActive":true,"taskOverdue":false,"kpiBreach":true,"dependencyBlocked":false}' | \
  python3 -m json.tool 2>/dev/null || \
  curl -s -X POST "$BASE_URL/api/rules/test" \
    -H "Content-Type: application/json" \
    -d '{"projectActive":true,"taskOverdue":false,"kpiBreach":true,"dependencyBlocked":false}'
echo ""
read -p "Press Enter to continue..."

# Step 3: Add a new rule
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 3: Add a New Rule (While Server is Running!)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Adding 'All Systems Critical' rule..."
echo "Condition: projectActive AND taskOverdue AND kpiBreach AND dependencyBlocked"
echo "Actions: RED ALERT, Notify Everyone, Emergency Shutdown Protocols"
echo ""
curl -s -X POST "$BASE_URL/api/rules/add" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "All Systems Critical",
    "condition": "projectActive AND taskOverdue AND kpiBreach AND dependencyBlocked",
    "actions": [
      "RED ALERT",
      "Notify Everyone",
      "Emergency Shutdown Protocols"
    ]
  }' | python3 -m json.tool 2>/dev/null || \
  curl -s -X POST "$BASE_URL/api/rules/add" \
    -H "Content-Type: application/json" \
    -d '{
      "name": "All Systems Critical",
      "condition": "projectActive AND taskOverdue AND kpiBreach AND dependencyBlocked",
      "actions": [
        "RED ALERT",
        "Notify Everyone",
        "Emergency Shutdown Protocols"
      ]
    }'
echo ""
echo ""
echo "⚡ Rule added! Check the server console to see it appear in real-time!"
echo ""
read -p "Press Enter to continue..."

# Step 4: Verify the rule was added
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 4: Verify the New Rule"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Listing all rules (should now show 7 rules)..."
echo ""
curl -s "$BASE_URL/api/rules" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL/api/rules"
echo ""
read -p "Press Enter to continue..."

# Step 5: Test with the critical scenario
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 5: Test the Critical Scenario"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Testing ALL conditions true (worst case scenario)..."
echo ""
curl -s -X POST "$BASE_URL/api/rules/test" \
  -H "Content-Type: application/json" \
  -d '{"projectActive":true,"taskOverdue":true,"kpiBreach":true,"dependencyBlocked":true}' | \
  python3 -m json.tool 2>/dev/null || \
  curl -s -X POST "$BASE_URL/api/rules/test" \
    -H "Content-Type: application/json" \
    -d '{"projectActive":true,"taskOverdue":true,"kpiBreach":true,"dependencyBlocked":true}'
echo ""
echo ""
echo "Note: Due to first-match evaluation, KPI Breach rule matched first."
echo "To make 'All Systems Critical' match, it should be added earlier in the rule list."
echo ""
read -p "Press Enter to continue..."

# Step 6: Add another rule with different conditions
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 6: Add Another Custom Rule"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Adding 'Weekend Check' rule..."
echo "Condition: projectActive AND NOT kpiBreach AND NOT taskOverdue"
echo ""
curl -s -X POST "$BASE_URL/api/rules/add" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Weekend Check",
    "condition": "projectActive AND NOT kpiBreach AND NOT taskOverdue",
    "actions": [
      "Everything looks good!",
      "Enjoy your weekend"
    ]
  }' | python3 -m json.tool 2>/dev/null || \
  curl -s -X POST "$BASE_URL/api/rules/add" \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Weekend Check",
      "condition": "projectActive AND NOT kpiBreach AND NOT taskOverdue",
      "actions": [
        "Everything looks good!",
        "Enjoy your weekend"
      ]
    }'
echo ""
read -p "Press Enter to continue..."

# Step 7: Final rules list
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 7: Final Rules List"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "Now we have 8 rules total!"
echo ""
curl -s "$BASE_URL/api/rules" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL/api/rules"
echo ""

echo ""
echo "════════════════════════════════════════════════════════════"
echo "  Demonstration Complete! "
echo "════════════════════════════════════════════════════════════"
echo ""
echo "Key Takeaways:"
echo " Rules were added WITHOUT restarting the server"
echo " Rules were added WITHOUT recompiling any code"
echo " Changes are visible immediately in the console"
echo " New rules can be tested right away"
echo ""
echo "This demonstrates the power of the Interpreter Pattern:"
echo "  • Dynamic rule modification at runtime"
echo "  • Business logic separated from code"
echo "  • Easy extensibility and maintenance"
echo ""
echo "For more examples, see: docs/API.md"
echo "════════════════════════════════════════════════════════════"
