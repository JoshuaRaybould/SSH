package test.java.org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class IngredientsTest {
    @Test
    void testValidValues() {
        assertEquals(50, CalculateQuality.calculateQuality(50, 100));
    }

    @Test
    void testZeroExpected() {
        assertEquals(0, CalculateQuality.calculateQuality(50, 0));
    }

}
