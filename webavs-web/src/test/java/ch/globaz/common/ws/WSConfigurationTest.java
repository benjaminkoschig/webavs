package ch.globaz.common.ws;

import ch.globaz.common.ws.configuration.WSConfiguration;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WSConfigurationTest {
    private static WSConfiguration config = new WSConfiguration();

    @Test
    public void provierAndPathClasses() {
        Set<?> result = config.getClasses();
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void exceptionHandlerClasses() {
        Map<String, ExceptionHandler> result = config.getExceptionMapperClasses();
        assertThat(result).hasSizeGreaterThanOrEqualTo(1);
    }
}
