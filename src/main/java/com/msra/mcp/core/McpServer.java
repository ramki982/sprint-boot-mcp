package com.msra.mcp.core;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;
import com.msra.mcp.service.McpIntrospectionService;
import com.msra.mcp.config.McpProperties;
import com.msra.mcp.model.McpTool;
import com.msra.mcp.model.McpRequest;

public class McpServer {

    private final McpIntrospectionService introspectionService;
    private final McpProperties properties;
    private final ApplicationContext applicationContext;

    public McpServer(McpIntrospectionService introspectionService, McpProperties properties, ApplicationContext applicationContext) {
        this.introspectionService = introspectionService;
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    public List<McpTool> getTools() {
        // Return the list of tools discovered by the introspection service.
        return introspectionService.discoverTools(applicationContext);
    }

    public List<McpTool> listTools() {
        return introspectionService.discoverTools(applicationContext);
    }

    public Object executeTool(McpRequest request) {
        // Find the tool by name
        Optional<McpTool> toolOptional = introspectionService.discoverTools(applicationContext).stream()
            .filter(tool -> tool.getName().equals(request.getToolName()))
            .findFirst();

        if (toolOptional.isEmpty()) {
            throw new IllegalArgumentException("Tool not found: " + request.getToolName());
        }

        McpTool tool = toolOptional.get();

        try {
            // Use reflection to invoke the tool's method
            Object bean = applicationContext.getBean(tool.getBeanName());
            Method method = bean.getClass().getMethod(tool.getMethodName(), Map.class);
            return method.invoke(bean, request.getParameters());
        } catch (Exception e) {
            throw new RuntimeException("Error executing tool: " + request.getToolName(), e);
        }
    }
}
