package ch.globaz.ij.business.services;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisation;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisationSearchModel;

public interface IJBaseIndemnisationService extends
        JadeCrudService<IJSimpleBaseIndemnisation, IJSimpleBaseIndemnisationSearchModel> {

    /**
     * Retourne une instance de IJBaseIndemnisation pré-initialisé avec certaines valeurs récupérées depuis le prononcé.
     * L'entité renvoyé n'est pas stockée en base de données
     * 
     * @param idPrononce
     *            L'id du prononcé associé à cette base d'indemnisation
     * @return Une instance de IJBaseIndemnisation dont certaines valeurs sont pré-initialisé
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public IJSimpleBaseIndemnisation getNewEntity(String idPrononce) throws JadeApplicationException,
            JadePersistenceException;
}
