package medaid.hospitalauths.service;

import medaid.hospitalauths.dto.HospitalAuthorizationResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HospitalAuthorizationService {

    public HospitalAuthorizationResponse authorizeMember(String memberNumber) {
        HospitalAuthorizationResponse response = new HospitalAuthorizationResponse();

                

        return response;
    }

}
