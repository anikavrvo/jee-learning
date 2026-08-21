package medaid.hospitalauths.api;

import medaid.hospitalauths.dto.MemberResponse;
import medaid.hospitalauths.service.MemberService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/members")
public class MemberResource {

    @Inject
    private MemberService memberService;

    @GET
    @Path("/{memberNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public MemberResponse getMember(
            @PathParam("memberNumber") String memberNumber) {

        return memberService.getMemberByMemberNumber(memberNumber);
    }
}