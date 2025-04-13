package com.msra.mcp.model;

import java.util.Map;

/**
 * Represents a request to execute an MCP Tool.
 */
public class McpRequest {

    private String toolName;
    private Map<String, Object> parameters; // Parameter name and value

    public McpRequest(String toolName, Map<String, Object> parameters) {
        this.toolName = toolName;
        this.parameters = parameters;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}