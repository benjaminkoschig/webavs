package ch.globaz.common.jadedb;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import org.mockito.Mockito;

public class JadeManagerTest {

    @Test
    public void testCreateOrderByTableDefinitionArray() throws Exception {
        JadeManager manager = Mockito.spy(JadeManager.class);
        assertThat(manager._getOrder(null)).isNull();

        manager.createOrderBy("COL1", "COL2");
        assertThat(manager._getOrder(null)).isEqualTo("COL1,COL2");

        manager.createOrderBy(TableDefTest.COL1, TableDefTest.COL2);
        assertThat(manager._getOrder(null)).isEqualTo("COL1,COL2");

    }

}
