package ch.globaz.perseus.business.services.impotsource;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.impotsource.ImpotSourceException;

public interface ImpotSourceService extends JadeApplicationService {

    /**
     * Permet de g�n�rer la liste corrective XML d'imp�t � la source
     * 
     * @param session
     * @param anneeLC
     * @param numDebiteur
     * @return a pathname string , null si aucune ligne n'est renseign�e dans le fichier XML
     * @throws JadePersistenceException
     * @throws ImpotSourceException
     * @throws Exception
     */
    public String genererLC(BSession session, String anneeLC, String numDebiteur) throws ImpotSourceException,
            JadePersistenceException, Exception;

    /**
     * Permet de g�n�rer la liste recapitulative XML d'imp�t � la source
     * 
     * @param session
     * @param idPeriode
     * @param numDebiteur
     * @return a pathname string , null si aucune ligne n'est renseign�e dans le fichier XML
     * @throws JadePersistenceException
     * @throws ImpotSourceException
     * @throws Exception
     */
    public String genererLR(BSession session, String idPeriode, String numDebiteur) throws ImpotSourceException,
            JadePersistenceException, Exception;

}