package ch.globaz.common.sql;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.exceptions.CommonTechnicalException;

public class QueryWriterExecutorTest {

    @Test(expected = CommonTechnicalException.class)
    public void testExecuteAggregateEmptyQuery() throws Exception {
        QueryWriterExecutor.query("").forClass(Object.class).execute();
        failBecauseExceptionWasNotThrown(CommonTechnicalException.class);
    }

    @Test(expected = CommonTechnicalException.class)
    public void testExecuteAggregate() throws Exception {
        QueryWriterExecutor.select("").execute();
        failBecauseExceptionWasNotThrown(CommonTechnicalException.class);
    }

}
