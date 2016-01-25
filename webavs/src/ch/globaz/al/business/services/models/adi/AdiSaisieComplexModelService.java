package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.models.adi.AdiSaisieComplexModel;
import ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel;

/**
 * 
 * Services de la persistance de AdiSaisieComplexModel
 * 
 * @author GMO
 * 
 */
public interface AdiSaisieComplexModelService extends JadeApplicationService {
    /**
     * Service de création d'un modèle adiSaisieComplexModel
     * 
     * @param adiSaisieComplexModel
     *            le modèle à saisir
     * @return AdiSaisieComplexModel le modèle crée
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieComplexModel create(AdiSaisieComplexModel adiSaisieComplexModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service de suppression d'un modèle adiSaisie
     * 
     * @param adiSaisieComplexModel
     *            le modèle à effacer
     * @return AdiSaisieComplexModel le modèle effacé
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieComplexModel delete(AdiSaisieComplexModel adiSaisieComplexModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Initialise un adiSaisieComplexModel avec des valeurs par défaut si il est nouveau, sinon le charge
     * 
     * @param adiSaisieComplexModel
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
    public AdiSaisieComplexModel initModel(AdiSaisieComplexModel adiSaisieComplexModel, HashMap listeASaisir)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de lecture d'un adiSaisieModel
     * 
     * @param idSaisieModel
     *            id du modèle qu'on veut charger
     * @return AdiSaisieComplexModel chargé
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AdiSaisieComplexModel read(String idSaisieModel) throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de recherche sur le modèle AdiSaisieComplexModel
     * 
     * @param searchModel
     *            modèle de recherche contenant les critères définis
     * @return le modèle de recherche contenant les résultats correspondants aux critères
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiSaisieComplexSearchModel search(AdiSaisieComplexSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

}
