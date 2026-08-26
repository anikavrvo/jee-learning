package medaid.hospitalauths.dto;

import java.time.LocalDate;
import java.util.Optional;

public class HospitalAuthorizationResponse {
    int authorizationRequestId;
    String responseStatus;
    Optional<String> responseReason;
    LocalDate respondedAt;

    public HospitalAuthorizationResponse() {
    }

    //getters and setters
    public int getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    public void setAuthorizationRequestId(int authorizationRequestId) {
        this.authorizationRequestId = authorizationRequestId;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Optional<String> getResponseReason() {
        return responseReason;
    }

    public void setResponseReason(Optional<String> responseReason) {
        this.responseReason = responseReason;
    }

    public LocalDate getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(LocalDate respondedAt) {
        this.respondedAt = respondedAt;
    }
}
