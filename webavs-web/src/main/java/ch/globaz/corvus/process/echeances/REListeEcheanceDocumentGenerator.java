package ch.globaz.corvus.process.echeances;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.REEcheancesManager;
import globaz.corvus.db.echeances.RERelationEcheances;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REListerEcheanceRenteJoinMembresFamille;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lyra.process.LYAbstractListGenerator;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory.TypeAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;
import com.sun.star.uno.RuntimeException;

public abstract class REListeEcheanceDocumentGenerator extends LYAbstractListGenerator {

    private static final long serialVersionUID = 1797341552702571546L;
    private final REAnalyseurEcheances analyseur;
    private final Set<String> conjointDejaTraites;

    /**
     * Echéances retenue pour la génération des documents
     */
    private final List<REListerEcheanceRenteJoinMembresFamille> echeancesRetenues;

    public REListeEcheanceDocumentGenerator(BSession session, String titre, String moisTraitement,
            REAnalyseurEcheancesFactory.TypeAnalyseurEcheances... typesAnalyseur) throws Exception {
        this(session, titre, moisTraitement, "", typesAnalyseur);
    }

    public REListeEcheanceDocumentGenerator(BSession session, String titre, String moisTraitement,
            String idGroupeLocalite, REAnalyseurEcheancesFactory.TypeAnalyseurEcheances... typesAnalyseur)
            throws Exception {
        super(session, "PRESTATIONS", "GLOBAZ", titre, initManager(session, moisTraitement, idGroupeLocalite),
                REApplication.DEFAULT_APPLICATION_CORVUS);

        if (session == null) {
            throw new NullPointerException("Session null");
        }
        if (JadeStringUtil.isBlank(titre)) {
            throw new Exception("ERREUR_TITRE_OBLIGATOIRE");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(moisTraitement)) {
            throw new Exception(session.getLabel("ERREUR_MOIS_TRAITEMENT_INVALIDE"));
        }
        if (typesAnalyseur.length == 0) {
            throw new Exception("ERREUR_AUCUN_MOTIF_ECHEANCE");
        }

        setMoisTraitement(moisTraitement);
        conjointDejaTraites = new HashSet<String>();
        echeancesRetenues = new ArrayList<REListerEcheanceRenteJoinMembresFamille>();
        analyseur = buildAnalyseur(typesAnalyseur);
        setSendCompletionMail(true);
    }

    private static REEcheancesManager initManager(BSession session, String moisTraitement, String idGroupeLocalite)
            throws PropertiesException {
        REEcheancesManager manager = new REEcheancesManager(session, moisTraitement, idGroupeLocalite);
        // TODO
        // manager.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        return manager;
    }

    @Override
    public void _beforeExecuteReport() {

        StringBuilder companyName = new StringBuilder();

        String nomCaisse = FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase());
        try {
            companyName.append(nomCaisse);
            companyName.append(" - ");
            companyName.append(getSession().getApplication().getProperty("noCaisse")).append("/")
                    .append(getSession().getApplication().getProperty("noAgence"));
        } catch (Exception ex) {
            companyName = new StringBuilder();
            companyName.append(nomCaisse);
        }

