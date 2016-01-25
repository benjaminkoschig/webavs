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
 * Factory responsable de la création des actions de transition Une seule instance existe au sein du système (singleton)
 * </p>
 * 
 * @author Pascal Lovy, 27-oct-2004
 */
public final class COTransitionActionService {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne le nom complétement qualifié de la classe d'action pour cette transition.
     * 
     * @param transition
     * @return un nom qualifé complet
     */
    public String getActionClassName(COTransition transition) {
        // Si le nom de l'action n'est pas précédé du nom du package
        if (transition.getTransitionAction().indexOf(".") == -1) {
            // On ajoute le nom du package dans lequel on se trouve
            return COTransitionAction.class.getPackage().getName() + "." + transition.getTransitionAction();
        } else {
            return transition.getTransitionAction();
        }
    }

    /**
     * Retourne l'action de transition reliant deux étapes et satisfaisant aux critères donnés.
     * 
     * @param session
     * @param transaction
     * @param contenteiux
     * @param idEtapeSuivante
     *            l'identifiant de l'étape d'arrivée pour la transition ou null pour indifférent
     * @param manuel
     *            un booléen pour trouver la transition qui satisfait le critère ou null pour indifférent
     * @param auto
     *            un booléen pour trouver la transition qui satisfait le critère ou null pour indifférent
     * @return une transition ou null s'il n'en existe pas, dans ce cas, un message d'erreur et stocké dans la
     *         transaction.
     * @throws Exception
     *             s'il n'est pas possible de joindre la base ou pas possible de créer une instance de l'action
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
            // Si on ne trouve pas de transition correspondant à l'étape en
            // cours,
            // on va regarder pour la dernière étape non spécifique effectuée.
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
     * Retourne l'action de transition reliant deux étapes et satisfaisant aux critères donnés.
     * 
     * @param session
     * @param transaction
     * @param idEtape
     *            l'identifiant de l'étape de départ pour la transition ou null pour indifférent
     * @param idEtapeSuivante
     *            l'identifiant de l'étape d'arrivée pour la transition ou null pour indifférent
     * @param manuel
     *            un booléen pour trouver la transition qui satisfait le critère ou null pour indifférent
     * @param auto
     *            un booléen pour trouver la transition qui satisfait le critère ou null pour indifférent
     * @return une transition ou null s'il n'en existe pas, dans ce cas, un message d'erreur et stocké dans la
     *         transaction.
     * @throws Exception
     *             s'il n'est pas possible de joindre la base ou pas possible de créer une instance de l'action
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
     * Retourne une nouvelle instance de l'action de transition demandée.
     * 
     * @param transition
     *            La transition à effectuer
     * @return L'action de transition
     * @throws COTransitionException
     *             En cas de problème d'instanciation de l'action
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
