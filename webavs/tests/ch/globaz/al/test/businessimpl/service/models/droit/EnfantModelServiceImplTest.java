package ch.globaz.al.test.businessimpl.service.models.droit;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSAnnonceRafam;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author PTA
 * 
 */
public class EnfantModelServiceImplTest {

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
            EnfantModel enfantModel = new EnfantModel();
            // MANDATORY
            ALImplServiceLocator.getEnfantModelService().create(enfantModel);
            assertEquals(6, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            enfantModel.setCapableExercer(new Boolean(true));
            enfantModel.setAllocationNaissanceVersee(new Boolean(true));

            // DATABASE INTEGRITY
            enfantModel.setIdTiersEnfant("un");
            enfantModel.setIdPaysResidence("france");
            enfantModel.setCantonResidence("jura");
            enfantModel.setTypeAllocationNaissance("-200");
            enfantModel.setMontantAllocationNaissanceFixe("cent");

            ALImplServiceLocator.getEnfantModelService().create(enfantModel);

            // 6 messagees d'erreur sur les valeurs d'intégrité de la base
            assertEquals(5, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            enfantModel.setIdTiersEnfant("2");
            enfantModel.setMontantAllocationNaissanceFixe("1500.45");
            enfantModel.setIdPaysResidence("100");
            // CS INTEGRITY
            enfantModel.setCantonResidence(ALCSPays.PAYS_SUISSE);
            enfantModel.setTypeAllocationNaissance(ALCSAnnonceRafam.STATUT_FAMILIAL_BENEFICIAIRE_BEAU_PERE);
            ALImplServiceLocator.getEnfantModelService().create(enfantModel);

            // 3 messages d'erreurs sur le non respect des CS
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();

            // BUSINESSINTEGRITY
            // insertion de données en respectant les codes systems, mais sans
            // tenir compte des règles métier
            enfantModel.setCantonResidence(ALCSCantons.JU);
            enfantModel.setTypeAllocationNaissance(ALCSDroit.NAISSANCE_TYPE_NAIS);

            // si le pays de résidence est la suisse canton de résidence devrait
            // être obligatoire
            enfantModel.setCantonResidence(null);

            ALImplServiceLocator.getEnfantModelService().create(enfantModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // BUSINESSINTEGRITYBIS
            // pays non valide
            enfantModel.setIdPaysResidence("1");
            enfantModel.setCantonResidence(ALCSCantons.JU);

            ALImplServiceLocator.getEnfantModelService().create(enfantModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // BUSINESSINTEGRITYTER
            // insertion de données en ne respectant pas toutes les règles
            // métier

            enfantModel.setMontantAllocationNaissanceFixe(null);

            ALImplServiceLocator.getEnfantModelService().create(enfantModel);
            assertEquals(2, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);

            JadeThread.logClear();
            // ALL OK!
            // insertion de données en respectant les règles métier
            enfantModel.setIdPaysResidence("100");
            enfantModel.setMontantAllocationNaissanceFixe("1200");
            enfantModel.setIdTiersEnfant("1");
            ALImplServiceLocator.getEnfantModelService().create(enfantModel);
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
            EnfantModel enfantModel = ALImplServiceLocator.getEnfantModelService().read("7445");
            ALImplServiceLocator.getEnfantModelService().delete(enfantModel);
            // permet un message d'erreur
            assertEquals(1, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).length);
            JadeThread.logClear();

            enfantModel = ALImplServiceLocator.getEnfantModelService().read("4");
            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
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
            ALImplServiceLocator.getEnfantModelService().read("1");

            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
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
            EnfantModel enfantModel = ALImplServiceLocator.getEnfantModelService().read("4");
            enfantModel.setCantonResidence(ALCSCantons.BE);
            enfantModel.setAllocationNaissanceVersee(new Boolean(false));
            enfantModel.setTypeAllocationNaissance(ALCSDroit.NAISSANCE_TYPE_AUCUNE);
            ALImplServiceLocator.getEnfantModelService().update(enfantModel);

            // Permet de contrôler qu'il n'y a pas de message d'erreur
            assertEquals(null, JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
