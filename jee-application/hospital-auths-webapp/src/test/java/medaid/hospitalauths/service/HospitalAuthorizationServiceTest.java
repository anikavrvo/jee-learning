package medaid.hospitalauths.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import medaid.hospitalauths.TestSupport;
import medaid.hospitalauths.common.AuthorizationRequestStatus;
import medaid.hospitalauths.dto.HospitalAuthorizationResponse;
import medaid.hospitalauths.repository.AuthorizationRequestRepository;
import medaid.hospitalauths.repository.AuthorizationResponseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class HospitalAuthorizationServiceTest {
    private MemberValidationService validationService;
    private MemberEligibilityService eligibilityService;
    private AuthorizationRequestRepository requestRepository;
    private AuthorizationResponseRepository responseRepository;
    private HospitalAuthorizationService service;

    @Before
    public void setUp() throws Exception {
        validationService = mock(MemberValidationService.class);
        eligibilityService = mock(MemberEligibilityService.class);
        requestRepository = mock(AuthorizationRequestRepository.class);
        responseRepository = mock(AuthorizationResponseRepository.class);
        service = new HospitalAuthorizationService();
        TestSupport.inject(service, "memberValidationService", validationService);
        TestSupport.inject(service, "memberEligibilityService", eligibilityService);
        TestSupport.inject(service, "authorizationRequestRepository", requestRepository);
        TestSupport.inject(service, "authorizationResponseRepository", responseRepository);
    }

    @Test
    public void rejectsBeforePersistenceWhenMemberIsInvalid() {
        when(validationService.validateMember("M001")).thenReturn(false);

        HospitalAuthorizationResponse response = service.authorizeMember("M001");

        assertEquals(AuthorizationRequestStatus.REJECTED.name(), response.getResponseStatus());
        verify(requestRepository, never()).createForMember(any(), any(), any());
        verify(responseRepository, never()).createResponseForRequest(any(Integer.class), any(), any(), any());
    }

    @Test
    public void createsRequestThenRejectsWithoutHospitalBenefit() {
        when(validationService.validateMember("M001")).thenReturn(true);
        when(requestRepository.createForMember("M001", "Hospital authorization", "REJECTED")).thenReturn(7);
        when(eligibilityService.hasHospitalBenefit("M001")).thenReturn(false);

        HospitalAuthorizationResponse response = service.authorizeMember("M001");

        assertEquals(7, response.getAuthorizationRequestId());
        assertEquals(AuthorizationRequestStatus.REJECTED.name(), response.getResponseStatus());
        verify(responseRepository, never()).createResponseForRequest(any(Integer.class), any(), any(), any());
    }

    @Test
    public void createsAndPersistsApprovedResponse() {
        when(validationService.validateMember("M001")).thenReturn(true);
        when(requestRepository.createForMember("M001", "Hospital authorization", "APPROVED")).thenReturn(7);
        when(eligibilityService.hasHospitalBenefit("M001")).thenReturn(true);

        HospitalAuthorizationResponse response = service.authorizeMember("M001");

        assertEquals(7, response.getAuthorizationRequestId());
        assertEquals(AuthorizationRequestStatus.APPROVED.name(), response.getResponseStatus());
        assertTrue(response.getResponseReason().isEmpty());
        assertEquals(LocalDate.now(), response.getRespondedAt());
        verify(responseRepository).createResponseForRequest(
            7, AuthorizationRequestStatus.APPROVED.name(), Optional.empty(), response.getRespondedAt());
        InOrder order = inOrder(validationService, requestRepository, eligibilityService);
        order.verify(validationService).validateMember("M001");
        order.verify(eligibilityService).hasHospitalBenefit("M001");
        order.verify(requestRepository).createForMember("M001", "Hospital authorization", "APPROVED");
    }
}
