package globaz.aquila.service.transition;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * <h1>Description</h1>
 * <p>
 * Factory responsable de la cr�ation des actions de transition Une seule instance existe au sein du syst�me (singleton)
 * </p>
 * 
 * @author Pascal Lovy, 27-oct-2004
 */
public final class COTransitionActionService {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne le nom compl�tement qualifi� de la classe d'action pour cette transition.
     * 
     * @param transition
     * @return un nom qualif� complet
     */
    public String getActionClassName(COTransition transition) {
        // Si le nom de l'action n'est pas pr�c�d� du nom du package
        if (transition.getTransitionAction().indexOf(".") == -1) {
            // On ajoute le nom du package dans lequel on se trouve
            return COTransitionAction.class.getPackage().getName() + "." + transition.getTransitionAction();
        } else {
            return transition.getTransitionAction();
        }
    }

    /**
     * Retourne l'action de transition reliant deux �tapes et satisfaisant aux crit�res donn�s.
     * 
     * @param session
     * @param transaction
     * @param contenteiux
     * @param idEtapeSuivante
     *            l'identifiant de l'�tape d'arriv�e pour la transition ou null pour indiff�rent
     * @param manuel
     *            un bool�en pour trouver la transition qui satisfait le crit�re ou null pour indiff�rent
     * @param auto
     *            un bool�en pour trouver la transition qui satisfait le crit�re ou null pour indiff�rent
     * @return une transition ou null s'il n'en existe pas, dans ce cas, un message d'erreur et stock� dans la
     *         transaction.
     * @throws Exception
     *             s'il n'est pas possible de joindre la base ou pas possible de cr�er une instance de l'action
     */
    public COTransitionAction getTransitionAction(BSession session, BTransaction transaction,
            COContentieux contenteiux, String idEtapeSuivante, Boolean manuel, Boolean auto) throws Exception {
        // rechercher la transition
        COTransitionManager transitionManager = new COTransitionManager();

        transitionManager.setSession(session);
        transitionManager.setForIdEtape(contenteiux.getIdEtape()); // dans ce
        // cas
        // devrait
        // etre
        // idEtapePrecedente
        transitionManager.setForIdEtapeSuivante(idEtapeSuivante);
        transitionManager.setForManuel(manuel);
        transitionManager.setForAuto(auto);
        if (transaction != null) {
            transitionManager.find(transaction);
        } else {
            transitionManager.find();
        }

        if (transitionManager.size() == 1) {
            COTransition transition = (COTransition) transitionManager.getFirstEntity();

            return this.getTransitionAction(transition);
        } else if (transitionManager.size() == 0) {
            // Si on ne trouve pas de transition correspondant � l'�tape en
            // cours,
            // on va regarder pour la derni�re �tape non sp�cifique effectu�e.
            return this.getTransitionAction(session, transaction, contenteiux.getIdEtapePrecedenteNonSpecifique(),
                    idEtapeSuivante, manuel, auto);
        } else {
            if (transaction != null) {
                transaction.addErrors(session.getLabel("AQUILA_PAS_DE_TRANSITION"));
            } else {
                session.addError(session.getLabel("AQUILA_PAS_DE_TRANSITION"));
            }

            return null;
        }
    }

    /**
     * Retourne l'action de transition reliant deux �tapes et satisfaisant aux crit�res donn�s.
     * 
     * @param session
     * @param transaction
     * @param idEtape
     *            l'identifiant de l'�tape de d�part pour la transition ou null pour indiff�rent
     * @param idEtapeSuivante
     *            l'identifiant de l'�tape d'arriv�e pour la transition ou null pour indiff�rent
     * @param manuel
     *            un bool�en pour trouver la transition qui satisfait le crit�re ou null pour indiff�rent
     * @param auto
     *            un bool�en pour trouver la transition qui satisfait le crit�re ou null pour indiff�rent
     * @return une transition ou null s'il n'en existe pas, dans ce cas, un message d'erreur et stock� dans la
     *         transaction.
     * @throws Exception
     *             s'il n'est pas possible de joindre la base ou pas possible de cr�er une instance de l'action
     */
    public COTransitionAction getTransitionAction(BSession session, BTransaction transaction, String idEtape,
            String idEtapeSuivante, Boolean manuel, Boolean auto) throws Exception {
        // rechercher la transition
        COTransitionManager transitionManager = new COTransitionManager();

        transitionManager.setSession(session);
        transitionManager.setForIdEtape(idEtape);
        transitionManager.setForIdEtapeSuivante(idEtapeSuivante);
        transitionManager.setForManuel(manuel);
        transitionManager.setForAuto(auto);
        if (transaction != null) {
            transitionManager.find(transaction);
        } else {
            transitionManager.find();
        }

        if (transitionManager.size() == 1) {
            COTransition transition = (COTransition) transitionManager.getFirstEntity();

            return this.getTransitionAction(transition);
        } else {
            if (transaction != null) {
                transaction.addErrors(session.getLabel("AQUILA_PAS_DE_TRANSITION"));
            } else {
                session.addError(session.getLabel("AQUILA_PAS_DE_TRANSITION"));
            }

            return null;
        }
    }

    /**
     * Retourne une nouvelle instance de l'action de transition demand�e.
     * 
     * @param transition
     *            La transition � effectuer
     * @return L'action de transition
     * @throws COTransitionException
     *             En cas de probl�me d'instanciation de l'action
     */
    public COTransitionAction getTransitionAction(COTransition transition) throws COTransitionException {
        String className = getActionClassName(transition);
        COTransitionAction action = null;

        try {
            // Instancie une nouvelle action
            action = (COTransitionAction) Class.forName(className).newInstance();
            action.setTransition(transition);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }

        // Affectation de la transition
        action.setTransition(transition);

        return action;
    }
}
