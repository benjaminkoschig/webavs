package ch.globaz.vulpecula.external.exceptions;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Spy;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.utils.Pair;

public class ViewExceptionTest {

    @Spy
    private ViewException exception;
    private List<Pair<SpecificationMessage, List<String>>> messages = new ArrayList<Pair<SpecificationMessage, List<String>>>();

    @Before
    public void setUp() throws Exception {
        exception = spy(new ViewException(messages));
    }

    @Ignore
    @Test
    public void getLocalizedMessage_GivenPeriodeInvalideAndTwoParameters_ShouldBeValid() {
        doReturn(Locale.FRENCH).when(exception).getUserLocale();

        addMessageAndParameters(SpecificationMessage.PERIODE_INVALIDE, "01.01.2014", "05.10.2014");

        assertThat(exception.getMessage(), is("La date du 01.01.2014 au 05.10.2014 est invalide<br />"));
    }

    private void addMessageAndParameters(SpecificationMessage specificationMessage, String... parameters) {
        List<String> listeParameters = Arrays.asList(parameters);
        Pair<SpecificationMessage, List<String>> pair = new Pair<SpecificationMessage, List<String>>(
                specificationMessage, listeParameters);
        messages.add(pair);
    }
}
