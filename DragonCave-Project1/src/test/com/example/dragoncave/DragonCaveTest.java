package com.example.dragoncave;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DragonCaveTest {

    DragonCave caved;

    @BeforeEach
    void setUp() {
        caved = new DragonCave();
    }

    @DisplayName("Test 1: Junit for cave-1 Verification:")
    @Test
    void chooseCave() {
        assertEquals(1, caved.chooseCave(),"Cave -1, Verified cave");
    }

    @DisplayName("Test 2: Junit for cave-2 Verification:")
    @Test
    void chooseCave2() {
        assertEquals(2, caved.chooseCave(),"Cave -2, Verified cave");
    }

    @AfterEach
    void tearDown() {
    }

}