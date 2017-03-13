package ch.globaz.common.sql;

import ch.globaz.common.domaine.CodeSystemEnum;

enum CodeSystemTest implements CodeSystemEnum<CodeSystemTest> {
    TEST_1("1"),
    TEST_2("2");

    private String csCode;

    CodeSystemTest(String csCode) {
        this.csCode = csCode;
    }

    @Override
    public String getValue() {
        return csCode;
    }
}
