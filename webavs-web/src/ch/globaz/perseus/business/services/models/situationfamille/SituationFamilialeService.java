package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;

public interface SituationFamilialeService extends JadeApplicationService {

    /**
     * Permet d'ajouter et cr�er un conjoint � la situation familiale
     * 
     * @param situationFamiliale
     * @param newIdTiers
     *            Id tiers du conjoint
     * @return La situation familiale modifi�e
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public SituationFamiliale addConjoint(SituationFamiliale situationFamiliale, String newIdTiers)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet de changer le conjoint dans une situation familiale, cette m�thode supprimera aussi les donn�es
     * financi�res de l'ancien conjoint.
     * 
     * @param situationFamiliale
     *            La situation familiale
     * @param newIdTiers
     *            Le nouvel idTiers pour le nouveau conjoint
     * @param idDemande
     *            L'id de la demande concern�e
     * @return La nouvelle situation familiale
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public SituationFamiliale changeConjoint(SituationFamiliale situationFamiliale, String newIdTiers, String idDemande)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet la cr�ation d'une entit� SituationFamiliale
     * 
     * @param situationFamiliale
     *            La SituationFamiliale � cr�er
     * @return La SituationFamiliale cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SituationFamiliale create(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * Permet la suppression d'une entit� SituationFamiliale
     * 
     * @param situationFamiliale
     *            La SituationFamiliale � supprimer
     * @return La SituationFamiliale supprim�
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SituationFamiliale delete(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * Permet de charger en m�moire une SituationFamiliale
     * 
     * @param idSituationFamiliale
     *            L'identifiant de la SituationFamiliale � charger en m�moire
     * @return La SituationFamiliale charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SituationFamiliale read(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * 
     * Permet la mise � jour d'une entit� SituationFamiliale
     * 
     * @param situationFamiliale
     *            La SituationFamiliale � mettre � jour
     * @return La SituationFamiliale mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SituationFamiliale update(SituationFamiliale situationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

}
