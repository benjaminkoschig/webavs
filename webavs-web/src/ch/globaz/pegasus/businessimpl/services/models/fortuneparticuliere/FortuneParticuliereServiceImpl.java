package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
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
import ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliereSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.FortuneParticuliereService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;

public class FortuneParticuliereServiceImpl extends PegasusAbstractServiceImpl implements FortuneParticuliereService {

    private AssuranceRenteViagere copyAssuranceRenteViagere(FortuneParticuliere fortuneParticuliere, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AssuranceRenteViagere assuranceRenteViagere = new AssuranceRenteViagere();
        try {
            assuranceRenteViagere
                    .setSimpleDonneeFinanciereHeader(fortuneParticuliere.getSimpleDonneeFinanciereHeader());
            assuranceRenteViagere.setSimpleAssuranceRenteViagere(fortuneParticuliere.getAssuranceRenteViagere()
                    .getSimpleAssuranceRenteViagere());
            assuranceRenteViagere.setCompagnie(fortuneParticuliere.getAssuranceRenteViagere().getCompagnie());
            assuranceRenteViagere = createAssuranceRenteViagere(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    assuranceRenteViagere);
            return assuranceRenteViagere;
        } catch (AssuranceRenteViagereException e) {
            throw new DonneeFinanciereException("Unable to copy the assuranceRenteViagere", e);
        }
    }

    private AutreFortuneMobiliere copyAutreFortuneMobiliere(FortuneParticuliere fortuneParticuliere, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AutreFortuneMobiliere autreFortuneMobiliere = new AutreFortuneMobiliere();
        try {
            autreFortuneMobiliere
                    .setSimpleDonneeFinanciereHeader(fortuneParticuliere.getSimpleDonneeFinanciereHeader());
            autreFortuneMobiliere.setSimpleAutreFortuneMobiliere(fortuneParticuliere.getSimpleAutreFortuneMobiliere());
            autreFortuneMobiliere = createAutreFortuneMobiliere(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    autreFortuneMobiliere);
            return autreFortuneMobiliere;
        } catch (AutreFortuneMobiliereException e) {
            throw new DonneeFinanciereException("Unable to copy the autreFortuneMobiliere", e);
        }
    }

    private Betail copyBetail(FortuneParticuliere fortuneParticuliere, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Betail betail = new Betail();
        try {
            betail.setSimpleDonneeFinanciereHeader(fortuneParticuliere.getSimpleDonneeFinanciereHeader());
            betail.setSimpleBetail(fortuneParticuliere.getSimpleBetail());
            betail = createBetail(newDroit.getSimpleVersionDroit(), droitMembreFamille, betail);
            return betail;
        } catch (BetailException e) {
            throw new DonneeFinanciereException("Unable to copy the betail", e);
        }
    }

    @Override
    public void copyFortuneParticuliere(Droit newDroit, Droit oldDroit,
            DroitMembreFamilleSearch droitMembreFamilleSearch) throws DonneeFinanciereException {
        try {
            List<String> lisCsType = new ArrayList<String>();

            lisCsType.add(IPCDroits.CS_ASSURANCE_RENTE_VIAGERE);
            lisCsType.add(IPCDroits.CS_AUTRES_FORTUNES_MOBILIERES);
            lisCsType.add(IPCDroits.CS_BETAIL);
            lisCsType.add(IPCDroits.CS_MARCHANDISES_STOCK);
            lisCsType.add(IPCDroits.CS_NUMERAIRES);
            lisCsType.add(IPCDroits.CS_PRETS_ENVERS_TIERS);
            lisCsType.add(IPCDroits.CS_VEHICULE);

            FortuneParticuliereSearch searchModel = new FortuneParticuliereSearch();
            searchModel.setWhereKey(FortuneParticuliereSearch.FOR_ALL_VALABLE_LE);
            searchModel.setInCsTypeDonneeFinancierer(lisCsType);
            searchModel.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }

            searchModel = PegasusServiceLocator.getDroitService().searchFortuneParticuliere(searchModel);

            // RenteIjApi renteIjApiOld = null;
            FortuneParticuliere fortuneParticuliere = null;
            String csType = null;
            DroitMembreFamille droitMembreFamille = null;
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                try {

                    fortuneParticuliere = (FortuneParticuliere) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, fortuneParticuliere.getMembreFamilleEtendu()
                                    .getDroitMembreFamille().getSimpleDroitMembreFamille().getIdMembreFamilleSF());

                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        csType = fortuneParticuliere.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();

                        fortuneParticuliere.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils
                                .copySimpleDonneFianciere(fortuneParticuliere.getSimpleDonneeFinanciereHeader()));

