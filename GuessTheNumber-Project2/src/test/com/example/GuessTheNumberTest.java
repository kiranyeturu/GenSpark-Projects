package com.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class GuessTheNumberTest {
    GuessTheNumber guessN;
    int min = 1;
    int max = 20;

    @BeforeEach
    void setUp() {
    guessN = new GuessTheNumber();
    }

    @Test
    void guessingNumberGame() {
        assertTrue(min <= mynum && mynum <= max);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void main() {
    }
}