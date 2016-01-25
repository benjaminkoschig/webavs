package ch.globaz.lyra.business.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.lyra.business.exceptions.LYException;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheance;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheanceSearchModel;

public interface LYEcheanceService extends JadeApplicationService {

    /**
     * Retourne un processus d'�ch�ances en fonction de son identifiant. Les param�tres (optionnels) sont retourn�s
     * avec.<br/>
     * Si le processus d'�ch�ances n'existe pas, retourne <code>null</code>
     * 
     * @param idEcheance
     * @return
     * @throws JadePersistenceException
     *             si un probl�me survient durant l'acc�s aux donn�es
     */
    public LYSimpleEcheance read(String idEcheance) throws LYException, JadePersistenceException;

    /**
     * Recherche les processus d'�ch�ances en fonction des valeurs renseign�es dans le mod�le de recherche
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     *             si un probl�me survient durant l'acc�s aux donn�es
     */
    public LYSimpleEcheanceSearchModel search(LYSimpleEcheanceSearchModel searchModel) throws LYException,
            JadePersistenceException;
}
