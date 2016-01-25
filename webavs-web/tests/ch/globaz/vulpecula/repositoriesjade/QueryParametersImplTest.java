package ch.globaz.vulpecula.repositoriesjade;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class QueryParametersImplTest {
    private QueryParametersImpl queryParametersImpl;

    @Test
    public void test() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "test");
        queryParametersImpl = new QueryParametersImpl(params, "id");

        assertEquals("test", queryParametersImpl.get("id"));
    }
}
