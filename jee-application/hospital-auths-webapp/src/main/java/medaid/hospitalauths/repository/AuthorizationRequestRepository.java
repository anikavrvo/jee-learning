package medaid.hospitalauths.repository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@ApplicationScoped
public class AuthorizationRequestRepository {
    @Resource(lookup = "HospitalAuthsDataSource")
    private DataSource dataSource;

    public int createForMember(String memberNumber, String procedureDescription, String requestStatus) {
        String sql =
            "INSERT INTO authorizations.authorization_request " +
            "(member_id, plan_id, procedure_description, request_status) " +
            "SELECT m.member_id, mp.plan_id, ?, ? " +
            "FROM member.member AS m " +
            "JOIN member.member_plan AS mp ON m.member_id = mp.member_id " +
            "WHERE m.member_number = ? " +
            "ORDER BY mp.member_plan_id " +
            "LIMIT 1 " +
            "RETURNING authorization_request_id";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, procedureDescription);
            statement.setString(2, requestStatus);
            statement.setString(3, memberNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalStateException(
                        "No plan found for member: " + memberNumber
                    );
                }
                return resultSet.getInt("authorization_request_id");
            }
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to persist authorization request for member: " + memberNumber,
                e
            );
        }
    }
}
