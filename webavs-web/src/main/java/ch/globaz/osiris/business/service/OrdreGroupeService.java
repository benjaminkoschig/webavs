package ch.globaz.osiris.business.service;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.osiris.exception.OsirisException;

public interface OrdreGroupeService extends JadeApplicationService {

    /**
     * Permet la creation d'un ordre groupe
     * 
     * @param journal
     * @param idOrganeExecution
     * @param numeroOG
     * @param dateEcheance
     * @param typeOrdre
     * @param natureOrdre
     * @param libelleOG
     * @throws OsirisException
     */
    public void createOrdreGroupeeAndPrepare(String idJournal, String idOrganeExecution, String numeroOG,
            String dateEcheance, String typeOrdre, String natureOrdre, String isoGestionnaire, String isoHighPriority)
            throws OsirisException;

}
