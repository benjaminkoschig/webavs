package ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCPresation;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValidationAcFactory;

public class GeneratePrestationTestCase {

    private String dateDernierPmt = "09.2013";
    private String dateSuppression = "06.2013";

    private SimplePrestation generatePrestation(List<PCAccordee> pcas) {
        GeneratePrestation generatePrestation = new GeneratePrestation(dateDernierPmt, dateSuppression);
        SimplePrestation prestation = generatePrestation.generate(pcas, new BigDecimal(300), "30");
        return prestation;
    }

    @Test
    public void testCompteAnnexeRequerantConjoint() throws Exception {
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca1 = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "300", null); // 3600
        PCAccordee pca2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "12.2013", "300", null); // 3600
        PCAccordee pca3 = ValidationAcFactory.generatePcaConjointCalcule("01.2013", "12.2013", "300", null); // 3600

        pcas.add(pca1);
        pcas.add(pca2);
        pcas.add(pca3);
        SimplePrestation prestation = generatePrestation(pcas);
        Assert.assertEquals(pca1.getSimpleInformationsComptabilite().getIdCompteAnnexe(),
                prestation.getIdCompteAnnexeRequerant());
        Assert.assertEquals(pca3.getSimpleInformationsComptabilite().getIdCompteAnnexe(),
                prestation.getIdCompteAnnexeConjoint());
    }

    @Test
    public void testCompteAnnexeRequerantSeul() throws Exception {
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca1 = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "300", null); // 3600
        PCAccordee pca2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "12.2013", "300", null); // 3600
        pcas.add(pca1);
        pcas.add(pca2);
        SimplePrestation prestation = generatePrestation(pcas);
        Assert.assertEquals(pca1.getSimpleInformationsComptabilite().getIdCompteAnnexe(),
                prestation.getIdCompteAnnexeRequerant());
        Assert.assertNull(prestation.getIdCompteAnnexeConjoint());
    }

    @Test
    public void testGeneratePrestation() {
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca1 = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "300", null); // 3600
        PCAccordee pca2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "12.2013", "300", null); // 3600
        PCAccordee pca3 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", null, "300", null); // 3600

        pcas.add(pca1);
        pcas.add(pca2);
        pcas.add(pca3);
        SimplePrestation prestation = generatePrestation(pcas);
        Assert.assertEquals(IPCPresation.CS_TYPE_DE_PRESTATION_DECISION, prestation.getCsTypePrestation());
        Assert.assertEquals("07.2013", prestation.getDateDebut());
        Assert.assertEquals(dateDernierPmt, prestation.getDateFin());
        Assert.assertNull(prestation.getIdCompteAnnexeConjoint());
        Assert.assertNotNull(prestation.getIdCompteAnnexeRequerant());
        Assert.assertNull(prestation.getIdLot());
        Assert.assertEquals(pca1.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
                prestation.getIdTiersBeneficiaire());
        Assert.assertEquals("30", prestation.getIdVersionDroit());
        // Assert.assertEquals("", prestation.getMoisAn());
        Assert.assertEquals("-300", prestation.getMontantTotal());
    }

    @Test
    public void testGeneratePrestationDateFin() throws Exception {
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca1 = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "300", null); // 3600
        PCAccordee pca2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "12.2013", "300", null); // 3600
        pcas.add(pca1);
        pcas.add(pca2);
        SimplePrestation prestation = generatePrestation(pcas);
        Assert.assertEquals("12.2013", prestation.getDateFin());
    }

}
