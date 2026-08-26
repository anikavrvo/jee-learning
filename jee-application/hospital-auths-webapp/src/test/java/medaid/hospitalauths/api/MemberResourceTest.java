package medaid.hospitalauths.api;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import medaid.hospitalauths.TestSupport;
import medaid.hospitalauths.dto.MemberResponse;
import medaid.hospitalauths.service.MemberService;
import org.junit.Test;

public class MemberResourceTest {
    @Test
    public void delegatesMemberLookup() throws Exception {
        MemberService service = mock(MemberService.class);
        MemberResponse expected = new MemberResponse();
        when(service.getMemberByMemberNumber("M001")).thenReturn(expected);
        MemberResource resource = new MemberResource();
        TestSupport.inject(resource, "memberService", service);

        assertSame(expected, resource.getMember("M001"));
        verify(service).getMemberByMemberNumber("M001");
    }
}
