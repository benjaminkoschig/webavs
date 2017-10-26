package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import globaz.babel.api.ICTDocument;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.document.TextMerger;
import ch.globaz.common.document.babel.BabelTextDefinition;
import ch.globaz.common.document.babel.TextMergerBabelTopaz;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.decision.EtatDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculOO;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.home.HomeUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions.PlanCalculTextDefinition;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class PCPlanCalculHandlerOO {

    // enum pour le type de bloc du plan de calcul
    private static enum TypeBloc {
        DEPENSE,
        FORTUNE,
        REVENU,
        TOTAL
    }

    private static final String CSS_SOULIGNE = "souligne";

    private static final String DATE_DEBUT = "{date_debut}";
    private static final String DATE_FIN = "{date_fin}";
    private static final String DECISION_DU = "{date_decision}";
    private static final String DESC_BALISE_FERME = "</span>";
    private static final String DESC_BALISE_OUVRE = "<br /><span class='slib'>";
    private static final String INDENT = "   ";
    private static final String MONNAIE = "CHF";
    private ICTDocument babelDoc = null;
    private DecisionApresCalculOO dacOO = null;
    private PlanDeCalculWitMembreFamilleSearch plaCalMembreFamSearch = null;
    private PCPlanCalculHandler planCalcul = null;

    /**
     * Construction et retourne le data (DocumentData) du plan de calcul
     * 
     * @param data
     * @param document
     * @param tupleRoot
     * @return data, instance de DocumentData contenat le plan de calcul
     * @throws Exception
     */
    public DocumentData build(ICTDocument babelDoc, DecisionApresCalculOO dacOO, DocumentData data,
            TupleDonneeRapport tupleRoot, boolean isMembresFamillesInclus) throws Exception {

        this.dacOO = dacOO;
        this.babelDoc = babelDoc;
        initSearch();
        String idTiersBeneficiaire = dacOO.getDecisionHeader().getPersonneEtendue().getTiers().getIdTiers();

        planCalcul = PCPlanCalculHandler.getHandlerForIdPlanCalcul(getSession(), dacOO.getPlanCalcul()
                .getIdPlanDeCalcul(), idTiersBeneficiaire);

        planCalcul.generateBlocs(tupleRoot, babelDoc.getCodeIsoLangue());
        if (isMembresFamillesInclus) {
            createBlocMembresFamille(data);
        } else {
            data.addData("isPcalMbrfInclude", "FALSE");
        }
        TextMerger<BabelTextDefinition> textMerger = new TextMergerBabelTopaz(babelDoc, data);
        // chargement du tiers et passage de la langue au textGiver pour le libellé HOME
        TITiers tiers = loadTiers(idTiersBeneficiaire);
        textMerger.getTextGiver().setLangue(tiers.getLangueIso());
        createLibelleHome(textMerger, dacOO, tupleRoot);
        createLibelleGeneraux(data, tiers);
        // Bloc fortune
        createBloc(data, planCalcul.getBlocFortune(), TypeBloc.FORTUNE);
        // Bloc revenu
        createBloc(data, planCalcul.getBlocRevenusDeterminants(), TypeBloc.REVENU);
        // Bloc dépenses
        createBloc(data, planCalcul.getBlocDepensesReconnues(), TypeBloc.DEPENSE);
        // Bloc final résumé
        createBlocResume(data, planCalcul.getBlocResume());

        data.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_PLAN_CALCUL);
        // Retourne le documentData mis à jour
        return data;
    }

    private void createLibelleHome(TextMerger<BabelTextDefinition> textMerger, DecisionApresCalculOO dacOO,
            TupleDonneeRapport tupleRoot) {
        Home home;
        try {
            home = HomeUtil.readHomeByPlanCacule(dacOO.getPcAccordee().getSimplePCAccordee(), tupleRoot);
            if (home != null && !home.isNew()) {
                textMerger.addTextToDocument(PlanCalculTextDefinition.HOME_LABEL);
                textMerger.addTextToDocument(PlanCalculTextDefinition.HOME_DESCRIPTION,
                        HomeUtil.formatDesignationHome(home, textMerger.getTextGiver()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Découpe le libelle pour permettre de récupérer la legende
     * 
     * @param ligne
     * @return le libelle simple
     */
    private String[] buildLibelle(PCLignePlanCalculHandler ligne) {
        // recup du libelle
        String lib = ligne.getLibelle();
        // on sépare le libelle et la legende avec une virgule
        lib = PRStringUtils.replaceString(lib, PCPlanCalculHandlerOO.DESC_BALISE_OUVRE, "@");
        // on supprime le span du fond
        lib = PRStringUtils.replaceString(lib, PCPlanCalculHandlerOO.DESC_BALISE_FERME, "");

        String[] tab = lib.split("@");

        return tab;
    }

    /**
     * Création des lignes de chaque bloc du plan de calcul
     * 
     * @param data
     * @param listeLigneBloc
     * @param bloc
     * @return data, DocumentData mis à jour
     */
    private DocumentData createBloc(DocumentData data, ArrayList<PCLignePlanCalculHandler> listeLigneBloc, TypeBloc bloc) {
        // sur plusieurs lignes
        Collection table = getTable(bloc);

        for (PCLignePlanCalculHandler ligne : listeLigneBloc) {

            // on ne prend pas en compte la ligne total
            if (!IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL.equals(ligne.getCsCode())
                    && !IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL.equals(ligne.getCsCode())
                    && !IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL.equals(ligne.getCsCode())) {

                DataList line = getDataList(ligne, bloc);
                DataList subLine = null;
                // gestion indentation.....!!!!
                if (IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES.equals(ligne.getCsCode())
                        || IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP.equals(ligne.getCsCode())
                        || IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU.equals(ligne.getCsCode())
                        || IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU.equals(ligne
                                .getCsCode())) {

                    String libelle = buildLibelle(ligne)[0];
                    line.addData("LIBELLE", PCPlanCalculHandlerOO.INDENT + libelle);
                } else {
                    // gestion libelles/sousLibelles
                    if (buildLibelle(ligne).length == 1) {
                        String libelle = buildLibelle(ligne)[0];
                        line.addData("LIBELLE", libelle);
                    } else {
                        String libelle = buildLibelle(ligne)[0];
                        String sousLibelle = buildLibelle(ligne)[1];
                        line.addData("LIBELLE", libelle);
                        subLine = new DataList("sousLibelle");
                        subLine.addData("LIBELLE", sousLibelle);
                        // table.add(subLine);
                    }

                }

                if (!JadeStringUtil.isEmpty(ligne.getValCol1().getStrValeur())) {
                    line.addData("CHF1", PCPlanCalculHandlerOO.MONNAIE);
                    line.addData("MONTANT_C1", new FWCurrency(ligne.getValCol1().getStrValeur()).toStringFormat());
                }
                if (!JadeStringUtil.isEmpty(ligne.getValCol2().getStrValeur())) {
                    line.addData("CHF2", PCPlanCalculHandlerOO.MONNAIE);
                    line.addData("MONTANT_C2", new FWCurrency(ligne.getValCol2().getStrValeur()).toStringFormat());
                }
                if (!JadeStringUtil.isEmpty(ligne.getValCol3().getStrValeur())) {
                    line.addData("CHF3", PCPlanCalculHandlerOO.MONNAIE);
                    line.addData("MONTANT_C3", new FWCurrency(ligne.getValCol3().getStrValeur()).toStringFormat());
                }

                table.add(line);
                // TODO hack a voir...
                if (subLine != null) {
                    table.add(subLine);
                }

            } else {
                data = createLastligne(data, bloc, listeLigneBloc.get(listeLigneBloc.size() - 1));
            }

        }

        data.add(table);

        return data;
    }

    private void createBlocMembresFamille(DocumentData data) {

        data.addData("isPcalMbrfInclude", "TRUE");
        Collection tableFamilles = new Collection("tabFamille");
        DataList ayantDroit = new DataList("ayantDroit");
        ayantDroit.addData("AYANT_DROIT_LIBELLE", babelDoc.getTextes(1).getTexte(12).getDescription());
        ayantDroit.addData("AYANT_DROIT", planCalcul.getRequerantInfos() + " , "
                + babelDoc.getTextes(1).getTexte(11).getDescription() + " " + planCalcul.getNssInfos());
        tableFamilles.add(ayantDroit);

        if (planCalcul.getConjointInfos() != null) {
            DataList conjoint = new DataList("conjoint");
            conjoint.addData("CONJOINT_LIBELLE", babelDoc.getTextes(1).getTexte(13).getDescription());
            conjoint.addData("CONJOINT", planCalcul.getConjointInfos());
            tableFamilles.add(conjoint);
        }

        StringBuilder enfantCompris = new StringBuilder("");

        // -2 on enleve la derniere virgule
        if (enfantCompris.length() > 0) {
            for (String enfant : planCalcul.getEnfantsCompris()) {
                enfantCompris.append(enfant).append(", ");
            }
            DataList enfants = new DataList("enfants");

            enfants.addData("ENFANT_COMPRIS_LIBELLE", babelDoc.getTextes(1).getTexte(14).getDescription());
            enfants.addData("ENFANT_COMPRIS", enfantCompris.substring(0, enfantCompris.length() - 2).toString());
            tableFamilles.add(enfants);
        }
        data.add(tableFamilles);
    }

    /**
     * Création du bloc résumé final du plan de calcul
     * 
     * @param data
     * @param liste
     * @return data
     */
    private DocumentData createBlocResume(DocumentData data, ArrayList<PCLignePlanCalculHandler> liste) {
        int cptLigne = 0;// Compteur de ligne
        for (PCLignePlanCalculHandler ligne : liste) {
            // premiere ligne excedent revenus ou depenses
            if (cptLigne == 0) {
                data.addData("totalL1", ligne.getLibelle());
                data.addData("CHF1L1", PCPlanCalculHandlerOO.MONNAIE);
                data.addData("MONTANTL1_C1", new FWCurrency(ligne.getValCol1().getStrValeur()).toStringFormat());
                data.addData("CHF2L1", PCPlanCalculHandlerOO.MONNAIE);
                data.addData("MONTANTL1_C2", new FWCurrency(ligne.getValCol2().getStrValeur()).toStringFormat());
                data.addData("CHF3L1", PCPlanCalculHandlerOO.MONNAIE);
                data.addData("MONTANTL1_C3", new FWCurrency(ligne.getValCol3().getStrValeur()).toStringFormat());
            } else {
                if ((cptLigne == 1) && (liste.size() == 3)) {
                    // Cas ass maladie
                    data.addData("totalL2", ligne.getLibelle());
                    data.addData("CHF3L2", PCPlanCalculHandlerOO.MONNAIE);
                    data.addData("MONTANTL2_C3", new FWCurrency(ligne.getValCol3().getStrValeur()).toStringFormat());
                } else {
                    // cas ligne finale pca mensuelle
                    data.addData("totalL3", ligne.getLibelle());
                    data.addData("CHF3L3", PCPlanCalculHandlerOO.MONNAIE);
                    data.addData("MONTANTL3_C3", new FWCurrency(ligne.getValCol3().getStrValeur()).toStringFormat());
                }
            }

            cptLigne++;
        }
        return data;
    }

    /**
     * Création de la ligne total des différents blcos du plan de calcul
     * 
     * @param data
     *            , instance DocumentData
     * @param bloc
     *            , type de bloc du plan de calcul
     * @param ligne
     *            , ligne du plan de calcul
     * @return, data
     */
    private DocumentData createLastligne(DocumentData data, TypeBloc bloc, PCLignePlanCalculHandler ligne) {
        switch (bloc) {

            case FORTUNE:
                data.addData("LIBELLETOTAL_F", ligne.getLibelle());//
                data.addData("CHF", PCPlanCalculHandlerOO.MONNAIE);
                data.addData("MONTANT_TOTAL_F", new FWCurrency(ligne.getValCol3().getStrValeur()).toStringFormat());
                break;

            case DEPENSE:
                data.addData("LIBELLETOTAL_D", ligne.getLibelle());//
                data.addData("CHF", PCPlanCalculHandlerOO.MONNAIE);
                data.addData("MONTANT_TOTAL_D", new FWCurrency(ligne.getValCol3().getStrValeur()).toStringFormat());
                break;

            case REVENU:
                data.addData("LIBELLETOTAL_R", ligne.getLibelle());//
                data.addData("CHF", PCPlanCalculHandlerOO.MONNAIE);
                data.addData("MONTANT_TOTAL_R", new FWCurrency(ligne.getValCol3().getStrValeur()).toStringFormat());
                break;

            case TOTAL:
                // table = new Collection("tabTotal");
                break;
        }
        return data;
    }

    private DocumentData createLibelleGeneraux(DocumentData data, TITiers tiers)
            throws PersonneDansPlanCalculException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {
        // Requerant infos et mebre famille

        String toAppendToPcalHeader = "";
        // Construction du au, dés le
        if (JadeStringUtil.isEmpty(dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision())) {
            toAppendToPcalHeader = PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(1).getDescription(),
                    PCPlanCalculHandlerOO.DATE_DEBUT, "01."
                            + dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision());
        } else {
            int month = Integer.parseInt(dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision()
                    .substring(0, 2));
            int year = Integer.parseInt(dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision()
                    .substring(3));

            toAppendToPcalHeader = PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(2).getDescription(),
                    PCPlanCalculHandlerOO.DATE_DEBUT, "01."
                            + dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision())
                    + " "
                    + PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(3).getDescription(),
                            PCPlanCalculHandlerOO.DATE_FIN, PegasusDateUtil.getLastDayOfMonth(month - 1, year) + "."
                                    + dacOO.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision());
        }

        // PAGE
        data.addData("PAGE_NUMERO",
                LanguageResolver.resolveLibelleFromLabel(tiers.getLangueIso(), "PAGE", getSession()));

        data.addData(
                "PCAL_HEADER",
                PRStringUtils.replaceString(babelDoc.getTextes(1).getTexte(4).getDescription(),
                        PCPlanCalculHandlerOO.DECISION_DU, dacOO.getDecisionHeader().getSimpleDecisionHeader()
                                .getDateDecision())
                        + " " + toAppendToPcalHeader);
        data.addData("PCAL_JUSTIFICATIF", babelDoc.getTextes(1).getTexte(10).getDescription());
        data.addData("PCAL_FORTUNE", babelDoc.getTextes(1).getTexte(5).getDescription());
        data.addData("PCAL_REVENUS", babelDoc.getTextes(1).getTexte(6).getDescription());
        data.addData("PCAL_DEPENSES", babelDoc.getTextes(1).getTexte(7).getDescription());

        // Double versement, 2 rentes principales à domicilels
        if (!JadeStringUtil.isBlankOrZero(dacOO.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionConjoint())
                && getEtatDecision().equals(EtatDecision.OCTROI)) {
            data.addData("isPrestationDouble", "TRUE");
            String preLibelle = babelDoc.getTextes(1).getTexte(8).getDescription();
            data.addData("VERSMENT_DOUBLE_TITRE", babelDoc.getTextes(1).getTexte(9).getDescription());
            // Valeurs requerant
            data.addData("LIBELLE_REQ", preLibelle + " " + getPersonneAsString(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
            data.addData("MONTANT_REQ", new FWCurrency(dacOO.getPcAccordee().getSimplePrestationsAccordees()
                    .getMontantPrestation()).toStringFormat());
            // Valuers conjoint
            data.addData("LIBELLE_CON", preLibelle + " " + getPersonneAsString(IPCDroits.CS_ROLE_FAMILLE_CONJOINT));
            data.addData("MONTANT_CON", new FWCurrency(dacOO.getPcAccordee().getSimplePrestationsAccordeesConjoint()
                    .getMontantPrestation()).toStringFormat());

        } else {
            data.addData("isPrestationDouble", "FALSE");
        }
        return data;
    }

    /**
     * Retourne le flavor a appliquer pour la ligne du template OO
     * 
     * @param ligne
     * @param bloc
     * @return instance de DataList
     */
    private DataList getDataList(PCLignePlanCalculHandler ligne, TypeBloc bloc) {
        // Si ligne 1 souligné
        if (PCPlanCalculHandlerOO.CSS_SOULIGNE.equals(ligne.getValCol1().getCssClass())) {
            // Si colone 1 et 2
            if (PCPlanCalculHandlerOO.CSS_SOULIGNE.equals(ligne.getValCol2().getCssClass())) {
                // Si colone 1 ET 2 ET 3 souligné
                if (PCPlanCalculHandlerOO.CSS_SOULIGNE.equals(ligne.getValCol3().getCssClass())) {
                    return new DataList("ligneColUnATroisS");
                } else {
                    // COL 1 ET COL 2
                    return new DataList("ligneColUnDeuxS");
                }
            } else {
                // Colone1 seul
                return new DataList("ligneColUnS");
            }
        }
        // Si colone 2 souligné
        if (PCPlanCalculHandlerOO.CSS_SOULIGNE.equals(ligne.getValCol2().getCssClass())) {
            if (PCPlanCalculHandlerOO.CSS_SOULIGNE.equals(ligne.getValCol3().getCssClass())) {
                return new DataList("ligneColDeuxTroisS");
            } else {
                return new DataList("ligneColDeuxS");
            }
        }

        if (PCPlanCalculHandlerOO.CSS_SOULIGNE.equals(ligne.getValCol3().getCssClass())) {
            return new DataList("ligneColTroisS");
        }
        return new DataList("ligneStandard");
    }

    private EtatDecision getEtatDecision() throws DecisionException {

        if (dacOO.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {
            return EtatDecision.OCTROI;
        } else if (dacOO.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
            return EtatDecision.PARTIEL;
        } else {
            return EtatDecision.REFUS;
        }

    }

    private String getPersonneAsString(String membre) {
        String retString = null;

        for (JadeAbstractModel model : plaCalMembreFamSearch.getSearchResults()) {
            // si le mebre de famille est emfant ou conjoint

            if (((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getSimpleDroitMembreFamille()
                    .getCsRoleFamillePC().equals(membre)) {
                retString = (((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille().getNom()
                        + " " + ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                        .getPrenom());
            }
        }
        return retString;
    }

    private BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    /**
     * Retourne en fonction du type de bloc, l'identifiant du tableau pour le template OO
     * 
     * @param bloc
     * @return instance Collection (ch.globaz.datajuicer)
     */
    private Collection getTable(TypeBloc bloc) {
        Collection table = null;

        switch (bloc) {
            case FORTUNE:
                table = new Collection("tabFortune");
                break;
            case DEPENSE:
                table = new Collection("tabDepense");
                break;
            case REVENU:
                table = new Collection("tabRevenu");
                break;
            case TOTAL:
                table = new Collection("tabTotal");
                break;
        }
        return table;
    }

    private TITiers loadTiers(String idTiers) {

        if (idTiers == null) {
            throw new CommonTechnicalException("the idTiers can't be null");
        }

        TITiers tiersToReturn = new TITiers();
        try {
            tiersToReturn.setId(idTiers);
            tiersToReturn.setSession(getSession());
            tiersToReturn.retrieve();
        } catch (Exception e) {
            throw new CommonTechnicalException(
                    "An exception happened while trying to load the tiers for the following id : [" + idTiers + "]");
        }
        return tiersToReturn;
    }

    private void initSearch() throws PersonneDansPlanCalculException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        // recherche des membres familles compris dans le calcul PC, avec id pcaccordées
        PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
        search.setForIdPcal(dacOO.getPlanCalcul().getIdPlanDeCalcul());// (this.planDeCalcul.getIdPCAccordee());
        search.setForComprisPcal(Boolean.TRUE);
        search.setOrderKey("orderByNaissance");
        // search.setForCsRoleFamille(IPCDroits.CS_ROLE_FAMILLE_ENFANT);
        plaCalMembreFamSearch = PegasusServiceLocator.getPCAccordeeService().search(search);
    }
}
