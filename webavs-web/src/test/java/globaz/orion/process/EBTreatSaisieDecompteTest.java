package globaz.orion.process;

import static org.junit.Assert.*;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.xmlns.eb.sdd.DecompteAndLignes;
import ch.globaz.xmlns.eb.sdd.DecompteEntity;

public class EBTreatSaisieDecompteTest {

    @Test
    public void testResolveDateDebut() throws Exception {

        Date uneDate = new Date("20150515");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(uneDate.getDate());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        DecompteEntity entity = new DecompteEntity();
        entity.setMoisDecompte(date2);
        DecompteAndLignes decompte = new DecompteAndLignes();
        decompte.setDecompte(entity);

        assertEquals("La date de debut est le 01.05.2016", EBTreatSaisieDecompte.resolveDateDebut(decompte),
                "01.05.2015");
    }

    @Test
    public void testResolveDateFin() throws Exception {
        Date uneDate = new Date("20160215");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(uneDate.getDate());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        DecompteEntity entity = new DecompteEntity();
        entity.setMoisDecompte(date2);
        DecompteAndLignes decompte = new DecompteAndLignes();
        decompte.setDecompte(entity);

        assertEquals("La date de fin est le 29.02.2016", EBTreatSaisieDecompte.resolveDateFin(decompte), "29.02.2016");
    }

    @Test
    public void testResolveDateDeSaisie() throws Exception {
        Date uneDate = new Date("20160215");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(uneDate.getDate());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        DecompteEntity entity = new DecompteEntity();
        entity.setDateDeSaisie(date2);
        DecompteAndLignes decompte = new DecompteAndLignes();
        decompte.setDecompte(entity);

        assertEquals("La date est le 15.02.2016", EBTreatSaisieDecompte.resolveDateDeSaisie(decompte), "15.02.2016");
    }

    @Test
    public void testResolveMoisAnneeReleve() throws Exception {
        Date uneDate = new Date("20160215");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(uneDate.getDate());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        DecompteEntity entity = new DecompteEntity();
        entity.setMoisDecompte(date2);
        DecompteAndLignes decompte = new DecompteAndLignes();
        decompte.setDecompte(entity);

        assertEquals("La date mois année est le 02.2016", EBTreatSaisieDecompte.resolveMoisAnneeReleve(decompte),
                "02.2016");
    }

}
