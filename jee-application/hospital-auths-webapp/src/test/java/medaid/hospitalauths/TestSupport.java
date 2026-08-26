package medaid.hospitalauths;

import java.lang.reflect.Field;

public final class TestSupport {
    private TestSupport() {
    }

    public static void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
