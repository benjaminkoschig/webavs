package ch.globaz.pegasus.business.services.process.statistiquesofas;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.process.StatistiquesOFASException;
import ch.globaz.pegasus.business.models.process.statistiquesofas.PlanCalculeDemandeDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.process.statistiquesofas.RenteApiIjSearch;

public interface StatistiquesOFASService extends JadeApplicationService {
    /**
     * Creer le fichier text des statistiques ofas
     * 
     * @param idExecutionProcess
     * @return Le chemin ou se trouve le fichier
     * @throws StatistiquesOFASException
     */
    public String genereateFileStatOFAS(String idExecutionProcess) throws StatistiquesOFASException;

    /**
     * Permet de chercher des planCalculeDemandeDroitMembreFamilleS selon le mod�le de crit�res.
     * 
     * @param DonneesFinanciere
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws StatistiquesOFASException
     */
    public PlanCalculeDemandeDroitMembreFamilleSearch search(PlanCalculeDemandeDroitMembreFamilleSearch search)
            throws JadePersistenceException, StatistiquesOFASException;

    /**
     * Retourne seulement les montant de : rente, ijai, api Retourne aussi le type de rente et le type de rente PC
     * 
     * @param search
     * @return
     * @throws JadePersistenceException
     * @throws StatistiquesOFASException
     */
    public RenteApiIjSearch search(RenteApiIjSearch search) throws JadePersistenceException, StatistiquesOFASException;

}
