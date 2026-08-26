package medaid.hospitalauths.api;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.ApplicationPath;

import org.junit.Test;

public class AuthorizationApiTest {
    @Test
    public void exposesAuthorizationApplicationPath() {
        ApplicationPath path = AuthorizationApi.class.getAnnotation(ApplicationPath.class);
        assertTrue(path != null);
        assertTrue("/authorization".equals(path.value()));
    }
}
