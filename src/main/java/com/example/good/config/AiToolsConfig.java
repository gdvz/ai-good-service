package com.example.good.config;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiToolsConfig {

    @Tool(description = "Get the current stock level and location for a given product ID")
    public String checkProductStockById(@ToolParam(description= "Product Id") String productId) {
        return "Product ID "+productId+" Stock remaining 5 each";
    }

    @Tool(description = "Get the weather for a given location")
    public String weatherByLocation(@ToolParam(description= "City or state name") String location) {
        return "Weather in "+location+" is good. It's summer.";
    }
}
