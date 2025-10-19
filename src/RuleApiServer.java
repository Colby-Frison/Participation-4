/**
 * RuleApiServer.java
 * 
 * HTTP API server for demonstrating live rule adding capability.
 * This server exposes endpoints to add, view, and test rules in real-time,
 * showcasing the power of the Interpreter Pattern.
 */
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class RuleApiServer {
    
    private static InterpreterDashboard dashboard;
    private static final int PORT = 8080;
    
    public static void main(String[] args) throws IOException {
        dashboard = new InterpreterDashboard();
        
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Register endpoints
        server.createContext("/api/rules", new ListRulesHandler());
        server.createContext("/api/rules/add", new AddRuleHandler());
        server.createContext("/api/rules/test", new TestRuleHandler());
        server.createContext("/api/rules/clear", new ClearRulesHandler());
        server.createContext("/", new HomeHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Rule Engine API Server - Live Rule Adding Demo           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println();
        System.out.println("Available endpoints:");
        System.out.println("  GET  /                    - API documentation");
        System.out.println("  GET  /api/rules           - List all rules");
        System.out.println("  POST /api/rules/add       - Add a new rule");
        System.out.println("  POST /api/rules/test      - Test rules with conditions");
        System.out.println("  POST /api/rules/clear     - Clear all rules");
        System.out.println();
        System.out.println("Press Ctrl+C to stop the server");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println();
        
        printCurrentRules();
    }
    
    /**
     * Home endpoint - provides API documentation
     */
    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Rule Engine API</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; line-height: 1.6; }
                        h1 { color: #333; }
                        h2 { color: #666; margin-top: 30px; }
                        pre { background: #f4f4f4; padding: 15px; border-radius: 5px; overflow-x: auto; }
                        .endpoint { background: #e8f4f8; padding: 10px; margin: 10px 0; border-radius: 5px; }
                        code { background: #f4f4f4; padding: 2px 6px; border-radius: 3px; }
                    </style>
                </head>
                <body>
                    <h1>🚀 Rule Engine API - Live Rule Adding Demo</h1>
                    <p>This API demonstrates the power of the Interpreter Pattern by allowing rules to be added and modified at runtime.</p>
                    
                    <h2>Available Endpoints</h2>
                    
                    <div class="endpoint">
                        <strong>GET /api/rules</strong>
                        <p>List all current rules in the engine.</p>
                    </div>
                    
                    <div class="endpoint">
                        <strong>POST /api/rules/add</strong>
                        <p>Add a new rule to the engine.</p>
                        <p><strong>Body:</strong></p>
                        <pre>{
  "name": "Rule Name",
  "condition": "projectActive AND kpiBreach",
  "actions": ["Action 1", "Action 2"]
}</pre>
                    </div>
                    
                    <div class="endpoint">
                        <strong>POST /api/rules/test</strong>
                        <p>Test the rules with specific conditions.</p>
                        <p><strong>Body:</strong></p>
                        <pre>{
  "projectActive": true,
  "taskOverdue": false,
  "kpiBreach": true,
  "dependencyBlocked": false
}</pre>
                    </div>
                    
                    <div class="endpoint">
                        <strong>POST /api/rules/clear</strong>
                        <p>Clear all rules from the engine.</p>
                    </div>
                    
                    <h2>Quick Start Examples</h2>
                    
                    <h3>View Current Rules</h3>
                    <pre>curl http://localhost:8080/api/rules</pre>
                    
                    <h3>Add a Simple Rule</h3>
                    <pre>curl -X POST http://localhost:8080/api/rules/add \\
  -H "Content-Type: application/json" \\
  -d '{"name":"Test Rule","condition":"projectActive","actions":["Log Test"]}'</pre>
                    
                    <h3>Test Rules</h3>
                    <pre>curl -X POST http://localhost:8080/api/rules/test \\
  -H "Content-Type: application/json" \\
  -d '{"projectActive":true,"taskOverdue":false,"kpiBreach":true,"dependencyBlocked":false}'</pre>
                    
                    <p><a href="/api/rules">View Current Rules →</a></p>
                </body>
                </html>
                """;
            
            sendResponse(exchange, 200, response, "text/html");
        }
    }
    
    /**
     * List all rules endpoint
     */
    static class ListRulesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
                return;
            }
            
            List<Rule> rules = dashboard.getRuleEngine().getRules();
            StringBuilder json = new StringBuilder();
            json.append("{\n  \"count\": ").append(rules.size()).append(",\n");
            json.append("  \"rules\": [\n");
            
            for (int i = 0; i < rules.size(); i++) {
                Rule rule = rules.get(i);
                json.append("    {\n");
                json.append("      \"index\": ").append(i).append(",\n");
                json.append("      \"name\": \"").append(escapeJson(rule.getName())).append("\",\n");
                json.append("      \"condition\": \"").append(escapeJson(rule.getCondition().toString())).append("\"\n");
                json.append("    }");
                if (i < rules.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  ]\n}");
            
            sendResponse(exchange, 200, json.toString(), "application/json");
        }
    }
    
    /**
     * Add rule endpoint
     */
    static class AddRuleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
                return;
            }
            
            String body = readRequestBody(exchange);
            
            try {
                // Parse JSON manually (avoiding external dependencies)
                String name = extractJsonString(body, "name");
                String condition = extractJsonString(body, "condition");
                String[] actions = extractJsonArray(body, "actions");
                
                if (name == null || condition == null || actions == null) {
                    sendResponse(exchange, 400, 
                        "{\"error\":\"Missing required fields: name, condition, actions\"}", 
                        "application/json");
                    return;
                }
                
                // Parse the condition expression
                Expression expr = parseExpression(condition);
                
                // Create and add the rule
                Rule rule = new Rule(name, expr, actions);
                dashboard.getRuleEngine().addRule(rule);
                
                System.out.println("\n[RULE ADDED]");
                System.out.println("  Name: " + name);
                System.out.println("  Condition: " + condition);
                System.out.println("  Actions: " + String.join(", ", actions));
                System.out.println();
                
                printCurrentRules();
                
                String response = String.format(
                    "{\"success\":true,\"message\":\"Rule '%s' added successfully\",\"totalRules\":%d}",
                    escapeJson(name),
                    dashboard.getRuleEngine().getRules().size()
                );
                
                sendResponse(exchange, 201, response, "application/json");
                
            } catch (Exception e) {
                String errorMsg = "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}";
                sendResponse(exchange, 400, errorMsg, "application/json");
            }
        }
    }
    
    /**
     * Test rules endpoint
     */
    static class TestRuleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
                return;
            }
            
            String body = readRequestBody(exchange);
            
            try {
                boolean projectActive = extractJsonBoolean(body, "projectActive");
                boolean taskOverdue = extractJsonBoolean(body, "taskOverdue");
                boolean kpiBreach = extractJsonBoolean(body, "kpiBreach");
                boolean dependencyBlocked = extractJsonBoolean(body, "dependencyBlocked");
                
                List<String> actions = dashboard.processProjectDecision(
                    projectActive, taskOverdue, kpiBreach, dependencyBlocked
                );
                
                System.out.println("\n[RULE EVALUATION]");
                System.out.println("  Conditions:");
                System.out.println("    projectActive: " + projectActive);
                System.out.println("    taskOverdue: " + taskOverdue);
                System.out.println("    kpiBreach: " + kpiBreach);
                System.out.println("    dependencyBlocked: " + dependencyBlocked);
                System.out.println("  Result: " + actions);
                System.out.println();
                
                StringBuilder json = new StringBuilder();
                json.append("{\n");
                json.append("  \"conditions\": {\n");
                json.append("    \"projectActive\": ").append(projectActive).append(",\n");
                json.append("    \"taskOverdue\": ").append(taskOverdue).append(",\n");
                json.append("    \"kpiBreach\": ").append(kpiBreach).append(",\n");
                json.append("    \"dependencyBlocked\": ").append(dependencyBlocked).append("\n");
                json.append("  },\n");
                json.append("  \"actions\": [\n");
                for (int i = 0; i < actions.size(); i++) {
                    json.append("    \"").append(escapeJson(actions.get(i))).append("\"");
                    if (i < actions.size() - 1) json.append(",");
                    json.append("\n");
                }
                json.append("  ]\n");
                json.append("}");
                
                sendResponse(exchange, 200, json.toString(), "application/json");
                
            } catch (Exception e) {
                String errorMsg = "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}";
                sendResponse(exchange, 400, errorMsg, "application/json");
            }
        }
    }
    
    /**
     * Clear rules endpoint
     */
    static class ClearRulesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}", "application/json");
                return;
            }
            
            dashboard.getRuleEngine().clearRules();
            
            System.out.println("\n[RULES CLEARED]");
            System.out.println("  All rules have been removed from the engine.");
            System.out.println();
            
            sendResponse(exchange, 200, 
                "{\"success\":true,\"message\":\"All rules cleared\"}", 
                "application/json");
        }
    }
    
    // Helper methods
    
    private static void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
    
    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
    
    private static String extractJsonString(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
    
    private static String[] extractJsonArray(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\\[([^\\]]+)\\]";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            String arrayContent = m.group(1);
            String[] parts = arrayContent.split(",");
            String[] result = new String[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = parts[i].trim().replaceAll("^\"|\"$", "");
            }
            return result;
        }
        return null;
    }
    
    private static boolean extractJsonBoolean(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*(true|false)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Boolean.parseBoolean(m.group(1));
        }
        return false;
    }
    
    private static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    private static Expression parseExpression(String condition) {
        condition = condition.trim();
        
        // Handle outer parentheses
        if (condition.startsWith("(") && condition.endsWith(")") && matchingParens(condition)) {
            return parseExpression(condition.substring(1, condition.length() - 1));
        }
        
        // Handle NOT expression
        if (condition.toUpperCase().startsWith("NOT ")) {
            String inner = condition.substring(4).trim();
            return new NotExpression(parseExpression(inner));
        }
        
        // Try to split by AND or OR (at top level, not inside parentheses)
        String[] andParts = trySplitByOperator(condition, " AND ");
        if (andParts != null) {
            Expression left = parseExpression(andParts[0]);
            Expression right = parseExpression(andParts[1]);
            return new AndExpression(left, right);
        }
        
        String[] orParts = trySplitByOperator(condition, " OR ");
        if (orParts != null) {
            Expression left = parseExpression(orParts[0]);
            Expression right = parseExpression(orParts[1]);
            return new OrExpression(left, right);
        }
        
        // Terminal expression
        return new TerminalExpression(condition);
    }
    
    private static boolean matchingParens(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') count++;
            else if (str.charAt(i) == ')') count--;
            if (count == 0 && i < str.length() - 1) return false;
        }
        return count == 0;
    }
    
    private static String[] trySplitByOperator(String condition, String operator) {
        int level = 0;
        String upperCondition = condition.toUpperCase();
        String upperOperator = operator.toUpperCase();
        
        for (int i = 0; i < condition.length(); i++) {
            char c = condition.charAt(i);
            if (c == '(') level++;
            else if (c == ')') level--;
            else if (level == 0 && upperCondition.substring(i).startsWith(upperOperator)) {
                return new String[] {
                    condition.substring(0, i).trim(),
                    condition.substring(i + operator.length()).trim()
                };
            }
        }
        
        return null;
    }
    
    private static void printCurrentRules() {
        List<Rule> rules = dashboard.getRuleEngine().getRules();
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Current Rules in Engine: " + rules.size());
        System.out.println("─────────────────────────────────────────────────────────────");
        for (int i = 0; i < rules.size(); i++) {
            System.out.println((i + 1) + ". " + rules.get(i).getName());
        }
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println();
    }
}
