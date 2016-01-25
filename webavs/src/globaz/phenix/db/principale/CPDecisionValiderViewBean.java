package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.communications.CPValidationCalculCommunicationManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPDecisionValiderViewBean extends CPDecision implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private String colonneSelection = "";
    private Vector<String> docListe = new Vector<String>();
    private String libellePassage = "";
    private globaz.musca.api.IFAPassage passage = null;
    private globaz.pyxis.db.tiers.TITiersViewBean tiers = null;
    private Boolean validationForcee = new Boolean(false);

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // --- Initialisation des zones
        setIdPassage("");
        setLibellePassage("");
        try {
            // -------- Recherche des données de l'affilié --------
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(getIdTiers());
            persAvs.retrieve(transaction);
            setTiers(persAvs);
            // -------- Recherche des données de base pour l'encodage --------
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
        try {
            // Recherche du prochain id passage à facturer
            globaz.musca.api.IFAPassage passage = null;
            // Recherche si séparation indépendant et non-actif - Inforom 314s
            Boolean isSeprationIndNac = false;
            try {
                isSeprationIndNac = new Boolean(GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .getProperty(FAApplication.SEPARATION_IND_NA));
            } catch (Exception e) {
                isSeprationIndNac = Boolean.FALSE;
            }
            if (isSeprationIndNac) {
                if (isNonActif()) {
                    passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                            FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
                } else {
                    passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                            FAModuleFacturation.CS_MODULE_COT_PERS_IND);
                }
            } else {
                passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                        FAModuleFacturation.CS_MODULE_COT_PERS);
            }
            if (passage != null) {
                setIdPassage(passage.getIdPassage());
                if (passage.getLibelle().length() > 0) {
                    setLibellePassage(passage.getLibelle());
                }
                setPassage(passage);
            }
            if (JadeStringUtil.isEmpty(getResponsable())) {
                setResponsable(getSession().getUserId());
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0094"));

        }
    }

    @Override
    public void _afterUpdate(BTransaction transaction) throws Exception {
        // Créer l'état "valider"
        try {
            // Supprimer les sorties de la même année
            CPSortieManager manager = new CPSortieManager();
            manager.setSession(getSession());
            manager.changeManagerSize(0);
            manager.setForAnnee(getAnneeDecision());
            manager.setForIdPassage(getIdPassage());
            // PO 5265
            manager.setForIdAffiliation(getIdAffiliation());
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                CPSortie mySortie = (CPSortie) manager.getEntity(i);
                mySortie.delete();
            }
            // Metre l'état de la communication en retour à validé
            if (!JadeStringUtil.isIntegerEmpty(getIdCommunication())) {
                CPCommunicationFiscaleRetourViewBean comRetour = new CPCommunicationFiscaleRetourViewBean();
                comRetour.setSession(getSession());
                comRetour.setIdRetour(getIdCommunication());
                comRetour.retrieve(transaction);
                if (!comRetour.isNew()) {
                    comRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_VALIDE);
                    comRetour.update(transaction);
                    // Mise à jour du journal
                    CPJournalRetour jrnRetour = new CPJournalRetour();
                    jrnRetour.setSession(getSession());
                    jrnRetour.setIdJournalRetour(comRetour.getIdJournalRetour());
                    jrnRetour.retrieve(transaction);
                    if (!jrnRetour.isNew()) {
                        jrnRetour.setStatus(CPJournalRetour.CS_VALIDE_PARTIEL);
                        jrnRetour.update(transaction);
                    }
                    // On va mettre a jour le codeValidation
                    CPValidationCalculCommunicationManager calculManager = new CPValidationCalculCommunicationManager();
                    calculManager.setSession(getSession());
                    calculManager.setForIdDecision(getIdDecision());
                    calculManager.find();
                    for (int i = 0; i < calculManager.getSize(); i++) {
                        CPValidationCalculCommunication calcul = (CPValidationCalculCommunication) calculManager
                                .getEntity(i);
                        calcul.setValidation(Boolean.FALSE);
                        calcul.update();
                    }
                }
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0095") + e.getMessage());
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        FAPassage passage = new FAPassage();
        passage.setSession(getSession());
        passage.setIdPassage(getIdPassage());
        passage.retrieve();
        // Erreur si la décision na pas de cotisation
        if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision())) {
            if (!validationForcee.booleanValue() || getImpression().equals(Boolean.TRUE)) {
                CPCotisationManager cotiManager = new CPCotisationManager();
                cotiManager.setSession(getSession());
                cotiManager.setForIdDecision(getIdDecision());
                if (cotiManager.getCount() == 0) {
                    _addError(statement.getTransaction(), getAffiliation().getAffilieNumero() + " : "
                            + getSession().getLabel("CP_MSG_0178"));
                }
            }
        } else {
            // PO 7630 - Le contrôle du montant encodé ne peut se faire qu'avec les CI et non les compteurs
            // du fait que la cotisation n'est peut être pas encore facturée (année en cours)
            // --
            // Recherche du montant d'imputation - Prendre toute les imputations à l'état validé
            // 2 cas de figure différents:
            // 1) Il y a aussi une décision validée (imputation + décision dans le même passage)
            // 2) la décision est déjà comptabilisée
            testerMontantImputationSaisi(statement);
        }
        if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
            _addError(statement.getTransaction(), getSession().getLabel("NUMPASSAGE_INVALIDE"));
        } else if (passage.getStatus().equals(FAPassage.CS_ETAT_COMPTABILISE) || passage.isEstVerrouille()) {
            _addError(statement.getTransaction(),
                    getAffiliation().getAffilieNumero() + " : " + getSession().getLabel("CP_MSG_0177"));
        } else {
            // PO 3916
            FAPassageModuleManager modPassManager = new FAPassageModuleManager();
            modPassManager.setSession(getSession());
            modPassManager.setForIdPassage(getIdPassage());
            Boolean isSeprationIndNac = Boolean.FALSE;
            try {
                isSeprationIndNac = new Boolean(GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .getProperty(FAApplication.SEPARATION_IND_NA));
            } catch (Exception e) {
                isSeprationIndNac = Boolean.FALSE;
            }
            if (isSeprationIndNac) {
                if (isNonActif()) {
                    modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
                } else {
                    modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS_IND);
                }
            } else {
                modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS);
            }
            modPassManager.find();
            if (modPassManager.getSize() == 0) {
                _addError(statement.getTransaction(), getAffiliation().getAffilieNumero() + " : "
                        + getSession().getLabel("CP_MSG_0193"));
            }
        }
        /*
         * PO 01562: possibilité de forcer la validation et de bypasser les plausibilités décrit dans le PO
         */
        if (!statement.getTransaction().hasErrors() && !validationForcee.booleanValue()
                && (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision()))) {
            boolean trouve = false;
            // Test si décision même genre même période dans même passage
            CPDecisionManager decisionMng = new CPDecisionManager();
            decisionMng.setSession(getSession());
            decisionMng.setForIdTiers(getIdTiers());
            decisionMng.setForIdAffiliation(getIdAffiliation());
            decisionMng.setForIdPassage(getIdPassage());
            decisionMng.setForGenreAffilie(getGenreAffilie());
            decisionMng.setForAnneeDecision(getAnneeDecision());
            decisionMng.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
            decisionMng.find();
            int j = 0;
            while (j < decisionMng.size()) {
                CPDecision decisionTrouvee = (CPDecision) decisionMng.getEntity(j);
                long dateDebutDecisionAvalider = Long.parseLong(JACalendar.format(getDebutDecision(),
                        JACalendar.FORMAT_YYYYMMDD));
                long dateFinDecisionAvalider = Long.parseLong(JACalendar.format(getFinDecision(),
                        JACalendar.FORMAT_YYYYMMDD));
                long dateDebutDecisionTrouvee = Long.parseLong(JACalendar.format(decisionTrouvee.getDebutDecision(),
                        JACalendar.FORMAT_YYYYMMDD));
                long dateFinDecisionTrouvee = Long.parseLong(JACalendar.format(decisionTrouvee.getFinDecision(),
                        JACalendar.FORMAT_YYYYMMDD));
                if (((dateDebutDecisionAvalider <= dateDebutDecisionTrouvee) && (dateFinDecisionAvalider >= dateDebutDecisionTrouvee))
                        || ((dateDebutDecisionAvalider <= dateFinDecisionTrouvee) && (dateDebutDecisionTrouvee <= dateDebutDecisionAvalider))) {
                    trouve = true;
                }
                j++;
            }
            if (trouve) {
                _addError(statement.getTransaction(), getAffiliation().getAffilieNumero() + " : "
                        + getSession().getLabel("CP_MSG_0190"));
            }
        }
        String anneeEncours = Integer.toString(JACalendar.getYear(JACalendar.today().toString()));
        // Ne pas valider une décision dans un passage périodique si la période
        // de décision est comprise dans la période
        // de la facturation périodique
        // Ex: Si Périodique de juin => décision ok si dateFinDecision
        // <01.06.anneeencours
        // impossible si dateFindecision > 01.06 et date de début <30.06
        if (!passage.isNew() && FAPassage.CS_TYPE_PERIODIQUE.equals(passage.getIdTypeFacturation())
                && getAnneeDecision().equalsIgnoreCase(anneeEncours)) {
            AFAffiliation affi = new AFAffiliation();
            if (getAffiliation() == null) {
                affi.setSession(getSession());
                affi.setAffiliationId(getIdAffiliation());
                affi.retrieve();
            } else {
                affi = getAffiliation();
            }
            if (!CPToolBox.controlePeriodique(getSession(), getDebutDecision(), getFinDecision(), passage,
                    anneeEncours, affi)) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0110") + " "
                        + getAffiliation().getAffilieNumero() + ": " + getSession().getLabel("CP_MSG_0096"));
            }
        }
    }

    public void addDoc(String docName) {
        docListe.add(docName);
    }

    protected BigDecimal ajoutImputationValidee(CPDonneesCalcul donneeCalcul, BigDecimal montantCIImputation,
            CPDecisionManager decM) throws Exception {
        String varCIImputation;
        // => test si il n'y a pas une autre imputation de validé
        CPDecisionManager impM = new CPDecisionManager();
        impM.setSession(getSession());
        impM.setForIdAffiliation(getIdAffiliation());
        impM.setForAnneeDecision(getAnneeDecision());
        impM.setForTypeDecision(CPDecision.CS_IMPUTATION);
        impM.setForEtat(CPDecision.CS_VALIDATION);
        impM.find();
        Boolean stopLecture = false;
        for (int j = 0; (j < impM.getSize()) && (stopLecture == false); j++) {
            CPDecision imputationValidee = (CPDecision) impM.getEntity(j);
            varCIImputation = donneeCalcul.getMontant(imputationValidee.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
            if (JadeStringUtil.isEmpty(varCIImputation) == false) {
                montantCIImputation = montantCIImputation
                        .add(new BigDecimal(JANumberFormatter.deQuote(varCIImputation)));
            }
            if (imputationValidee.getComplementaire() == Boolean.TRUE) {
                stopLecture = true;
            }
        }
        return montantCIImputation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    public String getColonneSelection() {
        return colonneSelection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.06.2003 15:44:10)
     * 
     * @return java.util.Vector
     */
    public java.util.Vector<String> getDocListe() {
        return docListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public String getLibelleGenreAffilie() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getGenreAffilie());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getLibelleTypeDecision() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getTypeDecision());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 10:24:14)
     * 
     * @return globaz.musca.api.IFAPassage
     */
    public globaz.musca.api.IFAPassage getPassage() {
        return passage;
    }

    /**
     * @return
     */
    public Boolean getValidationForcee() {
        return validationForcee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 13:16:17)
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    @Override
    public globaz.pyxis.db.tiers.TITiersViewBean loadTiers() {
        return tiers;
    }

    protected String rechercheNumAvs() throws Exception {
        String numAvs = "";
        String dateNss = ((CPApplication) getSession().getApplication()).getDateNNSS();
        if (BSessionUtil.compareDateFirstLower(getSession(), getFinDecision(), dateNss)) {
            TIHistoriqueAvs hist = new TIHistoriqueAvs();
            hist.setSession(getSession());
            try {
                numAvs = hist.findPrevKnownNumAvs(getIdTiers(), getFinDecision());
                if (JadeStringUtil.isEmpty(numAvs)) {
                    numAvs = hist.findNextKnownNumAvs(getIdTiers(), getDebutDecision());
                }
            } catch (Exception e) {
                numAvs = "";
            }
        }
        // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n° avs
        if (JadeStringUtil.isEmpty(numAvs)) {
            TITiersViewBean tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            tiers.setIdTiers(getIdTiers());
            tiers.retrieve();
            if (!tiers.isNew()) {
                numAvs = tiers.getNumAvsActuel();
            }
        }
        return numAvs;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    public void setColonneSelection(String value) {
        colonneSelection = value;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.06.2003 15:44:10)
     * 
     * @param newDocListe
     *            java.util.Vector
     */
    public void setDocListe(java.util.Vector<String> newDocListe) {
        docListe = newDocListe;
    }

    public void setLibellePassage(String string) {
        libellePassage = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 10:24:14)
     * 
     * @param newPassage
     *            globaz.musca.api.IFAPassage
     */
    public void setPassage(globaz.musca.api.IFAPassage newPassage) {
        passage = newPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 13:16:17)
     * 
     * @param newTiers
     *            globaz.pyxis.db.tiers.TITiersViewBean
     */
    @Override
    public void setTiers(globaz.pyxis.db.tiers.TITiersViewBean newTiers) {
        tiers = newTiers;
    }

    /**
     * @param boolean1
     */
    public void setValidationForcee(Boolean boolean1) {
        validationForcee = boolean1;
    }

    protected void testerMontantImputationSaisi(globaz.globall.db.BStatement statement) throws Exception,
            NumberFormatException {
        // PO 7630 - Le contrôle du montant encodé ne peut se faire qu'avec les CI et non les compteurs
        // du fait que la cotisation n'est peut être pas encore facturée (année en cours)
        // --
        // Recherche du montant d'imputation - Prendre toute les imputations à l'état validé
        // 2 cas de figure différents:
        // 1) Il y a aussi une décision validée (imputation + décision dans le même passage)
        // 2) la décision est déjà comptabilisée
        CPDonneesCalcul donneeCalcul = new CPDonneesCalcul();
        donneeCalcul.setSession(getSession());
        String varCIImputation = donneeCalcul.getMontant(getIdDecision(), CPDonneesCalcul.CS_REV_CI);
        BigDecimal montantCIImputation = new BigDecimal("0");
        if (JadeStringUtil.isEmpty(varCIImputation) == false) {
            montantCIImputation = new BigDecimal(JANumberFormatter.deQuote(varCIImputation));
        }

        BigDecimal montantCP = new BigDecimal(0);
        // Recherche de toute les décisions validéee (cas ou il y aurait des complémentaires)
        CPDecisionManager decM = new CPDecisionManager();
        decM.setSession(getSession());
        decM.setForIdAffiliation(getIdAffiliation());
        decM.setForAnneeDecision(getAnneeDecision());
        decM.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
        decM.setForEtat(CPDecision.CS_VALIDATION);
        decM.find();
        if (decM.getSize() > 0) {
            for (int i = 0; i < decM.getSize(); i++) {
                CPDecision decVal = (CPDecision) decM.getEntity(i);
                String montantCIDecision = donneeCalcul.getMontant(decVal.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                CPDonneesBase base = new CPDonneesBase();
                base.setSession(getSession());
                base.setIdDecision(decVal.getIdDecision());
                base.retrieve();
                if (!base.isNew() && !JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {
                    float revenuCiImputation = 0;
                    // recherche coti payé en tant que salarié
                    float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
                    // Calcul du Ci qui doit être imputer selon le montant
                    // de cotisation payé en tant que salarié
                    CPCotisation coti = CPCotisation._returnCotisation(getSession(), decVal.getIdDecision(),
                            CodeSystem.TYPE_ASS_COTISATION_AVS_AI);

                    if (coti != null) {
                        // Calcul du CI
                        revenuCiImputation = (Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel())) - cotiEncode)
                                * (float) 9.9;
                        revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2, JANumberFormatter.NEAR);
                        revenuCiImputation = Float.parseFloat(JANumberFormatter.deQuote(montantCIDecision))
                                - revenuCiImputation;
                    } else {
                        revenuCiImputation = cotiEncode * (float) 9.9;
                        revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2, JANumberFormatter.NEAR);
                    }
                    montantCP = montantCP.add(new BigDecimal(Float.parseFloat(JANumberFormatter
                            .deQuote(montantCIDecision)) - revenuCiImputation));
                } else {
                    montantCP = montantCP.add(new BigDecimal(JANumberFormatter.deQuote(montantCIDecision)));
                }
            }
            // Si la décision d'imputation annule et remplace (getComplementaire=true car le sens du libellé dans
            // l'écran est inversé) on test uniquement par rapport à la décision
            // Si elle est complémentaire (getComplementaire=false) :on soustrait l'éventuel montant d'imputation
            // qu'il y aurait dans les cis.
            // et l'on regarde si n'y a pas déjà une autre imputation de validé
            if (Boolean.FALSE.equals(getComplementaire())) { // => Test si imputation annule et remplace ou
                                                             // complémentaire
                // Si décision complémentaire (annule et remplace = false)
                // => rechercher si un éventuel montant ci d'imputation existe
                String numAvs = rechercheNumAvs();
                CICompteIndividuelUtil ci = new CICompteIndividuelUtil();
                ci.setSession(getSession());
                montantCP = montantCP.add(ci.getSommeParAnneeCodeAmortissement(numAvs, getAffiliation()
                        .getAffilieNumero(), "", getAnneeDecision(), CIEcriture.CS_CODE_MIS_EN_COMTE));
                // Ajout des imputation qui seraient déjà à l'état validé (cas peu probable mais possible)
                montantCIImputation = ajoutImputationValidee(donneeCalcul, montantCIImputation, decM);
            }
            if ((montantCP.compareTo(montantCIImputation) == -1) || (montantCP.compareTo(new BigDecimal(0)) == -1)) {
                _addError(statement.getTransaction(), getAffiliation().getAffilieNumero() + " : "
                        + getSession().getLabel("CP_MSG_0205") + " (" + montantCP.toString() + " , "
                        + montantCIImputation + ")");
            }
        } else {
            String numAvs = rechercheNumAvs();
            java.math.BigDecimal montantEnCI = new BigDecimal(0);
            CICompteIndividuelUtil ci = new CICompteIndividuelUtil();
            ci.setSession(getSession());
            if (getComplementaire().equals(Boolean.TRUE)) {
                // cas annule et remplace
                // recherche du montant CI sans les imputations car ce montant sera remplacé
                montantEnCI = ci.getSommeParAnneeGenre(numAvs, getAnneeDecision(), false, "", true);
            } else {
                // recherche du montant non actif CI pour l'année de l'imputation
                montantEnCI = ci.getSommeParAnneeGenre(numAvs, getAnneeDecision(), true, "", true);
            }
            // Ajout des imputation qui seraient déjà à l'état validé (cas peu probable mais possible)
            if (Boolean.FALSE.equals(getComplementaire())) {
                montantCIImputation = ajoutImputationValidee(donneeCalcul, montantCIImputation, decM);
            }
            if (montantEnCI.compareTo(montantCIImputation) == -1) {
                _addError(statement.getTransaction(), getAffiliation().getAffilieNumero() + " : "
                        + getSession().getLabel("CP_MSG_0204") + " (" + montantEnCI.toString() + " , "
                        + montantCIImputation + ")");
            }
        }
    }

}
