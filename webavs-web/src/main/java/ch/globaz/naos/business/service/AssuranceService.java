package ch.globaz.naos.business.service;

import ch.globaz.naos.exception.MajorationFraisAdminException;
import globaz.globall.db.BSession;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.naos.business.model.AssuranceSimpleModel;

import java.util.List;

public interface AssuranceService extends JadeApplicationService {
    public String getAssuranceLibelle(String idAssurance, String langue) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet de retourner un AssuranceSimpleModel en se basant sur l'id
     *
     * @param idAssurance l'id de l'assurance
     * @return le modèle
     */
    public AssuranceSimpleModel read(String idAssurance) throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de retourner les ids de toutes les assurances
     *
     * @param session la session
     * @return Tous les ids d'assurances
     */
    public List<String> getIdsAssurancesTous(BSession session) throws MajorationFraisAdminException;

}
