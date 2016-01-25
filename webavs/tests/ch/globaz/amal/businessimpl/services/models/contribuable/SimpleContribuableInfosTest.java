package ch.globaz.amal.businessimpl.services.models.contribuable;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class SimpleContribuableInfosTest {

    /**
     * JUnit pour le modèle simpleContribuableInfos
     * 
     * Modèle un peu particulier, il n'y a que de la lecture et de la suppression sur cette table.
     * 
     * @param name
     */
    // public SimpleContribuableInfosTest(String name) {
    // super(name);
    // }

    public void testCreate() {
        // Pas de création de simpleContribuableInfos
    }

    public void testDelete() {
        try {
            SimpleContribuableInfos simpleContribuableInfos = new SimpleContribuableInfos();
            // Effacement d'un modèle null
            // Résultat attendu : Exception
            boolean bContribuableNullException = false;
            try {
                simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().deleteInfo(null);
            } catch (ContribuableException e) {
                bContribuableNullException = true;
            }
            Assert.assertTrue(bContribuableNullException);

            // Effacement d'un contribuable nouveau (isNew == true)
            // Résultat attendu : Exception
            boolean bContribuableIsNewException = false;
            try {
                simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().deleteInfo(
                        simpleContribuableInfos);
            } catch (Exception e) {
                bContribuableIsNewException = true;
            }
            Assert.assertTrue(bContribuableIsNewException);

            // Effacement d'un contribuable existant
            // Résultat attendu : Delete ok
            boolean bDeleteOk = true;
            try {
                simpleContribuableInfos = new SimpleContribuableInfos();
                simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().readInfos("1");
                simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().deleteInfo(
                        simpleContribuableInfos);
                Assert.assertTrue(simpleContribuableInfos.getIsTransfered());
            } catch (Exception e) {
                bDeleteOk = false;
            }
            Assert.assertTrue(bDeleteOk);
            Assert.assertFalse(JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
        } catch (Exception e) {

        } finally {
            // doFinally();
        }

    }

    public void testRead() {
        try {
            // Lecture d'un SimpleContribuableInfos qui n'existe pas
            // Résultat attendu : isNew == true
            SimpleContribuableInfos simpleContribuableInfos = new SimpleContribuableInfos();
            simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().readInfos("-1");
            Assert.assertTrue(simpleContribuableInfos.isNew());

            // Lecture d'un SimpleContribuableInfos qui existe
            // Résultat attendu : isNew == false
            simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().readInfos("1");
            Assert.assertFalse(simpleContribuableInfos.isNew());
        } catch (Exception e) {
            Assert.fail("Erreur lors de la lecture d'un contribuableInfo qui n'existe pas ! --> " + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testSearch() {
        // Pas de search de simpleContribuableInfos
    }

    public void testTransfert() {
        // Transfert un contribuable historique dans la table des contribuables standards
        // et supprime le contribuable historique
        // Résultat attendu : Le contrib. devra se trouver dans la table des contrib. standards
        // et ne devra plus exister dans la table des contribuables historiques
        try {
            // On lit le contribuable depuis l'historique
            SimpleContribuableInfos simpleContribuableInfos = new SimpleContribuableInfos();
            simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().readInfos("1");

            String nomTiers = simpleContribuableInfos.getNom();
            String prenomTiers = simpleContribuableInfos.getPrenom();
            PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
            personneEtendueComplexModel.getTiers().setDesignation1(nomTiers);
            personneEtendueComplexModel.getTiers().setDesignation2(prenomTiers);
            personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().create(
                    personneEtendueComplexModel);

            SimpleContribuable simpleContribuable = new SimpleContribuable();
            simpleContribuable.setNoContribuable(simpleContribuableInfos.getNumeroContribuableActuel());
            simpleContribuable.setIdTier(personneEtendueComplexModel.getTiers().getIdTiers());
            simpleContribuable.setIsContribuableActif(true);
            AmalImplServiceLocator.getSimpleContribuableService().create(simpleContribuable);

            simpleContribuableInfos = AmalImplServiceLocator.getContribuableService().deleteInfo(
                    simpleContribuableInfos);

            Assert.assertTrue(simpleContribuableInfos.getIsTransfered());
            Assert.assertFalse(personneEtendueComplexModel.isNew());
            Assert.assertFalse(simpleContribuable.isNew());
        } catch (Exception e) {
            Assert.fail("Erreur pendant le transfert ! ==>" + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testUpdate() {
        // Pas de update de simpleContribuableInfos
    }
}
