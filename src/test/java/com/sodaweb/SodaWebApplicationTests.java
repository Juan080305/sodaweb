package com.sodaweb;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class SodaWebApplicationTests {

    @Test
    void aplicacionExiste() {
        assertNotNull(new SodaWebApplication());
    }

}
