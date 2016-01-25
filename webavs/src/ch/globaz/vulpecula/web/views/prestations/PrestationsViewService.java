package ch.globaz.vulpecula.web.views.prestations;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.naos.business.model.TauxAssuranceSimpleModel;
import ch.globaz.pyxis.business.model.AdressePaiementComplexModel;
import ch.globaz.pyxis.business.model.AdressePaiementSearchComplexModel;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.congepaye.CongePayeService;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.domain.models.absencejustifiee.LienParente;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.TauxCongePaye;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.parametrage.ConfigurationSM;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.domain.models.servicemilitaire.GenreSM;
import ch.globaz.vulpecula.domain.specifications.congepaye.CPPeriodeInActivitePoste;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.AssuranceTauxComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.AssuranceConverter;
import ch.globaz.vulpecula.external.services.CotisationService;
import ch.globaz.vulpecula.external.services.musca.PassageSearchException;
import ch.globaz.vulpecula.external.services.musca.PassageService;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.util.RubriqueUtil;
import ch.globaz.vulpecula.web.gson.CotisationsGSON;

public class PrestationsViewService {

    private final CongePayeService congePayeService = VulpeculaServiceLocator.getCongePayeService();
    private final CotisationService cotisationService = VulpeculaServiceLocator.getCotisationService();
    private final PosteTravailService posteTravailService = VulpeculaServiceLocator.getPosteTravailService();

    private final BSession session = BSessionUtil.getSessionFromThreadContext();

    private final Logger LOGGER = LoggerFactory.getLogger(PrestationsViewService.class);

    public boolean checkAuMoinsUnPostePourPrestationEtTravailleur(String genrePrestation, String idTravailleur)
            throws ViewException {
        TypePrestation type = TypePrestation.fromValue(genrePrestation);
        boolean hasAuMoinsUnPoste = false;
        List<PosteTravail> postesList = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdTravailleurWithDependencies(idTravailleur);
        for (PosteTravail posteTravail : postesList) {
            if (hasAuMoinsUnPoste) {
                break;
            }

            Adhesion adh = VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(
                    posteTravail.getEmployeur().getId());
            int idCaisseMetier = Integer.valueOf(adh.getCodeAdministrationPlanCaisse());
            switch (type) {
                case ABSENCES_JUSTIFIEES:
                    if (posteTravail.hasDroitAJ(idCaisseMetier)) {
                        hasAuMoinsUnPoste = true;
                    }
                    break;
                case CONGES_PAYES:
                    if (posteTravail.hasDroitCP(idCaisseMetier)) {
                        hasAuMoinsUnPoste = true;
                    }
                    break;
                case SERVICES_MILITAIRE:
                    if (posteTravail.hasDroitSM(idCaisseMetier)) {
                        hasAuMoinsUnPoste = true;
                    }
                    break;
                default:
                    break;
            }
        }
        if (!hasAuMoinsUnPoste) {
            throw new ViewException(SpecificationMessage.SAISIE_RAPIDE_PAS_DE_POSTE);
        }
        return hasAuMoinsUnPoste;
    }

