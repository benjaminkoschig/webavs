package globaz.osiris.db.ordres.sepa;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperationOrdreManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.sepa.exceptions.ISODataMissingXMLException;
import globaz.osiris.process.CAProcessOrdre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Formateur du xml, Processus sans validation pour gagner en perf lors de l'execution d'un OG déjà comptabilisé.
 * 
 * @author cel
 * 
 */
public class CAProcessFormatOrdreSEPALite extends CAProcessFormatOrdreSEPA {

    private final static Logger logger = LoggerFactory.getLogger(CAProcessFormatOrdreSEPALite.class);

    @Override
    public void executeOrdreVersement(CAOrdreGroupe og, CAProcessOrdre context) throws Exception {

        // Sous contrôle d'exceptions
        CAOperationOrdreVersement ov = null;
        try {

            // Instancier un manager
            CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
            mgr.setSession(context.getSession());
            mgr.setForIdOrdreGroupe(og.getIdOrdreGroupe());
            mgr.setOrderBy(CAOperationOrdreManager.ORDER_IDORDREGROUPE_NUMTRANSACTION);

            // Récupérer les ordres
            int aTraiter = mgr.getCount();
            // Calculer le nombre à exécuter
            context.setState(og.getSession().getLabel("6113"));
            context.setProgressScaleValue(aTraiter);
            logger.info("préparation au traitement express de {} OV", aTraiter);
            BStatement cursorOpen = mgr.cursorOpen(context.getTransaction());
            // Boucler sur les entités

            while ((ov = (CAOperationOrdreVersement) mgr.cursorReadNext(cursorOpen)) != null) {

                // Vérifier le contexte d'exécution
                if (context.isAborted()) {
                    return;
                }

                // Récupérer l'ordre de versement
                if (ov.getNumTransaction().isEmpty()) {
                    logger.error("Les OV sans numéro de transaction ne devraient pas passer par le preocess Express");
                }
                this.format(ov);
                context.incProgressCounter();

            }
            mgr.clear();
        } catch (ISODataMissingXMLException isoe) {
            og.getMemoryLog().logMessage("5204", ov.getNumTransaction(), FWMessage.ERREUR, og.getClass().getName());
            throw isoe;
        } catch (Exception e) {
            og.getMemoryLog().logMessage("5204", ov.getNumTransaction(), FWMessage.ERREUR, og.getClass().getName());
            logger.error("OG execution failed on ", e);
            throw new ISODataMissingXMLException("Exception in ISO lite Process for pain001 [" + e.getClass().getName()
                    + "]", e);
        }

    }

}
