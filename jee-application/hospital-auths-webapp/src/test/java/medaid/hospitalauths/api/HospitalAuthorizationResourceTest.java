package medaid.hospitalauths.api;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import medaid.hospitalauths.TestSupport;
import medaid.hospitalauths.dto.HospitalAuthorizationResponse;
import medaid.hospitalauths.service.HospitalAuthorizationService;
import org.junit.Test;

public class HospitalAuthorizationResourceTest {
    @Test
    public void delegatesAuthorization() throws Exception {
        HospitalAuthorizationService service = mock(HospitalAuthorizationService.class);
        HospitalAuthorizationResponse expected = new HospitalAuthorizationResponse();
        when(service.authorizeMember("M001")).thenReturn(expected);
        HospitalAuthorizationResource resource = new HospitalAuthorizationResource();
        TestSupport.inject(resource, "hospitalAuthorizationService", service);

        assertSame(expected, resource.authorizeMember("M001"));
        verify(service).authorizeMember("M001");
    }
}
