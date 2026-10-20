package edu.assignment04;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class App {
    private App() { }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("APP_PORT", "9090"));
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/health", exchange -> respond(exchange, 200, "OK"));
        server.createContext("/hello", exchange -> {
            String name = queryParam(exchange.getRequestURI().getRawQuery(), "name");
            respond(exchange, 200, GreetingService.greet(name));
        });
        server.createContext("/", exchange -> respond(exchange, 200,
            "Assignment 04 - Jenkins Java Pipeline. Try /health or /hello?name=Student"));
        server.start();
        System.out.println("Assignment 04 demo is listening on port " + port);
    }

    static String queryParam(String rawQuery, String key) {
        if (rawQuery == null || rawQuery.isEmpty()) return null;
        for (String pair : rawQuery.split("&")) {
            String[] parts = pair.split("=", 2);
            if (URLDecoder.decode(parts[0], StandardCharsets.UTF_8).equals(key)) {
                return parts.length == 2
                    ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                    : "";
            }
        }
        return null;
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
