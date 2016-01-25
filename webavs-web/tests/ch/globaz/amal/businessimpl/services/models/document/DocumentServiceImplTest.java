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
            // Cr�ation d'un document avec des param�tres erron�s dans le mod�le
            // Attente de r�sultat >> JadeThread.hasMessage � true le checker doit avoir r�agit
            SimpleDocument currentDocument = new SimpleDocument();
            currentDocument.setDateEnvoi(null);
            currentDocument.setNumModele("-1");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().create(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation d'un document avec param�tres erron�s "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Cr�ation d'un document avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� avec isNew � false
            currentDocument = new SimpleDocument();
            currentDocument.setDateEnvoi("01.01.2011");
            currentDocument.setIdDetailFamille("1");
            currentDocument.setLibelleEnvoi("Bonjour ma belle");
            currentDocument.setNumModele("42000001");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().create(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation d'un documnet avec param�tres corrects "
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
            // Attente de r�sultat >> exception de type DocumentException
            boolean bDocumentException = false;
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(null);
            } catch (DocumentException ex) {
                bDocumentException = true;
            }
            Assert.assertEquals(true, bDocumentException);

            // Effacement d'un document qui est nouveau >> isNew = true
            // Attente de r�sultat >> exception depuis la persistence
            boolean bJadePersistenceException = false;
            currentDocument = new SimpleDocument();
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(currentDocument);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un document existant qui a d�j� �t� envoy�
            // Attente de r�sultat >> effacement NOK car document avec status sent
            // Message du checker
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("1");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de l'effacement d'un document existant avec lien status "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Effacement d'un document existant qui n'a pas �t� envoy�
            // Attente de r�sultat >> effacement OK car document avec status not sent
            // TODO : ID � CHECKER AVANT LE LANCEMENT DES TESTS
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("550486");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().delete(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de l'effacement d'un document existant avec lien status "
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
            // Attente de r�sultat >> currentDocument id=-1, tous les autres champs sont null
            SimpleDocument currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("-1");
            Assert.assertEquals("-1", currentDocument.getId());
            Assert.assertEquals("-1", currentDocument.getIdDetailEnvoiDocument());
            Assert.assertNull(currentDocument.getDateEnvoi());
            Assert.assertNull(currentDocument.getLibelleEnvoi());
            Assert.assertNull(currentDocument.getNumModele());
            Assert.assertEquals(true, currentDocument.isNew());
            // Lecture d'un document avec un id OK
            // Attente de r�sultat >> un mod�le renseign� et exploitable
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
            // Recherche d'un document avec des crit�res de recherche erron�s
            // Attente de r�sultat >> nombre de r�sultat = 0
            SimpleDocumentSearch documentSearch = new SimpleDocumentSearch();
            documentSearch.setForIdDetailFamille("-12");
            documentSearch.setForIdDocument("-13");
            documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
            int iNbResults = documentSearch.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche d'un document avec des crit�res de recherche cibl�s
            // Attente de r�sultat >> nombre de r�sultat = 1
            // Attente de r�sultat >> r�sultat exploitable (mod�le)
            documentSearch = new SimpleDocumentSearch();
            documentSearch.setForIdDocument("1");
            documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
            iNbResults = documentSearch.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            SimpleDocument currentDocument = (SimpleDocument) documentSearch.getSearchResults()[0];
            // Attente de r�sultat >> un mod�le renseign� et exploitable
            Assert.assertEquals("1", currentDocument.getId());
            Assert.assertEquals("1", currentDocument.getIdDetailEnvoiDocument());
            Assert.assertNotNull(currentDocument.getDateEnvoi());
            Assert.assertNotNull(currentDocument.getLibelleEnvoi());
            Assert.assertNotNull(currentDocument.getNumModele());
            Assert.assertEquals(false, currentDocument.isNew());

            // Recherche d'un document sans crit�res de recherche
            // Attente de r�sultat >> nombre de r�sultat >= 1
            // peu provoquer une erreur si les donn�es ne sont pas correctes
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
     * Test de mise � jour d'un simpleDocument
     */
    @Ignore
    @Test
    public void testUpdate() {
        try {
            // Mise � jour d'un document qui est nouveau >> isNew = true
            // Attente de r�sultat >> Exception de la persistence
            SimpleDocument currentDocument = new SimpleDocument();
            currentDocument.setDateEnvoi("01.01.2011");
            currentDocument.setIdDetailFamille("1");
            currentDocument.setLibelleEnvoi("Hello poulain");
            currentDocument.setNumModele("42000001");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().update(currentDocument);
                Assert.fail("Exception non soulev�e lors de la mise � jour d'un document isNew");
            } catch (Exception ex) {
                JadeThread.logClear();
            }
            // Mise � jour d'une document existant avec des param�tres erron�s
            // Attente de r�sultat >> Message dans JadeThread lev� par le checker
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("1");
            currentDocument.setIdDetailFamille("-1");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().update(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'un document avec param�tres erron�s");
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise � jour d'une annonce existante avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� en retour = mod�le de base updat�
            currentDocument = AmalImplServiceLocator.getSimpleDocumentService().read("1");
            currentDocument.setDateEnvoi("01.01.2011");
            currentDocument.setIdDetailFamille("1");
            currentDocument.setNumModele("42000001");
            try {
                currentDocument = AmalImplServiceLocator.getSimpleDocumentService().update(currentDocument);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'un document avec param�tres corrects");
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
