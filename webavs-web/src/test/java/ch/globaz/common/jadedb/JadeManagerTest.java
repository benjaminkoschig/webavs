package ch.globaz.common.jadedb;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.mockito.Mockito;

public class JadeManagerTest {

    @Test
    public void testCreateOrderByTableDefinitionArray() throws Exception {
        JadeManager manager = Mockito.spy(JadeManager.class);
        assertThat(manager._getOrder(null)).isNull();

        manager.defineOrderBy("COL1", "COL2");
        assertThat(manager._getOrder(null)).isEqualTo("COL1,COL2");

        manager.defineOrderBy(TableDefTest.COL1, TableDefTest.COL2);
        assertThat(manager._getOrder(null)).isEqualTo("COL1,COL2");

    }

}
