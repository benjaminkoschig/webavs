package ch.globaz.common.domaine;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public class CodeSystemEnumUtilsTest {

    private static enum TestEnum implements CodeSystemEnum<TestEnum> {

        ELE1("1");

        private String value;

        TestEnum(String valule) {
            value = valule;
        }

        @Override
        public String getValue() {
            return value;
        }

        public static TestEnum fromValue(String value) {
            return CodeSystemEnumUtils.valueOfById(value, TestEnum.class);
        }

        public static boolean isValid(String value) {
            return CodeSystemEnumUtils.isValid(value, TestEnum.class);
        }

    }

    @Test
    public void testValueOfById() throws Exception {
        assertEquals(TestEnum.ELE1, TestEnum.fromValue("1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByIdError() throws Exception {
        assertEquals(TestEnum.ELE1, TestEnum.fromValue("3"));
    }

    @Test
    public void testIsValidTrue() throws Exception {
        assertTrue(TestEnum.isValid("1"));
    }

    @Test
    public void testIsValidFalse() throws Exception {
        assertFalse(TestEnum.isValid("2"));
    }

}
