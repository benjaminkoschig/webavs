package globaz.orion.process.importpucs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import ch.globaz.orion.business.models.pucs.PucsFile;

public class PucsItemTest {
    @Test
    public void createSearchString_GivenSpecialCharacters_ShouldReturnAStringWithoutThem() {
        PucsFile pucsFile = mock(PucsFile.class);
        when(pucsFile.getAnneeDeclaration()).thenReturn("2014");
        when(pucsFile.getNomAffilie()).thenReturn("Arn��a G��ser");
        when(pucsFile.getId()).thenReturn("m�f�chi���s.xml");
        String test = PucsItem.createSearchString(pucsFile);
        assertEquals("2014MEFICHIEEAS.XMLARNEAA GEESER", test);
    }
}
