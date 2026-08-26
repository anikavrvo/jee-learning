package medaid.hospitalauths.repository;

import java.lang.reflect.Field;

import javax.sql.DataSource;

public final class RepositoryTestSupport {
    private RepositoryTestSupport() {
    }

    public static void injectDataSource(Object repository, DataSource dataSource) throws Exception {
        inject(repository, "dataSource", dataSource);
    }

    public static void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
