package medaid.hospitalauths.api;

import static org.junit.Assert.assertEquals;

import medaid.hospitalauths.dto.HealthResponse;
import org.junit.Test;

public class HealthResourceTest {
    @Test
    public void healthReturnsUp() {
        HealthResponse response = new HealthResource().health();

        assertEquals("UP", response.getStatus());
    }
}
