package globaz.corvus.annonce.service;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

public class REAbstractAnnonceXmlServiceTest {
    @Spy
    private MyExtendAbstract testService;

    @Before
    public void setUp() throws Exception {
        testService = Mockito.spy(new MyExtendAbstract());
    }

    @Test
    public void testConvertAAMMtoBigDecimal() throws Exception {
        assertTrue(0 == testService.convertAAMMtoBigDecimal("1700").compareTo(
                new BigDecimal(17.00).setScale(2, RoundingMode.HALF_UP)));
        assertEquals(testService.convertAAMMtoBigDecimal("2505"),
                new BigDecimal(25.05).setScale(2, RoundingMode.HALF_UP));
        assertEquals(testService.convertAAMMtoBigDecimal(""), new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));

    }

    private class MyExtendAbstract extends REAbstractAnnonceXmlService {
        public MyExtendAbstract() {
            super();
        }
    }

}
