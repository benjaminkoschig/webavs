package globaz.corvus.annonce.service;

import static org.junit.Assert.*;
import java.math.BigDecimal;
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
    public void testConvertAAMMtoBeigDecimal() throws Exception {
        assertTrue(0 == testService.convertAAMMtoBeigDecimal("1700").compareTo(new BigDecimal(17.00)));
        assertTrue(0 == testService.convertAAMMtoBeigDecimal("2505").compareTo(new BigDecimal(25.05)));
        assertTrue(0 == testService.convertAAMMtoBeigDecimal("").compareTo(new BigDecimal(0)));

    }

    private class MyExtendAbstract extends REAbstractAnnonceXmlService {
        public MyExtendAbstract() {
            super();
        }
    }

}
