package ch.globaz.al.test.businessimpl.service.models.dossier;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.utils.Constantes;

/**
 * @author jts
 * 
 */
public class DossierModelServiceImplTest {

    /**
     * id de dossier existant en DB
     */
    private String idDossier = "151";
    /**
     * id de dossier existant en DB n'ayant pas de prestations
     */
    private String idDossierSansPrest = "24977";

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierModelServiceImpl#create(ch.globaz.al.business.models.dossier.DossierModel)}
     * .
     */
    @Test
    @Ignore
    public void testCreate() {
        try {

            DossierModel model = new DossierModel();

            /*
             * Mandatory
             */
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(13, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * DB integrity
             */
            model.setIdAllocataire("a");
            model.setNumeroAffilie("a");
            model.setDebutValidite("a");
            model.setFinValidite("a");
            model.setDebutActivite("a");
            model.setFinActivite("a");
            model.setNbJoursDebut("a");
            model.setNbJoursFin("a");
            model.setTauxOccupation("a");
            model.setEtatDossier("a");
            model.setIdTiersCaisseConjoint("a");
            model.setImprimerDecision(new Boolean(true));
            model.setRetenueImpot(new Boolean(true));
            model.setStatut("a");
            model.setTauxVersement("a");
            model.setActiviteAllocataire("a");
            model.setMontantForce("a");
            model.setTarifForce("a");
            model.setUniteCalcul("a");
            model.setCategorieProf("a");
            model.setLoiConjoint("a");
            model.setMotifReduction("a");
            model.setNumSalarieExterne(Constantes.STRING_1000);
            model.setIdDossierConjoint("a");
            model.setIdTiersBeneficiaire("a");
            model.setReference("a");
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(22, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * CS integrity
             */
            model.setIdAllocataire("9999999");
            model.setNumeroAffilie("9999999");
            model.setDebutValidite(null);
            model.setFinValidite(null);
            model.setDebutActivite("01.02.2009");
            model.setFinActivite("01.01.2000");
            model.setNbJoursDebut(null);
            model.setNbJoursFin(null);
            model.setTauxOccupation("100.0");
            model.setEtatDossier("0");
            model.setIdTiersCaisseConjoint("999999999");
            model.setStatut("0");
            model.setTauxVersement("100.0");
            model.setActiviteAllocataire("0");
            model.setMontantForce("123.45");
            model.setTarifForce("1");
            model.setUniteCalcul("0");
            model.setLoiConjoint("1");
            model.setMotifReduction("0");
            model.setIdTiersBeneficiaire("999999999");
            model.setNumSalarieExterne("abc123");
            model.setIdDossierConjoint("1");
            model.setActiviteAllocataire("1");
            model.setEtatDossier("1");
            model.setStatut("1");
            model.setUniteCalcul("1");
            model.setMotifReduction("1");
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(7, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * Business integrity
             */
            model.setIdAllocataire("5");
            model.setEtatDossier(ALCSDossier.ETAT_ACTIF);
            model.setStatut(ALCSDossier.STATUT_CS);
            model.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
            model.setTarifForce(ALCSTarif.CATEGORIE_AG);
            model.setUniteCalcul(ALCSDossier.UNITE_CALCUL_MOIS);
            model.setLoiConjoint(ALCSTarif.CATEGORIE_AI);
            model.setMotifReduction(ALCSDossier.MOTIF_REDUC_PAR);
            model.setLoiConjoint(null);

            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(8, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // Business integrity 2
            model.setDebutValidite("01.01.2009");
            model.setFinValidite("31.01.2000");
            model.setDebutActivite("01.01.2009");
            model.setFinActivite("03.10.2009");
            model.setIdAllocataire("15770");
            model.setIdDossierConjoint("447");
            model.setTauxVersement("70.0");
            model.setMotifReduction(ALCSDossier.MOTIF_REDUC_COMP);
            model.setMontantForce("123.45");
            model.setNumeroAffilie("108.1041");
            model.setLoiConjoint(ALCSTarif.CATEGORIE_NE);
            model.setIdTiersCaisseConjoint("197772");
            model.setIdTiersBeneficiaire("197772");

            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(3, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // Business integrity 3
            model.setMotifReduction(ALCSDossier.MOTIF_REDUC_COMP);
            model.setTauxVersement("100.0");
            model.setDebutValidite("05.01.2009");
            model.setFinValidite("25.01.2010");
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // Business integrity 4
            model.setNbJoursDebut("2");
            model.setNbJoursFin("2");
            model.setFinValidite("25.01.2009");
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // Business integrity 5
            model.setNbJoursDebut("2");
            model.setNbJoursFin("2");
            model.setFinValidite(null);
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // Business integrity 6
            model.setDebutValidite(null);
            model.setNbJoursFin(null);
            model.setFinValidite("31.01.2009");
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // données OK
            model.setDebutValidite("01.01.2009");
            model.setFinValidite(null);
            model.setNbJoursDebut(null);
            model.setNbJoursFin(null);
            ALServiceLocator.getDossierModelService().create(model);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierModelServiceImpl#delete(ch.globaz.al.business.models.dossier.DossierModel)}
     * .
     */
    @Ignore
    @Test
    public void testDelete() {
        try {

            // Tentative de suppression d'un dossier ayant des prestations
            // doit lever une erreur
            DossierModel model = ALServiceLocator.getDossierModelService().read(idDossier);
            ALServiceLocator.getDossierModelService().delete(model);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // Tentative de suppression d'un dossier sans prestation
            DossierModel mod = ALServiceLocator.getDossierModelService().read(idDossierSansPrest);
            ALServiceLocator.getDossierModelService().delete(mod);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierModelServiceImpl#read(java.lang.String)} .
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            DossierModel model = ALServiceLocator.getDossierModelService().read(idDossier);
            assertEquals(false, model.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.dossier.DossierModelServiceImpl#update(ch.globaz.al.business.models.dossier.DossierModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            DossierModel model = ALServiceLocator.getDossierModelService().read(idDossier);
            ALServiceLocator.getDossierModelService().update(model);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
