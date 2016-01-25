package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.home.MembreFamilleHomeException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.MembreFamilleAllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.AutreApi;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.IjApg;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.MembreFamilleAllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeFieldSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeSearch;
import ch.globaz.pegasus.business.vo.donneeFinanciere.RenteAvsAiVO;

public interface RenteIjApiService extends JadeApplicationService {
    /**
     * Copy les données financières de renteIjApi (allocationImpotant, autreApi, autreRente, IJAI, RenteAvsAI)
     * 
     * @param newDroit
     * @param oldDroit
     * @param droitMembreFamilleSearch
     * @throws DonneeFinanciereException
     */
    public void copyRenteIjApi(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException;

    /**
     * Permet de créer une allocation impotente <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitment de masse tell que la copie des données finacière.
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newAllocationImpotent
     * @return AllocationImpotent
     * @throws JadePersistenceException
     * @throws AllocationImpotentException
     * @throws DonneeFinanciereException
     */
    public AllocationImpotent createAllocationImpotent(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AllocationImpotent newAllocationImpotent)
            throws JadePersistenceException, AllocationImpotentException, DonneeFinanciereException;

    /**
     * Permet de créer une autre API <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du droit
     * (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance de
     * cause! A utiliser surtout dans des traitment de masse tell que la copie des données finacière.
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newAllocationImpotent
     * @return newAutreApi
     * @throws JadePersistenceException
     * @throws AutreApiException
     * @throws DonneeFinanciereException
     */
    public AutreApi createAutreApi(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            AutreApi newAutreApi) throws AutreApiException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer une autre Rente <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du
     * droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance
     * de cause! A utiliser surtout dans des traitment de masse tell que la copie des données finacière.
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newAutreRente
     * @return AllocationImpotent
     * @throws JadePersistenceException
     * @throws AutreRenteException
     * @throws DonneeFinanciereException
     */
    public AutreRente createAutreRente(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            AutreRente newAutreRente) throws JadePersistenceException, AutreRenteException, DonneeFinanciereException;

    /**
     * Permet de créer une ijApg <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du droit
     * (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance de
     * cause! A utiliser surtout dans des traitment de masse tell que la copie des données finacière.
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newIndemniteJournaliereAi
     * @return ijApg
     * @throws JadePersistenceException
     * @throws IjApgException
     * @throws DonneeFinanciereException
     */
    public IjApg createIjApg(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, IjApg ijApg)
            throws JadePersistenceException, DonneeFinanciereException, IjApgException;

    /**
     * Permet de créer une indemniteJournaliereAi <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitment de masse tell que la copie des données finacière.
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newIndemniteJournaliereAi
     * @return AllocationImpotent
     * @throws JadePersistenceException
     * @throws IndemniteJournaliereAiException
     * @throws DonneeFinanciereException
     */
    public IndemniteJournaliereAi createIndemniteJournaliereAi(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, IndemniteJournaliereAi newIndemniteJournaliereAi)
            throws JadePersistenceException, IndemniteJournaliereAiException, DonneeFinanciereException;

    /**
     * Permet de créer une indemniteJournaliereAi <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitment de masse tell que la copie des données finacière.
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newRenteAvsAi
     * @return AllocationImpotent
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     * @throws DonneeFinanciereException
     */
    public RenteAvsAi createRenteAvsAi(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            RenteAvsAi newRenteAvsAi) throws JadePersistenceException, RenteAvsAiException, DonneeFinanciereException;

    /**
     * Suppression des entitée renteIjApiService en fonction d'une version de droit dans les donneé financière header
     * 
     * @param idsDonneFinanciere
     * @param typeDonneFinianciere
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws RevenuActiviteLucrativeDependanteException
     */
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Retourne le degré (léger, grave, moyen) de l'allocation pour impotant. Utilisé uniquement par les RFM
     * 
     * @param idTiers L'idTiers du bénéficiaire
     * @param idVersionDroit La version du droit PC
     * @param date La date de la période du type de home
     * @return Le modèle de critère avec les résultats
     * 
     * @throws MembreFamilleHomeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     */
    public MembreFamilleAllocationImpotent retrieveDegreAllocationImpotent(String idTiers, String idVersionDroit,
            String date) throws MembreFamilleAllocationImpotentException, JadePersistenceException;

    public RenteIjApiSearch search(RenteIjApiSearch search) throws JadePersistenceException, RenteAvsAiException;

    /**
     * Retourne pour une PCA donnée, toutes les rentes AVS/AI comprises dans les données financière pour la version du
     * droit de la PCA
     * 
     * @param idPCAccodee
     * @param dateValable
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     */
    public List<RenteAvsAiVO> searchRenteAvsAiByIdPCAccordee(String idPCAccodee, String dateValable)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, RenteAvsAiException;

    /**
     * Retourne les personne qui on une rente et qui on une pcAccorde
     * 
     * 
     * @param RenteMembreFamilleCalculeSearch
     * @return
     * 
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     */
    public RenteMembreFamilleCalculeFieldSearch searchRenteMembreFamilleCalcule(
            RenteMembreFamilleCalculeFieldSearch search) throws JadePersistenceException, RenteAvsAiException;

    /**
     * Retourne les personne qui on une rente et qui on une pcAccorde
     * 
     * 
     * @param RenteMembreFamilleCalculeSearch
     * @return
     * 
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     */
    public RenteMembreFamilleCalculeSearch searchRenteMembreFamilleCalcule(RenteMembreFamilleCalculeSearch search)
            throws JadePersistenceException, RenteAvsAiException;

}
