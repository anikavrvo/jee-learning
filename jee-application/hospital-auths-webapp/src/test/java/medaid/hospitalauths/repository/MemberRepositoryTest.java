package medaid.hospitalauths.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

import javax.sql.DataSource;

import medaid.hospitalauths.dto.MemberResponse;
import org.junit.Test;

public class MemberRepositoryTest {
    @Test
    public void mapsMemberAndLoadsPlans() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        MemberPlanRepository planRepository = mock(MemberPlanRepository.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(org.mockito.ArgumentMatchers.anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("member_number")).thenReturn("M001");
        when(resultSet.getString("email")).thenReturn("member@example.com");
        when(resultSet.getString("phone")).thenReturn("0882345678");
        when(resultSet.getString("member_status")).thenReturn("ACTIVE");
        when(planRepository.findByMemberNumber("M001")).thenReturn(Collections.emptyList());
        MemberRepository repository = new MemberRepository();
        RepositoryTestSupport.injectDataSource(repository, dataSource);
        RepositoryTestSupport.inject(repository, "memberPlanRepository", planRepository);

        MemberResponse response = repository.findByMemberNumber("M001");

        assertEquals("M001", response.getMemberNumber());
        assertEquals("member@example.com", response.getEmail().get());
        assertEquals("ACTIVE", response.getStatus());
        assertSame(Collections.emptyList().getClass(), response.getPlans().getClass());
    }

    @Test
    public void returnsNullForUnknownMember() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(org.mockito.ArgumentMatchers.anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        MemberRepository repository = new MemberRepository();
        RepositoryTestSupport.injectDataSource(repository, dataSource);

        assertEquals(null, repository.findByMemberNumber("M404"));
    }
}
