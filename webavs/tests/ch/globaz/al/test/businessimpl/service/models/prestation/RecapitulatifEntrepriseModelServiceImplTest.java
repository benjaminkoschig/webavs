package ch.globaz.al.test.businessimpl.service.models.prestation;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * @author PTA
 * 
 */
public class RecapitulatifEntrepriseModelServiceImplTest {

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.RecapitulatifEntrepriseModelServiceImpl#create(ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel)}
     * .
     */
    @Ignore
    @Test
    public void testCreate() {

        try {

            RecapitulatifEntrepriseModel recapEntrepModel = new RecapitulatifEntrepriseModel();

            /*
             * MANDATORY
             */
            // TODO rajouter le genre d'asssurance
            ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recapEntrepModel);
            assertEquals(6, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();
            // saisie de données obligatoires sans tenir compte de l'intégrité
            // des données de la base
            recapEntrepModel.setNumeroAffilie("un");
            recapEntrepModel.setPeriodeDe("13.2009");
            recapEntrepModel.setPeriodeA("23.2009");
            recapEntrepModel.setNumeroFacture("dix");
            recapEntrepModel.setEtatRecap("duex");
            recapEntrepModel.setBonification("trois");

            /*
             * DATABASEINTEGRITY
             */
            ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recapEntrepModel);
            assertEquals(5, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // CODESYSTEMINTGRITY
            recapEntrepModel.setPeriodeDe("09.2009");
            recapEntrepModel.setPeriodeA("03.2009");
            recapEntrepModel.setNumeroFacture("5678");

            recapEntrepModel.setBonification("340000");
            recapEntrepModel.setEtatRecap("650000");

            ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recapEntrepModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            // saisie de données en respectant le(s) code(s) système(s)
            recapEntrepModel.setEtatRecap(ALCSPrestation.ETAT_SA);
            recapEntrepModel.setBonification(ALCSPrestation.BONI_INDIRECT);
            ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recapEntrepModel);
            /*
             * BUSINESSINTEGRITY
             */
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            /*
             * ALL OK!
             */
            recapEntrepModel.setPeriodeDe("03.2009");
            recapEntrepModel.setPeriodeA("04.2009");
            recapEntrepModel.setNumeroAffilie("999.9999");

            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.RecapitulatifEntrepriseModelServiceImpl#delete(ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel)}
     * .
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            // lecture de l'identifiant de Récapitulatif entreprise
            RecapitulatifEntrepriseModel recapEntrepModel = ALServiceLocator.getRecapitulatifEntrepriseModelService()
                    .read("3");
            ALServiceLocator.getRecapitulatifEntrepriseModelService().delete(recapEntrepModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.RecapitulatifEntrepriseModelServiceImpl#read(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            ALServiceLocator.getRecapitulatifEntrepriseModelService().read("3");
            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());

        }

    }

    /**
     * Test method for
     * {@link ch.globaz.al.businessimpl.services.models.prestation.RecapitulatifEntrepriseModelServiceImpl#update(ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel)}
     * .
     */
    @Ignore
    @Test
    public void testUpdate() {

        try {
            // recherche de l'enregistrement à modifier par l'identifiant
            RecapitulatifEntrepriseModel recapEntrepModel = ALServiceLocator.getRecapitulatifEntrepriseModelService()
                    .read("3");
            // modification à faire
            recapEntrepModel.setNumeroAffilie("524.1001");

            // mise à jour de l'enregistrement
            ALServiceLocator.getRecapitulatifEntrepriseModelService().update(recapEntrepModel);
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
