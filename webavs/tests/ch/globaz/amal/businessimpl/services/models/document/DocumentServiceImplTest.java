/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.document;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author DHI
 * 
 */
public class DocumentServiceImplTest {

    @Ignore
    @Test
    public void testCreate() {

        try {
            // Création d'un document avec des paramètres erronés dans le modèle
            // Attente de résultat >> JadeThread.hasMessage à true le checker doit avoir réagit
            SimpleDocument currentDocument = new SimpleDocument();
            currentDocument.setDateEnvoi(null);
            currentDocument.setNumModele("-1");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().create(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création d'un document avec paramètres erronés "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Création d'un document avec des paramètres corrects
            // Attente de résultat >> modèle renseigné avec isNew à false
            currentDocument = new SimpleDocument();
            currentDocument.setDateEnvoi("01.01.2011");
            currentDocument.setIdDetailFamille("1");
            currentDocument.setLibelleEnvoi("Bonjour ma belle");
            currentDocument.setNumModele("42000001");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().create(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la création d'un documnet avec paramètres corrects "
                        + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            Assert.assertEquals(false, currentDocument.isNew());
            Assert.assertEquals("01.01.2011", currentDocument.getDateEnvoi());
            Assert.assertEquals("1", currentDocument.getIdDetailFamille());
            Assert.assertEquals("Bonjour ma belle", currentDocument.getLibelleEnvoi());
            Assert.assertEquals("42000001", currentDocument.getNumModele());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }

    }

    /**
     * Test de suppression d'un simpleDocument
     */
    @Ignore
    @Test
    public void testDelete() {
        try {
            SimpleDocument currentDocument = new SimpleDocument();

            // Effacement d'un document null
            // Attente de résultat >> exception de type DocumentException
            boolean bDocumentException = false;
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(null);
            } catch (DocumentException ex) {
                bDocumentException = true;
            }
            Assert.assertEquals(true, bDocumentException);

            // Effacement d'un document qui est nouveau >> isNew = true
            // Attente de résultat >> exception depuis la persistence
            boolean bJadePersistenceException = false;
            currentDocument = new SimpleDocument();
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(currentDocument);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un document existant qui a déjà été envoyé
            // Attente de résultat >> effacement NOK car document avec status sent
            // Message du checker
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("1");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de l'effacement d'un document existant avec lien status "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Effacement d'un document existant qui n'a pas été envoyé
            // Attente de résultat >> effacement OK car document avec status not sent
            // TODO : ID à CHECKER AVANT LE LANCEMENT DES TESTS
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("550486");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de l'effacement d'un document existant avec lien status "
                        + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals(true, currentDocument.isNew());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de lecture d'un simpleDocument
     */
    @Ignore
    @Test
    public void testRead() {
        try {
            // Lecture d'un document avec un id bidon
            // Attente de résultat >> currentDocument id=-1, tous les autres champs sont null
            SimpleDocument currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("-1");
            Assert.assertEquals("-1", currentDocument.getId());
            Assert.assertEquals("-1", currentDocument.getIdDetailEnvoiDocument());
            Assert.assertNull(currentDocument.getDateEnvoi());
            Assert.assertNull(currentDocument.getLibelleEnvoi());
            Assert.assertNull(currentDocument.getNumModele());
            Assert.assertEquals(true, currentDocument.isNew());
            // Lecture d'un document avec un id OK
            // Attente de résultat >> un modèle renseigné et exploitable
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("1");
            Assert.assertEquals("1", currentDocument.getId());
            Assert.assertEquals("1", currentDocument.getIdDetailEnvoiDocument());
            Assert.assertNotNull(currentDocument.getDateEnvoi());
            Assert.assertNotNull(currentDocument.getLibelleEnvoi());
            Assert.assertNotNull(currentDocument.getNumModele());
            Assert.assertEquals(false, currentDocument.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de recherche d'un simpleDocument
     */
    @Ignore
    @Test
    public void testSearch() {
        try {
            // Recherche d'un document avec des critères de recherche erronés
            // Attente de résultat >> nombre de résultat = 0
            SimpleDocumentSearch documentSearch = new SimpleDocumentSearch();
            documentSearch.setForIdDetailFamille("-12");
            documentSearch.setForIdDocument("-13");
            documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
            int iNbResults = documentSearch.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche d'un document avec des critères de recherche ciblés
            // Attente de résultat >> nombre de résultat = 1
            // Attente de résultat >> résultat exploitable (modèle)
            documentSearch = new SimpleDocumentSearch();
            documentSearch.setForIdDocument("1");
            documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
            iNbResults = documentSearch.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            SimpleDocument currentDocument = (SimpleDocument) documentSearch.getSearchResults()[0];
            // Attente de résultat >> un modèle renseigné et exploitable
            Assert.assertEquals("1", currentDocument.getId());
            Assert.assertEquals("1", currentDocument.getIdDetailEnvoiDocument());
            Assert.assertNotNull(currentDocument.getDateEnvoi());
            Assert.assertNotNull(currentDocument.getLibelleEnvoi());
            Assert.assertNotNull(currentDocument.getNumModele());
            Assert.assertEquals(false, currentDocument.isNew());

            // Recherche d'un document sans critères de recherche
            // Attente de résultat >> nombre de résultat >= 1
            // peu provoquer une erreur si les données ne sont pas correctes
            // TODO : A REACTIVER APRES UN NOUVEL IMPORT DE DONNEES
            // documentSearch = new SimpleDocumentSearch();
            // documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
            // iNbResults = documentSearch.getSize();
            // Assert.assertEquals(true, (iNbResults >= 1));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de mise à jour d'un simpleDocument
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            // Mise à jour d'un document qui est nouveau >> isNew = true
            // Attente de résultat >> Exception de la persistence
            SimpleDocument currentDocument = new SimpleDocument();
            currentDocument.setDateEnvoi("01.01.2011");
            currentDocument.setIdDetailFamille("1");
            currentDocument.setLibelleEnvoi("Hello poulain");
            currentDocument.setNumModele("42000001");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().update(currentDocument);
                Assert.fail("Exception non soulevée lors de la mise à jour d'un document isNew");
            } catch (Exception ex) {
                JadeThread.logClear();
            }
            // Mise à jour d'une document existant avec des paramètres erronés
            // Attente de résultat >> Message dans JadeThread levé par le checker
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("1");
            currentDocument.setIdDetailFamille("-1");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().update(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour d'un document avec paramètres erronés");
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise à jour d'une annonce existante avec des paramètres corrects
            // Attente de résultat >> modèle renseigné en retour = modèle de base updaté
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("1");
            currentDocument.setDateEnvoi("01.01.2011");
            currentDocument.setIdDetailFamille("1");
            currentDocument.setNumModele("42000001");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().update(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulevée lors de la mise à jour d'un document avec paramètres corrects");
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            Assert.assertEquals("01.01.2011", currentDocument.getDateEnvoi());
            Assert.assertEquals("1", currentDocument.getIdDetailFamille());
            Assert.assertEquals("42000001", currentDocument.getNumModele());
            Assert.assertEquals(false, currentDocument.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
