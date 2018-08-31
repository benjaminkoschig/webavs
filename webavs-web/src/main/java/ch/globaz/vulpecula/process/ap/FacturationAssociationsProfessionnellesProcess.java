package ch.globaz.vulpecula.process.ap;

import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModuleManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModel;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModelSearch;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.registre.CotisationAssociationProfessionnelleService;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationEmployeur;
import ch.globaz.vulpecula.domain.models.association.AssociationFacture;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.association.CotisationFacture;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import ch.globaz.vulpecula.domain.models.association.FacturesAssociations;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.models.registre.TypeParamCotisationAP;
import ch.globaz.vulpecula.domain.repositories.association.AssociationCotisationRepository;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.affiliation.Particularite;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteRepositoryJade.LoadOptions;
import com.sun.star.lang.IllegalArgumentException;

public class FacturationAssociationsProfessionnellesProcess extends BProcessWithContext {
    private static final long serialVersionUID = 4673022589446026422L;
    private String idEmployeur = "";
    private String idAssociationProfessionnelle = "";
    private String annee = "";
    private String idPassage = "";
    private Boolean replaceFacture = false;
    private GenreCotisationAssociationProfessionnelle genre;
    private TreeMap<String, ListePourImpression> facturesOrderByRaisonSociale = new TreeMap<String, ListePourImpression>();
    boolean isOnError = false;
    private static final String GENRE_ASSOCIATION_PROFESSIONNELLE = "68900004";
    private static final String TYPE_CPP = "507014";

    private String associationLibelle = "";
    private String employeurLibelle = "";
    private String passageLibelle = "";

    private AssociationCotisationRepository associationCotisationRepository;
    private CotisationAssociationProfessionnelleService cotisationAssociationProfessionnelleService;

    private String erreurProcess = "";
    private StringBuffer parametresManquants = new StringBuffer();

    List<ParametreCotisationAssociation> parametres = new ArrayList<ParametreCotisationAssociation>();

    public static class ListePourImpression {
        private String idEmployeur;
        private Annee annee;
        private EtatFactureAP etat;
        private String idAssociation;
        private String message;
        private Montant masse;
        private Montant montantTotal;
        private static TreeMap<String, Montant> cotisationsMontant = new TreeMap<String, Montant>();
        private static TreeMap<String, String> cotisationsLabels = new TreeMap<String, String>();

        public static void resetCotisationsTrees() {
            cotisationsMontant = new TreeMap<String, Montant>();
            cotisationsLabels = new TreeMap<String, String>();
        }

        /**
         * @return the cotisationsLabels
         */
        public static TreeMap<String, String> getCotisationsLabels() {
            return cotisationsLabels;
        }

        public static String getCotisationsLabels(String cotisationKey) {
            return cotisationsLabels.get(cotisationKey);
        }

        /**
         * @return the cotisationLabels
         */
        public void addCotisationLabel(String cotisationKey, String cotisationLabel) {
            cotisationsLabels.put(cotisationKey, cotisationLabel);
        }

        public void addCotisationMontant(String cotisationKey, Montant cotisationTotal) {
            cotisationsMontant.put(cotisationKey, cotisationTotal);
        }

        /**
         * @return the cotisationsMontant
         */
        public TreeMap<String, Montant> getCotisationsMontant() {
            return cotisationsMontant;
        }

        public Montant getCotisationsMontant(String key) {
            return cotisationsMontant.get(key);
        }

        public Montant getMasse() {
            return masse;
        }

        public void setMasse(Montant masse) {
            this.masse = masse;
        }

        public Montant getMontantTotal() {
            return montantTotal;
        }

        public void setMontantTotal(Montant montantTotal) {
            this.montantTotal = montantTotal;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ListePourImpression() {
        }

        public String getIdEmployeur() {
            return idEmployeur;
        }

        public void setIdEmployeur(String idEmployeur) {
            this.idEmployeur = idEmployeur;
        }

        public String getIdAssociation() {
            return idAssociation;
        }

        public void setIdAssociation(String idAssociation) {
            this.idAssociation = idAssociation;
        }

        public Annee getAnnee() {
            return annee;
        }

        public EtatFactureAP getEtat() {
            return etat;
        }

        public void setAnnee(Annee annee) {
            this.annee = annee;
        }

        public void setEtat(EtatFactureAP etat) {
            this.etat = etat;
        }
    }

    static class MasseCalculResult {
        private final Montant masse;
        private final EtatFactureAP etat;
        private final StringBuffer messageErreur;
        private final Montant masseCPR;

        public MasseCalculResult(Montant masse, EtatFactureAP etat, Montant masseCPR) {
            this.masse = masse;
            this.etat = etat;
            messageErreur = new StringBuffer();
            this.masseCPR = masseCPR;
        }

        public MasseCalculResult(Montant masse, EtatFactureAP etat, StringBuffer messageErreur, Montant masseCPR) {
            this.masse = masse;
            this.etat = etat;
            this.messageErreur = messageErreur;
            this.masseCPR = masseCPR;
        }

        public Montant getMasse() {
            return masse;
        }

        public Montant getMasseCPR() {
            return masseCPR;
        }

        public EtatFactureAP getEtat() {
            return etat;
        }

        public StringBuffer getMessageErreur() {
            return messageErreur;
        }
    }

    public FacturationAssociationsProfessionnellesProcess() {
        associationCotisationRepository = VulpeculaRepositoryLocator.getAssociationCotisationRepository();
        cotisationAssociationProfessionnelleService = VulpeculaServiceLocator
                .getCotisationAssociationProfessionnelleService();
    }

    FacturationAssociationsProfessionnellesProcess(AssociationCotisationRepository associationCotisationRepository,
            CotisationAssociationProfessionnelleService cotisationAssociationProfessionnelleService) {
        this.associationCotisationRepository = associationCotisationRepository;
        this.cotisationAssociationProfessionnelleService = cotisationAssociationProfessionnelleService;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        // Il faut controler que le passage selectionné soit bien de type association professionnelle
        if (!isValidPassage()) {
            return false;
        }
        loadInfosMail();

        Annee annee = new Annee(getAnnee());
        List<String> associationsATraiter = findAsociationsToSearch(idAssociationProfessionnelle);
        List<String> listeIdEmployeurs = VulpeculaRepositoryLocator.getAssociationCotisationRepository()
                .findEmployeursForYear(getIdEmployeur(), associationsATraiter, annee, getGenre());

        createFactures(listeIdEmployeurs, annee, associationsATraiter);

        // Impression des protocoles
        if (!facturesOrderByRaisonSociale.isEmpty()) {
            ListFacturationAPExcel list = new ListFacturationAPExcel(getSession(),
                    DocumentConstants.LISTES_FACTURATION_AP, DocumentConstants.LISTES_FACTURATION_AP_DOC_NAME);
            list.setListePourImpression(facturesOrderByRaisonSociale.values());
            list.create();
            registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), list.getOutputFile());
        }

