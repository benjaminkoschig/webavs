package globaz.aquila.process.batch.utils;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * @author sel
 */
public class COTransitionUtil {

    /**
     * Sequence contentieux (AVS, ARD, PP)
     */
    private COSequence sequence = null;

    private COTransition transitionForCreerCtx = null;

    /**
     * @param session
     * @param transaction
     * @param idSequence
     * @throws Exception
     */
    public COTransitionUtil(BSession session, BTransaction transaction, String idSequence) throws Exception {
        super();

        sequence = loadSequence(session, transaction, idSequence);
        transitionForCreerCtx = loadTransitionCreerContentieux(session, transaction, idSequence);
    }

    /**
     * @return the sequence
     */
    public COSequence getSequence() {
        return sequence;
    }

    /**
     * @return the transitionForCreerCtx
     */
    public COTransition getTransitionForCreerCtx() {
        return transitionForCreerCtx;
    }

    /**
     * Charge la séquence ayant l'identifiant donné, les séquences sont mises en cache.
     * 
     * @param session
     * @param transaction
     * @param idSequence
     * @return
     * @throws Exception
     */
    private COSequence loadSequence(BSession session, BTransaction transaction, String idSequence) throws Exception {
        COSequence sequence = new COSequence();
        sequence.setIdSequence(idSequence);
        sequence.setSession(session);
        sequence.retrieve(transaction);

        if (sequence.hasErrors() || sequence.isNew()) {
            throw new Exception(session.getLabel("AQUILA_SEQUENCE_INTROUVABLE"));
        }

        return sequence;
    }

    /**
     * Charge la transition de création du contentieux pour la séquence donnée, les transitions sont mises en cache.
     * 
     * @param session
     * @param transaction
     * @param idSequence
     * @return
     * @throws Exception
     */
    private COTransition loadTransitionCreerContentieux(BSession session, BTransaction transaction, String idSequence)
            throws Exception {
        // Rechercher l'étape de création pour cette séquence
        COEtapeManager etapeManager = new COEtapeManager();
        // TODO sel : ne faire qu'une seule requete !!
        etapeManager.setForLibEtape(ICOEtape.CS_CONTENTIEUX_CREE);
        etapeManager.setForIdSequence(idSequence);
        etapeManager.setSession(session);
        etapeManager.find(transaction);

        if (etapeManager.size() != 1) {
            throw new Exception(COLogMessageUtil.formatMessage(
                    new StringBuffer(session.getLabel("AQUILA_PROCESS_CREER_CONTENTIEUX_ERREUR_SEQUENCE")),
                    new Object[] { idSequence }));
        }

        // Rechercher la transition pour cette étape
        COTransitionManager transitionManager = new COTransitionManager();

        transitionManager.setSession(session);
        transitionManager.setForIdEtape(((COEtape) etapeManager.getFirstEntity()).getIdEtape());
        transitionManager.find(transaction);

        if (transitionManager.size() == 1) {
            return (COTransition) transitionManager.getFirstEntity();
        } else {
            throw new Exception(COLogMessageUtil.formatMessage(
                    new StringBuffer(session.getLabel("AQUILA_PROCESS_CREER_CONTENTIEUX_ERREUR_SEQUENCE")),
                    new Object[] { idSequence }));
        }
    }

    /**
     * @param sequence
     *            the sequence to set
     */
    public void setSequence(COSequence sequence) {
        this.sequence = sequence;
    }

    /**
     * @param transitionForCreerCtx
     *            the transitionForCreerCtx to set
     */
    public void setTransitionForCreerCtx(COTransition transitionForCreerCtx) {
        this.transitionForCreerCtx = transitionForCreerCtx;
    }
}
