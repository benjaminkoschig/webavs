package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;

public interface SituationFamilialeService extends JadeApplicationService {

    /**
     * Permet d'ajouter et créer un conjoint à la situation familiale
     * 
     * @param situationFamiliale
     * @param newIdTiers
     *            Id tiers du conjoint
     * @return La situation familiale modifiée
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public SituationFamiliale addConjoint(SituationFamiliale situationFamiliale, String newIdTiers)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet de changer le conjoint dans une situation familiale, cette méthode supprimera aussi les données
     * financières de l'ancien conjoint.
     * 
     * @param situationFamiliale
     *            La situation familiale
     * @param newIdTiers
     *            Le nouvel idTiers pour le nouveau conjoint
     * @param idDemande
     *            L'id de la demande concernée
     * @return La nouvelle situation familiale
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public SituationFamiliale changeConjoint(SituationFamiliale situationFamiliale, String newIdTiers, String idDemande)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet la création d'une entité SituationFamiliale
     * 
     * @param situationFamiliale
     *            La SituationFamiliale à créer
     * @return La SituationFamiliale créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SituationFamiliale create(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * Permet la suppression d'une entité SituationFamiliale
     * 
     * @param situationFamiliale
     *            La SituationFamiliale à supprimer
     * @return La SituationFamiliale supprimé
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SituationFamiliale delete(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * Permet de charger en mémoire une SituationFamiliale
     * 
     * @param idSituationFamiliale
     *            L'identifiant de la SituationFamiliale à charger en mémoire
     * @return La SituationFamiliale chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SituationFamiliale read(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * 
     * Permet la mise à jour d'une entité SituationFamiliale
     * 
     * @param situationFamiliale
     *            La SituationFamiliale à mettre à jour
     * @return La SituationFamiliale mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SituationFamiliale update(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

}