                        if ((fortuneParticuliere.getAssuranceRenteViagere() != null)
                                && IPCDroits.CS_ASSURANCE_RENTE_VIAGERE.equals(csType)) {
                            copyAssuranceRenteViagere(fortuneParticuliere, newDroit, droitMembreFamille);
                        }
                        if ((fortuneParticuliere.getSimpleAutreFortuneMobiliere() != null)
                                && IPCDroits.CS_AUTRES_FORTUNES_MOBILIERES.equals(csType)) {
                            copyAutreFortuneMobiliere(fortuneParticuliere, newDroit, droitMembreFamille);
                        }
                        if ((fortuneParticuliere.getSimpleBetail() != null) && IPCDroits.CS_BETAIL.equals(csType)) {
                            copyBetail(fortuneParticuliere, newDroit, droitMembreFamille);
                        }
                        if ((fortuneParticuliere.getSimpleMarchandisesStock() != null)
                                && IPCDroits.CS_MARCHANDISES_STOCK.equals(csType)) {
                            copyMarchandisesStock(fortuneParticuliere, newDroit, droitMembreFamille);
                        }
                        if ((fortuneParticuliere.getSimpleNumeraire() != null)
                                && IPCDroits.CS_NUMERAIRES.equals(csType)) {
                            copyNumeraire(fortuneParticuliere, newDroit, droitMembreFamille);
                        }
                        if ((fortuneParticuliere.getSimplePretEnversTiers() != null)
                                && IPCDroits.CS_PRETS_ENVERS_TIERS.equals(csType)) {
                            copyPretEnversTiers(fortuneParticuliere, newDroit, droitMembreFamille);
                        }
                        if ((fortuneParticuliere.getSimpleVehicule() != null) && IPCDroits.CS_VEHICULE.equals(csType)) {
                            copyVehicule(fortuneParticuliere, newDroit, droitMembreFamille);
                        }
                    }
                } catch (JadeCloneModelException e) {
                    throw new DonneeFinanciereException("Unable to clone for the copy", e);
                }
            }
        } catch (DroitException e) {
            throw new DonneeFinanciereException("Unable to search the renteIjApi for the copy", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new DonneeFinanciereException(
                    "Unable to copy the donneeFinanciere (fortune particuliere) probleme with the persistence" + e);
        }
    }

    private MarchandisesStock copyMarchandisesStock(FortuneParticuliere fortuneParticuliere, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        MarchandisesStock betail = new MarchandisesStock();
        try {
            betail.setSimpleDonneeFinanciereHeader(fortuneParticuliere.getSimpleDonneeFinanciereHeader());
            betail.setSimpleMarchandisesStock(fortuneParticuliere.getSimpleMarchandisesStock());
            betail = createMarchandisesStock(newDroit.getSimpleVersionDroit(), droitMembreFamille, betail);
            return betail;
        } catch (MarchandisesStockException e) {
            throw new DonneeFinanciereException("Unable to copy the betail", e);
        }
    }

    private Numeraire copyNumeraire(FortuneParticuliere fortuneParticuliere, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Numeraire numeraire = new Numeraire();
        try {
            numeraire.setSimpleDonneeFinanciereHeader(fortuneParticuliere.getSimpleDonneeFinanciereHeader());
            numeraire.setSimpleNumeraire(fortuneParticuliere.getSimpleNumeraire());
            numeraire = createNumeraire(newDroit.getSimpleVersionDroit(), droitMembreFamille, numeraire);
            return numeraire;
        } catch (NumeraireException e) {
            throw new DonneeFinanciereException("Unable to copy the numeraire", e);
        }
    }

    private PretEnversTiers copyPretEnversTiers(FortuneParticuliere fortuneParticuliere, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PretEnversTiers pretEnversTiers = new PretEnversTiers();
        try {
            pretEnversTiers.setSimpleDonneeFinanciereHeader(fortuneParticuliere.getSimpleDonneeFinanciereHeader());
            pretEnversTiers.setSimplePretEnversTiers(fortuneParticuliere.getSimplePretEnversTiers());
            pretEnversTiers = createPretEnversTiers(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    pretEnversTiers);
            return pretEnversTiers;
        } catch (PretEnversTiersException e) {
            throw new DonneeFinanciereException("Unable to copy the pretEnversTiers", e);
        }
    }

    private Vehicule copyVehicule(FortuneParticuliere fortuneParticuliere, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Vehicule vehicule = new Vehicule();
        try {
            vehicule.setSimpleDonneeFinanciereHeader(fortuneParticuliere.getSimpleDonneeFinanciereHeader());
            vehicule.setSimpleVehicule(fortuneParticuliere.getSimpleVehicule());
            vehicule = createVehicule(newDroit.getSimpleVersionDroit(), droitMembreFamille, vehicule);
            return vehicule;
        } catch (VehiculeException e) {
            throw new DonneeFinanciereException("Unable to copy the vehicule", e);
        }
    }

    @Override
    public AssuranceRenteViagere createAssuranceRenteViagere(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AssuranceRenteViagere newAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AssuranceRenteViagereException(
                    "Unable to create assurance rente viagere, the droitMembreFamille is null or new");
        }
        if (newAssuranceRenteViagere == null) {
            throw new AssuranceRenteViagereException("Unable to create assurance rente viagere, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to create assurance rente viagere, the simpleVersionDroit is null");
        }

        newAssuranceRenteViagere.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_ASSURANCE_RENTE_VIAGERE);

        try {
            newAssuranceRenteViagere.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newAssuranceRenteViagere.getSimpleDonneeFinanciereHeader()));

            newAssuranceRenteViagere = PegasusImplServiceLocator.getAssuranceRenteViagereService().create(
                    newAssuranceRenteViagere);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceRenteViagereException("Service not available - " + e.getMessage());
        }
        return newAssuranceRenteViagere;
    }

    @Override
    public AutreFortuneMobiliere createAutreFortuneMobiliere(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AutreFortuneMobiliere newAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AutreFortuneMobiliereException("Unable to create betail, the droitMembreFamille is null or new");
        }
        if (newAutreFortuneMobiliere == null) {
            throw new AutreFortuneMobiliereException("Unable to create betail, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new AutreFortuneMobiliereException("Unable to create betail, the simpleVersionDroit is null");
        }

        newAutreFortuneMobiliere.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_AUTRES_FORTUNES_MOBILIERES);

        try {

            newAutreFortuneMobiliere.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newAutreFortuneMobiliere.getSimpleDonneeFinanciereHeader()));

            newAutreFortuneMobiliere = PegasusImplServiceLocator.getAutreFortuneMobiliereService().create(
                    newAutreFortuneMobiliere);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreFortuneMobiliereException("Service not available - " + e.getMessage());
        }
        return newAutreFortuneMobiliere;
    }

    @Override
    public Betail createBetail(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            Betail newBetail) throws JadePersistenceException, BetailException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new BetailException("Unable to create betail, the droitMembreFamille is null or new");
        }
        if (newBetail == null) {
            throw new BetailException("Unable to create betail, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new BetailException("Unable to create betail, the simpleVersionDroit is null");
        }

        newBetail.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_BETAIL);

        try {
            newBetail.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            newBetail.getSimpleDonneeFinanciereHeader()));
            newBetail = PegasusImplServiceLocator.getBetailService().create(newBetail);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BetailException("Service not available - " + e.getMessage());
        }
        return newBetail;
    }

    @Override
    public MarchandisesStock createMarchandisesStock(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, MarchandisesStock newMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new MarchandisesStockException(
                    "Unable to create marchandises/stock, the droitMembreFamille is null or new");
        }
        if (newMarchandisesStock == null) {
            throw new MarchandisesStockException("Unable to create marchandises/stock, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new MarchandisesStockException("Unable to create marchandises/stock, the simpleVersionDroit is null");
        }

        newMarchandisesStock.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_MARCHANDISES_STOCK);
        try {
            newMarchandisesStock.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newMarchandisesStock.getSimpleDonneeFinanciereHeader()));

            newMarchandisesStock = PegasusImplServiceLocator.getMarchandisesStockService().create(newMarchandisesStock);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new MarchandisesStockException("Service not available - " + e.getMessage());
        }
        return newMarchandisesStock;
    }

    @Override
    public Numeraire createNumeraire(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            Numeraire newNumeraire) throws JadePersistenceException, NumeraireException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new NumeraireException("Unable to create pret envers tiers, the droitMembreFamille is null or new");
        }
        if (newNumeraire == null) {
            throw new NumeraireException("Unable to create pret envers tiers, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new NumeraireException("Unable to create pret envers tiers, the simpleVersionDroit is null");
        }

        newNumeraire.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_NUMERAIRES);

        try {
            newNumeraire.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            newNumeraire.getSimpleDonneeFinanciereHeader()));

            newNumeraire = PegasusImplServiceLocator.getNumeraireService().create(newNumeraire);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new NumeraireException("Service not available - " + e.getMessage());
        }
        return newNumeraire;
    }

    @Override
    public PretEnversTiers createPretEnversTiers(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, PretEnversTiers newPretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new PretEnversTiersException(
                    "Unable to create pret envers tiers, the droitMembreFamille is null or new");
        }
        if (newPretEnversTiers == null) {
            throw new PretEnversTiersException("Unable to create pret envers tiers, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new PretEnversTiersException("Unable to create pret envers tiers, the simpleVersionDroit is null");
        }

        newPretEnversTiers.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_PRETS_ENVERS_TIERS);

        try {
            newPretEnversTiers.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newPretEnversTiers.getSimpleDonneeFinanciereHeader()));

            newPretEnversTiers = PegasusImplServiceLocator.getPretEnversTiersService().create(newPretEnversTiers);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PretEnversTiersException("Service not available - " + e.getMessage());
        }
        return newPretEnversTiers;
    }

    @Override
    public Vehicule createVehicule(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            Vehicule newVehicule) throws JadePersistenceException, VehiculeException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new VehiculeException("Unable to create betail, the droitMembreFamille is null or new");
        }
        if (newVehicule == null) {
            throw new VehiculeException("Unable to create betail, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new VehiculeException("Unable to create betail, the simpleVersionDroit is null");
        }

        newVehicule.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_VEHICULE);

        try {
            newVehicule.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            newVehicule.getSimpleDonneeFinanciereHeader()));

            newVehicule = PegasusImplServiceLocator.getVehiculeService().create(newVehicule);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VehiculeException("Service not available - " + e.getMessage());
        }
        return newVehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * FortuneParticuliereService#deleteParListeIdDoFinH(java.util.List, java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_PRETS_ENVERS_TIERS + "-")) {
            PegasusImplServiceLocator.getSimplePretEnversTiersService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_ASSURANCE_RENTE_VIAGERE + "-")) {
            PegasusImplServiceLocator.getSimpleAssuranceRenteViagereService()
                    .deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_NUMERAIRES + "-")) {
            PegasusImplServiceLocator.getSimpleNumeraireService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_MARCHANDISES_STOCK + "-")) {
            PegasusImplServiceLocator.getSimpleMarchandisesStockService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_VEHICULE + "-")) {
            PegasusImplServiceLocator.getSimpleVehiculeService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_BETAIL + "-")) {
            PegasusImplServiceLocator.getSimpleBetailService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_AUTRES_FORTUNES_MOBILIERES + "-")) {
            PegasusImplServiceLocator.getSimpleAutreFortuneMobiliereService()
                    .deleteParListeIdDoFinH(idsDonneFinanciere);
        }
    }

}
