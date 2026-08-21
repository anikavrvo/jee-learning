package medaid.hospitalauths.dto;

import java.util.List;

public class MemberResponse {

    private String memberNumber;
    private String email;
    private String phone;
    private String status;
    private List<PlanResponse> plans;

    public MemberResponse() {
    }

    // getters and setters
    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlanResponse> getPlans() {
        return plans;
    }

    public void setPlans(List<PlanResponse> plans) {
        this.plans = plans;
    }
}
