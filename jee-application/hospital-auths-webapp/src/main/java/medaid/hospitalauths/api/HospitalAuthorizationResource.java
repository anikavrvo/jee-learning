package medaid.hospitalauths.api;

import medaid.hospitalauths.dto.HospitalAuthorizationResponse;
import medaid.hospitalauths.service.HospitalAuthorizationService;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hospital")
public class HospitalAuthorizationResource {
    @Inject
    private HospitalAuthorizationService hospitalAuthorizationService;

    @PUT
    @Path("/{memberNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public HospitalAuthorizationResponse authorizeMember(
            @PathParam("memberNumber") String memberNumber) {

        return hospitalAuthorizationService.authorizeMember(memberNumber);
    }
}
