package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleSituationFamiliale;

public interface SimpleSituationFamilialeService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleSituationFamiliale
     * 
     * @param situationFamiliale
     *            SimpleSituationFamiliale cr�er
     * @return SimpleSituationFamiliale cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleSituationFamiliale create(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet la suppression d'une entit� SimpleSituationFamiliale
     * 
     * @param situationFamiliale
     *            SimpleSituationFamiliale � supprimer
     * @return SimpleSituationFamiliale supprim�
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSituationFamiliale delete(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException;

    /**
     * Permet de charger en m�moire une SimpleSituationFamiliale
     * 
     * @param idSituationFamiliale
     *            L'identifiant SimpleSituationFamiliale � charger en m�moire
     * @return SimpleSituationFamiliale charg� en m�moire
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSituationFamiliale read(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * Permet la mise � jour d'une entit� SimpleSituationFamiliale
     * 
     * @param situationFamiliale
     *            SimpleSituationFamiliale � mettre � jour
     * @return SimpleSituationFamiliale mis � jour
     * @throws SituationFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSituationFamiliale update(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException;

}
