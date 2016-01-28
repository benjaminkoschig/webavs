package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetique;

public interface SimpleRevenuHypothetiqueService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleRevenuHypothetique
     * 
     * @param SimpleRevenuHypothetique
     *            L'entit� SimpleRevenuHypothetique � cr�er
     * @return L'entit� SimpleRevenuHypothetique cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHypothetique create(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws JadePersistenceException, RevenuHypothetiqueException;

    /**
     * Permet la suppression d'une entit� SimpleRevenuHypothetique
     * 
     * @param SimpleRevenuHypothetique
     *            L'entit� SimpleRevenuHypothetique � supprimer
     * @return L'entit� SimpleRevenuHypothetique supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRevenuHypothetique delete(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleRevenuHypothetique
     * 
     * @param idRevenuHypothetique
     *            L'identifiant de l'entit� SimpleRevenuHypothetique � charger en m�moire
     * @return L'entit� SimpleRevenuHypothetique charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHypothetique read(String idRevenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleRevenuHypothetique
     * 
     * @param SimpleRevenuHypothetique
     *            L'entit� SimpleRevenuHypothetique � mettre � jour
     * @return L'entit� SimpleRevenuHypothetique mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHypothetique update(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws JadePersistenceException, RevenuHypothetiqueException;
}
