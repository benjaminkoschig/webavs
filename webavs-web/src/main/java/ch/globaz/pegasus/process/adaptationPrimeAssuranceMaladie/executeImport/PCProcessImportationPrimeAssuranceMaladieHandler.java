package ch.globaz.pegasus.process.adaptationPrimeAssuranceMaladie.executeImport;

import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.constantes.ConstantesCalcul;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCTypePrimeAssuranceMaladie;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.droit.*;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocalite;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.calcul.PeriodesServiceImpl;
import ch.globaz.pegasus.primeassurancemaladie.PrimeAssuranceMaladieFromCSV;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.pegasus.process.adaptation.PCProcessDroitUpdateAbsract;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;


public class PCProcessImportationPrimeAssuranceMaladieHandler extends PCProcessDroitUpdateAbsract {

    private static final Logger LOG = LoggerFactory.getLogger(PCProcessImportationPrimeAssuranceMaladieHandler.class);

    private BSession bsession;
    private LinkedList<String> errors = new LinkedList();

    private Map<String, PrimeAssuranceMaladieFromCSV> listePrimeAssuranceMaladieFromCSV= null;
    private List<String> listeWarn;
    private static final int AGE_ADULTE = 26;
    private static final int AGE_JEUNE_ADULTE = 19;

    private DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("MM.yyyy")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1).toFormatter();

    private String messageAvertissementPrimeMoyenne = "Prime Moyenne d'assurance maladie ajoutée pour le(s) NSS(s) suivant(s) : ";
    private String messageAvertissementMontantExistant = "Le montant est déjà existant pour le(s) NSS(s) suivant(s) : ";



    public PCProcessImportationPrimeAssuranceMaladieHandler(Map<String, PrimeAssuranceMaladieFromCSV> listePrimeAssuranceMaladieFromCSV) {
        this.listePrimeAssuranceMaladieFromCSV = listePrimeAssuranceMaladieFromCSV;
        listeWarn = new ArrayList<>();
    }


    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        try {
            initBsession();
            fillDroitToUpdate();
            // Pour ne pas envoyer de double mail, un mail d'erreur de validation est déjà généré
            LOG.info("PCProcessImportationPrimeAssuranceMaladieHandler - Start Import - Création Prime d'assurance maladie");
            // S'il n'y pas de droit validé, alors, pas de mise à jour de prime.
            if ( Objects.nonNull(droitACalculer) && Objects.nonNull(currentDroit)) {
                updateDroitWithPrime();
            }
        } catch (AdaptationException e) {
            throw new AdaptationException(e.getMessage());
        } catch (Exception e) {
            throw new TechnicalException("Erreur dans le process d'import", e);
        } finally {
            closeBsession();
        }
    }

    /**
     * Update du droit, ajout de la prime
     *
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws AdaptationException
     * @throws ForfaitsPrimesAssuranceMaladieException
     * @throws AssuranceMaladieException
     */
    private void updateDroitWithPrime() throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException, AdaptationException, ForfaitsPrimesAssuranceMaladieException, AssuranceMaladieException {

        DroitMembreFamille requerant = null;
        ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere;
        requerant = findDroitFamilleFromIdDroit(currentDroit.getId());


        if (Objects.isNull(requerant)) {
            throw new AdaptationException("Le requérant n'a pas été trouvé.");
        }else {
            // Recherche du modificateur de droit donnee financiere
            modificateurDroitDonneeFinanciere = findModificateurDroitDonneeFinanciereFromIdDroit(currentDroit.getId());
            modificateurDroitDonneeFinanciere.getSimpleVersionDroit().setId(droitACalculer.getSimpleVersionDroit().getIdVersionDroit());

            createPrimeForMembreFamille(requerant, modificateurDroitDonneeFinanciere);

            MembreFamilleEtenduSearch membreSearch = new MembreFamilleEtenduSearch();
            membreSearch.setForIdDroit(currentDroit.getId());
            membreSearch.setOrderKey("orderByRole");
            membreSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(membreSearch);

            // Il faut maintenant ajouter la prime à l'ensemble de sa famille
            for (Object object : membreSearch.getSearchResults()) {
                MembreFamilleEtendu membreFamille = (MembreFamilleEtendu) object;

                if (!Objects.equals(membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel().trim()
                        , requerant.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel().trim())) {

                    createPrimeForMembreFamille(membreFamille.getDroitMembreFamille(), modificateurDroitDonneeFinanciere);
                }
            }

            if (!listeWarn.isEmpty()) {
                JadeProcessCommonUtils.addWarning(listeWarn.toString());
            }
        }
    }

    /**
     * Création d'une prime d'assurance maladie
     *
     *
     * @param membreFamille
     * @param modificateurDroitDonneeFinanciere
     * @throws JadePersistenceException
     * @throws AdaptationException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws ForfaitsPrimesAssuranceMaladieException
     * @throws AssuranceMaladieException
     */
    private void createPrimeForMembreFamille(DroitMembreFamille membreFamille, ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere) throws JadePersistenceException, AdaptationException, JadeApplicationServiceNotAvailableException, DroitException, ForfaitsPrimesAssuranceMaladieException, AssuranceMaladieException {
        PrimeAssuranceMaladieFromCSV assuranceMaladie;

        if (listePrimeAssuranceMaladieFromCSV.containsKey(membreFamille.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel())) {
            assuranceMaladie = createAssuranceMaladieFromCSV(membreFamille.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
        }else {
            assuranceMaladie = createAssuranceMaladieCSVFromDroitFamille(membreFamille);
        }

        // Création de la prime à persister
        PrimeAssuranceMaladie primeAssuranceMaladie = createEntityPrimeAssurancemaladieToPersist(assuranceMaladie, membreFamille);
        // On recherche la prime existante
        PrimeAssuranceMaladieSearch searchAssuranceMaladie = searchPrimeAssuranceMaladie(membreFamille);

        // Si le montant de la prime à importer est la même que celle qui existe, on ne créera pas de prime
        if (!isExistingPrimeAssuranceMaladieWithSameMontant(searchAssuranceMaladie, assuranceMaladie)) {
            boolean isCloseAndCreateNewPeriode = false;
            if (searchAssuranceMaladie.getSearchResults().length > 0) {
                LocalDate dateAdaptation = LocalDate.parse(properties.get(PCProcessAdapationEnum.DATE_ADAPTATION), formatter);
                PrimeAssuranceMaladie oldPrimeAssuranceMaladie = findPrimeFromSearchResult(searchAssuranceMaladie);
                if (Objects.nonNull(oldPrimeAssuranceMaladie)) {
                    if (LocalDate.parse(oldPrimeAssuranceMaladie.getSimpleDonneeFinanciereHeader().getDateDebut(), formatter).isBefore(dateAdaptation)) {
                        isCloseAndCreateNewPeriode = true;
                        // Ajout d'une date de fin
                        oldPrimeAssuranceMaladie.getSimpleDonneeFinanciereHeader().setDateFin(dateAdaptation.minusMonths(1).format(formatter));
                        // Update de la prime avec la date de fin
                        updateOldPrimeAssuranceMaladie(modificateurDroitDonneeFinanciere, membreFamille, oldPrimeAssuranceMaladie, assuranceMaladie.getNss());
                        primeAssuranceMaladie.setSimpleDonneeFinanciereHeader(updateDonneeFinanciereAfterUpdateOldPrime(oldPrimeAssuranceMaladie, primeAssuranceMaladie));
                    } else if (LocalDate.parse(oldPrimeAssuranceMaladie.getSimpleDonneeFinanciereHeader().getDateDebut(), formatter).isEqual(dateAdaptation)) {
                        // S'il existe une prime avec la même date que l'adaptation, il faut la supprimer avant de la recréer
                        deleteOldPrimeAssuranceMaladie(modificateurDroitDonneeFinanciere, oldPrimeAssuranceMaladie, assuranceMaladie.getNss());
                    } else {
                        throw new AdaptationException("Une prime existe déjà avec une date ultérieure à l'adaptation.");
                    }
                }
            }

            if (isCloseAndCreateNewPeriode) {
                createAndClosePrimeAssuranceMaladie(modificateurDroitDonneeFinanciere, primeAssuranceMaladie, assuranceMaladie.getNss());
            } else {
                // Création de la prime
                createPrimeAssuranceMaladie(modificateurDroitDonneeFinanciere, membreFamille, primeAssuranceMaladie, assuranceMaladie.getNss());
                // ajout d'un avertissement si le montant de la prime est la prime moyenne
            }
            if (assuranceMaladie.isPrimeMoyenne()) {
                listeWarn.add(messageAvertissementPrimeMoyenne + assuranceMaladie.getNss() + "\n");
            }
        } else {
            listeWarn.add(messageAvertissementMontantExistant + assuranceMaladie.getNss() + "\n");
        }


    }

    private PrimeAssuranceMaladieFromCSV createAssuranceMaladieFromCSV(String numAvsActuel) throws JadePersistenceException, AdaptationException, JadeApplicationServiceNotAvailableException, DroitException, ForfaitsPrimesAssuranceMaladieException {
        PrimeAssuranceMaladieFromCSV assuranceMaladieFromCSV = listePrimeAssuranceMaladieFromCSV.remove(numAvsActuel);
        if (assuranceMaladieFromCSV.getMontant().equals("0")) {
            assuranceMaladieFromCSV.setMontant(getMontantPrimeAssuranceMaladie(assuranceMaladieFromCSV, currentDroit));
        }
        return assuranceMaladieFromCSV;
    }

    private SimpleDonneeFinanciereHeader updateDonneeFinanciereAfterUpdateOldPrime(PrimeAssuranceMaladie oldPrimeAssuranceMaladie, PrimeAssuranceMaladie primeAssuranceMaladie) {
        SimpleDonneeFinanciereHeader newSimpleDonneeFinanciere = oldPrimeAssuranceMaladie.getSimpleDonneeFinanciereHeader();
        newSimpleDonneeFinanciere.setDateDebut(primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().getDateDebut());
        newSimpleDonneeFinanciere.setDateFin("");
        return newSimpleDonneeFinanciere;
    }

    /**
     * Suppression de l'ancienne prime d'assurance maladie
     *
     * @param modificateurDroitDonneeFinanciere
     * @param oldPrimeAssuranceMaladie
     * @param nss
     * @throws AdaptationException
     */
    private void deleteOldPrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere, PrimeAssuranceMaladie oldPrimeAssuranceMaladie, String nss) throws AdaptationException {
        try {
            PegasusServiceLocator.getDroitService().deletePrimeAssuranceMaladie(modificateurDroitDonneeFinanciere, oldPrimeAssuranceMaladie);

            if (bsession.getCurrentThreadTransaction().hasErrors()) {
                errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreurs de données lors de la suppression de l'ancienne prime pour le NSS : " + nss
                        + "\n" + JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR)[0].getMessageId());
                JadeThread.logClear();
            }
        } catch (Exception e) {
            errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreur lors de la suppression de la prime");
            throw new AdaptationException("Erreur lors de la suppression en DB de la prime pour le NSS : " + nss);
        }
    }

    /**
     * Recherche de la prime active en fonction d'un PrimeAssuranceMaladieSearch
     *
     * @param searchAssuranceMaladie
     * @return
     */
    private PrimeAssuranceMaladie findPrimeFromSearchResult(PrimeAssuranceMaladieSearch searchAssuranceMaladie){
        PrimeAssuranceMaladie primeAssuranceMaladie;

        for (Object object : searchAssuranceMaladie.getSearchResults()) {
            primeAssuranceMaladie = (PrimeAssuranceMaladie) object;
            if (primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().getDateFin().isEmpty()){
                return primeAssuranceMaladie;
            }
        }
        return null;
    }

    /**
     * Update de l'ancienne prime d'assurance maladie
     *
     * @param modificateurDroitDonneeFinanciere
     * @param membreFamille
     * @param primeAssuranceMaladie
     * @param nss
     * @throws AdaptationException
     */
    private void updateOldPrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere, DroitMembreFamille membreFamille, PrimeAssuranceMaladie primeAssuranceMaladie, String nss) throws AdaptationException {

        try {
            PegasusServiceLocator.getDroitService().updatePrimeAssuranceMaladie(modificateurDroitDonneeFinanciere,membreFamille,primeAssuranceMaladie);

            if (bsession.getCurrentThreadTransaction().hasErrors()) {
                errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreurs de données lors de la mise à jour de l'ancienne prime pour le NSS : " + nss
                        + "\n" + JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR)[0].getMessageId());
                JadeThread.logClear();
            }
        } catch (Exception e) {
            errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreur lors de l'update de l'ancienne prime");
            throw new AdaptationException("Erreur lors de la mise en DB de l'ancienne prime pour le NSS : " + nss);
        }

    }

    /**
     * Recherche si une prime existe avec le meme montant
     *
     * @param searchAssuranceMaladie
     * @param newPrimeAssuranceMaladie
     * @return
     */
    private boolean isExistingPrimeAssuranceMaladieWithSameMontant(PrimeAssuranceMaladieSearch searchAssuranceMaladie, PrimeAssuranceMaladieFromCSV newPrimeAssuranceMaladie) {
        if (searchAssuranceMaladie.getSearchResults().length > 0 && !newPrimeAssuranceMaladie.getMontant().isEmpty()) {
            for(Object object : searchAssuranceMaladie.getSearchResults()) {
                PrimeAssuranceMaladie primeAssuranceMaladieExistante = (PrimeAssuranceMaladie) object;
                // Si l'on trouve une prime assurance avec le même montant
                // Alors pas d'ecriture de prime
                if (new Float(primeAssuranceMaladieExistante.getSimplePrimeAssuranceMaladie().getMontant()).equals(new Float(newPrimeAssuranceMaladie.getMontant()))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recherche les primes d'assurance maladie d'un requerant
     *
     * @param requerant
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     * @throws DroitException
     */
    private PrimeAssuranceMaladieSearch searchPrimeAssuranceMaladie(DroitMembreFamille requerant) throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AssuranceMaladieException, DroitException {
        PrimeAssuranceMaladieSearch searchAssuranceMaladie = new PrimeAssuranceMaladieSearch();
        searchAssuranceMaladie.setIdDroitMembreFamille(requerant.getSimpleDroitMembreFamille().getIdDroitMembreFamille());
        VersionDroit versionDroit = getVersionDroit();
        if (Objects.nonNull(versionDroit)){
            searchAssuranceMaladie.setForNumeroVersion(versionDroit.getSimpleVersionDroit().getNoVersion());
            searchAssuranceMaladie.setWhereKey("forVersionedPrimeAssuranceMaladie");
        }
        searchAssuranceMaladie = PegasusServiceLocator.getAssuranceMaladieService().searchPrimeAssuranceMaladie(searchAssuranceMaladie);

        return searchAssuranceMaladie;
    }

    /**
     * Création d'une prime avec les informations contenu du CSV
     *
     * @param droitMembreFamille
     * @return
     * @throws JadePersistenceException
     * @throws AdaptationException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws ForfaitsPrimesAssuranceMaladieException
     */
    private PrimeAssuranceMaladieFromCSV createAssuranceMaladieCSVFromDroitFamille(DroitMembreFamille droitMembreFamille) throws JadePersistenceException, AdaptationException, JadeApplicationServiceNotAvailableException, DroitException, ForfaitsPrimesAssuranceMaladieException {

        PrimeAssuranceMaladieFromCSV assuranceMaladie = new PrimeAssuranceMaladieFromCSV();
        assuranceMaladie.setNss(droitMembreFamille.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
        assuranceMaladie.setNom(droitMembreFamille.getMembreFamille().getNom());
        assuranceMaladie.setPrenom(droitMembreFamille.getMembreFamille().getPrenom());
        assuranceMaladie.setDateNaissance(droitMembreFamille.getMembreFamille().getDateNaissance());
        assuranceMaladie.setMontant(getMontantPrimeAssuranceMaladie(assuranceMaladie, currentDroit));
        return assuranceMaladie;
    }

    /**
     *
     *
     * @param modificateurDroitDonneeFinanciere
     * @param primeAssuranceMaladie
     * @param nss
     * @throws AdaptationException
     */
    private void createAndClosePrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere, PrimeAssuranceMaladie primeAssuranceMaladie, String nss) throws AdaptationException {
        try {
            PegasusServiceLocator.getDroitService().createAndClosePrimeAssuranceMaladie(modificateurDroitDonneeFinanciere, primeAssuranceMaladie, false);
        } catch (DonneeFinanciereException e) {
            if (bsession.getCurrentThreadTransaction().hasErrors()) {
                errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreurs de données lors de la persistence de la prime pour le NSS : " + nss
                        + "\n" + JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR)[0].getMessageId());
                JadeThread.logClear();
            }
        } catch (Exception e) {
            errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreur lors de la persistence de la prime");
            throw new AdaptationException("Erreur lors de la mise en DB de la prime pour le NSS : " + nss);
        }
    }

    /**
     * Création de la prime en DB
     *
     * @param modificateurDroitDonneeFinanciere
     * @param requerant
     * @param primeAssuranceMaladie
     * @param nss
     * @throws AdaptationException
     */
    private void createPrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere, DroitMembreFamille requerant, PrimeAssuranceMaladie primeAssuranceMaladie, String nss) throws AdaptationException {
        try {
            PegasusServiceLocator.getDroitService().createPrimeAssuranceMaladie(modificateurDroitDonneeFinanciere,
                    requerant, primeAssuranceMaladie);
            if (bsession.getCurrentThreadTransaction().hasErrors()) {
                errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreurs de données lors de la persistence de la prime pour le NSS : " + nss
                        + "\n" + JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR)[0].getMessageId());
                JadeThread.logClear();
            }
        } catch (Exception e) {
            errors.add("PCProcessImportationPrimeAssuranceMaladieHandler#importFile : Erreur lors de la persistence de la prime");
            throw new AdaptationException("Erreur lors de la mise en DB de la prime pour le NSS : " + nss);
        }
    }

    /**
     * Création de l'objet PrimeAssuranceMaladie pour mise en DB
     *
     * @param assuranceMaladie
     * @param requerant
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws JadePersistenceException
     */
    private PrimeAssuranceMaladie createEntityPrimeAssurancemaladieToPersist(PrimeAssuranceMaladieFromCSV assuranceMaladie, DroitMembreFamille requerant) throws JadeApplicationServiceNotAvailableException
            , DroitException, JadePersistenceException {
        PrimeAssuranceMaladie primeAssuranceMaladie = new PrimeAssuranceMaladie();
        SimplePrimeAssuranceMaladie simplePrime = new SimplePrimeAssuranceMaladie();
        SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();

        VersionDroit versionDroit = getVersionDroit();

        // Chargement de la prime
        simplePrime.setMontant(assuranceMaladie.getMontant());
        // Chargement de la donnee financiere
        simpleDonneeFinanciereHeader.setDateDebut(properties.get(PCProcessAdapationEnum.DATE_ADAPTATION));
        if (Objects.nonNull(versionDroit)) {
            simpleDonneeFinanciereHeader.setIdVersionDroit(versionDroit.getId());
        }
        simpleDonneeFinanciereHeader.setIdDroitMembreFamille(requerant.getMembreFamille().getId());

        // Chargement de l'entity
        primeAssuranceMaladie.setSimplePrimeAssuranceMaladie(simplePrime);
        primeAssuranceMaladie.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);

        return primeAssuranceMaladie;
    }

    /**
     * Récupére la version d'un droit
     *
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws JadePersistenceException
     */
    private VersionDroit getVersionDroit() throws JadeApplicationServiceNotAvailableException, DroitException, JadePersistenceException {
        VersionDroitSearch searchVersionDroit = new VersionDroitSearch();
        searchVersionDroit.setForIdDemandePc(getEntity().getIdRef());
        searchVersionDroit.setWhereKey(VersionDroitSearch.CURRENT_VERSION);
        searchVersionDroit = PegasusServiceLocator.getDroitService().searchVersionDroit(searchVersionDroit);

        if (searchVersionDroit.getSearchResults().length == 1) {
            return (VersionDroit) searchVersionDroit.getSearchResults()[0];
        } else {
            return null;
        }
    }


    /**
     * Méthode qui va vérifier le montant de la prime
     *
     * si Montant vide -> Prime Moyenne
     * si Montant > Prime Mmoyenne -> Prime Moyenne
     * si Montant < Prime Moyenne -> Montant
     *
     * @param assuranceMaladieFromCSV
     * @return
     */
    private String getMontantPrimeAssuranceMaladie(PrimeAssuranceMaladieFromCSV assuranceMaladieFromCSV, Droit droit) throws JadeApplicationServiceNotAvailableException, DroitException, JadePersistenceException, ForfaitsPrimesAssuranceMaladieException, AdaptationException {

        String montant;
        String dateDebut = "01."+properties.get(PCProcessAdapationEnum.DATE_ADAPTATION);

        // récupère primes moyennes assurance maladie
        DonneesPersonnellesSearch donneesPersonnellesSearch = new DonneesPersonnellesSearch();
        donneesPersonnellesSearch.setForIdDroit(droit.getSimpleDroit().getId());
        donneesPersonnellesSearch.setWhereKey(DonneesPersonnellesSearch.FOR_DROIT);
        donneesPersonnellesSearch = PegasusServiceLocator.getDroitService().searchDonneesPersonnelles(
                donneesPersonnellesSearch);

        Map<String, JadeAbstractSearchModel> result = new HashMap<>();

        try {
            PeriodesServiceImpl.getPrimeLamal(droit, dateDebut, dateDebut, result, donneesPersonnellesSearch);
        } catch (CalculException e) {
            throw new AdaptationException("La prime moyenne n'a pu être défini pour la date du " + e.getParameters()[1] + " pour la localité :" + e.getParameters()[0], e);
        }

        int age = 0;
        try {
            age = calculAge(assuranceMaladieFromCSV, dateDebut);
        } catch (ParseException e) {
            throw new AdaptationException("Erreur de format de la date de Naissance pour le NSS : " + assuranceMaladieFromCSV.getNss());
        }


        String montantPrimeMoyenne = "";

        ForfaitPrimeAssuranceMaladieLocaliteSearch primeAssuranceMoyenneSearch = (ForfaitPrimeAssuranceMaladieLocaliteSearch) result.get(ConstantesCalcul.CONTAINTER_PRIME_MOYENNE_ASSURANCE_MALADIE);

        for (Object object : primeAssuranceMoyenneSearch.getSearchResults()) {
            ForfaitPrimeAssuranceMaladieLocalite primeAssuranceMoyenne = (ForfaitPrimeAssuranceMaladieLocalite) object;
            if (IPCTypePrimeAssuranceMaladie.CS_TYPE_PRIME_ENFANT.equals(primeAssuranceMoyenne.getSimpleForfaitPrimesAssuranceMaladie().getCsTypePrime())
                    && (age < AGE_JEUNE_ADULTE)) {
                montantPrimeMoyenne = primeAssuranceMoyenne.getSimpleForfaitPrimesAssuranceMaladie().getMontantPrimeMoy();
            }

            if (IPCTypePrimeAssuranceMaladie.CS_TYPE_PRIME_JEUNE_ADULTE.equals(primeAssuranceMoyenne.getSimpleForfaitPrimesAssuranceMaladie().getCsTypePrime())
                    && (age < AGE_ADULTE) && (age >= AGE_JEUNE_ADULTE)) {
                montantPrimeMoyenne = primeAssuranceMoyenne.getSimpleForfaitPrimesAssuranceMaladie().getMontantPrimeMoy();
            }

            if (IPCTypePrimeAssuranceMaladie.CS_TYPE_PRIME_ADULTE.equals(primeAssuranceMoyenne.getSimpleForfaitPrimesAssuranceMaladie().getCsTypePrime()) && (age >= AGE_ADULTE)) {
                montantPrimeMoyenne = primeAssuranceMoyenne.getSimpleForfaitPrimesAssuranceMaladie().getMontantPrimeMoy();
            }
        }

        montant = String.valueOf(Float.valueOf(montantPrimeMoyenne) / 12);
        assuranceMaladieFromCSV.setPrimeMoyenne(true);


        return montant;
    }

    /**
     * Méthode de calcul d'age
     *
     * @param assuranceMaladieFromCSV
     * @param dateDebut
     * @return
     * @throws ParseException
     */
    private int calculAge(PrimeAssuranceMaladieFromCSV assuranceMaladieFromCSV, String dateDebut) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formater2 = new SimpleDateFormat("dd.MM.yyyy");

        Calendar calPeriode = Calendar.getInstance();
        calPeriode.setTime(formater2.parse(dateDebut));
        Date dateNaissance = null;
        try {
            dateNaissance = formater.parse(assuranceMaladieFromCSV.getDateNaissance());
        } catch (ParseException e) {
            // Si le premier format fait une exception, on test avec le deuxième format. Si toujours une execption, alors on la remonte
            // car le format n'est pas pris en charge.
            dateNaissance = formater2.parse(assuranceMaladieFromCSV.getDateNaissance());
        }
        Calendar calPersonne = JadeDateUtil.getGlobazCalendar(formater2.format(dateNaissance));

        return  calPeriode.get(Calendar.YEAR) - calPersonne.get(Calendar.YEAR);
    }

    /**
     * Recherche du modificateur de droit donnée financière
     *
     * @param idDroit
     * @return
     * @throws JadePersistenceException
     */
    private ModificateurDroitDonneeFinanciere findModificateurDroitDonneeFinanciereFromIdDroit(String idDroit) throws JadePersistenceException {
        ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = new ModificateurDroitDonneeFinanciere();

        modificateurDroitDonneeFinanciere.setId(idDroit);
        modificateurDroitDonneeFinanciere = (ModificateurDroitDonneeFinanciere) JadePersistenceManager.read(modificateurDroitDonneeFinanciere);

        return modificateurDroitDonneeFinanciere;
    }

    /**
     * Recherche droit famille avec l'idDroit
     *
     * @param idDroit
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws JadePersistenceException
     */
    private DroitMembreFamille findDroitFamilleFromIdDroit(String idDroit) throws JadeApplicationServiceNotAvailableException, DroitException, JadePersistenceException {
        DroitMembreFamille requerant = null;
        MembreFamilleEtenduSearch membreSearch = new MembreFamilleEtenduSearch();
        membreSearch.setForIdDroit(idDroit);
        membreSearch.setOrderKey("orderByRole");
        membreSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(membreSearch);
        for (Iterator it = Arrays.asList(membreSearch.getSearchResults()).iterator(); it.hasNext(); ) {
            MembreFamilleEtendu membre = (MembreFamilleEtendu) it.next();
            List listeMembres = new ArrayList();
            listeMembres.add(membre);

            if (Objects.isNull(requerant) && membre.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC()
                    .equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
                requerant = membre.getDroitMembreFamille();
            }
        }

        return requerant;
    }


    private void initBsession() throws Exception {
        bsession = BSessionUtil.getSessionFromThreadContext();
        BSessionUtil.initContext(bsession, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    public Map<String, PrimeAssuranceMaladieFromCSV> getListePrimeAssuranceMaladieFromCSV() {
        return listePrimeAssuranceMaladieFromCSV;
    }

    public void setListePrimeAssuranceMaladieFromCSV(Map<String, PrimeAssuranceMaladieFromCSV> listePrimeAssuranceMaladieFromCSV) {
        this.listePrimeAssuranceMaladieFromCSV = listePrimeAssuranceMaladieFromCSV;
    }
}
