package medaid.hospitalauths.repository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Test;

public class AuthorizationResponseRepositoryTest {
    @Test
    public void persistsResponseFields() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(org.mockito.ArgumentMatchers.anyString())).thenReturn(statement);
        AuthorizationResponseRepository repository = new AuthorizationResponseRepository();
        RepositoryTestSupport.injectDataSource(repository, dataSource);

        LocalDate date = LocalDate.of(2026, 8, 26);
        repository.createResponseForRequest(12, "APPROVED", Optional.empty(), date);

        verify(statement).setInt(1, 12);
        verify(statement).setString(2, "APPROVED");
        verify(statement).setString(3, null);
        verify(statement).setDate(4, java.sql.Date.valueOf(date));
        verify(statement).executeUpdate();
    }
}
