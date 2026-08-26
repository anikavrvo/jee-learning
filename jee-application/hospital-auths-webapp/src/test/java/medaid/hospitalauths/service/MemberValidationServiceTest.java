package medaid.hospitalauths.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import medaid.hospitalauths.TestSupport;
import medaid.hospitalauths.dto.MemberResponse;
import medaid.hospitalauths.repository.MemberPlanRepository;
import medaid.hospitalauths.repository.MemberRepository;
import org.junit.Before;
import org.junit.Test;

public class MemberValidationServiceTest {
    private MemberRepository memberRepository;
    private MemberPlanRepository memberPlanRepository;
    private MemberValidationService service;

    @Before
    public void setUp() throws Exception {
        memberRepository = mock(MemberRepository.class);
        memberPlanRepository = mock(MemberPlanRepository.class);
        service = new MemberValidationService();
        TestSupport.inject(service, "memberRepository", memberRepository);
        TestSupport.inject(service, "memberPlanRepository", memberPlanRepository);
    }

    @Test
    public void rejectsUnknownMember() {
        when(memberRepository.findByMemberNumber("M001")).thenReturn(null);

        assertFalse(service.validateMember("M001"));
    }

    @Test
    public void rejectsMemberWithoutPlans() {
        when(memberRepository.findByMemberNumber("M001")).thenReturn(new MemberResponse());
        when(memberPlanRepository.findByMemberNumber("M001"))
            .thenReturn(Collections.emptyList());

        assertFalse(service.validateMember("M001"));
    }

    @Test
    public void acceptsMemberWithPlans() {
        when(memberRepository.findByMemberNumber("M001")).thenReturn(new MemberResponse());
        when(memberPlanRepository.findByMemberNumber("M001"))
            .thenReturn(Collections.singletonList(null));

        assertTrue(service.validateMember("M001"));
    }
}
