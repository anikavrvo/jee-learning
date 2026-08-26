package medaid.hospitalauths.service;

import medaid.hospitalauths.dto.HospitalAuthorizationResponse;
import medaid.hospitalauths.common.AuthorizationRequestStatus;
import medaid.hospitalauths.repository.AuthorizationRequestRepository;
import medaid.hospitalauths.repository.AuthorizationResponseRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class HospitalAuthorizationService {
    @Inject
    private MemberValidationService memberValidationService;

    @Inject
    private MemberEligibilityService memberEligibilityService;

    @Inject
    private AuthorizationRequestRepository authorizationRequestRepository;

    @Inject
    private AuthorizationResponseRepository authorizationResponseRepository;

    private String procedureDescription = "Hospital authorization";

    public HospitalAuthorizationResponse authorizeMember(String memberNumber) {
        HospitalAuthorizationResponse response = new HospitalAuthorizationResponse();

        if (!memberValidationService.validateMember(memberNumber)) {
            response.setResponseStatus(AuthorizationRequestStatus.REJECTED.name());
            response.setResponseReason(Optional.of("Member does not exist or has no plans"));
            return response;
        }

        int authorizationRequestId = authorizationRequestRepository.createForMember(
            memberNumber,
            procedureDescription
        );
        response.setAuthorizationRequestId(authorizationRequestId);

        if (!memberEligibilityService.hasHospitalBenefit(memberNumber)) {
            response.setResponseStatus(AuthorizationRequestStatus.REJECTED.name());
            response.setResponseReason(Optional.of("Member has no hospital benefit"));
            return response;
        }

        response.setResponseStatus(AuthorizationRequestStatus.APPROVED.name());
        response.setResponseReason(Optional.empty());
        response.setRespondedAt(LocalDate.now());

        authorizationResponseRepository.createResponseForRequest(
            authorizationRequestId,
            response.getResponseStatus(),
            response.getResponseReason(),
            response.getRespondedAt()
        );

        return response;
    }

}
