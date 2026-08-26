package medaid.hospitalauths.repository;

import java.time.LocalDate;
import java.util.Optional;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@ApplicationScoped
public class AuthorizationResponseRepository {
    @Resource(lookup = "HospitalAuthsDataSource")
    private DataSource dataSource;

    public void createResponseForRequest(int authorizationRequestId, String responseStatus,
            Optional<String> responseReason, LocalDate respondedAt) {
        String sql = "INSERT INTO authorizations.authorization_response "
                + "(authorization_request_id, response_status, response_reason, responded_at) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, authorizationRequestId);
            statement.setString(2, responseStatus);
            statement.setString(3, responseReason.orElse(null));
            statement.setDate(4, java.sql.Date.valueOf(respondedAt));
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist authorization response", e);
        }
    }
}
