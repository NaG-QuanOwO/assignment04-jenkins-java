package edu.assignment04;

public final class GreetingService {
    private GreetingService() { }

    public static String greet(String name) {
        String safeName = (name == null || name.isBlank()) ? "Ban" : name.trim();
        return "Xin chao, " + safeName + "!";
    }
}
