package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.models.adi.AdiSaisieModel;
import ch.globaz.al.business.models.adi.AdiSaisieSearchModel;

/**
 * Service de gestion de persistance des données de ADI du modèle de saisie - Décompte
 * 
 * @author GMO
 */
public interface AdiSaisieModelService extends JadeApplicationService {
    /**
     * Service de création d'une saisie ADI
     * 
     * @param adiSaisieModel
     *            le modèle à créer
     * @return AdiSaisieModel le modèle créé
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieModel create(AdiSaisieModel adiSaisieModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Service de suppression d'un modèle adiSaisie
     * 
     * @param adiSaisieModel
     *            le modèle à effacer
     * @return AdiSaisieModel le modèle effacé
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieModel delete(AdiSaisieModel adiSaisieModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un adiSaisieModel avec des valeurs par défaut si il est nouveau, sinon le charge
     * 
     * @param adiSaisieModel
     *            Le modèle adiSaisi à initialiser
     * @param listeASaisir
     *            permet de savoir avec quelles valeurs on doit initialiser le modèle (période,enfant)
     * @return Le modèle adiSaisi initialisé
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieModel initModel(AdiSaisieModel adiSaisieModel, HashMap listeASaisir)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Service de lecture d'un adiSaisieModel
     * 
     * @param idSaisieModel
     *            id du modèle qu'on veut charger
     * @return AdiSaisieModel chargé
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieModel read(String idSaisieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Service de recherche les saisies ADI
     * 
     * @param searchModel
     *            modèle de recherche
     * @return modèle de recherche avec les résultats
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieSearchModel search(AdiSaisieSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;
}
