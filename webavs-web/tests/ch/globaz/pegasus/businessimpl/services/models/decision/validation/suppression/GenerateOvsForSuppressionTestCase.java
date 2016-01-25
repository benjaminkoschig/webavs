package ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.externe.IPRConstantesExternes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCOrdresVersements;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValidationAcFactory;

public class GenerateOvsForSuppressionTestCase {
    @Test
    public void generateOvsTowOVDomaineAvsAI() throws OrdreVersementException, PCAccordeeException {
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression("06.2013", "09.2013");
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "300", null); // 3600
        pca.getSimplePCAccordee().setCsTypePC(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        // PRCodePrestationPC._150.getCodePrestationAsString();
        PCAccordee pca2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "03.2013", "400", null); // 1200
        pca2.getSimplePCAccordee().setCsTypePC(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);

        PCAccordee pca3 = ValidationAcFactory.generatePcaRequerantCalcule("04.2013", "05.2013", "200", null); // 400
        PCAccordee pca4 = ValidationAcFactory.generatePcaRequerantCalcule("06.2013", null, "100", null); // 300

        pcas.add(pca4);
        pcas.add(pca3);
        pcas.add(pca2);
        pcas.add(pca);

        List<SimpleOrdreVersement> ovs = generateOvs.generateOv(pcas);
        Assert.assertEquals(4, ovs.size());
        Assert.assertEquals(new BigDecimal(300), generateOvs.getMontantTotalRestitution());

    }

    @Test
    public void generateOvsWithCoupleSeparer() throws OrdreVersementException, PCAccordeeException {
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression("06.2013", "09.2013");
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();

        PCAccordee pcaR1 = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "2000", null); // 24000
        pcaR1.getSimplePCAccordee().setCsTypePC(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        PCAccordee pcaC1 = ValidationAcFactory.generatePcaConjointCalcule("01.2012", "12.2012", "10", null); // 120
        pcaC1.getSimplePCAccordee().setCsTypePC(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);

        PCAccordee pcaR2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", null, "1500", null); // 4500
        PCAccordee pcaC2 = ValidationAcFactory.generatePcaConjointCalcule("01.2013", null, "30", null); // 90

        pcas.add(pcaR2);
        pcas.add(pcaC2);
        pcas.add(pcaR1);
        pcas.add(pcaC1);

        List<SimpleOrdreVersement> ovs = generateOvs.generateOv(pcas);
        Assert.assertEquals(4, ovs.size());
        Assert.assertEquals(new BigDecimal(4590), generateOvs.getMontantTotalRestitution());

        Assert.assertEquals("Montant ov", "4500", ovs.get(0).getMontant());
        Assert.assertEquals("Montant ov", "90", ovs.get(1).getMontant());
        Assert.assertEquals("Montant ov", "0", ovs.get(2).getMontant());
        Assert.assertEquals("Montant ov", "0", ovs.get(3).getMontant());

        Assert.assertEquals("1", ovs.get(0).getNoGroupePeriode());
        Assert.assertEquals("Test idTiersAdressePaiement", pcaR1.getSimpleInformationsComptabilite()
                .getIdTiersAdressePmt(), ovs.get(0).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersBeneficiaire", pcaR1.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
                ovs.get(0).getIdTiers());
        Assert.assertEquals("Test idTiersAdressePaiement conjoint null", pcaR1
                .getSimpleInformationsComptabiliteConjoint().getIdTiersAdressePmt(), ovs.get(0)
                .getIdTiersAdressePaiementConjoint());

        Assert.assertEquals("1", ovs.get(1).getNoGroupePeriode());
        Assert.assertEquals("Test idTiersAdressePaiement", pcaC2.getSimpleInformationsComptabilite()
                .getIdTiersAdressePmt(), ovs.get(1).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersBeneficiaire", pcaC2.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
                ovs.get(1).getIdTiers());
        Assert.assertEquals("Test idTiersAdressePaiement conjoint null", pcaC2
                .getSimpleInformationsComptabiliteConjoint().getIdTiersAdressePmt(), ovs.get(1)
                .getIdTiersAdressePaiementConjoint());

        Assert.assertEquals("2", ovs.get(2).getNoGroupePeriode());
        Assert.assertEquals("Test idTiersAdressePaiement", pcaR2.getSimpleInformationsComptabilite()
                .getIdTiersAdressePmt(), ovs.get(2).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersBeneficiaire", pcaR2.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
                ovs.get(2).getIdTiers());
        Assert.assertEquals("Test idTiersAdressePaiement conjoint null", pcaR2
                .getSimpleInformationsComptabiliteConjoint().getIdTiersAdressePmt(), ovs.get(2)
                .getIdTiersAdressePaiementConjoint());

        Assert.assertEquals("2", ovs.get(3).getNoGroupePeriode());
        Assert.assertEquals("Test idTiersAdressePaiement", pcaC2.getSimpleInformationsComptabilite()
                .getIdTiersAdressePmt(), ovs.get(3).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersBeneficiaire", pcaC2.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
                ovs.get(3).getIdTiers());
        Assert.assertEquals("Test idTiersAdressePaiement conjoint null", pcaC2
                .getSimpleInformationsComptabiliteConjoint().getIdTiersAdressePmt(), ovs.get(3)
                .getIdTiersAdressePaiementConjoint());

    }

    @Test
    public void generateOvsWithDateSuppressionSameDateFin() throws OrdreVersementException, PCAccordeeException {
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression("06.2013", "09.2013");
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();

        PCAccordee pca = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "06.2013", "400", "100");
        pcas.add(pca);

        List<SimpleOrdreVersement> ovs = generateOvs.generateOv(pcas);
        Assert.assertEquals(0, ovs.size());
        Assert.assertEquals(new BigDecimal(0), generateOvs.getMontantTotalRestitution());
    }

    @Test
    public void generateOvsWithOnePca() throws OrdreVersementException, PCAccordeeException {
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression("06.2013", "09.2013");
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", null, "300", null);
        pcas.add(pca);
        List<SimpleOrdreVersement> ovs = generateOvs.generateOv(pcas);
        Assert.assertEquals(1, ovs.size());
        Assert.assertEquals("Nb ov", 1, ovs.size());
        Assert.assertEquals("Montant ov", "900", ovs.get(0).getMontant());
        Assert.assertEquals("Domaine comptat", IPCOrdresVersements.CS_DOMAINE_AVS, ovs.get(0).getCsTypeDomaine());
        Assert.assertEquals("Type ov", IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, ovs.get(0).getCsType());
        Assert.assertEquals("Type domaineApplication", IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, ovs
                .get(0).getIdDomaineApplication());
        Assert.assertEquals("Test idPca", pca.getSimplePCAccordee().getIdPCAccordee(), ovs.get(0).getIdPca());
        Assert.assertEquals("Test idTiersAdressePaiement", pca.getSimpleInformationsComptabilite()
                .getIdTiersAdressePmt(), ovs.get(0).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersBeneficiaire", pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
                ovs.get(0).getIdTiers());
        Assert.assertEquals("Test idTiersAdressePaiement conjoint null", pca
                .getSimpleInformationsComptabiliteConjoint().getIdTiersAdressePmt(), ovs.get(0)
                .getIdTiersAdressePaiementConjoint());

    }

    @Test
    public void generateOvsWithSousCodeGenre() throws OrdreVersementException, PCAccordeeException {
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression("06.2013", "09.2013");
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "300", "100"); // 3600
        pca.getSimplePCAccordee().setCsTypePC(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        // PRCodePrestationPC._150.getCodePrestationAsString();
        PCAccordee pca2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "03.2013", "400", "100"); // 1200
        pca2.getSimplePCAccordee().setCsTypePC(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);

        PCAccordee pca3 = ValidationAcFactory.generatePcaRequerantCalcule("04.2013", "05.2013", "200", "20"); // 400
        PCAccordee pca4 = ValidationAcFactory.generatePcaRequerantCalcule("06.2013", null, "100", "20"); // 300

        pcas.add(pca4);
        pcas.add(pca3);
        pcas.add(pca2);
        pcas.add(pca);

        List<SimpleOrdreVersement> ovs = generateOvs.generateOv(pcas);
        Assert.assertEquals(4, ovs.size());
        Assert.assertEquals(new BigDecimal(300), generateOvs.getMontantTotalRestitution());
    }

    @Test
    public void generateOvsWithTreePcaInSameDomaine() throws OrdreVersementException, PCAccordeeException {
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression("06.2013", "09.2013");
        List<PCAccordee> pcas = new ArrayList<PCAccordee>();
        PCAccordee pca = ValidationAcFactory.generatePcaRequerantCalcule("01.2012", "12.2012", "300", null); // 3600
        PCAccordee pca2 = ValidationAcFactory.generatePcaRequerantCalcule("01.2013", "03.2013", "400", null); // 1200
        PCAccordee pca3 = ValidationAcFactory.generatePcaRequerantCalcule("04.2013", null, "100", null); // 300

        pcas.add(pca3);
        pcas.add(pca2);
        pcas.add(pca);

        List<SimpleOrdreVersement> ovs = generateOvs.generateOv(pcas);
        Assert.assertEquals(3, ovs.size());
        Assert.assertEquals("1", ovs.get(0).getNoGroupePeriode());
        Assert.assertEquals("2", ovs.get(1).getNoGroupePeriode());
        Assert.assertEquals("3", ovs.get(2).getNoGroupePeriode());

        Assert.assertEquals("Montant ov", "300", ovs.get(0).getMontant());
        Assert.assertEquals("Domaine comptat", IPCOrdresVersements.CS_DOMAINE_AVS, ovs.get(0).getCsTypeDomaine());
        Assert.assertEquals("Type ov", IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, ovs.get(0).getCsType());
        Assert.assertEquals("Type domaineApplication", IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, ovs
                .get(0).getIdDomaineApplication());
        Assert.assertEquals("Test idPca", pca.getSimplePCAccordee().getIdPCAccordee(), ovs.get(0).getIdPca());
        Assert.assertEquals("Test idTiersAdressePaiement", pca.getSimpleInformationsComptabilite()
                .getIdTiersAdressePmt(), ovs.get(0).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersBeneficiaire", pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
                ovs.get(0).getIdTiers());
        Assert.assertEquals("Test idTiersAdressePaiement conjoint null", pca
                .getSimpleInformationsComptabiliteConjoint().getIdTiersAdressePmt(), ovs.get(0)
                .getIdTiersAdressePaiementConjoint());
    }

}
