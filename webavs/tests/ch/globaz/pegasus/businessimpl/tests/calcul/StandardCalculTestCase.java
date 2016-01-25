package ch.globaz.pegasus.businessimpl.tests.calcul;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import java.util.ArrayList;
import junit.framework.Assert;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.tests.util.TestBaseData;
import ch.globaz.pegasus.tests.util.calcul.CalculCasTest;

public class StandardCalculTestCase {

    public final void testCalcul3VersionsAvecRentesEtFortune() throws Exception {
        // *************************Version 1, rente 888, 01012011,
        CalculCasTest casTest = (CalculCasTest) TestBaseData.casForTest.get("756.7937.1338.41");
        casTest.setRenteAvsAi(CalculTestsUtil.createRentes(casTest, "888", "01.2011"));
        casTest = CalculTestsUtil.calculVersionDroit(casTest);
        casTest.setPcasForCas(CalculTestsUtil.findPCAForVersionDroit(casTest.getDroit().getId(), casTest.getDroit()
                .getSimpleVersionDroit().getNoVersion()));
        casTest = CalculTestsUtil.preValidAndValidDroit(casTest);
        JadeThread.commitSession();
        Assert.assertTrue("PCAccordéées version du droit 1, attendu:1, générée: " + casTest.getPcasForCas().size(),
                casTest.getPcasForCas().size() == 1);
        // *************************Version 2, rente 1188, 01052011,
        casTest.setDroit(PegasusServiceLocator.getDroitService().corrigerDroit(casTest.getDroit(), "01.01.2011",
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES));

        CalculTestsUtil.createAndCloseRenteAvsAi(casTest, "1188", "05.2011");
        casTest = CalculTestsUtil.calculVersionDroit(casTest);

        casTest.setPcasForCas(CalculTestsUtil.findPCAForVersionDroit(casTest.getDroit().getId(), casTest.getDroit()
                .getSimpleVersionDroit().getNoVersion()));
        casTest = CalculTestsUtil.preValidAndValidDroit(casTest);
        JadeThread.commitSession();

        ArrayList<ListPCAccordee> listePcaForDemande = new ArrayList<ListPCAccordee>();
        listePcaForDemande = CalculTestsUtil.findPcaForDemande(casTest.getDemande());
        int pcaCopie = 0;
        for (ListPCAccordee pca : listePcaForDemande) {
            if (!JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent())) {
                pcaCopie++;
            }
        }
        Assert.assertTrue("PCAccordéées version du droit 2, attendu:1; généré: " + casTest.getPcasForCas().size(),
                casTest.getPcasForCas().size() == 2);
        Assert.assertTrue("PCAccordées de la demande, attendu:2, généré: " + listePcaForDemande.size(),
                listePcaForDemande.size() == 2);
        Assert.assertTrue("PCAccordées copiés pour la demande, attendu 1, généré: " + pcaCopie, pcaCopie == 1);
        // *************************Version 3, rente 1088, 01082011,vehicule 200 01072011
        casTest.setDroit(PegasusServiceLocator.getDroitService().corrigerDroit(casTest.getDroit(), "01.01.2011",
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES));

