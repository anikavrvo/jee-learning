package medaid.hospitalauths.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.junit.Test;

public class AuthorizationRequestRepositoryTest {
    @Test
    public void createsRequestAndReturnsGeneratedId() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(org.mockito.ArgumentMatchers.anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("authorization_request_id")).thenReturn(12);
        AuthorizationRequestRepository repository = new AuthorizationRequestRepository();
        RepositoryTestSupport.injectDataSource(repository, dataSource);

        assertEquals(12, repository.createForMember("M001", "Hospital authorization"));
        verify(statement).setString(1, "Hospital authorization");
        verify(statement).setString(2, "M001");
    }
}
