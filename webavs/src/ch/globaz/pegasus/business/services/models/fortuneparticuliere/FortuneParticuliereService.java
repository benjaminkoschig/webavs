package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Betail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;

/**
 * @author DMA
 * 
 */
public interface FortuneParticuliereService extends JadeApplicationService {
    /**
     * Copy les données financières des fortune particuliere (AssuranceRenteViagere, AutreFortuneMobilier, Betail,
     * MarchandisesStock, Numeraire, PretEnversTiers, Vehicule)
     * 
     * @param newDroit
     * @param oldDroit
     * @param droitMembreFamilleSearch
     * @throws DonneeFinanciereException
     */
    public void copyFortuneParticuliere(Droit newDroit, Droit oldDroit,
            DroitMembreFamilleSearch droitMembreFamilleSearch) throws DonneeFinanciereException;

    /**
     * Permet de créer une AssuranceRenteViagere <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newAssuranceRenteViagere
     * @return AssuranceRenteViagere
     * @throws JadePersistenceException
     * @throws AssuranceRenteViagereException
     * @throws DonneeFinanciereException
     */
    public AssuranceRenteViagere createAssuranceRenteViagere(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AssuranceRenteViagere newAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException, DonneeFinanciereException;

    /**
     * 
     * Permet de créer une allocation impotente <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newAutreFortuneMobiliere
     * @return AutreFortuneMobiliere
     * @throws JadePersistenceException
     * @throws AutreFortuneMobiliereException
     * @throws DonneeFinanciereException
     */
    public AutreFortuneMobiliere createAutreFortuneMobiliere(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AutreFortuneMobiliere newAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException, DonneeFinanciereException;

    /**
     * Permet de créer une donnée financière betail <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newBetail
     * @return Betail
     * @throws JadePersistenceException
     * @throws BetailException
     * @throws DonneeFinanciereException
     */
    public Betail createBetail(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            Betail newBetail) throws JadePersistenceException, BetailException, DonneeFinanciereException;

    /**
     * Permet de créer une donnée financière marchandisesStock <b>ATTENTION en utilisant cette fonction on ne passe pas
     * par la validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé
     * en toute connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données
     * finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newMarchandisesStock
     * @return MarchandisesStock
     * @throws MarchandisesStockException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public MarchandisesStock createMarchandisesStock(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, MarchandisesStock newMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer une donnée financière numeraire <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newNumeraire
     * @return Numeraire
     * @throws JadePersistenceException
     * @throws NumeraireException
     * @throws DonneeFinanciereException
     */
    public Numeraire createNumeraire(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            Numeraire newNumeraire) throws JadePersistenceException, NumeraireException, DonneeFinanciereException;

    /**
     * Permet de créer une donnée financière pretEnversTiers <b>ATTENTION en utilisant cette fonction on ne passe pas
     * par la validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé
     * en toute connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données
     * finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newPretEnversTiers
     * @return PretEnversTiers
     * @throws PretEnversTiersException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public PretEnversTiers createPretEnversTiers(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, PretEnversTiers newPretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer une donnée financière vehicule <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param newVehicule
     * @return Vehicule
     * @throws JadePersistenceException
     * @throws VehiculeException
     * @throws DonneeFinanciereException
     */
    public Vehicule createVehicule(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            Vehicule newVehicule) throws JadePersistenceException, VehiculeException, DonneeFinanciereException;

    /**
     * Suppression des entitées "Fortune particuliere" en fonction d'une version de droit dans les données financière
     * header
     * 
     * @param idsDonneFinanciere
     * @param typeDonneFinianciere
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
