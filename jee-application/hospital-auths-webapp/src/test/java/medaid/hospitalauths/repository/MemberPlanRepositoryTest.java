package medaid.hospitalauths.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import medaid.hospitalauths.dto.MemberPlanResponse;
import org.junit.Test;

public class MemberPlanRepositoryTest {
    @Test
    public void mapsPlanRows() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(org.mockito.ArgumentMatchers.anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("member_plan_start_date")).thenReturn("2023-01-01");
        when(resultSet.getString("member_plan_end_date")).thenReturn(null);
        when(resultSet.getString("member_plan_status")).thenReturn("ACTIVE");
        when(resultSet.getString("plan_code")).thenReturn("P001");
        when(resultSet.getString("plan_name")).thenReturn("Basic Health Plan");
        when(resultSet.getString("plan_status")).thenReturn("ACTIVE");
        MemberPlanRepository repository = new MemberPlanRepository();
        RepositoryTestSupport.injectDataSource(repository, dataSource);

        List<MemberPlanResponse> responses = repository.findByMemberNumber("M001");

        assertEquals(1, responses.size());
        assertEquals("P001", responses.get(0).getPlanCode());
        assertEquals("Basic Health Plan", responses.get(0).getPlanName());
        assertEquals("2023-01-01", responses.get(0).getMemberPlanStartDate().toString());
    }
}
