package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * classe de validation des données de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class AnnonceRafam69dModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected static void checkBusinessIntegrity(AnnonceRafamModel model) throws JadePersistenceException,
            JadeApplicationException {
        // DO NOTHING
    }

    /**
     * Vérification des codesSystems
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        // DO NOTHING
    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param model
     *            Modèle à valider
     */
    protected static void checkDatabaseIntegrity(AnnonceRafamModel model) {
        // DO NOTHING
    }

    /**
     * vérification des données requises
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        // DO NOTHING
    }
}
