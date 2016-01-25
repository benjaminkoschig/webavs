package ch.globaz.pegasus.businessimpl.services.models.lot;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;

public class LotServiceImplTest {

    @Test
    public void testResolveDateToUse() throws Exception {
        LotServiceImpl service = new LotServiceImpl();
        String date = service.resolveDateToUse(new Date("08.2015"), new Date("28.07.2015"));
        assertEquals("01.08.2015", date);

        date = service.resolveDateToUse(new Date("11.2015"), new Date("28.10.2015"));
        assertEquals("02.11.2015", date);

        date = service.resolveDateToUse(new Date("08.2015"), new Date("28.08.2015"));
        assertEquals("28.08.2015", date);
    }

}
