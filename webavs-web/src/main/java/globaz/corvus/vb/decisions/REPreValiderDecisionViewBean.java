package globaz.corvus.vb.decisions;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.utils.REGestionnaireHelper;
import globaz.corvus.utils.REPmtMensuel;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author SCR
 */
public class REPreValiderDecisionViewBean extends PRAbstractViewBeanSupport {

    private static final Object[] METHODES_SEL_ADRESSE_COURRIER = new Object[] { new String[] {
            "idTiersAdresseCourrierPyxis", "getIdTiers" } };
    private static final Object[] METHODES_SEL_COPIE_ADMIN = new Object[] {
            new String[] { "idTiersDepuisPyxis", "idTiers" }, new String[] { "nomTiersDepuisPyxis", "nom" } };
    private static final Object[] METHODES_SEL_COPIE_TIERS = new Object[] {
            new String[] { "idTiersDepuisPyxis", "idTiers" }, new String[] { "nomTiersDepuisPyxis", "nom" },
            new String[] { "numAffilieDepuisPyxis", "numAffilieActuel" } };

    private String adresseCourrier = "";
    private String adresseFormattee = "";
    private String adressePaiementFormatee = "";
    private List<REAnnexeDecisionViewBean> annexesList = new ArrayList<REAnnexeDecisionViewBean>();
    private StringBuffer avertissementCopie = new StringBuffer();
    private List<RECopieDecisionViewBean> copiesList = new ArrayList<RECopieDecisionViewBean>();
    private String csGenreDecision = "";
    private String csTypePreparationDecision = null;
    private String dateDecision = null;
    REDecisionEntity decision = new REDecisionEntity();
    private REDecisionsContainer decisionContainer = null;
    private String decisionDepuis = null;
    public List documentsPreview = new ArrayList();
    private String eMailAddress = null;
    private Boolean hasRemarqueIncarceration = Boolean.FALSE;
    /**
     * Remarque dans la décision : Remariage d'une rente de survivant
     */
    private Boolean hasRemarqueRemariageRenteDeSurvivant = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente avec début du droit 5 ans avant date de dépôt de la demande
     */
    private Boolean hasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente avec montant minimum majoré pour invalidité précoce
     */
    private Boolean hasRemarqueRenteAvecMontantMinimumMajoreInvalidite = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente avecsupplément pour personne veuve
     */
    private Boolean hasRemarqueRenteAvecSupplementPourPersonneVeuve = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente de VEUF (m) limitée
     */
    private Boolean hasRemarqueRenteDeVeufLimitee = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente de VEUVE (f) limitée
     */
    private Boolean hasRemarqueRenteDeVeuveLimitee = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente réduite pour surassurance
     */
    private Boolean hasRemarqueRenteReduitePourSurassurance = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente pour enfants
     */
    private Boolean hasRemarqueRentesPourEnfants = Boolean.FALSE;
    private String idDecision = "";
    private String idDemandeRente = null;
    private String idTierAdresseCourrier = "";
    private String idTiersBeneficiairePrincipal = null;
    private String idTiersDepuisPyxis = "";
    private String idTiersRequerant = null;
    /**
     * ACK !! BZ5538 Lors de la préparation de la décision dans REPreValiderDecisionHelper, un message d'information
     * doit pouvoir être passé à l'écran de détails sans bloquer le flux normal de l'application.
     */
    private String informationMessage;
    private Boolean isAvecBonneFoi = Boolean.FALSE;
    private Boolean isDepuisRcListDecision = Boolean.FALSE;
    private boolean isInteretMoratoire = false;
    private Boolean isObligPayerCoti = Boolean.FALSE;
    private Boolean isRemAnnDeci = Boolean.FALSE;
    private Boolean isRemRedPlaf = Boolean.FALSE;
    private Boolean isRemSuppVeuf = Boolean.FALSE;
    private Boolean isRetourPyxis = Boolean.FALSE;
    private Boolean isSansBonneFoi = Boolean.FALSE;

