package medaid.hospitalauths.repository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import medaid.hospitalauths.dto.MemberPlanResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MemberPlanRepository {
    @Resource(lookup = "HospitalAuthsDataSource")
    private DataSource dataSource;

    public List<MemberPlanResponse> findByMemberNumber(String memberNumber) {

        String sql =
            "SELECT m.member_number , " +
            "mp.member_plan_start_date, mp.member_plan_end_date, mp.member_plan_status, " +
            "p.plan_code, p.plan_name, p.plan_status " +
            "FROM member.member AS m " +
            "JOIN member.member_plan AS mp ON m.member_id = mp.member_id " +
            "JOIN plan.plan AS p ON mp.plan_id = p.plan_id " +
            "WHERE m.member_number = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            List<MemberPlanResponse> responses = new ArrayList<>();
            statement.setString(1, memberNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    MemberPlanResponse response = new MemberPlanResponse();

                    response.setMemberPlanStartDate(
                        LocalDate.parse(
                            resultSet.getString("member_plan_start_date")
                        )
                    );

                    if (resultSet.getString("member_plan_end_date") != null) {
                        response.setMemberPlanEndDate(
                            LocalDate.parse(
                                resultSet.getString("member_plan_end_date")
                            )
                        );
                    }

                    response.setMemberPlanStatus(
                        resultSet.getString("member_plan_status")
                    );

                    response.setPlanCode(
                        resultSet.getString("plan_code")
                    );

                    response.setPlanName(
                        resultSet.getString("plan_name")
                    );

                    response.setPlanStatus(
                        resultSet.getString("plan_status")
                    );

                    responses.add(response);
                }
                return responses;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
