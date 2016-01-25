package ch.globaz.lyra.business.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.lyra.business.exceptions.LYException;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheance;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheanceSearchModel;

public interface LYEcheanceService extends JadeApplicationService {

    /**
     * Retourne un processus d'échéances en fonction de son identifiant. Les paramètres (optionnels) sont retournés
     * avec.<br/>
     * Si le processus d'échéances n'existe pas, retourne <code>null</code>
     * 
     * @param idEcheance
     * @return
     * @throws JadePersistenceException
     *             si un problème survient durant l'accès aux données
     */
    public LYSimpleEcheance read(String idEcheance) throws LYException, JadePersistenceException;

    /**
     * Recherche les processus d'échéances en fonction des valeurs renseignées dans le modèle de recherche
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     *             si un problème survient durant l'accès aux données
     */
    public LYSimpleEcheanceSearchModel search(LYSimpleEcheanceSearchModel searchModel) throws LYException,
            JadePersistenceException;
}
