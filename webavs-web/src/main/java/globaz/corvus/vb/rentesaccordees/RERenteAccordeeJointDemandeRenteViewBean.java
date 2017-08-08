/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.decisions.REDecisionsUtil;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author bsc
 * 
 *         scr
 * @deprecated, replaced by RERenteAccJoinTblTiersJoinDemandeRenteViewBean
 */
public class RERenteAccordeeJointDemandeRenteViewBean extends RERenteAccJoinTblTiersJoinDemandeRente implements
        FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] { new String[] {
            "idTiersAdressePmtICDepuisPyxis", "getIdTiers" } };

    private String adresseFormattee = "";
    private List attachedDocuments = null;
    // private String idTiersAdressePmt = "";
    // private String idDomaineAdressePmt = "";
    // infos relatives a l'adresse de paiement
    private String ccpOuBanqueFormatte = "";
    private String forCsEtat = "";
    private String forGenre = "";
    private String forIdTiersLiant = "";
    private String idCompteAnnexeIC = "";
    // Toutes les informations de l'informations comptabilité (REINCOM)
    private String idInfoComptaIC = "";
    private String idRenteCalculee = "";
    private String idTiersAdressePmtIC = "";
    // Depuis Pyxis
    private String idTiersAdressePmtICDepuisPyxis = "";
    private String isAdresseModifiee = "false";
    private String isChangeBeneficiaire = "";
    private Boolean isPreparationDecisionValide = null;
    private boolean isRechercheRenteEnCours = false;
    private Map mapAdrPmtMbresFamille = null;
    private String nbPostit = "";
    private String nssTiersBeneficiairea = "";
    private String nssTiersComplementaire1a = "";
    // Pour séparation dans la rcListe des RA
    // private REInformationsComptabilite infoCompta = null;
    private String nssTiersComplementaire2a = "";
    private String numAffilieDepuisPyxis = "";
    // champs supplementaires.
    private boolean retourDepuisPyxis = false;
    private PRTiersWrapper tiersBeneficiaire = null;
    private boolean tiersBeneficiaireChange = false;
    private PRTiersWrapper tiersCompl1 = null;
    private PRTiersWrapper tiersCompl2 = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(RERenteAccordeeJointDemandeRenteListViewBean.FIELDNAME_COUNT_POSTIT);
        idRenteCalculee = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        super._readProperties(statement);
    }

    /**
     * @return
     */
    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    public String getAnneeAnticipationJSP() {
        if (JadeStringUtil.isBlankOrZero(super.getAnneeAnticipation())) {
            return "";
        } else {
            return super.getAnneeAnticipation();
        }
    }

    public List getAttachedDocuments() {
        return attachedDocuments;
    }

    /**
     * @return
     */
    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    public String getCodeAuxilliaireJSP() {
        if (JadeStringUtil.isBlankOrZero(super.getCodeAuxilliaire())) {
            return "";
        } else {
            return super.getCodeAuxilliaire();
        }
    }

    public String getCodeSurvivantInvalideJSP() {
        return super.getCodeSurvivantInvalide();
    }

    /**
     * @return
     */
    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    public String getCsRelationAuRequerantLibelle() {

        return getSession().getCodeLibelle(getCsRelationAuRequerant());
    }

    public String getDateDebutAnticipationJSP() {
        // format xx.xxxx
        if (super.getDateDebutAnticipation().length() == 7) {
            // si 00.0000
            if (super.getDateDebutAnticipation().substring(0, 2).equals("00")
                    && super.getDateDebutAnticipation().substring(3).equals("0000")) {
                return "";
            } else {
                return super.getDateDebutAnticipation();
            }
        } else {
            return super.getDateDebutAnticipation();
        }
    }

    /*
     * Pour retourner la date de validation la plus récente de toutes les décisions de cette rente accordée
     */
    public String getDateDecision() throws Exception {

        JADate derDateDecision = new JADate();

        JACalendar cal = new JACalendarGregorian();

        REDecisionJointDemandeRenteManager mgr = new REDecisionJointDemandeRenteManager();
        mgr.setForNoDemandeRente(getNoDemandeRente());
        mgr.setForIdsRentesAccordeesIn(getIdPrestationAccordee());
        mgr.setSession(getSession());
        mgr.find();

        for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
            REDecisionJointDemandeRente entity = (REDecisionJointDemandeRente) iterator.next();

            JADate dateDecision = new JADate(entity.getDateDecision());

            if (cal.compare(derDateDecision, dateDecision) == JACalendar.COMPARE_FIRSTLOWER) {
                derDateDecision = dateDecision;
            }

        }

        return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(derDateDecision.toStrAMJ());

    }

    /**
     * @return
     */
    public String getDateNaissanceTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
        } else {
            return "";
        }
    }

    public String getDateRevocationAjournementJSP() {
        // format xx.xxxx
        if (super.getDateRevocationAjournement().length() == 7) {
            // si 00.0000
            if (super.getDateRevocationAjournement().substring(0, 2).equals("00")
                    && super.getDateRevocationAjournement().substring(3).equals("0000")) {
                return "";
            } else {
                return super.getDateRevocationAjournement();
            }
        } else {
            return super.getDateRevocationAjournement();
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {

        return PRNSSUtil.formatDetailRequerantListe(getNumeroAvsBenef(), getNomBenef() + " " + getPrenomBenef(),
                getDateNaissanceBenef(), getSexeBenef(), getNationaliteBenef());

    }

    public String getDetailRequerantDecede() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDecesBenef())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNumeroAvsBenef(), getNomBenef() + " "
                    + getPrenomBenef(), getDateNaissanceBenef(), getSexeBenef(), getNationaliteBenef(),
                    getDateDecesBenef());
        } else {
            return getDetailRequerant();
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerantDetail() {

        return PRNSSUtil.formatDetailRequerantDetail(getNssTiersBeneficiaire(), getNomPrenomTiersBeneficiaire(),
                getDateNaissanceTiersBeneficiaire(), getLibelleCourtSexe(), getLibellePays());

    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForGenre() {
        return forGenre;
    }

    public String getForIdTiersLiant() {
        return forIdTiersLiant;
    }

    public String getFractionRenteJSP() {
        if (JadeStringUtil.isBlankOrZero(super.getFractionRente())) {
            return "";
        } else {
            return super.getFractionRente();
        }
    }

    /**
     * @return
     */
    public String getIdCompteAnnexeIC() {
        return idCompteAnnexeIC;
    }

    /**
     * @return
     */
    public String getIdInfoComptaIC() {
        return idInfoComptaIC;
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    /**
     * @return
     */
    public String getIdTiersAdressePmtIC() {
        return idTiersAdressePmtIC;
    }

    /**
     * @return
     */
    public String getIdTiersAdressePmtICDepuisPyxis() {
        return idTiersAdressePmtICDepuisPyxis;
    }

    /**
     * @return
     */
    public String getIsAdresseModifiee() {
        return isAdresseModifiee;
    }

    /**
     * @return
     */
    public String getIsChangeBeneficiaire() {
        return isChangeBeneficiaire;
    }

    public boolean getIsRechercheRenteEnCours() {
        return isRechercheRenteEnCours;
    }

    public String getLibelleCodePrestation(String codePrestation) {
        return getSession().getCodeLibelle(codePrestation);
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (Homme ou Femme)
     */
    public String getLibelleCourtSexe() {

        return getSexeTiersBeneficiaire();

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession()
                .getCode(getSession().getSystemCode("CIPAYORI", getNationaliteTiersBeneficiaire())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(
                    getSession().getSystemCode("CIPAYORI", getNationaliteTiersBeneficiaire()));
        }

    }

    public Map getMapAdrPmtMbresFamille(String idTiers) {

        try {
            if (mapAdrPmtMbresFamille == null) {
                loadAdrPmtMbresFamille(idTiers);
            }
            return mapAdrPmtMbresFamille;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return RERenteAccordeeJointDemandeRenteViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    public String getMontantReducationAnticipationJSP() {
        FWCurrency currency = new FWCurrency(super.getMontantReducationAnticipation());
        if (currency.isZero()) {
            return "";
        } else {
            super.getMontantReducationAnticipation();
        }
        return super.getMontantReducationAnticipation();
    }

    public String getMontantRenteOrdiRemplaceeJSP() {
        FWCurrency currency = new FWCurrency(super.getMontantRenteOrdiRemplacee());
        if (currency.isZero()) {
            return "";
        } else {
            super.getMontantRenteOrdiRemplacee();
        }
        return super.getMontantRenteOrdiRemplacee();
    }

    /**
     * @return
     */
    public String getNationaliteTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE);
        } else {
            return "";
        }
    }

    public String getNbPostit() {
        return nbPostit;
    }

    public String getNoCsCodePrestation() {
        return getSession().getCode(getCodePrestation());
    }

    /**
     * @return
     */
    public String getNomPrenomTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNssTiersBeneficiaire() {

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        return tiers.getNumAvsActuel(getIdTiersBeneficiaire());
    }

    /**
     * @return
     */
    public String getNssTiersBeneficiairea() {
        return nssTiersBeneficiairea;
    }

    /**
     * @return
     */
    public String getNssTiersCompl1() {

        loadTiersCompl1();

        if (tiersCompl1 != null) {
            return NSUtil.formatWithoutPrefixe(tiersCompl1.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiersCompl1.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).length() > 14 ? true : false);
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNssTiersCompl2() {

        loadTiersCompl2();

        if (tiersCompl2 != null) {
            return NSUtil.formatWithoutPrefixe(tiersCompl2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiersCompl2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).length() > 14 ? true : false);
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNssTiersComplementaire1a() {
        return nssTiersComplementaire1a;
    }

    /**
     * @return
     */
    public String getNssTiersComplementaire2a() {
        return nssTiersComplementaire2a;
    }

    public String getNumAffilieDepuisPyxis() {
        return numAffilieDepuisPyxis;
    }

    public String getPrescriptionAppliqueeJSP() {
        if (JadeStringUtil.isBlankOrZero(super.getPrescriptionAppliquee())) {
            return "";
        } else {
            return super.getPrescriptionAppliquee();
        }
    }

    public boolean getRechercheRenteEnCours() {
        return isRechercheRenteEnCours;
    }

    public String getReductionFauteGraveJSP() {
        FWCurrency currency = new FWCurrency(super.getReductionFauteGrave());
        if (currency.isZero()) {
            return "";
        } else {
            super.getReductionFauteGrave();
        }
        return super.getReductionFauteGrave();
    }

    public String getRetenueBlocageLibelle() {
        try {
            if (getIsPrestationBloquee().booleanValue() && getIsRetenues().booleanValue()) {
                return getSession().getApplication()
                        .getLabel("JSP_RAC_L_R_B", getSession().getUserInfo().getLanguage());
            } else if (getIsPrestationBloquee().booleanValue()) {
                return getSession().getApplication().getLabel("JSP_RAC_L_B", getSession().getUserInfo().getLanguage());
            } else if (getIsRetenues().booleanValue()) {
                return getSession().getApplication().getLabel("JSP_RAC_L_R", getSession().getUserInfo().getLanguage());
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return
     */
    public String getSexeTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return getSession().getCodeLibelle(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_SEXE));
        } else {
            return "";
        }
    }

    @Override
    public BSpy getSpy() {
        if (JadeStringUtil.isBlank("" + super.getSpy())) {
            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(getIdPrestationAccordee());
            try {
                ra.retrieve();
            } catch (Exception e) {
            }
            return ra.getSpy();
        } else {
            return super.getSpy();
        }

    }

    public String getSupplementAjournementJSP() {
        FWCurrency currency = new FWCurrency(super.getSupplementAjournement());
        if (currency.isZero()) {
            return "";
        } else {
            super.getSupplementAjournement();
        }
        return super.getSupplementAjournement();
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public boolean hasPostit() {
        return Integer.parseInt(nbPostit) > 0;
    }

    public boolean isAfficherRepriseDuDroit() {

        if (JadeStringUtil.isBlankOrZero(getDateFinDroit())) {
            return false;
        }

        String cs1 = getCodeCasSpeciaux1();
        String cs2 = getCodeCasSpeciaux2();
        String cs3 = getCodeCasSpeciaux3();
        String cs4 = getCodeCasSpeciaux4();
        String cs5 = getCodeCasSpeciaux5();

        if (!JadeStringUtil.isBlankOrZero(cs1) && ("02".equals(cs1) || "05".equals(cs1))) {
            return false;
        }
        if (!JadeStringUtil.isBlankOrZero(cs2) && ("02".equals(cs2) || "05".equals(cs2))) {
            return false;
        }
        if (!JadeStringUtil.isBlankOrZero(cs3) && ("02".equals(cs3) || "05".equals(cs3))) {
            return false;
        }
        if (!JadeStringUtil.isBlankOrZero(cs4) && ("02".equals(cs4) || "05".equals(cs4))) {
            return false;
        }
        if (!JadeStringUtil.isBlankOrZero(cs5) && ("02".equals(cs5) || "05".equals(cs5))) {
            return false;
        }

        if (!JadeStringUtil.isBlankOrZero(getMontantReducationAnticipation())) {
            FWCurrency m = new FWCurrency(getMontantReducationAnticipation());
            if (!m.isZero()) {
                return false;
            }
        }

        if (!JadeStringUtil.isBlankOrZero(getSupplementAjournement())) {
            FWCurrency m = new FWCurrency(getSupplementAjournement());
            if (!m.isZero()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAnnulerDiminutionAuthorise() {
        if (IREPrestationAccordee.CS_ETAT_DIMINUE.equals(getCsEtat())
                && !JadeStringUtil.isBlankOrZero(getDateFinDroit())) {
            return true;
        } else {
            return false;
        }
    }

    // Retourne false si un montant est bloqué
    public boolean isDesactiverBlocageOnly() {

        if ((getIsPrestationBloquee() != null) && getIsPrestationBloquee().booleanValue()) {
            if (JadeStringUtil.isBlankOrZero(getIdEnteteBlocage())) {
                return true;
            } else {
                REEnteteBlocage blocage = new REEnteteBlocage();
                blocage.setSession(getSession());
                blocage.setIdEnteteBlocage(getIdEnteteBlocage());
                try {
                    blocage.retrieve();
                } catch (Exception e) {
                    // should never happend
                    e.printStackTrace();
                    return true;
                }

                FWCurrency mnt = new FWCurrency(blocage.getMontantBloque());
                mnt.sub(blocage.getMontantDebloque());
                if (mnt.isZero() || mnt.isNegative()) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public boolean isModifiable() {
        return JadeStringUtil.isEmpty(getCsEtat()) || IREPrestationAccordee.CS_ETAT_CALCULE.equals(getCsEtat());
    }

    public boolean isMontantADebloquer() {

        if (JadeStringUtil.isBlankOrZero(getIdEnteteBlocage())) {
            return false;
        } else {
            REEnteteBlocage blocage = new REEnteteBlocage();
            blocage.setSession(getSession());
            blocage.setIdEnteteBlocage(getIdEnteteBlocage());
            try {
                blocage.retrieve();
            } catch (Exception e) {
                // should never happend
                e.printStackTrace();
                return false;
            }
            FWCurrency mntB = new FWCurrency(blocage.getMontantBloque());
            return mntB.isPositive();
        }
    }

    /***
     * Permet de savoir si un lot de déblocage est en traitement
     * 
     * @return
     */
    public boolean isLotDeblocageEnTraitement() {
        RELotManager lotMgr = new RELotManager();
        lotMgr.setSession(getSession());
        lotMgr.setForCsType(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
        lotMgr.setForCsEtat(IRELot.CS_ETAT_LOT_EN_TRAITEMENT);

        try {
            lotMgr.find(1);

            return lotMgr.getFirstEntity() != null;

        } catch (Exception e) {
            // should never happend
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNssTiersBeneficiaire())) {
            return "";
        }

        if (getNssTiersBeneficiaire().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSSTiersBeneficiaire() {

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).length() > 14 ? "true"
                    : "false";
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSSTiersCompl1() {

        if (tiersCompl1 != null) {
            return tiersCompl1.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).length() > 14 ? "true" : "false";
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSSTiersCompl2() {

        if (tiersCompl2 != null) {
            return tiersCompl2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).length() > 14 ? "true" : "false";
        } else {
            return "";
        }

    }

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return true or false
     */
    public boolean isPreparationDecisionValide(String dateDernierPaiement) {
        return REDecisionsUtil.isPreparationDecisionAuthorise(getSession(), getNoDemandeRente());
    }

    /**
     * @return
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * @return
     */
    public boolean isTiersBeneficiaireChange() {
        return tiersBeneficiaireChange;
    }

    public void loadAdrPmtMbresFamille(String idTiers) throws Exception {

        if (mapAdrPmtMbresFamille == null) {
            mapAdrPmtMbresFamille = new HashMap();
        }

        globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);
        ISFMembreFamilleRequerant[] membresFamille = null;
        try {
            membresFamille = sf.getMembresFamille(idTiers);
        }
        // Peut arriver lorsque l'on change de bénéficiaire, et que ce dernier
        // n'existe pas dans les familles.
        catch (Exception e) {
            ;
        }
        if (membresFamille != null) {
            for (int i = 0; i < membresFamille.length; i++) {
                ISFMembreFamilleRequerant mbr = membresFamille[i];
                if (!JadeStringUtil.isBlankOrZero(mbr.getIdTiers())) {
                    String idTiersMF = mbr.getIdTiers();
                    // PRTiersWrapper tw = PRTiersHelper.getTiersParId(this.getSession(), idTiersMF);
                    TITiers tiers = new TITiers();
                    tiers.setSession(getSession());
                    tiers.setIdTiers(idTiersMF);
                    tiers.retrieve();
                    String nom = tiers.getDesignation1();
                    String prenom = tiers.getDesignation2();

                    // charcher une adresse de paiement pour ce beneficiaire
                    TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                            .getCurrentThreadTransaction(), idTiersMF,
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

                    REAdressePmtUtil adpmt = new REAdressePmtUtil();
                    if ((adresse != null) && !adresse.isNew()) {
                        TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                        source.load(adresse);

                        // formatter le no de ccp ou le no bancaire
                        if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                            adpmt.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
                        } else {
                            adpmt.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
                        }

                        // formatter l'adresse
                        adpmt.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));

                        adpmt.setIdTiers(idTiersMF);
                        adpmt.setNom(nom);
                        adpmt.setPrenom(prenom);
                        mapAdrPmtMbresFamille.put(idTiersMF, adpmt);
                    }
                }
            }
        }
    }

    /**
     * retrouve les propriétés du Beneficiaire
     */
    private void loadTiersBeneficiaire() {
        if (tiersBeneficiaire == null) {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiersBeneficiaire())) {
                try {
                    tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), getIdTiersBeneficiaire());
                } catch (Exception e) {
                    // on net fait rien
                }
            }
        }
    }

    /**
     * retrouve les propriétés du tiers compl 1
     */
    private void loadTiersCompl1() {
        if (tiersCompl1 == null) {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiersComplementaire1())) {
                try {
                    tiersCompl1 = PRTiersHelper.getTiersParId(getSession(), getIdTiersComplementaire1());
                } catch (Exception e) {
                    // on net fait rien
                }
            }
        }
    }

    /**
     * retrouve les propriétés du tiers compl 2
     */
    private void loadTiersCompl2() {
        if (tiersCompl2 == null) {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiersComplementaire2())) {
                try {
                    tiersCompl2 = PRTiersHelper.getTiersParId(getSession(), getIdTiersComplementaire2());
                } catch (Exception e) {
                    // on net fait rien
                }
            }
        }
    }

    /**
     * @param string
     */
    public void setAdresseFormattee(String string) {
        adresseFormattee = string;
    }

    /**
     * setter pour l'attribut adresse paiement.
     * 
     * @param adressePaiement
     *            une nouvelle valeur pour cet attribut
     * @throws Exception
     */
    public void setAdressePaiement(TIAdressePaiementData adressePaiement) throws Exception {
        if (adressePaiement != null) {
            idTiersAdressePmtIC = adressePaiement.getIdTiers();
        }
    }

    public void setAttachedDocuments(List attachedDocuments) {
        this.attachedDocuments = attachedDocuments;
    }

    /**
     * @param string
     */
    public void setCcpOuBanqueFormatte(String string) {
        ccpOuBanqueFormatte = string;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    public void setForIdTiersLiant(String forIdTiersLiant) {
        this.forIdTiersLiant = forIdTiersLiant;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexeIC(String string) {
        idCompteAnnexeIC = string;
    }

    /**
     * @param string
     */
    public void setIdInfoComptaIC(String string) {
        idInfoComptaIC = string;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        setIdPrestationAccordee(idRenteAccordee);
    }

    public void setIdRenteCalculee(String idRenteCalculee) {
        this.idRenteCalculee = idRenteCalculee;
    }

    /**
     * @param string
     */
    public void setIdTiersAdressePmtIC(String string) {
        idTiersAdressePmtIC = string;
    }

    /**
     * @param string
     */
    public void setIdTiersAdressePmtICDepuisPyxis(String string) {
        setIdTiersAdressePmtIC(string);
        idTiersAdressePmtICDepuisPyxis = string;
        retourDepuisPyxis = true;
        tiersBeneficiaireChange = true;

    }

    /**
     * @param string
     */
    public void setIsAdresseModifiee(String string) {
        isAdresseModifiee = string;
    }

    /**
     * @param string
     */
    public void setIsChangeBeneficiaire(String string) {
        isChangeBeneficiaire = string;
    }

    public void setIsRechercheRenteEnCours(boolean isRechercheRenteEnCours) {
        this.isRechercheRenteEnCours = isRechercheRenteEnCours;
    }

    public void setNbPostit(String nbPostit) {
        this.nbPostit = nbPostit;
    }

    /**
     * @param string
     */
    public void setNssTiersBeneficiairea(String string) {
        nssTiersBeneficiairea = string;
    }

    /**
     * @param string
     */
    public void setNssTiersComplementaire1a(String string) {
        nssTiersComplementaire1a = string;
    }

    /**
     * @param string
     */
    public void setNssTiersComplementaire2a(String string) {
        nssTiersComplementaire2a = string;
    }

    public void setNumAffilieDepuisPyxis(String numAffilieDepuisPyxis) throws Exception {

        this.numAffilieDepuisPyxis = numAffilieDepuisPyxis;

    }

    public void setRechercheRenteEnCours(boolean isRechercheRenteEnCours) {
        this.isRechercheRenteEnCours = isRechercheRenteEnCours;
    }

    /**
     * @param b
     */
    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

    /**
     * @param b
     */
    public void setTiersBeneficiaireChange(boolean b) {
        tiersBeneficiaireChange = b;
    }

}