        _setCompanyName(companyName.toString());
        setSendMailOnError(true);
    }

    @Override
    protected void bindPageHeader() throws Exception {
        super.bindPageHeader();
        if (getAjouterCommunePolitique()) {
            String utilisateur = getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey())
                    + " : " + getSession().getUserId();
            this._addHeaderLine(getFontDate(), utilisateur, null, null, null, null);
        }
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        IREEcheances uneEcheance = (IREEcheances) entity;

        try {
            List<REReponseModuleAnalyseEcheance> reponses = analyseur.analyserEcheance(uneEcheance);
            if (reponses.size() != 0) {
                ajouterTiers(uneEcheance, reponses);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Une IREEcheance peut contenir plusieurs rente accordée à la famille. Lorsque les modules d'analyse ont analysé
     * les rentes contenues dans IREEcheance, ils renvoient des motifs d'échéance concernant les rente de cette famille
     * qui arrivent à terme. Il s'agit de pouvoir lié les motifs ainsi reçu avec les rente concernées
     */
    private void agregerCasPousGenerationDesLettres(IREEcheances echeance,
            List<REReponseModuleAnalyseEcheance> reponseModulesAnalyse) {
        for (REReponseModuleAnalyseEcheance reponse : reponseModulesAnalyse) {
            if (reponse.getRente() == null) {
                System.out.println("Une échéance à été retenue sur le motif : " + reponse.getMotif().name()
                        + " sans avoir de prestation accordee !!!!");
            } else {
                ArrayList<REListerEcheanceRenteJoinMembresFamille> list = convertToOldEcheance(echeance,
                        reponseModulesAnalyse);
                for (REListerEcheanceRenteJoinMembresFamille entity : list) {
                    echeancesRetenues.add(entity);
                }
            }

        }
    }

    protected void ajouterTiers(IREEcheances echeance, List<REReponseModuleAnalyseEcheance> reponses) {
        List<REReponseModuleAnalyseEcheance> reponsesATraiter = validerAjouterTiers(echeance, reponses);

        if (reponsesATraiter.size() == 0) {
            return;
        } else {
            // on parcours les réponses pour les conjoints afin de vérifier
            // qu'ils n'aient pas déjà été traités par un autre module
            for (int i = 0; i < reponsesATraiter.size(); i++) {
                REReponseModuleAnalyseEcheance uneReponse = reponsesATraiter.get(i);

                // si motif conjoint âge AVS ou homme/femme âge AVS
                if (isMotifConjointOuHommeFemmeArrivantAgeAvs(uneReponse.getMotif())) {
                    if (conjointDejaTraites.contains(uneReponse.getIdTiers())) {
                        // si les cas a déjà été traité, on l'ignore
                        reponsesATraiter.remove(i);
                    }
                }
            }
            // TODO futur correction BZ8179
            // if(reponsesATraiter.size() == 0){
            // return;
            // }
            agregerCasPousGenerationDesLettres(echeance, reponsesATraiter);

            if (getAjouterCommunePolitique()) {
                _addCell("");
                _addCell(echeance.getCommunePolitique());
            }

            _addCell(echeance.getNssTiers());
            _addCell(echeance.getNomTiers() + " " + echeance.getPrenomTiers());
            _addCell(echeance.getDateNaissanceTiers());
            _addCell(RETiersForJspUtils.getInstance(getSession()).getLibelleCourtSexe(echeance.getCsSexeTiers()));

            StringBuilder codePrestations = new StringBuilder();
            for (IRERenteEcheances renteDuTiers : echeance.getRentesDuTiers()) {
                if (IREPrestationAccordee.CS_ETAT_DIMINUE.equals(renteDuTiers.getCsEtat()) == false) {
                    if (codePrestations.length() > 0) {
                        codePrestations.append(" - ");
                    }
                    codePrestations.append(renteDuTiers.getCodePrestation());
                }
            }
            _addCell(codePrestations.toString());

            StringBuilder motifs = new StringBuilder();
            for (REReponseModuleAnalyseEcheance uneReponse : reponsesATraiter) {
                if (motifs.length() > 0) {
                    motifs.append("\n");
                }
                motifs.append(getSession().getLabel(uneReponse.getMotif().getIdLabelMotif()));
                if (!JadeStringUtil.isBlank(uneReponse.getRemarques())) {
                    motifs.append(" ").append(uneReponse.getRemarques());
                }
                if (isMotifConjointOuHommeFemmeArrivantAgeAvs(uneReponse.getMotif())) {
                    // si le motif concernait l'âge AVS, on ajoute l'id tiers
                    conjointDejaTraites.add(uneReponse.getIdTiers());
                }
            }
            _addCell(motifs.toString());
        }
    }

    protected REAnalyseurEcheances buildAnalyseur(TypeAnalyseurEcheances... typesAnalyseur) throws Exception {
        return new REAnalyseurEcheancesFactory(getSession()).getInstance(getMoisTraitement(), typesAnalyseur);
    }

    /**
     * Conversion des entité implémentant IREEcheance vers le type REListerEcheanceRenteJoinMembresFamille. Cette
     * conversion est rendue obligatoire du fait que le processus 'Liste des travaux à effectuer process' génère des
     * IREEcheances et le modèle de document REEcheanceRenteOptimiseOO à besoin de
     * REListerEcheanceRenteJoinMembresFamille
     * 
     * @param ireEcheances
     *            Entité à convertit dans le type REListerEcheanceRenteJoinMembresFamille
     * @return Une nouvelle entité REListerEcheanceRenteJoinMembresFamille NON SYNCHRONISE AVEC LA DB !!!
     */
    private ArrayList<REListerEcheanceRenteJoinMembresFamille> convertToOldEcheance(IREEcheances ireEcheances,
            List<REReponseModuleAnalyseEcheance> reponseModulesAnalyse) {
        ArrayList<REListerEcheanceRenteJoinMembresFamille> newList = new ArrayList<REListerEcheanceRenteJoinMembresFamille>();
        // On va convertir chaque réponse
        for (REReponseModuleAnalyseEcheance reponse : reponseModulesAnalyse) {
            // Si on ne retrouve pas la rente qui à provoqué le motif, on ne pourra pas convertir tout cela...
            if (reponse.getRente() != null) {
                REListerEcheanceRenteJoinMembresFamille echeanceEnCours = prepareOldEcheanceObject(ireEcheances,
                        reponse.getRente(), reponse);

                if (echeanceEnCours != null) {
                    // on récupère les autres rentes, on exclue la rente déjà transformée. On transforme les autres et
                    // on les ajoute à la liste des rentes dépendantes de echeanceEnCours
                    for (IRERenteEcheances echeance : ireEcheances.getRentesDuTiers()) {

                        if (!echeanceEnCours.getIdRenteAccordee().equals(echeance.getIdPrestationAccordee())) {

                            REListerEcheanceRenteJoinMembresFamille echeanceDependante = prepareOldEcheanceObject(
                                    ireEcheances, echeance, reponse);
                            if (echeanceDependante != null) {

                                if (!echeanceDependante.getIdTiersAdressePaiement().equals(
                                        echeanceEnCours.getIdTiersAdressePaiement())) {
                                    echeanceEnCours.setHasSameIdAdressePaiement(false);
                                }

                                echeanceEnCours.getListeEcheanceLiees().add(echeanceDependante);
                            }
                        }
                    }
                    newList.add(echeanceEnCours);
                }
            }
        }
        return newList;
    }

    private REListerEcheanceRenteJoinMembresFamille prepareOldEcheanceObject(IREEcheances ireEcheances,
            IRERenteEcheances renteEnCours, REReponseModuleAnalyseEcheance reponse) {
        boolean doAdd = true;
        REListerEcheanceRenteJoinMembresFamille newEntity = new REListerEcheanceRenteJoinMembresFamille();

        // Le choix est de positionner la date formatée à 00000000, si le formatage déclenche une exception
        // on interrompt pas le processus mais on vas se trouver avec une
        // date incohérente dans les documents
        String dateNaissanceTiers = "00000000";

        // On ne connait pas forcément la date d'échéance de la période car on ne connait pas
        // la période correspondante à la prestation qui à soulevé le motif.
        // Ce qu'on sait, (vu avec RJE) c'est que cette date correspondra toujours au mois de traitement.
        String periodeDateDeFin = "00000000";
        try {
            dateNaissanceTiers = PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAAMMJJ(ireEcheances.getDateNaissanceTiers());
            periodeDateDeFin = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ("01." + getMoisTraitement());
        } catch (JAException e) {

            e.printStackTrace();
        }

        // Données du tiers reprise de IREEchecance
        newEntity.setIdTiers(ireEcheances.getIdTiers());
        newEntity.setNss(ireEcheances.getNssTiers());
        newEntity.setCsSexe(ireEcheances.getCsSexeTiers());
        newEntity.setNom(ireEcheances.getNomTiers());
        newEntity.setPrenom(ireEcheances.getPrenomTiers());
        newEntity.setDateNaissance(dateNaissanceTiers);
        newEntity.setDateDeces(ireEcheances.getDateDecesTiers());
        newEntity.setSexeConjoint(getSexeConjoint(ireEcheances));

        // Données liées à la prestation reprise depuis la rente renseignée dans le motif
        newEntity.setDateDebutDroit(renteEnCours.getDateDebutDroit());
        newEntity.setDateEcheanceRenteAccordee(renteEnCours.getDateEcheance());
        newEntity.setDateRevocationAjournement(renteEnCours.getDateRevocationAjournement());
        newEntity.setIdRenteAccordee(renteEnCours.getIdPrestationAccordee());
        newEntity.setCodePrestation(renteEnCours.getCodePrestation());
        newEntity.setCsTypeInfoComplementaire(renteEnCours.getCsTypeInfoComplementaire());
        newEntity.setDateRevocationAjournement(renteEnCours.getDateRevocationAjournement());
        newEntity.setMontantPrestation(renteEnCours.getMontant());
        newEntity.setMotifLettre(reponse.getMotif().name());
        newEntity.setPeriodeDateFin(periodeDateDeFin);
        newEntity.setMotifEcheance(reponse.getMotif());

        // Les données étant remontée Par REEcheanceManager, il faut rechercher 2 - 3 info en plus
        REPrestationsAccordees prestation = null;
        try {
            prestation = new REPrestationsAccordees();
            prestation.setSession(getSession());
            prestation.setIdPrestationAccordee(renteEnCours.getIdPrestationAccordee());
            prestation.retrieve();

            REInformationsComptabilite infoCompta = new REInformationsComptabilite();
            infoCompta.setIdInfoCompta(prestation.getIdInfoCompta());
            infoCompta.setSession(getSession());
            infoCompta.retrieve();
            newEntity.setIdTiersAdressePaiement(infoCompta.getIdTiersAdressePmt());

        } catch (Exception e) {
            // TODO best logging
            doAdd = false;
            e.printStackTrace();
        }
        doAdd = doAdd && !prestation.isNew();
        if (doAdd) {
            newEntity.setMontantPrestation(prestation.getMontantPrestation()); // doublon
            newEntity.setFractionRente(prestation.getFractionRente()); // Needed
            newEntity.setIdInfoCompta(prestation.getIdInfoCompta());
        }
        if (doAdd) {
            return newEntity;
        }

        return null;
    }

    public final List<REListerEcheanceRenteJoinMembresFamille> getEcheancesRetenues() {
        return echeancesRetenues;
    }

    /**
     * Recherche le sexe du conjoint. Si plusieurs relations sont trouvées lors de la récupération du sexe du conjoint,
     * c'est la première valeur non null qui sera récupérée et un avertissement sera émis dans le mail
     * 
     * @param echeances
     *            L'échéance à analyser pour récupérer le sexe du conjoint
     * @return Le code system du sexe du conjoint
     */
    private String getSexeConjoint(IREEcheances echeances) {
        String sexeConjoint = null;
        boolean addWarningMessage = false;
        // S'il n'y a pas de relations, on retourne directement null sans avertir cas ce cas peut arriver
        if (echeances.getRelations().size() == 0) {
            return null;
        } else if (echeances.getRelations().size() == 1) {
            if (echeances.getRelations().get(0).getCsSexeConjoint() != null) {
                sexeConjoint = echeances.getRelations().get(0).getCsSexeConjoint();
            }
        } else if (echeances.getRelations().size() > 1) {

            for (int ctr = 0; ctr < echeances.getRelations().size(); ctr++) {
                RERelationEcheances relation = (RERelationEcheances) echeances.getRelations().get(ctr);
                if (ctr == 0) {
                    sexeConjoint = relation.getCsSexeConjoint();
                } else {
                    if (relation.getCsSexeConjoint() == null) {
                        addWarningMessage = true;
                    } else {
                        if (sexeConjoint == null) {
                            sexeConjoint = relation.getCsSexeConjoint();
                            addWarningMessage = true;
                        }
                        if (!relation.getCsSexeConjoint().equals(sexeConjoint)) {
                            addWarningMessage = true;
                        }
                    }
                }
            }
        }
        // En cas de difficulté à retrouver le sexe du conjoint, on avertis dans le mail
        if ((sexeConjoint == null) || addWarningMessage) {
            StringBuilder message = new StringBuilder();
            message.append(getSession().getLabel("INCOHERANCE_DONNEES_RECUPERATION_SEXE_CONJOINT"));
            message.append(" : [");
            message.append(echeances.getNssTiers());
            message.append("]. ");
            message.append(getSession().getLabel("SI_LETTRE_GENEREE_VEUILLEZ_CONTROLLER"));
            getMemoryLog().logMessage(message.toString(), FWViewBeanInterface.WARNING, "REListeTravauxAEffectuer...");
        }
        return sexeConjoint;
    }

    @Override
    protected void initializeTable() {
        if (getAjouterCommunePolitique()) {
            ajouterCommunePolitiques();
            this._addColumnLeft("", 1);
            this._addColumnCenter(getSession()
                    .getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 11);
        }
        this._addColumnLeft(getSession().getLabel("NSS_DOCUMENT_ANALYSE_ECHEANCE"), 9);
        this._addColumnLeft(getSession().getLabel("NOM_PRENOM_DOCUMENT_ANALYSE_ECHEANCE"), 25);
        this._addColumnCenter(getSession().getLabel("DATE_NAISSANCE_DOCUMENT_ANALYSE_ECHEANCE"), 7);
        this._addColumnCenter(getSession().getLabel("SEXE_DOCUMENT_ANALYSE_ECHEANCE"), 5);
        this._addColumnCenter(getSession().getLabel("GENRE_PRESTATION_DOCUMENT_ANALYSE_ECHEANCE"), 10);
        this._addColumnRight(getSession().getLabel("MOTIF_DOCUMENT_ANALYSE_ECHEANCE"), 30);
        _groupManual();
    }

    protected void ajouterCommunePolitiques() {
        Set<String> setIdTiers = new HashSet<String>();
        if (getAjouterCommunePolitique()) {
            Vector<REEcheancesEntity> container = getManager().getContainer();

            for (REEcheancesEntity echeance : container) {
                setIdTiers.add(echeance.getIdTiers());
            }
            Date date = new Date();

            Map<String, String> mapCommuneParIdTiers = PRTiersHelper
                    .getCommunePolitique(setIdTiers, date, getSession());

            for (REEcheancesEntity echeance : container) {
                echeance.setCommunePolitique(mapCommuneParIdTiers.get(echeance.getIdTiers()));
            }

            Collections.sort(container, new Comparator<REEcheancesEntity>() {

                @Override
                public int compare(REEcheancesEntity o1, REEcheancesEntity o2) {
                    if (getAjouterCommunePolitique()) {
                        int result1 = o1.getCommunePolitique().compareTo(o2.getCommunePolitique());
                        if (result1 != 0) {
                            return result1;
                        }
                    }

                    int result2 = o1.getNomTiers().compareTo(o2.getNomTiers());
                    if (result2 != 0) {
                        return result2;
                    }

                    return o1.getPrenomTiers().compareTo(o2.getPrenomTiers());
                }
            });
        }
    }

    @SuppressWarnings("incomplete-switch")
    private boolean isMotifConjointOuHommeFemmeArrivantAgeAvs(REMotifEcheance motif) {
        switch (motif) {
            case ConjointAgeAvsDepasse:
            case ConjointArrivantAgeAvs:
            case FemmeAgeAvsDepasse:
            case FemmeArrivantAgeAvs:
            case FemmeArrivantAgeAvsAvecApiAi:
            case FemmeArrivantAgeAvsRenteAnticipee:
            case HommeAgeAvsDepasse:
            case HommeArrivantAgeAvs:
            case HommeArrivantAgeAvsAvecApiAi:
            case HommeArrivantAgeAvsRenteAnticipee:
                return true;
        }
        return false;
    }

    /**
     * Analyse un tiers, et ses réponses, afin de déterminer s'il faut l'ajouter au document.<br/>
     * Retourne la listes des réponses à afficher sur les listes (ou autre supports), et permet ainsi de filtrer les
     * réponses des modules<br/>
     * 
     * @param echeance
     * @param reponses
     * @return
     */
    protected abstract List<REReponseModuleAnalyseEcheance> validerAjouterTiers(IREEcheances echeance,
            List<REReponseModuleAnalyseEcheance> reponses);

    public boolean getAjouterCommunePolitique() {
        try {
            return CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        } catch (PropertiesException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }
}
