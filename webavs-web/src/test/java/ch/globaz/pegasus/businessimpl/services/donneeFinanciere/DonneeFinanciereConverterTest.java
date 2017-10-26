package ch.globaz.pegasus.businessimpl.services.donneeFinanciere;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereComplexModel;

public class DonneeFinanciereConverterTest {
    DonneeFinanciereConverter converter = new DonneeFinanciereConverter();

    @Test
    public void testConvertToDomain() throws Exception {
        DonneeFinanciereComplexModel dr = new DonneeFinanciereComplexModel();
        dr.setCsRoleFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        dr.setDateDebutDonneeFinanciere("01.2014");
        dr.setDateFinDonneeFinanciere(null);
        dr.setIdDonneeFinanciereHeader("20");
        dr.setCsTypeDonneeFinanciere(DonneeFinanciereType.ALLOCATION_FAMILIALLE.getValue());

        DonneeFinanciere df = converter.convertToDomain(dr);

        DonneeFinanciere dfExepected = new DonneeFinanciereHeader(
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT), new Date("01.2014"), null, "20", "1");

        assertEquals(dfExepected.getDebut(), df.getDebut());
        assertEquals(dfExepected.getFin(), df.getFin());
        assertEquals(dfExepected.getId(), df.getId());
        assertEquals(dfExepected.getRoleMembreFamille().getValue(), df.getRoleMembreFamille().getValue());

    }

    @Test
    public void testToDateNull() throws Exception {
        assertEquals(null, DonneeFinanciereConverter.toDate(null));
    }

    @Test
    public void testToDateVide() throws Exception {
        assertEquals(null, DonneeFinanciereConverter.toDate(""));
        assertEquals(null, DonneeFinanciereConverter.toDate("    "));
    }

    @Test
    public void testToDateMmsYyyy() throws Exception {
        assertEquals(new Date("01.2014"), DonneeFinanciereConverter.toDate("01.2014"));
    }

    @Test
    public void testToDateJjsMmsYYYY() throws Exception {
        assertEquals(new Date("04.03.2015"), DonneeFinanciereConverter.toDate("04.03.2015"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToDateFail() throws Exception {
        DonneeFinanciereConverter.toDate("0415");
    }
}
