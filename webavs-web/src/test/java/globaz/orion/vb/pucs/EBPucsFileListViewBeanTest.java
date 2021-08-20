package globaz.orion.vb.pucs;


import ch.globaz.orion.db.EBPucsFileManager;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class EBPucsFileListViewBeanTest {

    @Test
    public void testGetUsersJson() {
        EBPucsFileManager mock = mock(EBPucsFileManager.class);
        Mockito.when(mock.getUsers()).thenReturn(Arrays.asList("test1","test2"));
        EBPucsFileListViewBean viewBean = new EBPucsFileListViewBean(mock);
        assertThat(viewBean.getUsersJson()).isEqualTo("[\"test1\",\"test2\"]");
    }
}