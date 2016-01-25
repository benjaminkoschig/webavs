package ch.globaz.ij.business.services;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisation;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisationSearchModel;

public interface IJBaseIndemnisationService extends
        JadeCrudService<IJSimpleBaseIndemnisation, IJSimpleBaseIndemnisationSearchModel> {

    /**
     * Retourne une instance de IJBaseIndemnisation pr�-initialis� avec certaines valeurs r�cup�r�es depuis le prononc�.
     * L'entit� renvoy� n'est pas stock�e en base de donn�es
     * 
     * @param idPrononce
     *            L'id du prononc� associ� � cette base d'indemnisation
     * @return Une instance de IJBaseIndemnisation dont certaines valeurs sont pr�-initialis�
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public IJSimpleBaseIndemnisation getNewEntity(String idPrononce) throws JadeApplicationException,
            JadePersistenceException;
}
