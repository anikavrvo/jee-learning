package medaid.hospitalauths.service;

import medaid.hospitalauths.dto.HospitalAuthorizationResponse;
import medaid.hospitalauths.common.AuthorizationRequestStatus;
import medaid.hospitalauths.common.AuthorizationResponseStatus;
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
            response.setResponseStatus(AuthorizationResponseStatus.REJECTED.name());
            response.setResponseReason(Optional.of("Member does not exist or has no plans"));
            return response;
        }

        if (!memberEligibilityService.hasHospitalBenefit(memberNumber)) {
            response.setResponseStatus(AuthorizationResponseStatus.REJECTED.name());
            response.setResponseReason(Optional.of("Member has no hospital benefit"));
            int authorizationRequestId = authorizationRequestRepository.createForMember(
                memberNumber,
                procedureDescription,
                AuthorizationRequestStatus.REJECTED.name()
            );
            response.setAuthorizationRequestId(authorizationRequestId);
            return response;
        }

        int authorizationRequestId = authorizationRequestRepository.createForMember(
                memberNumber,
                procedureDescription,
                AuthorizationRequestStatus.APPROVED.name()
            );
        response.setAuthorizationRequestId(authorizationRequestId);
        response.setResponseStatus(AuthorizationResponseStatus.APPROVED.name());
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
