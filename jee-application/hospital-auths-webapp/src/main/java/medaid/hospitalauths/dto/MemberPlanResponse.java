package medaid.hospitalauths.dto;

import java.time.LocalDate;
import java.util.Optional;

public class MemberPlanResponse {
    LocalDate memberPlanStartDate;
    Optional<LocalDate> memberPlanEndDate;
    String memberPlanStatus;
    String planCode;
    String planName;
    String planStatus;

    public MemberPlanResponse(){
    }

    // getters and setters
    public LocalDate getMemberPlanStartDate(){
        return memberPlanStartDate;
    }

    public void setMemberPlanStartDate(LocalDate memberPlanStartDate){
        this.memberPlanStartDate = memberPlanStartDate;
    }

    public Optional<LocalDate> getMemberPlanEndDate(){
        return memberPlanEndDate;
    }

    public void setMemberPlanEndDate(LocalDate memberPlanEndDate){
        this.memberPlanEndDate = Optional.of(memberPlanEndDate);
    }

    public String getMemberPlanStatus(){
        return memberPlanStatus;
    }

    public void setMemberPlanStatus(String memberPlanStatus){
        this.memberPlanStatus = memberPlanStatus;
    }

    public String getPlanCode(){
        return planCode;
    }

    public void setPlanCode(String planCode){
        this.planCode = planCode;
    }

    public String getPlanName(){
        return planName;
    }

    public void setPlanName(String planName){
        this.planName = planName;
    }

    public String getPlanStatus(){
        return planStatus;
    }

    public void setPlanStatus(String planStatus){
        this.planStatus = planStatus;
    }
}
