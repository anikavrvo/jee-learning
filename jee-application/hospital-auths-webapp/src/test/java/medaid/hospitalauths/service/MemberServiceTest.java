package medaid.hospitalauths.service;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import medaid.hospitalauths.TestSupport;
import medaid.hospitalauths.dto.MemberResponse;
import medaid.hospitalauths.repository.MemberRepository;
import org.junit.Test;

public class MemberServiceTest {
    @Test
    public void returnsRepositoryMember() throws Exception {
        MemberRepository repository = mock(MemberRepository.class);
        MemberResponse expected = new MemberResponse();
        when(repository.findByMemberNumber("M001")).thenReturn(expected);
        MemberService service = new MemberService();
        TestSupport.inject(service, "memberRepository", repository);

        assertSame(expected, service.getMemberByMemberNumber("M001"));
        verify(repository).findByMemberNumber("M001");
    }
}
