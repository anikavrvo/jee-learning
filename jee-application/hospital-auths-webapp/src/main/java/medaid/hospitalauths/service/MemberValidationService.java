package medaid.hospitalauths.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import medaid.hospitalauths.repository.MemberPlanRepository;
import medaid.hospitalauths.repository.MemberRepository;

@ApplicationScoped
public class MemberValidationService {
    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MemberPlanRepository memberPlanRepository;

    public boolean validateMember(String memberNumber) {
        return memberRepository.findByMemberNumber(memberNumber) != null
            && !memberPlanRepository.findByMemberNumber(memberNumber).isEmpty();
    }
}
