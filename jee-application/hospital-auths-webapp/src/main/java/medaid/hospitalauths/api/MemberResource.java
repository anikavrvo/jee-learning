package medaid.hospitalauths.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import medaid.hospitalauths.dto.MemberResponse;

import java.util.Collections;

@Path("/members")
public class MemberResource {

    @GET
    @Path("/{memberNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public MemberResponse getMember(
            @PathParam("memberNumber") String memberNumber) {

        MemberResponse response = new MemberResponse();

        response.setMemberNumber(memberNumber);
        response.setEmail("member@example.com");
        response.setPhone("0821234567");
        response.setStatus("ACTIVE");
        response.setPlans(Collections.emptyList());

        return response;
    }
}
