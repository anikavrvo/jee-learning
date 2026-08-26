package medaid.hospitalauths.repository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@ApplicationScoped
public class PlanRepository {
    @Resource(lookup = "HospitalAuthsDataSource")
    private DataSource dataSource;

    public boolean hasHospitalBenefit(String memberNumber) {
        String sql =
            "SELECT 1 " +
            "FROM member.member AS m " +
            "JOIN member.member_plan AS mp ON m.member_id = mp.member_id " +
            "JOIN plan.plan_benefit AS pb ON mp.plan_id = pb.plan_id " +
            "JOIN plan.benefit AS b ON pb.benefit_id = b.benefit_id " +
            "WHERE m.member_number = ? " +
            "AND (LOWER(b.benefit_name) LIKE '%hospital%' " +
            "OR LOWER(COALESCE(b.benefit_description, '')) LIKE '%hospital%') " +
            "LIMIT 1";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, memberNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to check hospital benefits for member: " + memberNumber,
                e
            );
        }
    }
}
