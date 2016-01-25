package ch.globaz.al.test.businessimpl.service.models.droit;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author PTA
 * 
 */
public class DroitModelServiceImplTest_old {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.EnfantModelServiceImpl#create(ch.globaz.al.business.models.droit.EnfantModel)}
     * .
     */

    @Ignore
    @Test
    public void testCreate() {
        // context de test de création
        try {
            DroitModel droitModel = new DroitModel();
            // INTEGRITY MANDATORY
            ALImplServiceLocator.getDroitModelService().create(droitModel);
            assertEquals(7, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // DATABASEINTEGRITY
            droitModel.setImprimerEcheance(new Boolean(false));

            droitModel.setIdDossier("un");
            droitModel.setIdEnfant("un");
            droitModel.setIdTiersBeneficiaire("un");
            droitModel.setDebutDroit("20.15.2002");
            droitModel.setEtatDroit("ferme");
            droitModel.setFinDroitForcee("27.20.2009");
            droitModel.setMontantForce("fdjkhfl");
            droitModel.setMotifFin("grand");
            droitModel.setMotifReduction("tut");
            droitModel.setTarifForce("jfd");
            droitModel.setTauxVersement("peu");
            droitModel.setTypeDroit("aucuun");

            ALImplServiceLocator.getDroitModelService().create(droitModel);
            // 12 messages d'erreur sur l'intégrité des données
            assertEquals(12, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // CODE SYSTEME INTEGRITY
            droitModel.setIdDossier("3");
            droitModel.setIdEnfant("4");
            droitModel.setIdTiersBeneficiaire("2");
            droitModel.setDebutDroit("20.11.2009");
            droitModel.setEtatDroit("25");
            droitModel.setFinDroitForcee("27.12.2007");
            droitModel.setMontantForce("1500");
            droitModel.setMotifFin("57");
            droitModel.setMotifReduction("60");
            droitModel.setTarifForce("12");
            droitModel.setTauxVersement("20");
            droitModel.setTypeDroit("567");
            ALImplServiceLocator.getDroitModelService().create(droitModel);
            // messages d'erreur sur codes systems et règles d'intégrité
            assertEquals(5, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            droitModel.setMotifFin(ALCSDroit.MOTIF_FIN_ECH);
            droitModel.setMotifReduction(ALCSDroit.MOTIF_REDUC_CONAC);
            droitModel.setTypeDroit(ALCSDroit.TYPE_ENF);
            droitModel.setEtatDroit(ALCSDroit.ETAT_A);
            droitModel.setTarifForce(ALCSTarif.CATEGORIE_AI);
            // BUSINESSINTEGRITY
            droitModel.setIdDossier("1");
            droitModel.setIdTiersBeneficiaire("1");
            droitModel.setTypeDroit(ALCSDroit.TYPE_ENF);
            droitModel.setIdEnfant("0");
            droitModel.setTauxVersement("80");
            droitModel.setMotifReduction("");
            ALImplServiceLocator.getDroitModelService().create(droitModel);
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // BUSINESINTEGRITY bis
            droitModel.setIdEnfant(null);
            droitModel.setMotifReduction(null);
            droitModel.setMotifReduction(ALCSDroit.MOTIF_REDUC_COMP);
            droitModel.setTypeDroit(ALCSDroit.TYPE_FORM);

            ALImplServiceLocator.getDroitModelService().create(droitModel);
            // doit renvoyer 4 erreurs
            assertEquals(4, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // TEST sans erreur
            droitModel.setIdEnfant("15000");
            droitModel.setIdDossier("30766");
            droitModel.setMotifReduction(ALCSDroit.MOTIF_REDUC_CONAC);
            droitModel.setTypeDroit(ALCSDroit.TYPE_FORM);
            droitModel.setDebutDroit("20.10.2008");
            droitModel.setFinDroitForcee("20.02.2009");
            droitModel.setIdTiersBeneficiaire("1");

            ALImplServiceLocator.getDroitModelService().create(droitModel);
            // doit renvoyer 0 erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.EnfantModelServiceImpl#delete(ch.globaz.al.business.models.droit.EnfantModel)}
     * .
     */
    @Ignore
    @Test
    public void testDelete() {

        try {
            DroitModel droitModel = ALImplServiceLocator.getDroitModelService().read("3");
            ALImplServiceLocator.getDroitModelService().delete(droitModel);

            // un message d'erreur
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            droitModel = ALImplServiceLocator.getDroitModelService().read("49782");
            ALImplServiceLocator.getDroitModelService().delete(droitModel);

            // un message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.EnfantModelServiceImpl#read(java.lang.String)} .
     */
    @Ignore
    @Test
    public void testRead() {

        try {
            DroitModel droitModel = ALImplServiceLocator.getDroitModelService().read("1");

            // cas d'un droit non existant

            assertEquals(true, droitModel.isNew());

            droitModel = ALImplServiceLocator.getDroitModelService().read("3");

            // dans le cas du droitExistant
            assertEquals(false, droitModel.isNew());

            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.droit.EnfantModelServiceImpl#update(ch.globaz.al.business.models.droit.EnfantModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            DroitModel droitModel = ALImplServiceLocator.getDroitModelService().read("3");
            droitModel.setIdDossier("1");
            droitModel.setIdTiersBeneficiaire("1");
            droitModel.setIdEnfant("1");
            droitModel = ALImplServiceLocator.getDroitModelService().update(droitModel);

            // Permet de contrôler qu'il n'y a un message d'erreur
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            droitModel.setMotifFin(ALCSDroit.MOTIF_FIN_ECH);

            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
