package medaid.hospitalauths.dto;

import java.util.List;
import java.util.Optional;

public class MemberResponse {

    private String memberNumber;
    private Optional<String> email;
    private Optional<String> phone;
    private String status;
    private List<MemberPlanResponse> memberPlans;

    public MemberResponse() {
    }

    // getters and setters
    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Optional.ofNullable(email);
    }

    public Optional<String> getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = Optional.ofNullable(phone);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MemberPlanResponse> getPlans() {
        return memberPlans;
    }

    public void setPlans(List<MemberPlanResponse> memberPlans) {
        this.memberPlans = memberPlans;
    }
}
