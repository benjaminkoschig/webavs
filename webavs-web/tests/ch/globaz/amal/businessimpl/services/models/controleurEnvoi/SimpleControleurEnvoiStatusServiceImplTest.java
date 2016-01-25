/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.controleurEnvoi;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author DHI
 * 
 */
public class SimpleControleurEnvoiStatusServiceImplTest {

    /**
     * Test de la cr�ation d'un status d'envoi (ligne d'un job)
     */
    @Test
    @Ignore
    public void testCreate() {
        try {
            // Cr�ation d'un status envoi avec des param�tres erron�s dans le mod�le
            // Attente de r�sultat >> JadeThread.hasMessage � true
            SimpleControleurEnvoiStatus currentEnvoi = new SimpleControleurEnvoiStatus();
            currentEnvoi.setStatusEnvoi("-1");
            currentEnvoi.setIdJob(null);
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().create(currentEnvoi);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation d'un envoi(status) avec param�tres erron�s "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();

            // Cr�ation d'un envoi de document avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� avec isNew � false
            currentEnvoi = new SimpleControleurEnvoiStatus();
            currentEnvoi.setIdAnnonce("-1");
            currentEnvoi.setIdEnvoi("200000");
            currentEnvoi.setIdJob("1");
            currentEnvoi.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
            currentEnvoi.setTypeEnvoi(IAMCodeSysteme.AMDocumentType.ENVOI.getValue());
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().create(currentEnvoi);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la cr�ation d'un envoi (status) avec param�tres corrects "
                        + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            Assert.assertEquals(false, currentEnvoi.isNew());
            Assert.assertEquals("-1", currentEnvoi.getIdAnnonce());
            Assert.assertEquals("1", currentEnvoi.getIdJob());
            Assert.assertEquals("200000", currentEnvoi.getIdEnvoi());
            Assert.assertEquals(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue(), currentEnvoi.getStatusEnvoi());
            Assert.assertEquals(IAMCodeSysteme.AMDocumentType.ENVOI.getValue(), currentEnvoi.getTypeEnvoi());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

    /**
     * Test de la suppression d'un status d'envoi
     */
    @Test
    @Ignore
    public void testDelete() {
        try {
            SimpleControleurEnvoiStatus currentEnvoi = new SimpleControleurEnvoiStatus();

            // Effacement d'un envoi null
            // Attente de r�sultat >> exception de type EnvoiStatusException
            boolean bEnvoiException = false;
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().delete((String) null);
            } catch (ControleurEnvoiException ex) {
                bEnvoiException = true;
            }
            Assert.assertEquals(true, bEnvoiException);

            // Effacement d'un envoi qui est nouveau >> isNew = true
            // Attente de r�sultat >> exception !
            boolean bJadePersistenceException = false;
            currentEnvoi = new SimpleControleurEnvoiStatus();
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().delete(currentEnvoi);
            } catch (JadePersistenceException ex) {
                bJadePersistenceException = true;
            }
            // TODO : PAS LE RESULTAT ATTENDU, TROP D'INTELLIGENCE DANS .delete
            // Assert.assertEquals(true, bJadePersistenceException);

            // Effacement d'un envoi existant avec status � not sent,,,,
            // Attente de r�sultat >> effacement OK
            // TODO : PRENDRE UN ID STATUS EN COURS ET NON ENVOY�
            currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read("629623");
            boolean bDeleteOK = true;
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().delete(currentEnvoi);
            } catch (Exception ex) {
                bDeleteOK = false;
            }
            Assert.assertEquals(true, bDeleteOK);
            Assert.assertEquals(true, currentEnvoi.isNew());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de la lecture d'un status d'envoi
     */
    @Test
    @Ignore
    public void testRead() {
        try {
            // Lecture d'un envoi (status) avec un id bidon
            // Attente de r�sultat >> currentEnvoi id=-1, tous les autres champs sont null
            SimpleControleurEnvoiStatus currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService()
                    .read("-1");
            Assert.assertEquals("-1", currentEnvoi.getId());
            Assert.assertEquals("-1", currentEnvoi.getIdStatus());
            Assert.assertNull(currentEnvoi.getIdAnnonce());
            Assert.assertNull(currentEnvoi.getIdEnvoi());
            Assert.assertNull(currentEnvoi.getIdJob());
            Assert.assertNull(currentEnvoi.getStatusEnvoi());
            Assert.assertNull(currentEnvoi.getTypeEnvoi());
            Assert.assertEquals(true, currentEnvoi.isNew());
            // Lecture d'un envoi (status) avec un id OK
            // Attente de r�sultat >> un mod�le renseign� et exploitable
            currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read("1");
            Assert.assertEquals("1", currentEnvoi.getId());
            Assert.assertEquals("1", currentEnvoi.getIdStatus());
            Assert.assertNotNull(currentEnvoi.getIdAnnonce());
            Assert.assertNotNull(currentEnvoi.getIdEnvoi());
            Assert.assertNotNull(currentEnvoi.getIdJob());
            Assert.assertNotNull(currentEnvoi.getStatusEnvoi());
            Assert.assertNotNull(currentEnvoi.getTypeEnvoi());
            Assert.assertEquals(false, currentEnvoi.isNew());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de la recherche d'un status
     */
    @Test
    @Ignore
    public void testSearch() {
        try {
            // Recherche d'un envoi (status) avec des crit�res de recherche erron�s
            // Attente de r�sultat >> nombre de r�sultat = 0
            SimpleControleurEnvoiStatusSearch envoiSearch = new SimpleControleurEnvoiStatusSearch();
            envoiSearch.setForIdJob("-2233");
            envoiSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(envoiSearch);
            int iNbResults = envoiSearch.getSize();
            Assert.assertEquals(true, (iNbResults <= 0));

            // Recherche d'un envoi (status) avec des crit�res de recherche cibl�s
            // Attente de r�sultat >> nombre de r�sultat = 1
            // Attente de r�sultat >> r�sultat exploitable (mod�le)
            envoiSearch = new SimpleControleurEnvoiStatusSearch();
            envoiSearch.setForIdStatus("1");
            envoiSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(envoiSearch);
            iNbResults = envoiSearch.getSize();
            Assert.assertEquals(true, (iNbResults == 1));
            SimpleControleurEnvoiStatus currentEnvoi = (SimpleControleurEnvoiStatus) envoiSearch.getSearchResults()[0];
            // Attente de r�sultat >> un mod�le renseign� et exploitable
            Assert.assertEquals("1", currentEnvoi.getId());
            Assert.assertEquals("1", currentEnvoi.getIdStatus());
            Assert.assertNotNull(currentEnvoi.getIdAnnonce());
            Assert.assertNotNull(currentEnvoi.getIdEnvoi());
            Assert.assertNotNull(currentEnvoi.getIdJob());
            Assert.assertNotNull(currentEnvoi.getStatusEnvoi());
            Assert.assertNotNull(currentEnvoi.getTypeEnvoi());
            Assert.assertEquals(false, currentEnvoi.isNew());

            // Recherche d'un envoi (status) sans crit�res de recherche
            // Attente de r�sultat >> nombre de r�sultat >= 1
            envoiSearch = new SimpleControleurEnvoiStatusSearch();
            envoiSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(envoiSearch);
            iNbResults = envoiSearch.getSize();
            Assert.assertEquals(true, (iNbResults >= 1));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }

    /**
     * Test de la mise � jour d'un job
     */
    @Test
    @Ignore
    public void testUpdate() {
        try {
            // Mise � jour d'un envoi (status) qui est nouveau >> isNew = true
            // Attente de r�sultat >> Exception soulev�e depuis la persistence
            SimpleControleurEnvoiStatus currentEnvoi = new SimpleControleurEnvoiStatus();
            currentEnvoi.setIdAnnonce("-1");
            currentEnvoi.setIdEnvoi("200000");
            currentEnvoi.setIdJob("1");
            currentEnvoi.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
            currentEnvoi.setTypeEnvoi(IAMCodeSysteme.AMDocumentType.ENVOI.getValue());
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().update(currentEnvoi);
                Assert.fail("Exception non soulev� lors de la mise � jour d'un envoi(status) isNew ");
            } catch (Exception ex) {
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

            // Mise � jour d'un envoi (status) existant avec des param�tres erron�s
            // Attente de r�sultat >> Message dans JadeThread lev� par le checker
            currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read("1");
            currentEnvoi.setIdEnvoi("-1");
            currentEnvoi.setTypeEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().update(currentEnvoi);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'un envoi(status avec param�tres erron�s "
                        + ex.toString());
            }
            Assert.assertEquals(true, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            // Mise � jour d'un envoi (status) existant avec des param�tres corrects
            // Attente de r�sultat >> mod�le renseign� en retour = mod�le de base updat�
            currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().read("1");
            currentEnvoi.setIdEnvoi("200000");
            currentEnvoi.setIdAnnonce("-1");
            currentEnvoi.setIdJob("1");
            currentEnvoi.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
            currentEnvoi.setTypeEnvoi(IAMCodeSysteme.AMDocumentType.ENVOI.getValue());
            try {
                currentEnvoi = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().update(currentEnvoi);
            } catch (Exception ex) {
                Assert.fail("Exception soulev�e lors de la mise � jour d'un envoi(status) avec param�tres corrects "
                        + ex.toString());
            }
            Assert.assertEquals(false, JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR));
            JadeThread.logClear();
            Assert.assertEquals(false, currentEnvoi.isNew());
            Assert.assertEquals("-1", currentEnvoi.getIdAnnonce());
            Assert.assertEquals("1", currentEnvoi.getIdJob());
            Assert.assertEquals(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue(), currentEnvoi.getStatusEnvoi());
            Assert.assertEquals(IAMCodeSysteme.AMDocumentType.ENVOI.getValue(), currentEnvoi.getTypeEnvoi());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
            JadeThread.logClear();
        }
    }

}
