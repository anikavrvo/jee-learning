package medaid.hospitalauths.service;

import medaid.hospitalauths.dto.MemberResponse;
import medaid.hospitalauths.repository.MemberRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MemberService {
    @Inject
    private MemberRepository memberRepository;

    public MemberResponse getMemberByMemberNumber(String memberNumber) {
        return memberRepository.findByMemberNumber(memberNumber);
    }
}