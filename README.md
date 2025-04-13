# Spring Boot MCP Starter

A Spring Boot starter to make your application compliant with the Model Context Protocol (MCP).

## Features

- Automatically expose Spring REST controllers as MCP tools.
- Configurable via `application.properties` or `application.yml`.
- Supports tool discovery and execution.

## Installation

Add the dependency to your Spring Boot project:

```xml
<dependency>
    <groupId>com.msra.mcp</groupId>
    <artifactId>spring-boot-mcp-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration

Add the following properties to your `application.properties`:

```properties
mcp.server.enabled=true
mcp.server.path=/mcp
mcp.server.name=My MCP Server
```

## Usage

Once added, the starter will automatically expose your `@RestController` endpoints as MCP tools. You can access the MCP server at `/mcp`.

### Available Endpoints

- `/mcp/tools`: Lists all available tools.
- `/mcp/execute`: Executes a specific tool based on the provided request.

## Building and Using Locally

If you want to use this starter in another project (e.g., `spring-boot-mcp-demo`), follow these steps:

### 1. Build the Project

Run the following command to build the project and publish it to your local Maven repository:

```bash
./gradlew clean build publishToMavenLocal
```

This will compile the code, run tests, and publish the artifact to your local Maven repository (usually located at `~/.m2/repository`).

### 2. Add Dependency to Your Project

In your `spring-boot-mcp-demo` project, add the following dependency to your `build.gradle`:

```gradle
dependencies {
    implementation 'com.msra.mcp:spring-boot-mcp-starter:1.0.0'
}
```

If you are using Maven, add this to your `pom.xml`:

```xml
<dependency>
    <groupId>com.msra.mcp</groupId>
    <artifactId>spring-boot-mcp-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 3. Verify Integration

Ensure that the `spring-boot-mcp-starter` is correctly integrated by running your `spring-boot-mcp-demo` application and testing the MCP server functionality.

## Testing

To test the Spring Boot MCP Starter, follow these steps:

### 1. Create a Test Spring Boot Application

Create a new Spring Boot application and add the MCP starter dependency to your `build.gradle` or `pom.xml`.

#### Example `build.gradle`:

```gradle
dependencies {
    implementation 'com.msra.mcp:spring-boot-mcp-starter:1.0.0'
}
```

#### Example `pom.xml`:

```xml
<dependency>
    <groupId>com.msra.mcp</groupId>
    <artifactId>spring-boot-mcp-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Define REST Endpoints

Add some `@RestController` endpoints to your test application. For example:

```java
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, MCP!";
    }

    @PostMapping("/echo")
    public String echo(@RequestBody String message) {
        return message;
    }
}
```

### 3. Run the Application

Start your Spring Boot application. The MCP server will automatically expose the endpoints as MCP tools.

### 4. Test the MCP Server

- Access the MCP server at the configured path (e.g., `/mcp`).
- Use an MCP client (e.g., Cursor or Claude Desktop) to connect to the MCP server.
- Verify that the tools corresponding to your REST endpoints are discovered and can be executed.

### 5. Unit Tests

You can also write unit tests for your application using Spring Boot's testing framework. For example:

```java
@SpringBootTest
@AutoConfigureMockMvc
class McpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMcpEndpoint() throws Exception {
        mockMvc.perform(get("/mcp"))
               .andExpect(status().isOk());
    }
}
```

### 6. Debugging

If the MCP server is not working as expected:
- Check the logs for errors or warnings.
- Verify that the `mcp.server.enabled` property is set to `true`.
- Ensure your REST endpoints are properly annotated with `@RestController` and mapping annotations like `@GetMapping`.

## License

MIT License.