        CalculTestsUtil.createAndCloseRenteAvsAi(casTest, "1088", "08.2011");
        casTest.setVehicule(CalculTestsUtil.createVehicule(CalculTestsUtil.getDroitMembreFamille(casTest),
                casTest.getDroit(), "200", "07.2011"));
        casTest = CalculTestsUtil.calculVersionDroit(casTest);
        casTest.setPcasForCas(CalculTestsUtil.findPCAForVersionDroit(casTest.getDroit().getId(), casTest.getDroit()
                .getSimpleVersionDroit().getNoVersion()));
        casTest = CalculTestsUtil.preValidAndValidDroit(casTest);
        JadeThread.commitSession();
        listePcaForDemande = new ArrayList<ListPCAccordee>();
        listePcaForDemande = CalculTestsUtil.findPcaForDemande(casTest.getDemande());
        pcaCopie = 0;
        for (ListPCAccordee pca : listePcaForDemande) {
            if (!JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent())) {
                pcaCopie++;
            }
        }
        Assert.assertTrue("PCAccordéées version du droit 3, attendu:3; généré: " + casTest.getPcasForCas().size(),
                casTest.getPcasForCas().size() == 3);
        Assert.assertTrue("PCAccordées de la demande, attendu:4, généré: " + listePcaForDemande.size(),
                listePcaForDemande.size() == 4);
        Assert.assertTrue("PCAccordées copiés pour la demande, attendu 2, généré: " + pcaCopie, pcaCopie == 2);
        // *************************Version 4 vehicule 250 01092011
        casTest.setDroit(PegasusServiceLocator.getDroitService().corrigerDroit(casTest.getDroit(), "01.01.2011",
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES));

        CalculTestsUtil.createAndCloseVehicule(casTest, "250", "09.2011");
        casTest = CalculTestsUtil.calculVersionDroit(casTest);
        casTest.setPcasForCas(CalculTestsUtil.findPCAForVersionDroit(casTest.getDroit().getId(), casTest.getDroit()
                .getSimpleVersionDroit().getNoVersion()));
        casTest = CalculTestsUtil.preValidAndValidDroit(casTest);
        JadeThread.commitSession();
        listePcaForDemande = new ArrayList<ListPCAccordee>();
        listePcaForDemande = CalculTestsUtil.findPcaForDemande(casTest.getDemande());
        pcaCopie = 0;
        for (ListPCAccordee pca : listePcaForDemande) {
            if (!JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent())) {
                pcaCopie++;
            }
        }
        Assert.assertTrue("PCAccordéées version du droit 4, attendu:2; généré: " + casTest.getPcasForCas().size(),
                casTest.getPcasForCas().size() == 2);
        Assert.assertTrue("PCAccordées de la demande, attendu:5, généré: " + listePcaForDemande.size(),
                listePcaForDemande.size() == 5);
        Assert.assertTrue("PCAccordées copiés pour la demande, attendu 3, généré: " + pcaCopie, pcaCopie == 3);

    }

    public final void testCalcul3VersionsAvecRentesSimple() throws Exception {

        // *************************Version 1, rente 999, 01012011,
        CalculCasTest casTest = (CalculCasTest) TestBaseData.casForTest.get("756.4560.1707.48");
        casTest.setRenteAvsAi(CalculTestsUtil.createRentes(casTest, "999", "01.2011"));
        casTest = CalculTestsUtil.calculVersionDroit(casTest);
        casTest.setPcasForCas(CalculTestsUtil.findPCAForVersionDroit(casTest.getDroit().getId(), casTest.getDroit()
                .getSimpleVersionDroit().getNoVersion()));
        casTest = CalculTestsUtil.preValidAndValidDroit(casTest);
        JadeThread.commitSession();
        Assert.assertTrue("PCAccordéées version du droit 1, attendu:1, générée: " + casTest.getPcasForCas().size(),
                casTest.getPcasForCas().size() == 1);

        // *************************Version 2, rente 1199, 01052011,
        casTest.setDroit(PegasusServiceLocator.getDroitService().corrigerDroit(casTest.getDroit(), "01.01.2011",
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES));

        CalculTestsUtil.createAndCloseRenteAvsAi(casTest, "1199", "05.2011");
        casTest = CalculTestsUtil.calculVersionDroit(casTest);

        casTest.setPcasForCas(CalculTestsUtil.findPCAForVersionDroit(casTest.getDroit().getId(), casTest.getDroit()
                .getSimpleVersionDroit().getNoVersion()));
        casTest = CalculTestsUtil.preValidAndValidDroit(casTest);
        JadeThread.commitSession();

        ArrayList<ListPCAccordee> listePcaForDemande = new ArrayList<ListPCAccordee>();
        listePcaForDemande = CalculTestsUtil.findPcaForDemande(casTest.getDemande());
        int pcaCopie = 0;
        for (ListPCAccordee pca : listePcaForDemande) {
            if (!JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent())) {
                pcaCopie++;
            }
        }
        Assert.assertTrue("PCAccordéées version du droit 2, attendu:1; généré: " + casTest.getPcasForCas().size(),
                casTest.getPcasForCas().size() == 2);
        Assert.assertTrue("PCAccordées de la demande, attendu:2, généré: " + listePcaForDemande.size(),
                listePcaForDemande.size() == 2);
        Assert.assertTrue("PCAccordées copiés pour la demande, attendu 1, généré: " + pcaCopie, pcaCopie == 1);
        // *************************Version 3, rente 1099, 01082011,
        casTest.setDroit(PegasusServiceLocator.getDroitService().corrigerDroit(casTest.getDroit(), "01.01.2011",
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES));

        CalculTestsUtil.createAndCloseRenteAvsAi(casTest, "1099", "08.2011");
        casTest = CalculTestsUtil.calculVersionDroit(casTest);
        casTest.setPcasForCas(CalculTestsUtil.findPCAForVersionDroit(casTest.getDroit().getId(), casTest.getDroit()
                .getSimpleVersionDroit().getNoVersion()));
        casTest = CalculTestsUtil.preValidAndValidDroit(casTest);
        JadeThread.commitSession();
        listePcaForDemande = new ArrayList<ListPCAccordee>();
        listePcaForDemande = CalculTestsUtil.findPcaForDemande(casTest.getDemande());
        pcaCopie = 0;
        for (ListPCAccordee pca : listePcaForDemande) {
            if (!JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent())) {
                pcaCopie++;
            }
        }
        Assert.assertTrue("PCAccordéées version du droit 3, attendu:2; généré: " + casTest.getPcasForCas().size(),
                casTest.getPcasForCas().size() == 2);
        Assert.assertTrue("PCAccordées de la demande, attendu:3, généré: " + listePcaForDemande.size(),
                listePcaForDemande.size() == 3);
        Assert.assertTrue("PCAccordées copiés pour la demande, attendu 2, généré: " + pcaCopie, pcaCopie == 2);

    }

}
