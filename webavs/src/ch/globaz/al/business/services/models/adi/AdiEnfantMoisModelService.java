package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisSearchModel;

/**
 * interface de déclaration des services de gestion de persistance des données de ADI - Prestations par enfant / mois
 * 
 * @author PTA
 */
public interface AdiEnfantMoisModelService extends JadeApplicationService {

    /**
     * méthode de création ADI - Prestations par enfant / mois
     * 
     * @param adiEnfantMois
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel create(AdiEnfantMoisModel adiEnfantMois) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * méthode de suppression ADI - Prestations par enfant / mois
     * 
     * @param adiEnfantMois
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel delete(AdiEnfantMoisModel adiEnfantMois) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * méthode de lecture ADI - Prestations par enfant / mois
     * 
     * @param idAdiEnfantMoisModel
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel read(String idAdiEnfantMoisModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Méthode de recherche sur pour AdiEnfantMoisModel
     * 
     * @param adiEnfantMoisSearch
     *            selon modèle de recherche AdiEnfantSearchModel
     * @return le moèdle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisSearchModel search(AdiEnfantMoisSearchModel adiEnfantMoisSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * méthode de mise à jour ADI - Prestations par enfant / mois
     * 
     * @param adiEnfantMois
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel update(AdiEnfantMoisModel adiEnfantMois) throws JadeApplicationException,
            JadePersistenceException;
}
