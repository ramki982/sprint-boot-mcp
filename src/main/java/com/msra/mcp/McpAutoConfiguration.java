package com.msra.mcp;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.msra.mcp.core.McpServer;
import com.msra.mcp.config.McpProperties;
import com.msra.mcp.controller.McpEndpointController;
import com.msra.mcp.service.McpIntrospectionService;

@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(McpServer.class) // Assuming McpServer is part of the MCP SDK
@EnableConfigurationProperties(McpProperties.class)
public class McpAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(McpAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public McpEndpointController mcpEndpointController(McpServer mcpServer, McpProperties properties) {
        logger.debug("Creating McpEndpointController bean with McpServer: {} and McpProperties: {}", mcpServer, properties);
        return new McpEndpointController(mcpServer, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public McpIntrospectionService mcpIntrospectionService() {
        logger.debug("Creating McpIntrospectionService bean");
        return new McpIntrospectionService();
    }

    @Bean
    @ConditionalOnMissingBean
    public McpServer mcpServer(McpIntrospectionService introspectionService, McpProperties properties, ApplicationContext applicationContext) {
        logger.debug("Creating McpServer bean with McpIntrospectionService: {}, McpProperties: {}, and ApplicationContext: {}", introspectionService, properties, applicationContext);
        return new McpServer(introspectionService, properties, applicationContext);
    }
}
