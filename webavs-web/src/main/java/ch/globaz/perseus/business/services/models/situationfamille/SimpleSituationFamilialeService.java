package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleSituationFamiliale;

public interface SimpleSituationFamilialeService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleSituationFamiliale
     * 
     * @param situationFamiliale
     *            SimpleSituationFamiliale créer
     * @return SimpleSituationFamiliale créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleSituationFamiliale create(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet la suppression d'une entité SimpleSituationFamiliale
     * 
     * @param situationFamiliale
     *            SimpleSituationFamiliale à supprimer
     * @return SimpleSituationFamiliale supprimé
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSituationFamiliale delete(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet de charger en mémoire une SimpleSituationFamiliale
     * 
     * @param idSituationFamiliale
     *            L'identifiant SimpleSituationFamiliale à charger en mémoire
     * @return SimpleSituationFamiliale chargé en mémoire
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSituationFamiliale read(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * Permet la mise à jour d'une entité SimpleSituationFamiliale
     * 
     * @param situationFamiliale
     *            SimpleSituationFamiliale à mettre à jour
     * @return SimpleSituationFamiliale mis à jour
     * @throws SituationFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSituationFamiliale update(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException;

}
