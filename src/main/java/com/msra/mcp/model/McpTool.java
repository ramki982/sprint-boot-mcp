package com.msra.mcp.model;

import java.util.Map;

/**
 * Represents an MCP Tool, which corresponds to a REST endpoint in the application.
 */
public class McpTool {

    private String name;
    private String description;
    private Map<String, String> parameters; // Parameter name and type
    private String responseType;
    private String beanName;
    private String methodName;

    public McpTool(String name, String description, Map<String, String> parameters, String responseType) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
        this.responseType = responseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}