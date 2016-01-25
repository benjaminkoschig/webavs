/**
 * 
 */
package ch.globaz.amal.businessimpl.services.libra.journalisation;

import static org.junit.Assert.*;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.amal.utils.AMContextProvider;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * @author dhi
 * 
 */
public class JournalisationDocumentTest {

    /**
     * Test avec un tiers existant
     */
    @Ignore
    @Test
    public void testJournalisationTiersExistant() {
        try {

            BSession session = AMContextProvider.getSession();
            JadeThread.storeTemporaryObject("bsession", session);

            String libelleLibra = "Ajout du document DECMST1";
            String remarqueLibra = "Décision définitive";
            String idFamille = "51826193800";
            String idTiers = "147086";
            LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarqueWithTestDossier(idFamille,
                    libelleLibra, remarqueLibra, idTiers, ILIConstantesExternes.CS_DOMAINE_AMAL, true);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        } finally {

            JadeThread.logClear();
        }

    }

    /**
     * Test avec un tiers impossible
     */
    @Ignore
    @Test
    public void testJournalisationTiersNonExistant() {
        try {

            BSession session = AMContextProvider.getSession();
            JadeThread.storeTemporaryObject("bsession", session);

            String libelleLibra = "Ajout du document DECMST1";
            String remarqueLibra = "Décision définitive";
            String idFamille = "-15";
            String idTiers = "-12";
            LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarqueWithTestDossier(idFamille,
                    libelleLibra, remarqueLibra, idTiers, ILIConstantesExternes.CS_DOMAINE_AMAL, true);
            if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                for (int iMessage = 0; iMessage < JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
                    JadeBusinessMessage message = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage];
                    String contents = message.getContents(JadeThread.currentLanguage());
                    assertNotNull(contents, contents);
                }
            } else {
                fail("Erreur non générée depuis LIBRA, dossier flottant");
            }
        } catch (Exception e) {
            assertNotNull("Exception soulevée, correcte : " + e.toString(), e);
            e.printStackTrace();
        } finally {

            JadeThread.logClear();
        }

    }

}
