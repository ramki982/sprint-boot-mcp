package com.msra.mcp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.msra.mcp.core.McpServer;
import com.msra.mcp.config.McpProperties;
import com.msra.mcp.model.McpRequest;

@RestController
@RequestMapping("${mcp.server.path:/mcp}")
public class McpEndpointController {

    private static final Logger logger = LoggerFactory.getLogger(McpEndpointController.class);

    private final McpServer mcpServer;
    private final McpProperties properties;

    public McpEndpointController(McpServer mcpServer, McpProperties properties) {
        this.mcpServer = mcpServer;
        this.properties = properties;
        logger.debug("Initialized McpEndpointController with McpServer: {} and McpProperties: {}", mcpServer, properties);
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executeTool(@RequestBody McpRequest request) {
        logger.debug("Received request to execute tool with payload: {}", request);

        // Ensure McpEndpointController is not allowed to be executed
        if ("McpEndpointController".equals(request.getToolName())) {
            logger.warn("Attempt to execute McpEndpointController is not allowed");
            return ResponseEntity.badRequest().body("McpEndpointController cannot be executed");
        }

        ResponseEntity<?> response = ResponseEntity.ok(mcpServer.executeTool(request));
        logger.debug("Returning response: {}", response);
        return response;
    }

    @GetMapping("/tools")
    public ResponseEntity<?> listTools() {
        logger.debug("Received request to list available tools");

        // Filter out McpEndpointController from the list of tools
        ResponseEntity<?> response = ResponseEntity.ok(
            mcpServer.listTools().stream()
                .filter(tool -> !"McpEndpointController".equals(tool))
                .toList()
        );

        logger.debug("Returning tool list response: {}", response);
        return response;
    }
}
