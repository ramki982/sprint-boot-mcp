package com.msra.mcp.service;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Optional;
import java.util.Arrays;
import com.msra.mcp.model.McpTool;
import com.msra.mcp.controller.McpEndpointController;

public class McpIntrospectionService {

    private static final Logger log = LoggerFactory.getLogger(McpIntrospectionService.class);

    public List<McpTool> discoverTools(ApplicationContext context) {
        List<McpTool> tools = new ArrayList<>();
        log.debug("Discovering tools in application context...");
        context.getBeansWithAnnotation(RestController.class).forEach((name, bean) -> {
            Class<?> beanClass = bean.getClass();

            // Debug: Log the bean being processed
            log.debug("Processing bean: {} of type: {}", name, beanClass.getName());

            // Skip McpEndpointController
            if (beanClass.equals(McpEndpointController.class)) {
                log.debug("Skipping McpEndpointController");
                return;
            }

            for (Method method : beanClass.getDeclaredMethods()) {
                // Debug: Log the method being checked
                log.debug("Checking method: {}", method.getName());

                if (method.isAnnotationPresent(GetMapping.class) ||
                    method.isAnnotationPresent(PostMapping.class) ||
                    method.isAnnotationPresent(PutMapping.class) ||
                    method.isAnnotationPresent(DeleteMapping.class) ||
                    method.isAnnotationPresent(RequestMapping.class)) {

                    log.debug("Discovered tool for method: {}", method.getName());

                    // Extract operation ID (custom tool name)
                    String operationId = method.getName(); // Default to method name
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        if (requestMapping.name() != null && !requestMapping.name().isEmpty()) {
                            operationId = requestMapping.name();
                        }
                    }
                    log.debug("Operation ID for method {}: {}", method.getName(), operationId);

                    // Extract parameters
                    Map<String, String> parameters = new HashMap<>();
                    for (Parameter parameter : method.getParameters()) {
                        if (parameter.isAnnotationPresent(RequestParam.class)) {
                            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                            parameters.put(requestParam.value(), parameter.getType().getSimpleName());
                            log.debug("Discovered query parameter: {} of type: {}", requestParam.value(), parameter.getType().getSimpleName());
                        } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                            String key = pathVariable.value().isEmpty() ? parameter.getName() : pathVariable.value();
                            parameters.put(key, parameter.getType().getSimpleName());
                            log.debug("Discovered path parameter: {} of type: {}", key, parameter.getType().getSimpleName());
                        } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                            // Handle @RequestBody
                            Class<?> requestBodyClass = parameter.getType();
                            Map<String, String> requestBodyFields = new HashMap<>();

                            for (Field field : requestBodyClass.getDeclaredFields()) {
                                requestBodyFields.put(field.getName(), field.getType().getSimpleName());
                                log.debug("Discovered request body field: {} of type: {}", field.getName(), field.getType().getSimpleName());
                            }

                            parameters.put("requestBody", requestBodyFields.toString());
                        }
                    }

                    // Extract response schema
                    String responseType = "application/json"; // Default response type
                    if (method.isAnnotationPresent(ResponseBody.class)) {
                        responseType = Optional.ofNullable(method.getReturnType())
                            .map(Class::getSimpleName)
                            .orElse("application/json");
                        log.debug("Response type for method {}: {}", method.getName(), responseType);
                    }

                    McpTool tool = new McpTool(
                        operationId, // Use operation ID as the tool name
                        "Automatically discovered tool for method: " + method.getName(),
                        parameters, // Extracted parameters
                        responseType // Extracted response type
                    );

                    // Set beanName and methodName
                    tool.setBeanName(name);
                    tool.setMethodName(method.getName());

                    tools.add(tool);
                    log.debug("Tool added: {}", tool);
                }
            }
        });
        log.debug("Tool discovery completed. Total tools discovered: {}", tools.size());
        return tools;
    }
}
