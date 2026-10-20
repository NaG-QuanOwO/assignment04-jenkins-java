package edu.assignment04;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingServiceTest {
    @Test
    void greetsNamedUser() {
        assertEquals("Xin chao, Jenkins!", GreetingService.greet("Jenkins"));
    }

    @Test
    void defaultsWhenNameIsNull() {
        assertEquals("Xin chao, Ban!", GreetingService.greet(null));
    }

    @Test
    void trimsWhitespace() {
        assertEquals("Xin chao, Student!", GreetingService.greet(" Student "));
    }
}