        if (isOnError) {
            return false;
        }
        return true;
    }

    /**
     * Cette methode controle que le passage de facturation soit de type association professionnelle
     * 
     * @throws Exception
     * 
     */
    private Boolean isValidPassage() throws Exception {
        FAPassageModuleManager modPassManager = new FAPassageModuleManager();
        modPassManager.setSession(getSession());
        modPassManager.setForIdPassage(getIdPassage());
        modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_ASSOCIATIONS_PROF);
        modPassManager.setForStatus(FAPassage.CS_ETAT_OUVERT);
        modPassManager.find(0);
        if (modPassManager.getSize() == 0) {
            String message = this.getClass().toString() + "\n" + getSession().getLabel("FACTURATION_AP_ERROR_PASSAGE");
            isOnError = true;
            erreurProcess = message;
            return false;
        }
        return true;
    }

    /**
     *
     */
    private void loadInfosMail() {
        if (!JadeStringUtil.isEmpty(getIdAssociationProfessionnelle())) {
            Administration association = VulpeculaRepositoryLocator.getAdministrationRepository().findById(
                    getIdAssociationProfessionnelle());
            associationLibelle = association.getCodeAdministration();
        }

        if (!JadeStringUtil.isEmpty(getIdEmployeur())) {
            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(idEmployeur);
            employeurLibelle = employeur.getAffilieNumero() + " " + employeur.getDesignation1();
        }

        if (!JadeStringUtil.isEmpty(getIdPassage())) {
            passageLibelle = getIdPassage();
        }
    }

    /**
     * Retourne les associations professionnelles à rechercher.
     * Si l'on sélectionne un enfant, on prendra alors ses parents.
     * Si l'on sélectionne un parent, on prendra alors tous ses enfants.
     * 
     * @return Liste d'id d'association professionnelle (id administration)
     */
    private List<String> findAsociationsToSearch(String idAssociation) {

        List<String> ids = new ArrayList<String>();
        if (!idAssociation.isEmpty()) {
            ids.add(idAssociation);
            if (!GENRE_ASSOCIATION_PROFESSIONNELLE.equals(getGenre().getValue())) {
                CompositionTiersSimpleModelSearch search = new CompositionTiersSimpleModelSearch();
                search.setForIdTiersEnfant(idAssociation);
                // search.setForTypeLien(TYPE_CPP);
                List<CompositionTiersSimpleModel> listeComposition = RepositoryJade.searchForAndFetch(search);
                if (!listeComposition.isEmpty()) {
                    for (CompositionTiersSimpleModel composition : listeComposition) {
                        ids.add(composition.getIdTiersParent());
                    }
                }
            }
        }
        return ids;
    }

    private void createFactures(List<String> listeIdEmployeurs, Annee annee, List<String> associationsATraiter)
            throws Exception {

        checkParametresForCotisation(listeIdEmployeurs, annee, associationsATraiter);

        setProgressScaleValue(listeIdEmployeurs.size());
        // On boucle sur les employeurs
        for (String idEmployeur : listeIdEmployeurs) {
            if (isOnError) {
                break;
            }
            boolean generationFactures = canProcessFacture(annee, idEmployeur, associationsATraiter);

            if (generationFactures) {
                List<AssociationCotisation> listeCotisations = associationCotisationRepository
                        .findByIdEmployeurForYearWithDependencies(idEmployeur, annee, associationsATraiter, getGenre());
                if (!listeCotisations.isEmpty()) {
                    MasseCalculResult result = calculMassePourAnnee(idEmployeur, annee);

                    FacturesAssociations factures = createFactures(idEmployeur, listeCotisations, result,
                            associationsATraiter);

                    if (factures.containsFactures()) {
                        VulpeculaRepositoryLocator.getFactureAssociationRepository().create(factures);
                    }
                    incProgressCounter();
                }
            }
        }

    }

    /**
     * @param listeIdEmployeurs
     * @param annee
     * @param associationsATraiter
     * @throws JadePersistenceException
     */
    private void checkParametresForCotisation(List<String> listeIdEmployeurs, Annee annee,
            List<String> associationsATraiter) throws JadePersistenceException {
        // TODO ne pas faire par employeur mais pour toutes les coti
        // On test les associations à traiter, on regarde que toutes les cotisations possèdent au moins un paramètre.
        Map<String, AssociationCotisation> mapCotisationsUtilisees = new HashMap<String, AssociationCotisation>();
        for (String idEmployeur : listeIdEmployeurs) {
            // On remplit une liste de cotisations qui sont utilisées par tous les employeurs, pour chacune, on regarde
            // qu'il existe au moins un paramètre de cotisation
            List<AssociationCotisation> listeCotisations = associationCotisationRepository
                    .findByIdEmployeurForYearWithDependencies(idEmployeur, annee, associationsATraiter, getGenre());
            for (AssociationCotisation associationCotisation : listeCotisations) {
                if (!mapCotisationsUtilisees.containsKey(associationCotisation
                        .getCotisationAssociationProfessionnelle().getId())) {
                    mapCotisationsUtilisees.put(
                            associationCotisation.getCotisationAssociationProfessionnelle().getId(),
                            associationCotisation);
                }
            }
        }

        for (Map.Entry<String, AssociationCotisation> associationCotisation : mapCotisationsUtilisees.entrySet()) {
            if (!aFacturer(associationCotisation.getValue())) {
                continue;
            }
            if (!VulpeculaRepositoryLocator.getParametreCotisationAssociationRepository()
                    .isAuMoinsUnParametreForIdCotisation(
                            associationCotisation.getValue().getCotisationAssociationProfessionnelle().getId())) {
                // Il n'existe pas de paramètre pour la cotisation
                isOnError = true;
                parametresManquants.append(associationCotisation.getValue().getAssociationProfessionnelle()
                        .getCodeAdministration()
                        + " - " + associationCotisation.getValue().getLibelleCotisation() + "\n");
            }
        }
    }

    private boolean canProcessFacture(Annee annee, String idEmployeur, List<String> associationsATraiter) {
        FacturesAssociations facturesAssociationsExistantes = VulpeculaRepositoryLocator
                .getFactureAssociationRepository().findByIdEmployeurAndByAnnee(idEmployeur, annee, getGenre(),
                        associationsATraiter);

        boolean etatFactureGenereComptabilise = false;
        for (FactureAssociation factureAssociation : facturesAssociationsExistantes) {
            if (EtatFactureAP.GENERE.equals(factureAssociation.getEtat())
                    || EtatFactureAP.COMPTABILISE.equals(factureAssociation.getEtat())) {
                etatFactureGenereComptabilise = true;
                Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(idEmployeur);
                getMemoryLog().logMessage(
                        "Facture en état '" + factureAssociation.getEtat() + "' trouvée sur l'employeur "
                                + employeur.getAffilieNumero() + " - " + employeur.getRaisonSociale()
                                + " ==> Aucune facture remplacée", FWMessage.AVERTISSEMENT, this.getClass().getName());
                break;
            }
        }

        boolean generationFactures = true;
        if (facturesAssociationsExistantes.getFactures().size() > 0) {
            if (getReplaceFacture() && !etatFactureGenereComptabilise) {
                deleteFactures(facturesAssociationsExistantes);
                generationFactures = true;
            } else {
                generationFactures = false;
            }
        } else {
            generationFactures = true;
        }

        return generationFactures;
    }

    private void deleteFactures(FacturesAssociations facturesAssociations) {
        for (FactureAssociation factureAssociation : facturesAssociations) {
            VulpeculaRepositoryLocator.getFactureAssociationRepository().delete(factureAssociation);
        }

    }

    private Montant getMasseForcee(String idEmployeur, String idAssociationProfessionnelle) {
        List<AssociationEmployeur> listeAssociationEmployeur = VulpeculaRepositoryLocator
                .getAssociationEmployeurRepository().findByIdEmployeurAndIdAssociation(idEmployeur,
                        idAssociationProfessionnelle);
        Montant masseForcee = null;
        if (!listeAssociationEmployeur.isEmpty()) {
            AssociationEmployeur associationEmployeur = listeAssociationEmployeur.get(0);
            // On regarde si la masse a été surchargée
            if (!associationEmployeur.getMasseAssociation().isZero()) {
                masseForcee = associationEmployeur.getMasseAssociation();
            }
        }

        return masseForcee;
    }

    private FacturesAssociations createFactures(String idEmployeur, List<AssociationCotisation> listeCotisations,
            MasseCalculResult masseResult, List<String> associationsATraiter) throws Exception {
        String messageSurcharge = "";
        Map<String, EnteteFactureAssociation> entetes = new HashMap<String, EnteteFactureAssociation>();
        Montant masseSalariale = masseResult.getMasse();
        Set<String> idsAssociation = findAssociations(listeCotisations);
        List<LigneFactureAssociation> lignes = new ArrayList<LigneFactureAssociation>();

        for (AssociationCotisation associationCotisation : listeCotisations) {
            if (!isOnError) {
                if (!aFacturer(associationCotisation)) {
                    continue;
                }

                // Si l'association ne fait pas parti des associations à traiter, alors on ignore ce cas
                if (!associationsATraiter.isEmpty()
                        && !associationsATraiter.contains(associationCotisation.getIdAssociationProfessionnelle())) {
                    continue;
                }

                Montant masseForcee = getMasseForcee(idEmployeur,
                        associationCotisation.getIdAssociationProfessionnelle());
                if (masseForcee != null && !masseForcee.isZero()) {
                    masseSalariale = masseForcee;
                    messageSurcharge = getSession().getLabel("JSP_FACTURATION_MASSE_SURCHARGE") + masseForcee;
                } else if (associationCotisation.isNonMembre()) {
                    // Cas non membre (CPP) on prend la masse salariale CPR
                    masseSalariale = masseResult.getMasseCPR();
                    if (masseSalariale.isZero()) {
                        continue;
                    }
                }

                EnteteFactureAssociation entete = createEnteteFacture(idEmployeur,
                        associationCotisation.getIdAssociationProfessionnelle(), idsAssociation, masseResult, entetes,
                        associationCotisation.getGenre());

                if (isOnError) {
                    break;
                }
                // S'il s'agit d'un forfait, on ne facture pas les différents paramètres
                if (!associationCotisation.getForfait().isZero()) {
                    messageSurcharge = getSession().getLabel("JSP_FACTURATION_FORFAIT_SURCHARGE");
                    lignes.add(createLigneFacture(associationCotisation, entete));

                } else {
                    Montant masseReference = masseSalariale;
                    if (associationCotisation.isRabaisSpecial()) {
                        masseReference = entete.getMontantTotalForAssociation(associationCotisation
                                .getIdAssociationProfessionnelle());
                    }
                    parametres = VulpeculaRepositoryLocator.getParametreCotisationAssociationRepository()
                            .findForFourchetteAndIdCotisation(
                                    associationCotisation.getIdCotisationAssociationProfessionnelle(), masseReference);

                    boolean isTauxCumulatifDansLaFourchette = false;
                    entete.setMontantTotalCotisationForcee(Montant.ZERO);
                    for (ParametreCotisationAssociation parametreCotisationAssociation : parametres) {
                        switch (parametreCotisationAssociation.getTypeParam()) {
                        // Gérer les différents cas, il faut les traiter dans l'ordre, donc sans break pour tous les
                        // appliquer.
                            case FORFAIT_FIX:
                                lignes.addAll(calculMontantFixe(associationCotisation, parametreCotisationAssociation,
                                        entete));
                                break;

                            case TAUX_FIX:
                                List<LigneFactureAssociation> ligneFacture = calculTauxFixe(associationCotisation,
                                        parametreCotisationAssociation, masseSalariale, entete);
                                if (!ligneFacture.isEmpty()) {
                                    lignes.addAll(ligneFacture);
                                }
                                break;

                            case TAUX_CUMULATIF:
                                List<LigneFactureAssociation> ligneFacture2 = calculTauxVariable(associationCotisation,
                                        parametreCotisationAssociation, masseSalariale, entete);
                                if (!ligneFacture2.isEmpty()) {
                                    lignes.addAll(ligneFacture2);
                                }
                                isTauxCumulatifDansLaFourchette = true;
                                break;

                            case RABAIS:
                                List<LigneFactureAssociation> ligneFacture3 = calculRabais(associationCotisation,
                                        parametreCotisationAssociation, entete);
                                if (!ligneFacture3.isEmpty()) {
                                    lignes.addAll(ligneFacture3);
                                }
                                break;
                        }
                    }

                    // Pour les taux cumulatif, il faut voir si il existe des fourchettes inférieures à la masse.
                    if (!isTauxCumulatifDansLaFourchette) {
                        lignes.addAll(calculTauxVariable(associationCotisation, null, masseSalariale, entete));
                    }

                    ParametreCotisationAssociation facteur = getFacteurFromList(parametres);
                    if (facteur != null) {
                        LigneFactureAssociation ligneFacteur = applyFactor(lignes, facteur, associationCotisation,
                                entete);
                        lignes.add(ligneFacteur);
                    }
                }
            }
        }

        FacturesAssociations factures = new FacturesAssociations();
        if (!isOnError) {
            factures.addLignes(lignes);

            // On ajoute dans la liste les cas validés
            for (FactureAssociation factureAssociation : factures) {
                if (factureAssociation.getEtat().equals(EtatFactureAP.VALIDE)) {
                    ListePourImpression liste = new ListePourImpression();
                    ListePourImpression.resetCotisationsTrees();
                    for (AssociationFacture association : factureAssociation.getAssociations()) {
                        for (CotisationFacture cotisation : association.getCotisations()) {
                            if (cotisation.getCotisation().getGenre().equals(getGenre())) {
                                liste.addCotisationLabel(cotisation.getIdCotisation(), cotisation.getLibelle());
                                liste.addCotisationMontant(cotisation.getIdCotisation(), cotisation.getMontantTotal());
                            }
                        }
                    }

                    liste.setAnnee(factureAssociation.getAnnee());
                    liste.setEtat(factureAssociation.getEtat());
                    liste.setIdEmployeur(factureAssociation.getEnteteFacture().getIdEmployeur());
                    liste.setIdAssociation(factureAssociation.getAssociationParent().getId());
                    liste.setMontantTotal(factureAssociation.getMontantFacture());
                    liste.setMasse(masseSalariale);
                    if (JadeStringUtil.isEmpty(messageSurcharge) && masseResult.getMessageErreur().length() > 0) {
                        liste.setMessage(masseResult.getMessageErreur().toString());
                    } else if (!JadeStringUtil.isEmpty(messageSurcharge)) {
                        liste.setMessage(messageSurcharge);
                    }
                    Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(
                            factureAssociation.getEnteteFacture().getIdEmployeur());
                    facturesOrderByRaisonSociale.put(employeur.getRaisonSociale() + "-" + employeur.getId(), liste);
                }
            }
        }
        return factures;

    }

    private LigneFactureAssociation applyFactor(List<LigneFactureAssociation> lignes,
            ParametreCotisationAssociation facteur, AssociationCotisation associationCotisation,
            EnteteFactureAssociation entete) {

        Montant montantTotalFacteur = new Montant(BigDecimal.ZERO);
        Montant masse = new Montant(BigDecimal.ZERO);
        LigneFactureAssociation ligneFacteur = new LigneFactureAssociation();
        ligneFacteur.setEnteteFacture(entete);
        Double valFacteur = facteur.getFacteur();
        ligneFacteur.setAssociationCotisation(associationCotisation);
        ligneFacteur.setFacteur(valFacteur);
        ligneFacteur.setFourchetteDebut(facteur.getFourchetteDebut());
        ligneFacteur.setFourchetteFin(facteur.getFourchetteFin());

        for (LigneFactureAssociation ligne : lignes) {
            if (isLigneTypeTaux(ligne.getTypeParametre())) {
                Montant montantLigne = ligne.getMontantCotisation();
                masse = masse.add(montantLigne);
                Montant montantLigneFactorise = montantLigne.multiply(valFacteur).normalize();
                montantTotalFacteur = montantTotalFacteur.add(montantLigneFactorise);
            }
        }

        ligneFacteur.setMontantCotisation(montantTotalFacteur);
        ligneFacteur.setMassePourCotisation(masse);
        entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(), montantTotalFacteur);
        ligneFacteur.setTypeParametre(TypeParamCotisationAP.FACTEUR);
        entete.setMontantTotalCotisationForcee(entete.getMontantTotalCotisationForcee().add(montantTotalFacteur));
        return ligneFacteur;
    }

    private boolean isLigneTypeTaux(TypeParamCotisationAP typeParametre) {
        return TypeParamCotisationAP.TAUX_CUMULATIF.equals(typeParametre)
                || TypeParamCotisationAP.TAUX_FIX.equals(typeParametre);
    }

    /**
     * @param associationCotisation
     * @param entete
     * @return
     */
    private LigneFactureAssociation createLigneFacture(AssociationCotisation associationCotisation,
            EnteteFactureAssociation entete) {
        LigneFactureAssociation ligne = new LigneFactureAssociation();
        ligne.setEnteteFacture(entete);
        ligne.setMontantCotisation(associationCotisation.getForfait());
        ligne.setAssociationCotisation(associationCotisation);
        entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(),
                associationCotisation.getForfait());
        ligne.setTypeParametre(TypeParamCotisationAP.FORFAIT_COTISATION);
        return ligne;
    }

    private LigneFactureAssociation calculForfaitAssociation(EnteteFactureAssociation enteteForfait,
            Montant forfaitAssociation, String idAssociationProfessionnelle2) {
        LigneFactureAssociation ligne = new LigneFactureAssociation();
        ligne.setEnteteFacture(enteteForfait);
        AssociationCotisation associationCotisation = new AssociationCotisation();
        CotisationAssociationProfessionnelle coti = new CotisationAssociationProfessionnelle();
        Administration adm = new Administration();
        adm.setId(idAssociationProfessionnelle2);
        coti.setAssociationProfessionnelle(adm);
        associationCotisation.setCotisationAssociationProfessionnelle(coti);
        ligne.setAssociationCotisation(associationCotisation);
        ligne.setTypeParametre(TypeParamCotisationAP.FORFAIT_COTISATION);
        ligne.setMontantCotisation(forfaitAssociation);
        return ligne;
    }

    private Set<String> findAssociations(List<AssociationCotisation> listeCotisations) {
        Set<String> associations = new HashSet<String>();
        for (AssociationCotisation ac : listeCotisations) {
            associations.add(ac.getIdAssociationProfessionnelle());
        }
        return associations;
    }

    private List<LigneFactureAssociation> calculTauxVariable(AssociationCotisation associationCotisation,
            ParametreCotisationAssociation parametreCotisationAssociation, Montant masseSalariale,
            EnteteFactureAssociation entete) {
        if (masseSalariale.isZero()) {
            return new ArrayList<LigneFactureAssociation>();
        }

        List<LigneFactureAssociation> lignes = new ArrayList<LigneFactureAssociation>();

        // On va rechercher toutes les fourchettes inférieures
        List<ParametreCotisationAssociation> parametres = VulpeculaRepositoryLocator
                .getParametreCotisationAssociationRepository().findForFourchetteInferieuresAndIdCotisation(
                        associationCotisation.getIdCotisationAssociationProfessionnelle(), masseSalariale);

        for (ParametreCotisationAssociation fourchette : parametres) {
            // On créé une ligne par palier de taux
            if (masseSalariale.less(fourchette.getFourchetteFin())) {
                // On se trouve dans la dernière fourchette
                LigneFactureAssociation ligne = new LigneFactureAssociation();
                ligne.setEnteteFacture(entete);
                Montant montant = masseSalariale.substract(getMontantASoustraire(fourchette.getFourchetteDebut()));
                ligne.setMassePourCotisation(montant);
                Montant montantCotisation = montant.multiply(fourchette.getTaux()).normalize();
                ligne.setFourchetteDebut(fourchette.getFourchetteDebut());
                ligne.setFourchetteFin(fourchette.getFourchetteFin());
                ligne.setAssociationCotisation(associationCotisation);
                ligne.setTypeParametre(TypeParamCotisationAP.TAUX_CUMULATIF);
                ligne.setMontantCotisation(montantCotisation);
                ligne.setTauxCotisation(fourchette.getTaux());
                entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(), montantCotisation);
                entete.setMontantTotalCotisationForcee(entete.getMontantTotalCotisationForcee().add(montantCotisation));
                lignes.add(ligne);
            } else {
                LigneFactureAssociation ligne = new LigneFactureAssociation();
                ligne.setEnteteFacture(entete);
                Montant montant = fourchette.getFourchetteFin().substract(fourchette.getFourchetteDebut());
                ligne.setMassePourCotisation(montant);
                Montant montantCotisation = montant.multiply(fourchette.getTaux()).normalize();
                ligne.setFourchetteDebut(fourchette.getFourchetteDebut());
                ligne.setFourchetteFin(fourchette.getFourchetteFin());
                ligne.setAssociationCotisation(associationCotisation);
                ligne.setTypeParametre(TypeParamCotisationAP.TAUX_CUMULATIF);
                ligne.setMontantCotisation(montantCotisation);
                ligne.setTauxCotisation(fourchette.getTaux());
                entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(), montantCotisation);
                entete.setMontantTotalCotisationForcee(entete.getMontantTotalCotisationForcee().add(montantCotisation));
                lignes.add(ligne);
            }
        }
        return lignes;
    }

    /**
     * @param fourchette
     * @return
     */
    private Montant getMontantASoustraire(Montant fourchette) {
        if (fourchette.isZero()) {
            return Montant.ZERO;
        }
        return fourchette.substract(new Montant(1));
    }

    private List<LigneFactureAssociation> calculRabais(AssociationCotisation associationCotisation,
            ParametreCotisationAssociation parametreCotisationAssociation, EnteteFactureAssociation entete) {
        if (associationCotisation.isRabaisSpecial()) {
            List<LigneFactureAssociation> lignes = new ArrayList<LigneFactureAssociation>();

            Taux pourcentageRabais = parametreCotisationAssociation.getTaux();
            Montant masse = entete.getMontantTotalForAssociation(parametreCotisationAssociation
                    .getIdAssociationProfessionnelle());
            if (masse != null) {
                Montant montantRabais = masse.multiply(pourcentageRabais).normalize();
                LigneFactureAssociation ligne = new LigneFactureAssociation();
                ligne.setEnteteFacture(entete);
                ligne.setMassePourCotisation(masse);
                ligne.setFourchetteDebut(parametreCotisationAssociation.getFourchetteDebut());
                ligne.setFourchetteFin(parametreCotisationAssociation.getFourchetteFin());
                ligne.setAssociationCotisation(associationCotisation);
                ligne.setMontantCotisation(montantRabais.negate());
                ligne.setTauxCotisation(parametreCotisationAssociation.getTaux());
                entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(),
                        montantRabais.negate());
                ligne.setTypeParametre(TypeParamCotisationAP.RABAIS);
                lignes.add(ligne);
            }
            return lignes;
        } else {
            if (entete.getMontantTotalCotisationForcee().isZero()) {
                return new ArrayList<LigneFactureAssociation>();
            }
            List<LigneFactureAssociation> lignes = new ArrayList<LigneFactureAssociation>();

            Taux pourcentageRabais = parametreCotisationAssociation.getTaux();
            if (!entete.getMontantTotalCotisationForcee().isZero()) {
                Montant montantRabais = entete.getMontantTotalCotisationForcee().multiply(pourcentageRabais)
                        .normalize();
                LigneFactureAssociation ligne = new LigneFactureAssociation();
                ligne.setEnteteFacture(entete);
                ligne.setMassePourCotisation(entete.getMontantTotalCotisationForcee());
                ligne.setFourchetteDebut(parametreCotisationAssociation.getFourchetteDebut());
                ligne.setFourchetteFin(parametreCotisationAssociation.getFourchetteFin());
                ligne.setAssociationCotisation(associationCotisation);
                ligne.setMontantCotisation(montantRabais.negate());
                ligne.setTauxCotisation(parametreCotisationAssociation.getTaux());
                entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(),
                        montantRabais.negate());
                ligne.setTypeParametre(TypeParamCotisationAP.RABAIS);
                lignes.add(ligne);
            }
            return lignes;
        }
    }

    List<LigneFactureAssociation> calculTauxFixe(AssociationCotisation associationCotisation,
            ParametreCotisationAssociation parametreCotisationAssociation, Montant masseSalariale,
            EnteteFactureAssociation entete) {
        if (masseSalariale.isZero()) {
            return new ArrayList<LigneFactureAssociation>();
        }
        LigneFactureAssociation ligne = new LigneFactureAssociation();
        ligne.setEnteteFacture(entete);
        Taux taux = parametreCotisationAssociation.getTaux();
        Montant montant = masseSalariale.multiply(taux).normalize();
        ligne.setMassePourCotisation(masseSalariale);
        ligne.setFourchetteDebut(parametreCotisationAssociation.getFourchetteDebut());
        ligne.setFourchetteFin(parametreCotisationAssociation.getFourchetteFin());
        ligne.setAssociationCotisation(associationCotisation);
        ligne.setTauxCotisation(parametreCotisationAssociation.getTaux());
        ligne.setMontantCotisation(montant);
        entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(), montant);
        ligne.setTypeParametre(TypeParamCotisationAP.TAUX_FIX);
        entete.setMontantTotalCotisationForcee(entete.getMontantTotalCotisationForcee().add(montant));
        return Arrays.asList(ligne);
    }

    private List<LigneFactureAssociation> calculMontantFixe(AssociationCotisation associationCotisation,
            ParametreCotisationAssociation parametreCotisationAssociation, EnteteFactureAssociation entete) {
        LigneFactureAssociation ligne = new LigneFactureAssociation();
        ligne.setEnteteFacture(entete);
        Montant montant = parametreCotisationAssociation.getMontant();
        ligne.setMontantCotisation(montant);
        ligne.setAssociationCotisation(associationCotisation);
        entete.addToMontantTotal(associationCotisation.getIdAssociationProfessionnelle(), montant);
        ligne.setTypeParametre(TypeParamCotisationAP.FORFAIT_FIX);
        entete.setMontantTotalCotisationForcee(entete.getMontantTotalCotisationForcee().add(montant));
        return Arrays.asList(ligne);
    }

    /**
     * Création de l'entête facture pour l'association passé en paramètre. Si cette association dispose d'enfants, on
     * prend alors l'enfant qui est contenu dans la liste de {
     * 
     * @param idEmployeur String représentant l'id d'un employeur
     * @param idAssociationProfessionnelle String représentant l'id d'une association professionnelle (tiers
     *            administration)
     * @param result Masse salariale du décompte
     * @param entetes Etat de la facture à créer
     * @param genreCotisationAssociationProfessionnelle
     * @return Une entête de facture
     * @throws IllegalArgumentException
     */
    private EnteteFactureAssociation createEnteteFacture(String idEmployeur, String idAssociationProfessionnelle,
            Set<String> idsAssociations, MasseCalculResult result, Map<String, EnteteFactureAssociation> entetes,
            GenreCotisationAssociationProfessionnelle genreCotisationAssociationProfessionnelle)
            throws IllegalArgumentException {
        String idAssociationEntete = findAssociationEntete(idAssociationProfessionnelle, idsAssociations);
        if (!entetes.containsKey(idAssociationEntete)) {
            EnteteFactureAssociation entete = new EnteteFactureAssociation();

            Employeur employeur = new Employeur();
            employeur.setId(idEmployeur);
            entete.setEmployeur(employeur);

            Passage passage = new Passage();
            passage.setId(getIdPassage());
            entete.setPassageFacturation(passage);

            entete.initMontantTotal();
            if (GenreCotisationAssociationProfessionnelle.isNonMembre(genreCotisationAssociationProfessionnelle)) {
                entete.setMasseSalariale(result.getMasseCPR());
            } else {
                entete.setMasseSalariale(result.getMasse());
            }
            entete.setEtat(result.getEtat());
            entete.setAnneeFacture(new Annee(getAnnee()));

            // Si le genre est non-membre, on va rechercher le CPP
            if (GenreCotisationAssociationProfessionnelle.isNonMembre(genreCotisationAssociationProfessionnelle)) {
                List<String> idAssociationCPPs = VulpeculaServiceLocator
                        .getCotisationAssociationProfessionnelleService().findAssociationsCPP(idAssociationEntete);
                Administration admin = VulpeculaRepositoryLocator.getAdministrationRepository().findById(
                        idAssociationEntete);
                if (idAssociationCPPs.size() <= 0) {
                    String message = getSession().getLabel("FACTURATION_AP_CPP_NON_CONFIGURE") + " : "
                            + admin.getCodeAdministration();
                    getSession().addError(message);
                    isOnError = true;
                    erreurProcess = message;
                } else {
                    String idAssociationCPP = idAssociationCPPs.get(0);
                    entete.setModele(VulpeculaServiceLocator.getParametrageAPService().findByIdAdministration(
                            idAssociationCPP));
                    Administration association = new Administration();
                    association.setId(idAssociationCPP);
                    entete.setAssociationProfessionnelleParent(association);
                }
            } else {
                entete.setModele(VulpeculaServiceLocator.getParametrageAPService().findByIdAdministration(
                        idAssociationEntete));
                Administration association = new Administration();
                association.setId(idAssociationEntete);
                entete.setAssociationProfessionnelleParent(association);
            }

            entetes.put(idAssociationEntete, entete);
        }
        EnteteFactureAssociation entete = entetes.get(idAssociationEntete);
        return entete;
    }

    public String findAssociationEntete(String idAssociationProfessionnelle, Set<String> idsAssociations) {
        List<String> enfants = findAssociationsEnfant(idAssociationProfessionnelle);
        Set<String> enfantsEmployeur = getAssociationEnfant(idsAssociations, enfants);
        if (enfantsEmployeur.isEmpty()) {
            return idAssociationProfessionnelle;
        } else {
            return enfantsEmployeur.iterator().next();
        }
    }

    List<String> findAssociationsEnfant(String idAssociationProfessionnelle) {
        return VulpeculaServiceLocator.getCotisationAssociationProfessionnelleService().findAssociationsEnfant(
                idAssociationProfessionnelle);
    }

    /**
     * Retourne la liste des associations enfants contenu dans la liste des associations de l'employeur.
     * 
     * @param idsAssociations Liste des associations de l'employeur
     * @param idsAssociationsEnfants Liste des associations enfants d'UNE association de l'employeur
     * @return Retourne l'id d'une association enfant
     */
    private Set<String> getAssociationEnfant(Set<String> idsAssociations, List<String> idsAssociationsEnfants) {
        Set<String> associationsEnfantsContenusDansEmployeur = new HashSet<String>();
        for (String id : idsAssociationsEnfants) {
            if (idsAssociations.contains(id)) {
                associationsEnfantsContenusDansEmployeur.add(id);
            }
        }
        return associationsEnfantsContenusDansEmployeur;
    }

    /**
     * Détermine si on doit effectuer une facturation pour la ligne en cours.
     * 
     * @param categorie Catégorie de facturation
     * @param result Objet contenant les informations concernant la masse salariale. Notamment si il y avait des
     *            salaires sur l'année précédente.
     * @return true si à facturer
     */
    boolean aFacturer(AssociationCotisation associationCotisation) {
        return !(CategorieFactureAssociationProfessionnelle.NON_FACTURE.equals(associationCotisation.getFacturer())
                || CategorieFactureAssociationProfessionnelle.SUPPRIMER.equals(associationCotisation.getFacturer()) || CategorieFactureAssociationProfessionnelle.SOLDE_MINIME
                    .equals(associationCotisation.getFacturer()));
    }

    /**
     * Calcul de la masse et l'état de l'entête pour un employeur dont l'id est passé en paramètre.
     * <ul>
     * <li>Si la masse de l'année précédente est > 0, alors l'entête sera à l'état {@link EtatFactureAP#VALIDE}
     * <li>Si la masse de l'année précédente est = 0, alors on recherche l'année encore précédente
     * <ul>
     * <li>Sinon, le montant est 0 et l'état {@link EtatFactureAP#EN_ERREUR}
     * </ul>
     * <ul>
     * 
     * @param idEmployeur String représentant l'id d'un employeur
     * @return Objet contenant la masse et l'état de l'entête
     */
    MasseCalculResult calculMassePourAnnee(String idEmployeur, Annee annee) {
        Annee anneePrecedente = annee.previous();
        List<Decompte> decomptes = findDecomptes(idEmployeur, anneePrecedente);
        List<Decompte> decomptesCPR = findDecomptesCPR(idEmployeur, anneePrecedente);
        return calculMasse(decomptes, decomptesCPR, idEmployeur);
    }

    List<Decompte> findDecomptes(String idEmployeur, Annee annee) {
        return VulpeculaRepositoryLocator.getDecompteRepository().findByIdEmployeurAndPeriode(idEmployeur,
                annee.getFirstDayOfYear().getAnneeMois(), annee.getLastDayOfYear().getAnneeMois(), null,
                LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES, LoadOptions.NOT_LOAD_HISTORIQUE);
    }

    List<Decompte> findDecomptesCPR(String idEmployeur, Annee anneePrecedente) {
        // On regarde s'il existe une particularité sans personnel,
        // si oui il ne faut pas rechercher pour l'année complète
        List<Particularite> particularites = VulpeculaServiceLocator.getEmployeurService().findParticularites(
                idEmployeur, new Periode(anneePrecedente.getFirstDayOfYear(), anneePrecedente.getLastDayOfYear()));
        List<Periode> periodes = findPeriodeSalaireForParticularites(anneePrecedente, particularites);

        // Il faut ressortir la période où il n'y a pas de particularités et effectuer la rechercher des décomptes sur
        // cette période
        List<Decompte> listeDecompte = new ArrayList<Decompte>();
        for (Periode periode : periodes) {
            List<Decompte> decomptesCP = new ArrayList<Decompte>();
            // rechercher les décomptes CP sur l'année complète et les rajouter aux autres
            if (decomptesCP.size() == 0) {
                // rechercher les décomptes sans les CP selon les périodes
                decomptesCP = VulpeculaRepositoryLocator.getDecompteRepository()
                        .findByIdEmployeurAndPeriodeForCPPComplementaire(idEmployeur,
                                anneePrecedente.getFirstDayOfYear().getAnneeMois(),
                                anneePrecedente.getLastDayOfYear().getAnneeMois(), null,
                                LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES, LoadOptions.NOT_LOAD_HISTORIQUE);
            }
            decomptesCP.addAll(VulpeculaRepositoryLocator.getDecompteRepository().findByIdEmployeurAndPeriodeForCPP(
                    idEmployeur, periode.getDateDebut().getAnneeMois(), periode.getDateFin().getAnneeMois(), null,
                    LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES, LoadOptions.NOT_LOAD_HISTORIQUE));
            listeDecompte.addAll(decomptesCP);
        }
        return listeDecompte;
    }

    List<Periode> findPeriodeSalaireForParticularites(Annee annee, List<Particularite> particularites) {
        Periode periodeSalaires = new Periode(annee.getFirstDayOfYear(), annee.getLastDayOfYear());
        List<Periode> periodesSalairesListe = new ArrayList<Periode>();
        for (Particularite particularite : particularites) {
            Periode periodeParticularite = null;
            if (particularite.getDateFin() == null) {
                periodeParticularite = new Periode(particularite.getDateDebut(), new Date("31.12.3999"));
            } else {
                periodeParticularite = new Periode(particularite.getDateDebut(), particularite.getDateFin());
            }

            Date dateDebutParticularite = periodeParticularite.getDateDebut();
            Date dateFinParticularite = periodeParticularite.getDateFin();
            Date dateDebutSalaire = periodeSalaires.getDateDebut();
            Date dateFinSalaire = periodeSalaires.getDateFin();

            if (periodeSalaires.contains(periodeParticularite)) {
                if (!dateDebutSalaire.getMois().equals(dateDebutParticularite.getMois())) {
                    Periode periode1 = new Periode(dateDebutSalaire, dateDebutParticularite.addMonth(-1)
                            .getLastDayOfMonth());
                    periodesSalairesListe.add(periode1);
                }
                if (!dateFinParticularite.getMois().equals(dateFinSalaire.getMois())) {
                    Periode periode2 = new Periode(dateFinParticularite.addMonth(1).getFirstDayOfMonth(),
                            dateFinSalaire);
                    periodesSalairesListe.add(periode2);
                }
            } else if (periodeSalaires.chevauche(periodeParticularite)) {
                if (dateFinParticularite.afterOrEquals(dateDebutSalaire)) {
                    if (dateFinParticularite.afterOrEquals(dateFinSalaire)) {
                        if (dateFinSalaire.afterOrEquals(dateDebutParticularite)
                                && dateDebutParticularite.after(dateDebutSalaire)) {
                            Date dateFin = dateDebutParticularite.getLastDayOfMonth().addMonth(-1);
                            Periode periode = new Periode(dateDebutSalaire, dateFin);
                            periodesSalairesListe.add(periode);
                        }
                    } else {
                        Date dateDebut = dateFinParticularite.addMonth(1).getFirstDayOfMonth();
                        Periode periode = new Periode(dateDebut, dateFinSalaire);
                        periodesSalairesListe.add(periode);
                    }
                } else {
                    if (dateDebutParticularite.beforeOrEquals(dateFinSalaire)) {
                        Date dateFin = dateDebutParticularite.addMonth(-1).getLastDayOfMonth();
                        Periode periode = new Periode(dateDebutSalaire, dateFin);
                        periodesSalairesListe.add(periode);
                    } else {
                        periodesSalairesListe.add(periodeSalaires);
                    }
                }
            } else {
                periodesSalairesListe.add(periodeSalaires);
            }
        }

        if (particularites.isEmpty()) {
            periodesSalairesListe.add(periodeSalaires);
        }

        return periodesSalairesListe;
    }

    private MasseCalculResult calculMasse(List<Decompte> decomptes, List<Decompte> decomptesCPR, String idEmployeur2) {
        EtatFactureAP etat = EtatFactureAP.VALIDE;
        Montant montant = Montant.ZERO;
        Montant montantCPR = Montant.ZERO;
        StringBuffer message = new StringBuffer();
        for (Decompte decompte : decomptes) {
            if (decompte.getEtat().equals(EtatDecompte.COMPTABILISE)) {
                montant = montant.add(decompte.getMasseSalarialeTotal());
            } else if (decompte.getEtat().equals(EtatDecompte.GENERE)
                    || decompte.getEtat().equals(EtatDecompte.FACTURATION)
                    || decompte.getEtat().equals(EtatDecompte.OUVERT)
                    || decompte.getEtat().equals(EtatDecompte.RECEPTIONNE)
                    || decompte.getEtat().equals(EtatDecompte.RECTIFIE)
                    || decompte.getEtat().equals(EtatDecompte.VALIDE)
                    || decompte.getEtat().equals(EtatDecompte.SOMMATION)) {

                message.append(getSession().getLabel("JSP_FACTURATION_DECOMPTE_NON_COMPTA")
                        + decompte.getEmployeur().getDesignationCourt() + " " + decompte.getMoisPeriodeDebut() + "."
                        + decompte.getAnneePeriodeDebut() + " - " + decompte.getMoisPeriodeFin() + "."
                        + decompte.getAnneePeriodeFin() + "\n");
            }
        }

        for (Decompte decompte : decomptesCPR) {
            if (decompte.getEtat().equals(EtatDecompte.COMPTABILISE)) {
                Decompte decompteWithDependencies = VulpeculaRepositoryLocator.getDecompteRepository()
                        .findByIdWithDependencies(decompte.getId());
                montantCPR = montantCPR.add(decompteWithDependencies
                        .getMasseCotisationCalculees(TypeAssurance.CPR_TRAVAILLEUR));
            } else if (decompte.getEtat().equals(EtatDecompte.GENERE)
                    || decompte.getEtat().equals(EtatDecompte.FACTURATION)
                    || decompte.getEtat().equals(EtatDecompte.OUVERT)
                    || decompte.getEtat().equals(EtatDecompte.RECEPTIONNE)
                    || decompte.getEtat().equals(EtatDecompte.RECTIFIE)
                    || decompte.getEtat().equals(EtatDecompte.VALIDE)
                    || decompte.getEtat().equals(EtatDecompte.SOMMATION)) {

                message.append(getSession().getLabel("JSP_FACTURATION_DECOMPTE_NON_COMPTA")
                        + decompte.getEmployeur().getDesignationCourt() + " " + decompte.getMoisPeriodeDebut() + "."
                        + decompte.getAnneePeriodeDebut() + " - " + decompte.getMoisPeriodeFin() + "."
                        + decompte.getAnneePeriodeFin() + "\n");
            }
        }

        if (montant.isZero()) {
            return new MasseCalculResult(Montant.ZERO, etat, message, montantCPR);
        } else {
            return new MasseCalculResult(montant, etat, message, montantCPR);
        }
    }

    @Override
    protected String getEMailObject() {
        StringBuffer object = new StringBuffer();

        if (!JadeStringUtil.isEmpty(employeurLibelle)) {
            if (!JadeStringUtil.isEmpty(associationLibelle)) {
                object.append(getSession().getLabel("FACTURATION_AP_OBJECT") + " "
                        + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_ANNEE") + " : " + getAnnee() + " "
                        + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_ASSOCIATION") + " : " + associationLibelle + " "
                        + libelleGenre() + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_EMPLOYEUR") + " : "
                        + employeurLibelle + "\n" + "\n");
            } else {
                object.append(getSession().getLabel("FACTURATION_AP_OBJECT") + " "
                        + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_ANNEE") + " : " + getAnnee() + " "
                        + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_EMPLOYEUR") + " : " + employeurLibelle + "\n"
                        + "\n");
            }
        } else {
            if (!JadeStringUtil.isEmpty(associationLibelle)) {
                object.append(getSession().getLabel("FACTURATION_AP_OBJECT") + " "
                        + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_ANNEE") + " : " + getAnnee() + " "
                        + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_ASSOCIATION") + " : " + associationLibelle + " "
                        + libelleGenre() + getSession().getLabel("EMAIL_OBJECT_FACTU_AP_EMPLOYEUR") + " : "
                        + employeurLibelle + "\n" + "\n");
            } else {
                object.append(getSession().getLabel("FACTURATION_AP_OBJECT") + "\n" + "\n");
            }
        }

        object.append(getSession().getLabel("JSP_PASSAGE_FACTURATION") + " : " + passageLibelle + "\n");
        object.append(getSession().getLabel("JSP_AP_ECRASER_FACTURES_EXISTANTES") + " : "
                + getReplaceFacture().toString() + "\n\n");

        if (!JadeStringUtil.isEmpty(erreurProcess)) {
            object.append(erreurProcess + "\n" + "\n");
        }

        if (parametresManquants.length() > 0) {
            object.append(getSession().getLabel("FACTU_AP_AUCUN_PARAMETRE") + " : " + "\n");
            object.append(parametresManquants.toString() + "\n");
        }

        return object.toString();
    }

    private String libelleGenre() {
        if (genre == null) {
            return "";
        }
        return genre.toString() + " ";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getIdAssociationProfessionnelle() {
        return idAssociationProfessionnelle;
    }

    public void setIdAssociationProfessionnelle(String idAssociationProfessionnelle) {
        this.idAssociationProfessionnelle = idAssociationProfessionnelle;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public Boolean getReplaceFacture() {
        return replaceFacture;
    }

    public void setReplaceFacture(Boolean replaceFacture) {
        this.replaceFacture = replaceFacture;
    }

    /**
     * @return the genre
     */
    public GenreCotisationAssociationProfessionnelle getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(GenreCotisationAssociationProfessionnelle genre) {
        this.genre = genre;
    }

    private ParametreCotisationAssociation getFacteurFromList(List<ParametreCotisationAssociation> listParam) {
        ParametreCotisationAssociation typeParam = null;
        for (ParametreCotisationAssociation param : listParam) {
            if (TypeParamCotisationAP.FACTEUR.equals(param.getTypeParam())) {
                typeParam = param;
            }
        }
        return typeParam;
    }
}