    private List<REAnnexeDecisionViewBean> lstAnnexe = null;
    private List<RECopieDecisionViewBean> lstCopie = null;
    List lstCopieTronquee = null;
    private Map mapKey = new TreeMap();
    /**
     * BZ 5342 Lorsque l'on valide la décision, il peut y avoir des retenues sur certaines rentes accordées. On informe
     * l'utilisateur de ce fait à l'arrivée dans le nouvel écran PRE2002
     */
    private String messageRetenueSurRente = "";
    private String nomTiersDepuisPyxis = "";
    private String nssTiersBeneficiaire = null;
    private String numAffilieDepuisPyxis = "";
    private String remarqueDecision = "";
    private String requerantInfo = null;
    private String testRetenue = null;

    private String tiersBeneficiairePrincipalInfo = null;

    private String traiterParDecision = "";

    /**
     * charge une adresse de courrier valide.
     */
    private void chargerAdresseCourrier(BSession session) throws Exception {

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        if (JadeStringUtil.isIntegerEmpty(getIdTiersBeneficiairePrincipal())) {
            return;
        }

        // Si le tiers bénéficiaire principale n'a pas d'adresse de courrier, il
        // faut prendre l'adresse du tiers de l'
        // adresse de paiement de la ra
        String adresse = "";

        if (JadeStringUtil.isBlankOrZero(getIdTierAdresseCourrier())) {
            setIdTierAdresseCourrier(decision.getIdTiersAdrCourrier());
        }

        if (JadeStringUtil.isBlankOrZero(getIdTierAdresseCourrier())) {
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(session, getIdTiersBeneficiairePrincipal(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");

            setIdTierAdresseCourrier(getIdTiersBeneficiairePrincipal());
        } else {
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(session, getIdTierAdresseCourrier(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");
        }

        if (JadeStringUtil.isBlankOrZero(adresse)) {

            REValidationDecisionsManager mgr = new REValidationDecisionsManager();
            mgr.setSession(getSession());
            mgr.setForIdDecision(getIdDecision());
            mgr.find();

            REPrestationDue pd = new REPrestationDue();
            pd.setSession(getSession());
            pd.setIdPrestationDue(((REValidationDecisions) mgr.getFirstEntity()).getIdPrestationDue());
            pd.retrieve();

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(pd.getIdRenteAccordee());
            ra.retrieve();

            REInformationsComptabilite infoCom = new REInformationsComptabilite();
            infoCom.setSession(getSession());
            infoCom.setIdInfoCompta(ra.getIdInfoCompta());
            infoCom.retrieve();

            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(session, infoCom.getIdTiersAdressePmt(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");

        }

        setAdresseFormattee(adresse);
    }

    private void chargerAdressePaiement(BSession session) throws Exception {

        REValidationDecisionsManager mgr = new REValidationDecisionsManager();
        mgr.setSession(getSession());
        mgr.setForIdDecision(getIdDecision());
        mgr.find();

        REPrestationDue pd = new REPrestationDue();
        pd.setSession(getSession());
        pd.setIdPrestationDue(((REValidationDecisions) mgr.getFirstEntity()).getIdPrestationDue());
        pd.retrieve();

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(pd.getIdRenteAccordee());
        ra.retrieve();

        REInformationsComptabilite infoCom = new REInformationsComptabilite();
        infoCom.setSession(getSession());
        infoCom.setIdInfoCompta(ra.getIdInfoCompta());
        infoCom.retrieve();

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        if (!JadeStringUtil.isIntegerEmpty(getIdTiersBeneficiairePrincipal())) {

            // charcher une adresse de paiement pour ce beneficiaire
            TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                    .getCurrentThreadTransaction(), infoCom.getIdTiersAdressePmt(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            if ((adresse != null) && !adresse.isNew()) {
                if (!JadeStringUtil.isEmpty(adresse.getCcp())) {
                    if (!JadeStringUtil.isEmpty(adresse.getDesignation1_adr())) {
                        setAdressePaiementFormatee("<BR>" + adresse.getDesignation1_adr() + "<BR>" + adresse.getCcp());
                    } else {
                        setAdressePaiementFormatee("<BR>" + adresse.getDesignation1_tiers() + " "
                                + adresse.getDesignation2_tiers() + "<BR>" + adresse.getCcp());
                    }
                } else if (!JadeStringUtil.isEmpty(adresse.getIdTiersBanque())) {
                    if (!JadeStringUtil.isEmpty(adresse.getDesignation1_adr())) {
                        setAdressePaiementFormatee("<BR>" + adresse.getDesignation1_adr() + "<BR>"
                                + adresse.getCompte() + "<BR>" + adresse.getDesignation1_banque());
                    } else {
                        setAdressePaiementFormatee("<BR>" + adresse.getDesignation1_tiers() + " "
                                + adresse.getDesignation2_tiers() + "<BR>" + adresse.getCompte() + "<BR>"
                                + adresse.getDesignation1_banque());
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(adresse.getDesignation1_adr())) {
                        setAdressePaiementFormatee("<BR>" + adresse.getDesignation1_adr());
                    } else {
                        setAdressePaiementFormatee("<BR>" + adresse.getDesignation1_tiers() + " "
                                + adresse.getDesignation2_tiers());
                    }
                }
            }
        }
    }

    public String getAdresseCourrier() {
        return adresseCourrier;
    }

    public String getAdresseFormattee() throws Exception {

        chargerAdresseCourrier(getSession());

        return adresseFormattee;
    }

    public String getAdressePaiementFormatee() throws Exception {

        chargerAdressePaiement(getSession());

        return adressePaiementFormatee;
    }

    public Iterator getAnnexesIterator() throws Exception {
        return getAnnexesList().iterator();
    }

    public List<REAnnexeDecisionViewBean> getAnnexesList() {
        return annexesList;
    }

    public StringBuffer getAvertissementCopie() {
        return avertissementCopie;
    }

    public Iterator getCopiesIterator() throws Exception {
        return getCopiesList().iterator();
    }

    public List<RECopieDecisionViewBean> getCopiesList() {
        return copiesList;
    }

    public BSpy getCreationSpy() {
        return getDecision().getCreationSpy();
    }

    public String getCsEtatDecision() throws Exception {
        return getDecision().getCsEtat();
    }

    public String getCsEtatDecisionLibelle() throws Exception {
        return getSession().getCodeLibelle(getDecision().getCsEtat());
    }

    public String getCsGenreDecision() {
        return csGenreDecision;
    }

    public String getCsTypePreparationDecision() {
        return csTypePreparationDecision;
    }

    public String getCurrentDate() {
        return JACalendar.todayJJsMMsAAAA();
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public REDecisionEntity getDecision() {
        return decision;
    }

    public REDecisionsContainer getDecisionContainer() {
        return decisionContainer;
    }

    public String getDecisionDepuis() {
        return decisionDepuis;
    }

    public List getDocumentsPreview() {
        return documentsPreview;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getGenreDecision() throws Exception {
        return getDecision().getCsGenreDecision();
    }

    public Boolean getHasRemarqueIncarceration() {
        return hasRemarqueIncarceration;
    }

    public Boolean getHasRemarqueRemariageRenteDeSurvivant() {
        return hasRemarqueRemariageRenteDeSurvivant;
    }

    public final Boolean getHasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot() {
        return hasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot;
    }

    public final Boolean getHasRemarqueRenteAvecMontantMinimumMajoreInvalidite() {
        return hasRemarqueRenteAvecMontantMinimumMajoreInvalidite;
    }

    public final Boolean getHasRemarqueRenteAvecSupplementPourPersonneVeuve() {
        return hasRemarqueRenteAvecSupplementPourPersonneVeuve;
    }

    public Boolean getHasRemarqueRenteDeVeufLimitee() {
        return hasRemarqueRenteDeVeufLimitee;
    }

    public Boolean getHasRemarqueRenteDeVeuveLimitee() {
        return hasRemarqueRenteDeVeuveLimitee;
    }

    public final Boolean getHasRemarqueRenteReduitePourSurassurance() {
        return hasRemarqueRenteReduitePourSurassurance;
    }

    public Boolean getHasRemarqueRentesPourEnfants() {
        return hasRemarqueRentesPourEnfants;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdLot() {
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDecision(getIdDecision());
        try {
            prestMgr.find();
        } catch (Exception e) {
            return "";
        }

        if (prestMgr.size() > 1) {
            return "";
        }

        REPrestations prest = (REPrestations) prestMgr.getFirstEntity();
        return prest.getIdLot();
    }

    /**
     * Méthode pour affichages des ordres de versement
     */
    public String getIdPrestation() {
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDecision(getIdDecision());
        prestMgr.setForIdDemandeRente(getIdDemandeRente());

        try {
            prestMgr.find();
        } catch (Exception e) {
            return "";
        }

        REPrestations prest = (REPrestations) prestMgr.getFirstEntity();

        return prest.getIdPrestation();
    }

    public String getIdTierAdresseCourrier() {
        return idTierAdresseCourrier;
    }

    public String getIdTiersBeneficiairePrincipal() {
        return idTiersBeneficiairePrincipal;
    }

    public String getIdTiersDepuisPyxis() {
        return idTiersDepuisPyxis;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public String getInformationMessage() {
        return informationMessage;
    }

    public Boolean getIsAvecBonneFoi() {
        return isAvecBonneFoi;
    }

    public Boolean getIsDepuisRcListDecision() {
        return isDepuisRcListDecision;
    }

    /**
     * Inforom 500 Informe si des intérêts moratoires sont du au tiers (montant sup à 0 bien évidement). Si c'est le
     * cas, la case à cocher 'Intérêts moratoires' sera cochée automatiquement sur l'écran PRE2002 PreValiderDecision
     * 
     * @return Si le tiers à droit à des intérêts moratoires (montant sup à 0 bien évidement)
     */
    public boolean getIsInteretMoratoire() {
        return isInteretMoratoire;
    }

    public Boolean getIsObligPayerCoti() {
        return isObligPayerCoti;
    }

    public Boolean getIsRemAnnDeci() {
        return isRemAnnDeci;
    }

    public Boolean getIsRemRedPlaf() {
        return isRemRedPlaf;
    }

    public Boolean getIsRemSuppVeuf() {
        return isRemSuppVeuf;
    }

    public Boolean getIsSansBonneFoi() {
        return isSansBonneFoi;
    }

    public List<REAnnexeDecisionViewBean> getLstAnnexe() {
        return lstAnnexe;
    }

    public List<RECopieDecisionViewBean> getLstCopie() {
        return lstCopie;
    }

    public List getLstCopieTronquee() {
        return lstCopieTronquee;
    }

    public Map getMapKey() {
        return mapKey;
    }

    /**
     * @return the messageRetenueSurRente
     */
    public final String getMessageRetenueSurRente() {
        return messageRetenueSurRente;
    }

    public Object[] getMethodesSelecteurCopieAdmin() {
        return REPreValiderDecisionViewBean.METHODES_SEL_COPIE_ADMIN;
    }

    public Object[] getMethodesSelecteurCopieTiers() {
        return REPreValiderDecisionViewBean.METHODES_SEL_COPIE_TIERS;
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     */
    public Object[] getMethodesSelectionAdresseCourrier() {
        return REPreValiderDecisionViewBean.METHODES_SEL_ADRESSE_COURRIER;
    }

    /**
     * Méthode pour affichages des ordres de versement
     */
    public String getMontantPrestation() {
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDecision(getIdDecision());
        prestMgr.setForIdDemandeRente(getIdDemandeRente());

        try {
            prestMgr.find();
        } catch (Exception e) {
            return "";
        }

        REPrestations prest = (REPrestations) prestMgr.getFirstEntity();

        return prest.getMontantPrestation();
    }

    public String getNomTiersDepuisPyxis() {
        return nomTiersDepuisPyxis;
    }

    public String getNssTiersBeneficiaire() {
        return nssTiersBeneficiaire;
    }

    public String getNumAffilieDepuisPyxis() {
        return numAffilieDepuisPyxis;
    }

    public Vector getPersonnesReference() {

        BSession session = getSession();

        Vector v = REGestionnaireHelper.getResponsableData(session);
        String[] s = new String[2];

        s[0] = session.getUserId();
        s[1] = session.getUserId() + " - " + session.getUserFullName();

        boolean cont = false;

        int i;

        for (i = 0; i < v.size(); i++) {
            String[] c = (String[]) v.get(i);

            if (c[0].equals(s[0]) && c[1].equals(s[1])) {
                cont = true;
            }

        }

        if (!cont) {
            v.add(s);
        }

        return v;
    }

    public String getRemarque() throws Exception {
        if (null == getDecision().getRemarqueDecision()) {
            return "";
        } else {
            return getDecision().getRemarqueDecision();
        }
    }

    public String getRemarqueDecision() {
        return remarqueDecision;
    }

    public String getRequerantInfo() {
        return requerantInfo;
    }

    @Override
    public BSpy getSpy() {
        return getDecision().getSpy();
    }

    public String getTestRetenue() {
        return testRetenue;
    }

    public String getTiersBeneficiairePrincipalInfo() {
        return tiersBeneficiairePrincipalInfo;
    }

    public String getTraiterPar() throws Exception {
        return getDecision().getTraitePar();
    }

    public String getTraiterParDecision() {
        return traiterParDecision;
    }

    public Boolean isAvecBonneFoi() throws Exception {
        return getDecision().getIsAvecBonneFoi();
    }

    public Boolean isDateDecisionInferieureMoisPaiement() throws JAException {
        JADate dateDecision = new JADate(decision.getDateDecision());
        JADate datePaiement = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

        int moisDecision = dateDecision.getMonth();
        int moisPaiement = datePaiement.getMonth();

        if (moisDecision == moisPaiement) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean isDecisionSupprimable() throws Exception {

        BTransaction transaction = getSession().getCurrentThreadTransaction();

        if (getDecision().getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {

            // Si la prestation de la décision n'est pas dans un lot définitif
            REPrestationsManager prestationMgr = new REPrestationsManager();
            prestationMgr.setSession(getSession());
            prestationMgr.setForIdDecision(decision.getIdDecision());
            prestationMgr.find(1);

            REPrestations prestation = new REPrestations();

            if (prestationMgr.isEmpty()) {
                return Boolean.TRUE;
            } else {
                prestation = (REPrestations) prestationMgr.get(0);
            }

            if (prestation.isNew()) {

                return Boolean.TRUE;

            } else {

                if (JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {

                    return Boolean.TRUE;

                } else {

                    // Retrieve du lot
                    RELot lot = new RELot();
                    lot.setSession(getSession());
                    lot.setIdLot(prestation.getIdLot());
                    lot.retrieve(transaction);

                    if (!lot.isNew()) {

                        // si le lot est définitif
                        if (lot.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE)) {

                            // bz-5091
                            if (IREDecision.CS_TYPE_DECISION_STANDARD.equals(getDecision().getCsTypeDecision())) {
                                REDecisionJointDemandeRente d = new REDecisionJointDemandeRente();
                                d.setSession(getSession());
                                d.setIdDecision(getIdDecision());
                                d.retrieve(transaction);

                                // nous sommes dans le mois comptable précédant ou plus petit
                                // que le mois de début de la rente

                                RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
                                mgr.setSession(getSession());
                                mgr.setForIdDecision(getDecision().getIdDecision());
                                mgr.setUntilDateDebutDroit(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel
                                        .getDateDernierPmt(getSession())));
                                mgr.find(transaction);

                                if (mgr.isEmpty()) {
                                    return Boolean.TRUE;
                                } else {
                                    // On peut pas modifier
                                    return Boolean.FALSE;
                                }

                            } else {
                                return Boolean.FALSE;
                            }

                        } else {

                            return Boolean.TRUE;
                        }
                    } else {
                        return Boolean.FALSE;
                    }
                }

            }
        } else {
            return Boolean.TRUE;
        }
    }

    public Boolean isDecisionValider() throws Exception {
        if (getDecision().getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean isObligPayerCoti() throws Exception {
        return getDecision().getIsObliPayerCoti();
    }

    public boolean isOneDecision() throws Exception {

        boolean isOneDecision = false;
        REDecisionsManager decMgr = new REDecisionsManager();
        decMgr.setSession(getSession());
        decMgr.setForIdDemandeRente(getIdDemandeRente());
        decMgr.find();

        if (decMgr.size() == 1) {
            isOneDecision = true;
        }

        return isOneDecision;
    }

    public Boolean isRemAnnDeci() throws Exception {
        return getDecision().getIsRemAnnDeci();
    }

    public Boolean isRemRedPlaf() throws Exception {
        return getDecision().getIsRemRedPlaf();
    }

    public Boolean isRemSuppVeuf() throws Exception {
        return getDecision().getIsRemSuppVeuf();
    }

    public Boolean isRetourPyxis() {
        return isRetourPyxis;
    }

    public boolean isRoleValider() throws Exception {

        boolean isRoleValider = false;
        String[] sRoles = JadeAdminServiceLocatorProvider.getLocator().getRoleService()
                .findAllIdRoleForIdUser(getSession().getUserId());
        for (int i = 0; i < sRoles.length; i++) {
            if ("rREValider".equals(sRoles[i])) {
                isRoleValider = true;
            }
        }

        return isRoleValider;
    }

    public Boolean isSansBonneFoi() throws Exception {
        return getDecision().getIsSansBonneFoi();
    }

    public void setAdresseCourrier(String adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    public void setAdresseFormattee(String adresseFormattee) {
        this.adresseFormattee = adresseFormattee;
    }

    public void setAdressePaiementFormatee(String adressePaiementFormatee) {
        this.adressePaiementFormatee = adressePaiementFormatee;
    }

    public void setAnnexesList(List annexesList) {
        this.annexesList = annexesList;
    }

    public void setAvertissementCopie(StringBuffer avertissementCopie) {
        this.avertissementCopie = avertissementCopie;
    }

    public void setCopiesList(List copiesList) {
        this.copiesList = copiesList;
    }

    public void setCsGenreDecision(String csGenreDecision) {
        this.csGenreDecision = csGenreDecision;
    }

    public void setCsTypePreparationDecision(String csTypePreparationDecision) {
        this.csTypePreparationDecision = csTypePreparationDecision;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDecision(REDecisionEntity decision) {
        this.decision = decision;
    }

    public void setDecisionContainer(REDecisionsContainer decisionContainer) {
        this.decisionContainer = decisionContainer;
    }

    public void setDecisionDepuis(String decisionDepuis) {
        this.decisionDepuis = decisionDepuis;
    }

    public void setDocumentsPreview(List documentsPreview) {
        this.documentsPreview = documentsPreview;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setHasRemarqueIncarceration(Boolean hasRemarqueIncarceration) {
        this.hasRemarqueIncarceration = hasRemarqueIncarceration;
    }

    public final void setHasRemarqueRemariageRenteDeSurvivant(Boolean hasRemarqueRemariageRenteDeSurvivant) {
        this.hasRemarqueRemariageRenteDeSurvivant = hasRemarqueRemariageRenteDeSurvivant;
    }

    public final void setHasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot(
            Boolean hasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot) {
        this.hasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot = hasRemarqueRenteAvecDebutDroit5AnsAvantDateDepot;
    }

    public final void setHasRemarqueRenteAvecMontantMinimumMajoreInvalidite(
            Boolean hasRemarqueRenteAvecMontantMinimumMajoreInvalidite) {
        this.hasRemarqueRenteAvecMontantMinimumMajoreInvalidite = hasRemarqueRenteAvecMontantMinimumMajoreInvalidite;
    }

    public final void setHasRemarqueRenteAvecSupplementPourPersonneVeuve(
            Boolean hasRemarqueRenteAvecSupplementPourPersonneVeuve) {
        this.hasRemarqueRenteAvecSupplementPourPersonneVeuve = hasRemarqueRenteAvecSupplementPourPersonneVeuve;
    }

    public final void setHasRemarqueRenteDeVeufLimitee(Boolean hasRemarqueRenteDeVeufLimitee) {
        this.hasRemarqueRenteDeVeufLimitee = hasRemarqueRenteDeVeufLimitee;
    }

    public final void setHasRemarqueRenteDeVeuveLimitee(Boolean hasRemarqueRenteDeVeuveLimitee) {
        this.hasRemarqueRenteDeVeuveLimitee = hasRemarqueRenteDeVeuveLimitee;
    }

    public final void setHasRemarqueRenteReduitePourSurassurance(Boolean hasRemarqueRenteReduitePourSurassurance) {
        this.hasRemarqueRenteReduitePourSurassurance = hasRemarqueRenteReduitePourSurassurance;
    }

    public void setHasRemarqueRentesPourEnfants(boolean hasRemarqueRentesPourEnfants) {
        this.hasRemarqueRentesPourEnfants = hasRemarqueRentesPourEnfants;
    }

    public final void setHasRemarqueRentesPourEnfants(Boolean hasRemarqueRentesPourEnfants) {
        this.hasRemarqueRentesPourEnfants = hasRemarqueRentesPourEnfants;
    }

    public void setIdDecision(String idDecision) throws Exception {

        decision.setIdDecision(idDecision);
        decision.setSession(getSession());
        decision.retrieve();

        setDecision(decision);

        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTierAdresseCourrier(String idTierAdresseCourrier) {
        this.idTierAdresseCourrier = idTierAdresseCourrier;
    }

    public void setIdTiersAdresseCourrierPyxis(String idTiersAdresseCourrierPyxis) {
        // isRetourPyxis = true;
        idTierAdresseCourrier = idTiersAdresseCourrierPyxis;

        try {
            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresseFormattee = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), idTiersAdresseCourrierPyxis,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");
        } catch (Exception e) {
        }
    }

    public void setIdTiersBeneficiairePrincipal(String idTiersBeneficiairePrincipal) {
        this.idTiersBeneficiairePrincipal = idTiersBeneficiairePrincipal;
    }

    public void setIdTiersDepuisPyxis(String idTiersDepuisPyxis) {
        this.idTiersDepuisPyxis = idTiersDepuisPyxis;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setInformationMessage(String informationMessage) {
        this.informationMessage = informationMessage;
    }

    public void setIsAvecBonneFoi(Boolean isAvecBonneFoi) {
        this.isAvecBonneFoi = isAvecBonneFoi;
    }

    public void setIsDepuisRcListDecision(Boolean isDepuisRcListDecision) {
        this.isDepuisRcListDecision = isDepuisRcListDecision;
    }

    /**
     * Inforom 500 Défini si des intérêts moratoires sont du au tiers (montant sup à 0 bien évidement). Si c'est le cas,
     * la case à cocher 'Intérêts moratoires' devra être cochée automatiquement sur l'écran PRE2002 PreValiderDecision
     * 
     * @param isInteretMoratoire
     */
    public void setIsInteretMoratoire(boolean isInteretMoratoire) {
        this.isInteretMoratoire = isInteretMoratoire;
    }

    public void setIsObligPayerCoti(Boolean isObligPayerCoti) {
        this.isObligPayerCoti = isObligPayerCoti;
    }

    public void setIsRemAnnDeci(Boolean isRemAnnDeci) {
        this.isRemAnnDeci = isRemAnnDeci;
    }

    public void setIsRemRedPlaf(Boolean isRemRedPlaf) {
        this.isRemRedPlaf = isRemRedPlaf;
    }

    public void setIsRemSuppVeuf(Boolean isRemSuppVeuf) {
        this.isRemSuppVeuf = isRemSuppVeuf;
    }

    public void setIsRetourPyxis(Boolean isRetourPyxis) {
        this.isRetourPyxis = isRetourPyxis;
    }

    public void setIsSansBonneFoi(Boolean isSansBonneFoi) {
        this.isSansBonneFoi = isSansBonneFoi;
    }

    public void setLstAnnexe(List lstAnnexe) {
        this.lstAnnexe = lstAnnexe;
    }

    public void setLstCopie(List lstCopie) {
        this.lstCopie = lstCopie;
    }

    public void setLstCopieTronquee(List lstCopieTronquee) {
        this.lstCopieTronquee = lstCopieTronquee;
    }

    public void setMapKey(Map mapKey) {
        this.mapKey = mapKey;
    }

    /**
     * @param messageRetenueSurRente
     *            the messageRetenueSurRente to set
     */
    public final void setMessageRetenueSurRente(String messageRetenueSurRente) {
        this.messageRetenueSurRente = messageRetenueSurRente;
    }

    public void setNomTiersDepuisPyxis(String nomTiersDepuisPyxis) {
        this.nomTiersDepuisPyxis = nomTiersDepuisPyxis;
    }

    public void setNssTiersBeneficiaire(String nssTiersBeneficiaire) {
        this.nssTiersBeneficiaire = nssTiersBeneficiaire;
    }

    public void setNumAffilieDepuisPyxis(String numAffilieDepuisPyxis) {
        this.numAffilieDepuisPyxis = numAffilieDepuisPyxis;
    }

    public void setRemarqueDecision(String remarqueDecision) {
        this.remarqueDecision = remarqueDecision;
    }

    public void setRequerantInfo(String requerantInfo) {
        this.requerantInfo = requerantInfo;
    }

    public void setTestRetenue(String testRetenue) {
        this.testRetenue = testRetenue;
    }

    public void setTiersBeneficiairePrincipalInfo(String tiersBeneficiairePrincipalInfo) {
        this.tiersBeneficiairePrincipalInfo = tiersBeneficiairePrincipalInfo;
    }

    public void setTraiterParDecision(String traiterParDecision) {
        this.traiterParDecision = traiterParDecision;
    }

    @Override
    public boolean validate() {
        return true;
    }
}