package medaid.hospitalauths.service;

import medaid.hospitalauths.repository.PlanRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MemberEligibilityService {
    @Inject
    private PlanRepository planRepository;

    public boolean hasHospitalBenefit(String memberNumber) {
        return planRepository.hasHospitalBenefit(memberNumber);
    }
}
