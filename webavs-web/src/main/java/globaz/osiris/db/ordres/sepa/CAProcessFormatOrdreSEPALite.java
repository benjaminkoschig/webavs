package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BManager;
import globaz.osiris.db.comptes.CAOperationOrdreManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
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
            mgr.find(context.getTransaction(), BManager.SIZE_NOLIMIT);

            // Boucler sur les entités
            for (int i = 0; i < mgr.size(); i++) {

                // Vérifier le contexte d'exécution
                if (context.isAborted()) {
                    return;
                }

                // Récupérer l'ordre de versement
                CAOperationOrdreVersement ov = (CAOperationOrdreVersement) mgr.getEntity(i);
                if (ov.getNumTransaction().isEmpty()) {
                    logger.error("Les OV sans numéro de transaction ne devraient pas passer par le preocess Express");
                }
                this.format(ov);
                ov = null;
                context.incProgressCounter();

            }
            mgr.clear();
        } catch (Exception e) {
            og.erreur(context.getTransaction(), e.getMessage());
        }

    }

}
