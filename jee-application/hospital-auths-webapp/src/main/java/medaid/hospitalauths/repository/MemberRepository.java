package medaid.hospitalauths.repository;

import medaid.hospitalauths.dto.MemberResponse;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

@ApplicationScoped
public class MemberRepository {

    @Resource(lookup = "HospitalAuthsDataSource")
    private DataSource dataSource;

    public MemberResponse findByMemberNumber(String memberNumber) {

        String sql =
            "SELECT member_number, email, phone, member_status " +
            "FROM member.member " +
            "WHERE member_number = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, memberNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return null;
                }

                MemberResponse response = new MemberResponse();

                response.setMemberNumber(
                    resultSet.getString("member_number")
                );

                response.setEmail(
                    resultSet.getString("email")
                );

                response.setPhone(
                    resultSet.getString("phone")
                );

                response.setStatus(
                    resultSet.getString("member_status")
                );

                response.setPlans(
                    Collections.emptyList()
                );

                return response;
            }

        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to find member: " + memberNumber,
                e
            );
        }
    }
}