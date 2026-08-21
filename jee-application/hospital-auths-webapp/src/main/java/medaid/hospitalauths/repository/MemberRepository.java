package medaid.hospitalauths.repository;

import medaid.hospitalauths.dto.MemberResponse;

import java.util.Collections;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemberRepository {

    public MemberResponse findByMemberNumber(String memberNumber) {
        MemberResponse response = new MemberResponse();

        response.setMemberNumber(memberNumber);
        response.setEmail("member@gmail.com");
        response.setPhone("0821234567");
        response.setStatus("ACTIVE");
        response.setPlans(Collections.emptyList());

        return response;
    }
}