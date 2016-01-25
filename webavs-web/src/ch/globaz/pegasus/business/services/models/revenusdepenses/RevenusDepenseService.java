package ch.globaz.pegasus.business.services.models.revenusdepenses;

/**
 * 
 * @author DMA
 * @date 5 nov. 2010
 */
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenus;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique;

public interface RevenusDepenseService extends JadeApplicationService {

    public void copyRevenusDepense(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException;

    /**
     * Permet de créer une AllocationsFamiliales <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param allocationsFamiliales
     * @return AllocationsFamiliales
     * @throws AllocationsFamilialesException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public AllocationsFamiliales createAllocationsFamiliales(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un AutresRevenus <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du
     * droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance
     * de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param autresRevenus
     * @return
     * @throws AutresRevenusException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public AutresRevenus createAutresRevenus(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AutresRevenus autresRevenus) throws AutresRevenusException,
            JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un ContratEntretienViager <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newContratEntretienViager
     * @return ContratEntretienViager
     * @throws ContratEntretienViagerException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public ContratEntretienViager createContratEntretienViager(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, ContratEntretienViager newContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer une CotisationsPsal <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du
     * droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance
     * de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param cotisationsPsal
     * @return CotisationsPsal
     * @throws CotisationsPsalException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public CotisationsPsal createCotisationsPsal(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, CotisationsPsal cotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer une PensionAlimentaire <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation
     * du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param pensionAlimentaire
     * @return PensionAlimentaire
     * @throws PensionAlimentaireException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public PensionAlimentaire createPensionAlimentaire(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, PensionAlimentaire pensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un RevenuActiviteLucrativeDependante <b>ATTENTION en utilisant cette fonction on ne passe pas par
     * la validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en
     * toute connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données
     * finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newRevenuActiviteLucrativeDependante
     * @return RevenuActiviteLucrativeDependante
     * @throws RevenuActiviteLucrativeDependanteException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public RevenuActiviteLucrativeDependante createRevenuActiviteLucrativeDependante(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeDependante newRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un RevenuActiviteLucrativeIndependante <b>ATTENTION en utilisant cette fonction on ne passe pas
     * par la validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé
     * en toute connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données
     * finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newRevenuActiviteLucrativeIndependante
     * @return RevenuActiviteLucrativeIndependante
     * @throws RevenuActiviteLucrativeIndependanteException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public RevenuActiviteLucrativeIndependante createRevenuActiviteLucrativeIndependante(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeIndependante newRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un RevenuHypothetique <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation
     * du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newRevenuHypothetique
     * @return RevenuHypothetique
     * @throws RevenuHypothetiqueException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public RevenuHypothetique createRevenuHypothetique(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, RevenuHypothetique newRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Suppression des entitée revenuDepense en fonction d'une version de droit dans les donneé financière header
     * 
     * @param idsDonneFinanciere
     * @param typeDonneFinianciere
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws RevenuActiviteLucrativeDependanteException
     */
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RevenuActiviteLucrativeDependanteException;
}
