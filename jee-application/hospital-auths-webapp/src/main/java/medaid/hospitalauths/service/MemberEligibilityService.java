package medaid.hospitalauths.service;

import medaid.hospitalauths.repository.MemberPlanRepository;
import medaid.hospitalauths.repository.MemberRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MemberEligibilityService {
    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MemberPlanRepository memberPlanRepository;

    
}