    /**
     * détermine si on doit soustraire ou ajouter les cotisations
     * Les électriciens mensuels dont le bénéficaire n'est pas travailleur doivent ajouter les cotisations
     * sinon on les soustraie.
     */
    public boolean mustSubstractCotisations(String idPosteTravail, String beneficiaire) {
        // Si electricien et employeur --> Additionne
        // Sinon --> Soustrait
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        if (RubriqueUtil.Convention.ELECTRICIEN.equals(RubriqueUtil.Convention.fromValue(poste.getEmployeur()
                .getConvention().getCode()))) {
            if (poste.isMensuel()) {
                if (!beneficiaire.equals(Beneficiaire.TRAVAILLEUR.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retourne le dernier CCP et numéro de compte d'un tiers sous forme de String pour affichage dans l'écran de saisie
     * rapide des prestations
     */
    public String getNumeroCompteForPoste(String idPosteTravail, String beneficiaire) {
        String numCompte = "";
        List<AdressePaiementComplexModel> adressesPaiements = new ArrayList<AdressePaiementComplexModel>();

        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        String idTiers = poste.getIdTiersEmployeur();

        if (Beneficiaire.TRAVAILLEUR.getValue().equals(beneficiaire)) {
            idTiers = poste.getTravailleurIdTiers();
        }

        AdressePaiementSearchComplexModel searchModel = new AdressePaiementSearchComplexModel();
        searchModel.setForIdTiers(idTiers);
        searchModel.setForDate(Date.now().getSwissValue());
        searchModel.setWhereKey("widgetSearch");
        try {
            JadePersistenceManager.search(searchModel);
            for (int i = 0; i < searchModel.getSize(); i++) {
                AdressePaiementComplexModel adressePaiementComplexModel = (AdressePaiementComplexModel) searchModel
                        .getSearchResults()[i];
                adressesPaiements.add(adressePaiementComplexModel);
            }
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }

        if (adressesPaiements.size() > 0) {
            AdressePaiementComplexModel adresse = adressesPaiements.get(0);
            if (!JadeStringUtil.isEmpty(adresse.getAdressePaiement().getNumCcp())) {
                numCompte += adresse.getAdressePaiement().getNumCcp();
            }
            if (!JadeStringUtil.isEmpty(adresse.getAdressePaiement().getNumCompteBancaire())) {
                if (!JadeStringUtil.isEmpty(numCompte)) {
                    numCompte += " / ";
                }
                numCompte += adresse.getAdressePaiement().getNumCompteBancaire();
            }
        }

        return numCompte;
    }

    /**
     * Retourne le dernier CCP et numéro de compte d'un tiers sous forme de String pour affichage dans l'écran de saisie
     * rapide des prestations
     */
    public String getNumeroCompteForTravailleur(String idTiers) {
        String numCompte = "";
        List<AdressePaiementComplexModel> adressesPaiements = new ArrayList<AdressePaiementComplexModel>();

        AdressePaiementSearchComplexModel searchModel = new AdressePaiementSearchComplexModel();
        searchModel.setForIdTiers(idTiers);
        searchModel.setForDate(Date.now().getSwissValue());
        searchModel.setWhereKey("widgetSearch");
        try {
            JadePersistenceManager.search(searchModel);
            for (int i = 0; i < searchModel.getSize(); i++) {
                AdressePaiementComplexModel adressePaiementComplexModel = (AdressePaiementComplexModel) searchModel
                        .getSearchResults()[i];
                adressesPaiements.add(adressePaiementComplexModel);
            }
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }

        if (adressesPaiements.size() > 0) {
            AdressePaiementComplexModel adresse = adressesPaiements.get(0);
            if (!JadeStringUtil.isEmpty(adresse.getAdressePaiement().getNumCcp())) {
                numCompte += adresse.getAdressePaiement().getNumCcp();
            }
            if (!JadeStringUtil.isEmpty(adresse.getAdressePaiement().getNumCompteBancaire())) {
                if (!JadeStringUtil.isEmpty(numCompte)) {
                    numCompte += " / ";
                }
                numCompte += adresse.getAdressePaiement().getNumCompteBancaire();
            }
        }

        return numCompte;
    }

    /**
     * Détermine si on doit soustraire ou ajouter l'AVS et l'AC.
     * Les électriciens horaire qui cotise au BMS doivent déduire les cotisations AVS et AC.
     */
    public boolean mustSubstractCotisationsForAJ(String idPosteTravail) {
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        if (RubriqueUtil.Convention.ELECTRICIEN.equals(RubriqueUtil.Convention.fromValue(poste.getEmployeur()
                .getConvention().getCode()))) {

            if (!poste.isMensuel()) {
                if (cotise(poste.getIdEmployeur())) {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * @param poste
     */
    private boolean cotise(String idEmployeur) {
        Taux cotise = VulpeculaServiceLocator.getCotisationService().findTauxForEmployeurAndType(idEmployeur,
                TypeAssurance.COTISATION_AVS_AI, new Date());
        if (cotise != null && !cotise.isZero()) {
            return true;
        }
        return false;
    }

    /**
     * Détermine si on doit tenir compte des cotisations pour le congé payé.
     * 
     * @throws URISyntaxException
     * @throws JAXBException
     */
    public boolean tenirCompteDesCotisations(String idPosteTravail) throws JAXBException, URISyntaxException {
        int numeroCaisse = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(idPosteTravail);
        if (numeroCaisse != PosteTravail.CAISSE_METIER_INVALIDE) {
            return TableParametrage.getInstance().hasCotisationsCongesPays(numeroCaisse);
        } else {
            return false;
        }
    }

    /**
     * Retourne le taux de congé payé par rapport à la caisse métier du poste de travail
     * 
     * 
     * @param anneeDebut
     * @param anneeFin
     * @param idPosteTravail
     * @return
     */
    public String getTauxCPParametrage(String idPosteTravail) {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        int noCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(idPosteTravail);

        if (posteTravail == null) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
        if (noCaisseMetier != 0) {
            Taux taux = new Taux(TableParametrage
                    .getInstance()
                    .getNbJoursTaux(Integer.valueOf(noCaisseMetier), posteTravail.getAgeTravailleur(),
                            posteTravail.getAnneesService()).getTaux());
            return taux.getValue();
        } else {
            return "0";
        }
    }

    /**
     * Retourne le taux de gratification par rapport à la caisse métier du poste de travail
     * 
     * 
     * @param idPosteTravail
     * @return
     */
    public String getTauxGratification(String idPosteTravail) {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        int noCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(idPosteTravail);

        if (posteTravail == null) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
        if (noCaisseMetier != 0) {
            Taux taux = new Taux(TableParametrage.getInstance().getGratification(noCaisseMetier));
            return taux.getValue();
        } else {
            return "0";
        }
    }

    /**
     * Retourne le taux de couverture APG par rapport à la caisse métier du poste de travail et le genre de SM
     * 
     * 
     * @param idPosteTravail
     * @return
     */
    public String getTauxCouvertureAPG(String idPosteTravail, String idGenreSM) {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        int noCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(idPosteTravail);

        GenreSM genreSM = GenreSM.fromValue(idGenreSM);

        if (posteTravail == null) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
        if (noCaisseMetier != 0) {
            Taux taux = TableParametrage.getInstance().getCouvertureAPG(noCaisseMetier, genreSM);
            return taux.getValue();
        } else {
            return "0";
        }
    }

    /**
     * Retourne les heures de travaillé en 1 journée selon la caisse métier du poste de travail
     * 
     * @param anneeDebut
     * @param anneeFin
     * @param idPosteTravail
     * @return double les heures travaillés par jour
     */
    public double getHeuresTravailJour(String idPosteTravail, String dateDebut) {
        if (idPosteTravail != null && idPosteTravail.length() > 0) {
            return VulpeculaServiceLocator.getPosteTravailService().getNombreHeuresParJour(idPosteTravail,
                    new Date(dateDebut));

        } else {
            return 0;
        }
    }

    /**
     * Retourne les cotisations du postes de travail pour l'ajout au CP
     * 
     * @param idPosteTravail
     * @return
     */
    public List<CotisationsGSON> getCotisationsForAffichage(String idPosteTravail, String anneeDebut,
            String beneficiaireAsString, String idCongePaye) {

        if (!congePayeService.tenirCompteDesCotisations(idPosteTravail)) {
            return new ArrayList<CotisationsGSON>();
        }

        Set<CotisationsGSON> cotiListe = new HashSet<CotisationsGSON>();
        Taux total = Taux.ZERO();
        Annee annee = new Annee(anneeDebut);
        Beneficiaire beneficiaire = Beneficiaire.fromValue(beneficiaireAsString);
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        // Si est mensualisé et bénéficiaire Employeur ou note de crédit on rajoute l'AVS et l'AC.
        if (posteTravail.isMensuel() && beneficiaire != null && !beneficiaire.equals(Beneficiaire.TRAVAILLEUR)
                && !cotise(posteTravail.getIdEmployeur())) {
            total.addTaux(addCotisationAVS(cotiListe, annee));
            total.addTaux(addCotisationAC(cotiListe, annee));
        }

        List<Cotisation> cotisations = loadCotisation(idCongePaye, annee, beneficiaire, posteTravail);
        for (Cotisation cotisation : cotisations) {
            Taux taux = cotisation.getTaux();

            if (cotisationService.isEnAgeDeCotiser(posteTravail.getTravailleur(), cotisation.getId(), anneeDebut)) { // TODO
                total = total.addTaux(taux);
                String libelle = cotisation.getAssuranceLibelle(I18NUtil.getUserLocale());
                CotisationsGSON coti = new CotisationsGSON(cotisation.getAssuranceId(), taux.getValue(), libelle,
                        cotisation.getTypeAssurance());
                cotiListe.add(coti);
            }
        }
        List<CotisationsGSON> returnList = new ArrayList<CotisationsGSON>(cotiListe);
        Collections.sort(returnList);
        return returnList;
    }

    /**
     * Retourne les cotisations du postes de travail pour l'ajout au CP
     * 
     * @param idPosteTravail
     * @return
     */
    public List<CotisationsGSON> getCotisations(String idPosteTravail, String anneeDebut, String beneficiaireAsString) {
        return getCotisationsForAffichage(idPosteTravail, anneeDebut, beneficiaireAsString, "");
    }

    /**
     * @param idCongePaye
     * @param annee
     * @param beneficiaire
     * @param posteTravail
     * @param cotisations
     * @return
     */
    private List<Cotisation> loadCotisation(String idCongePaye, Annee annee, Beneficiaire beneficiaire,
            PosteTravail posteTravail) {
        if (idCongePaye != null && idCongePaye.length() > 0) {
            CongePaye congePaye = congePayeService.findCongePayeById(idCongePaye);
            List<TauxCongePaye> listTaux = congePaye.getTauxCongePayes();

            List<Cotisation> cotisationsForCP = new ArrayList<Cotisation>();
            for (TauxCongePaye tauxCongePaye : listTaux) {
                Cotisation cotisation = new Cotisation();
                cotisation.setTaux(tauxCongePaye.getTaux());
                cotisation.setAssurance(tauxCongePaye.getAssurance());
                cotisationsForCP.add(cotisation);
            }

            return cotisationsForCP;
        } else {
            return posteTravailService.getCotisationsElectrciensForCP(posteTravail, annee, beneficiaire);
        }
    }

    /**
     * @param cotiListe
     * @param annee
     * @return
     */
    private Taux addCotisationAVS(Set<CotisationsGSON> cotiListe, Annee annee) {
        Taux txCoti = new Taux(0);
        TauxAssuranceSimpleModel tx = cotisationService.findTauxParitaireAVS(annee.getFirstDayOfYear());
        if (tx != null) {
            txCoti = new Taux(tx.getValeurEmployeur());
            String libelle = session.getLabel("JSP_COTISATIONS_PARITAIRES_AVS");
            String taux = txCoti.getValue();
            CotisationsGSON coti = new CotisationsGSON(tx.getAssuranceId(), taux, libelle,
                    TypeAssurance.COTISATION_AVS_AI);
            cotiListe.add(coti);
        }
        return txCoti;
    }

    /**
     * @param cotiListe
     * @param annee
     * @return
     */
    private Taux addCotisationAC(Set<CotisationsGSON> cotiListe, Annee annee) {
        Taux txCoti = new Taux(0);
        TauxAssuranceSimpleModel tx = cotisationService.findTauxParitaireAC(annee.getFirstDayOfYear());
        if (tx != null) {
            txCoti = new Taux(tx.getValeurEmployeur());
            String libelle = session.getLabel("JSP_COTISATIONS_PARITAIRES_AC");
            String taux = txCoti.getValue();
            CotisationsGSON coti = new CotisationsGSON(tx.getAssuranceId(), taux, libelle,
                    TypeAssurance.ASSURANCE_CHOMAGE);
            cotiListe.add(coti);
        }
        return txCoti;
    }

    /**
     * Retourne si le poste de travail dispose de compteurs actifs
     */
    public boolean hasCompteursActifs(String idPosteTravail) {
        return VulpeculaServiceLocator.getCompteurService().hasCompteurs(idPosteTravail);
    }

    /**
     * Retourne si le poste de travail dispose d'une caisse métier
     * 
     * @param idPosteTravail Id du poste de travail à contrôler
     * @return true si possède une caisse métier
     */
    public boolean hasCaisseMetier(String idPosteTravail) {
        return VulpeculaServiceLocator.getPosteTravailService().hasCaisseMetier(idPosteTravail);
    }

    /**
     * Retourne si le poste de travail dispose de lignes saisies pour la période actuelle.
     * 
     * @param idPosteTravail String id du poste de travail
     * @param anneeDebut Année de début
     * @param anneeFin Année de fin
     * @return true si des lignes existent pour la période passées en paramètre
     */
    public boolean hasLignesPourPeriode(String idPosteTravail, String anneeDebut, String anneeFin) {
        return VulpeculaServiceLocator.getCompteurService().hasLignePourPeriodeSaisie(idPosteTravail,
                new Annee(anneeDebut), new Annee(anneeFin));
    }

    /**
     * Recherche du nombre de jours accordés pour un type de prestation AJ.
     * 
     * @param idPosteTravail id du poste de travail à partir duquel le nombre d'années de service sera déterminé.
     * @param stringDate Date devant être prise en compte pour calculer l'année de service. Elle représente la date de
     *            fin pour la calculer.
     * @param csType Type de prestation AJ
     * @param csLienParente Lien de parenté dans le cas d'un deuil
     * @return Double représentant le nombre de jours accordés
     * @throws JAXBException
     * @throws URISyntaxException
     */
    public double findNombreJourPourPrestationAJ(String idPosteTravail, String stringDate, String csType,
            String csLienParente) throws JAXBException, URISyntaxException {
        TypeAbsenceJustifiee type = TypeAbsenceJustifiee.fromValue(csType);
        LienParente lien = LienParente.fromValue(csLienParente);
        int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(idPosteTravail);
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        try {
            return TableParametrage.getInstance().getNombreJoursPrestationAJ(idCaisseMetier, type, lien,
                    poste.getAnneesService());
        } catch (IllegalArgumentException ex) {
            LOGGER.info(ex.toString());
            return 0;
        }
    }

    /**
     * Retourne la configuration relative à une caisse métier pour un type de prestataion SM
     * 
     * @param idPosteTravail String représentant l'id du poste de travail
     * @param csGenreSM Code système représentant un genre de service militaire
     * @return String représentant un object JSON serialisé
     * @throws JAXBException
     * @throws URISyntaxException
     */
    public ConfigurationSM findConfigurationPourPrestationSM(String idPosteTravail, String csGenreSM, String dateDebut)
            throws JAXBException, URISyntaxException {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        GenreSM genreSM = GenreSM.fromValue(csGenreSM);
        Date date = new Date(dateDebut);

        int ageTravailleur = posteTravail.getAgeTravailleur(date);
        int noCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(idPosteTravail);

        ConfigurationSM config = null;
        if (noCaisseMetier > 0) {
            config = TableParametrage.getInstance().getConfigurationSM(noCaisseMetier, genreSM, ageTravailleur);
        }
        return config;
    }

    public PeriodeContenueDansPosteNotification isPeriodeContenueDansPosteForCP(String idPosteTravail,
            String anneeDebut, String anneeFin) {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        CongePaye congePaye = new CongePaye();
        congePaye.setAnneeDebut(new Annee(anneeDebut));
        congePaye.setAnneeFin(new Annee(anneeFin));
        congePaye.setPosteTravail(posteTravail);
        boolean contenuDansPoste = new CPPeriodeInActivitePoste().isValid(congePaye);
        String message = SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, anneeDebut + "-" + anneeFin, posteTravail
                        .getPeriodeActivite().toString());
        return new PeriodeContenueDansPosteNotification(contenuDansPoste, message);
    }

    public PeriodeContenueDansPosteNotification isPeriodeContenueDansPoste(String idPosteTravail, String dateDebut,
            String dateFin) {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        Periode periodeAJ = new Periode(dateDebut, dateFin);
        boolean contenuDansPoste = posteTravail.getPeriodeActivite().contains(periodeAJ);
        String message = SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, periodeAJ.toString(), posteTravail
                        .getPeriodeActivite().toString());
        return new PeriodeContenueDansPosteNotification(contenuDansPoste, message);
    }

    /**
     * Retourne si le poste de travail a le droit aux prestations AJ durant la période allant de la date de début à la
     * date de fin.
     * 
     * @param idPosteTravail String représentant l'id d'un poste de travail
     * @param debut Date de début de la prestation
     * @param fin Date de fin de la prestation
     * @return Messages d'erreur à afficher à l'écran
     */
    public ErrorMessages isValidForAJ(String idPosteTravail, String debut, String fin) {
        return isValidForX(idPosteTravail, debut, fin, TypePrestation.ABSENCES_JUSTIFIEES);
    }

    /**
     * Retourne si le poste de travail a le droit aux prestations SM durant la période allant de la date de début à la
     * date de fin.
     * 
     * @param idPosteTravail String représentant l'id d'un poste de travail
     * @param debut Date de début de la prestation
     * @param fin Date de fin de la prestation
     * @return Messages d'erreur à afficher à l'écran
     */
    public ErrorMessages isValidForSM(String idPosteTravail, String debut, String fin) {
        return isValidForX(idPosteTravail, debut, fin, TypePrestation.SERVICES_MILITAIRE);
    }

    /**
     * Retourne si le poste de travail a le droit aux prestations X durant la période allant de la date de début à la
     * date de fin.
     * 
     * @param idPosteTravail String représentant l'id d'un poste de travail
     * @param debut Date de début de la prestation
     * @param fin Date de fin de la prestation
     * @return Messages d'erreur à afficher à l'écran
     */
    public ErrorMessages isValidForX(String idPosteTravail, String debut, String fin, TypePrestation typePrestation) {
        ErrorMessages errorMessages = new ErrorMessages();

        Date dateDebut = new Date(debut);
        Date dateFin = new Date(fin);
        Periode periodeAJ = new Periode(dateDebut, dateFin);

        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        boolean assuranceActive = posteTravailService.hasAssuranceActiveForX(posteTravail, dateDebut, dateFin,
                typePrestation);
        boolean periodeContenueDansPoste = posteTravail.getPeriodeActivite().contains(periodeAJ);

        if (!periodeContenueDansPoste) {
            String message = SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                    SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, periodeAJ.toString(), posteTravail
                            .getPeriodeActivite().toString());
            errorMessages.addMessage(message);
        }

        if (!assuranceActive) {
            String message = SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                    SpecificationMessage.PRESTATION_ASSURANCE_NON_ACTIVE);
            errorMessages.addMessage(message);
        }

        return errorMessages;
    }

    public PeriodeContenueDansPosteNotification isPeriodeAnnuelleContenueDansPoste(String idPosteTravail,
            String anneeDebut, String anneeFin) {
        Date dateDebut = new Annee(anneeDebut).getFirstDayOfYear();
        Date dateFin = new Annee(anneeFin).getFirstDayOfYear();
        return isPeriodeContenueDansPoste(idPosteTravail, dateDebut.getSwissValue(), dateFin.getSwissValue());
    }

    public PrestationsInfo getPrestationsInfos(String typePrestation) throws PassageSearchException {
        TypePrestation prestation = TypePrestation.fromValue(typePrestation);
        PassageService passageService = VulpeculaServiceLocator.getPassageService();
        Passage passage = passageService.findPassageActif(prestation);
        switch (prestation) {
            case ABSENCES_JUSTIFIEES:
                return new PrestationsInfo(passage, getBeneficiairesForAJ());
            case CONGES_PAYES:
                return new PrestationsInfo(passage, getBeneficiairesForCP());
            case SERVICES_MILITAIRE:
                return new PrestationsInfo(passage, getBeneficiairesForSM());
            default:
                throw new IllegalArgumentException("Type prestation : " + typePrestation + " non valide");
        }
    }

    private static class PrestationsInfo {
        final Passage passage;
        final List<CodeSystem> beneficiaires;

        public PrestationsInfo(Passage passage, List<CodeSystem> beneficiaires) {
            this.passage = passage;
            this.beneficiaires = beneficiaires;
        }
    }

    private static class PeriodeContenueDansPosteNotification {
        final boolean value;
        final String message;

        public PeriodeContenueDansPosteNotification(boolean value, String message) {
            this.value = value;
            this.message = message;
        }
    }

    private static class ErrorMessages {
        List<String> messages = new ArrayList<String>();
        boolean valid = true;

        void addMessage(String message) {
            valid = false;
            messages.add(message);
        }
    }

    /**
     * Retourne tous les postes d'un travailleur qui ont le droit à la prestation
     */
    public List<PosteTravail> findPostesAyantDroitsPrestationsPourTravailleur(String idTravailleur,
            String genrePrestation) {
        List<PosteTravail> listeTousPostes = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdTravailleur(idTravailleur);
        List<PosteTravail> listePostesPossibles = new ArrayList<PosteTravail>();
        TypePrestation prestation = TypePrestation.fromValue(genrePrestation);

        for (PosteTravail posteTravail : listeTousPostes) {
            int idCaisseMetier = Integer.valueOf((VulpeculaRepositoryLocator.getAdhesionRepository()
                    .findCaisseMetier(posteTravail.getEmployeur().getId())).getIdTiersAdministration());
            switch (prestation) {
                case ABSENCES_JUSTIFIEES:
                    if (posteTravail.hasDroitAJ(idCaisseMetier)) {
                        listePostesPossibles.add(posteTravail);
                    }
                    break;
                case CONGES_PAYES:
                    if (posteTravail.hasDroitCP(idCaisseMetier)) {
                        listePostesPossibles.add(posteTravail);
                    }
                    break;
                case SERVICES_MILITAIRE:
                    if (posteTravail.hasDroitSM(idCaisseMetier)) {
                        listePostesPossibles.add(posteTravail);
                    }
                    break;
                default:
                    break;
            }
        }
        return listePostesPossibles;
    }

    /**
     * Retourne les différents taux AVS et AC utilisés pour les AJ
     * 
     * @param dateAsString Date à laquelle déterminer les taux
     * @return Tableau de String. 0 contient le taux AVS, 1 contient le taux AC.
     */
    public String[] findTauxForAJ(String dateAsString) {
        Date date = new Date(dateAsString);
        String[] taux = new String[2];

        TauxAssuranceSimpleModel tauxAVS = VulpeculaServiceLocator.getCotisationService().findTauxParitaireAVS(date);
        TauxAssuranceSimpleModel tauxAC = VulpeculaServiceLocator.getCotisationService().findTauxParitaireAC(date);

        if (tauxAVS == null) {
            throw new IllegalStateException("Pas de configuration pour la cotisation AVS");
        }

        if (tauxAC == null) {
            throw new IllegalStateException("Pas de configuration pour la cotistion AC");
        }

        taux[0] = new Taux(tauxAVS.getValeurEmployeur()).getValue();
        taux[1] = new Taux(tauxAC.getValeurEmployeur()).getValue();
        return taux;
    }

    /**
     * DTO utilisé pour transférer les informations concernant les assurances.
     */
    private static class AssuranceInfos implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String idAssurance;
        public final String taux;
        public final String libelleAssurance;

        public AssuranceInfos(String idAssurance, String taux, String libelleAssurance) {
            this.idAssurance = idAssurance;
            this.taux = taux;
            this.libelleAssurance = libelleAssurance;
        }
    }

    /**
     * Retourne les assurances sur lesquels calculer le service militaire par rapport à une date afin de récupérer les
     * bons taux.
     * La méthode retourne uniquement les taux AVS/AC indépendemment des cotisations auxquels le poste de travail
     * cotisent.
     * 
     * @param date Date à laquelle déterminer les taux
     * @return Liste d'informations sur les assurances
     */
    public List<AssuranceInfos> findAssurancesInfosForSM(String date) {
        List<AssuranceInfos> assurancesInfos = new ArrayList<PrestationsViewService.AssuranceInfos>();

        CotisationService cotisationService = VulpeculaServiceLocator.getCotisationService();

        Locale locale = I18NUtil.getUserLocale();

        AssuranceTauxComplexModel assuranceTauxAVS = cotisationService.findAssuranceTauxParitaireAVS(new Date(date));
        if (assuranceTauxAVS != null) {
            Assurance assuranceAVS = AssuranceConverter.convertToDomain(assuranceTauxAVS.getAssuranceSimpleModel());
            AssuranceInfos infosAVS = new AssuranceInfos(assuranceAVS.getId(), new Taux(assuranceTauxAVS
                    .getTauxAssuranceSimpleModel().getValeurEmployeur()).getValue(), assuranceAVS.getLibelle(locale));
            assurancesInfos.add(infosAVS);
        }

        AssuranceTauxComplexModel assuranceTauxAC = cotisationService.findAssuranceTauxParitaireAC(new Date(date));
        if (assuranceTauxAC != null) {
            Assurance assuranceAC = AssuranceConverter.convertToDomain(assuranceTauxAC.getAssuranceSimpleModel());
            AssuranceInfos infosAC = new AssuranceInfos(assuranceAC.getId(), new Taux(assuranceTauxAC
                    .getTauxAssuranceSimpleModel().getValeurEmployeur()).getValue(), assuranceAC.getLibelle(locale));
            assurancesInfos.add(infosAC);
        }

        return assurancesInfos;
    }

    public static List<CodeSystem> getBeneficiairesForAJ() {
        return CodeSystemUtil.getCodeSystem(Beneficiaire.getBeneficiairesForAJ(), I18NUtil.getLangues());
    }

    public static List<CodeSystem> getBeneficiairesForCP() {
        return CodeSystemUtil.getCodeSystem(Beneficiaire.getBeneficiairesForCP(), I18NUtil.getLangues());
    }

    public static List<CodeSystem> getBeneficiairesForSM() {
        return CodeSystemUtil.getCodeSystem(Beneficiaire.getBeneficiairesForSM(), I18NUtil.getLangues());
    }
}
