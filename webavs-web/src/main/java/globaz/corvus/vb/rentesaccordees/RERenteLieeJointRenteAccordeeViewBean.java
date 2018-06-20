/*
 * Créé le 5 juil. 07
 */

package globaz.corvus.vb.rentesaccordees;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteLieeJointPrestationAccordee;
import globaz.corvus.utils.decisions.REDecisionsUtil;
import globaz.corvus.vb.lots.RELotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author HPE
 * 
 */

public class RERenteLieeJointRenteAccordeeViewBean extends RERenteLieeJointPrestationAccordee implements
        FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePaiementDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineAdressePaiement", "idApplication" } };

    private static final Object[] METHODES_SEL_BENEFICIAIRE = new Object[] {
            new String[] { "idTiersDepuisPyxis", "idTiers" }, new String[] { "nom", "nom" },
            new String[] { "numAffilieDepuisPyxis", "idExterneAvoirPaiement" } };
    private String adresseFormattee = "";
    // infos relatives a l'adresse de paiement
    private String ccpOuBanqueFormatte = "";

    private String nbPostit = "";
    private String noDemandeRente = "";
    private String nssTiersBeneficiairea = "";
    private String nssTiersComplementaire1a = "";

    private String nssTiersComplementaire2a = "";
    private String numAffilieDepuisPyxis = "";
    private PRTiersWrapper tiersBeneficiaire = null;

    private PRTiersWrapper tiersCompl1 = null;

    private PRTiersWrapper tiersCompl2 = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(RERenteLieeJointRenteAccordeeListViewBean.FIELDNAME_COUNT_POSTIT);
        super._readProperties(statement);
    }

    public boolean isPreparationDecisionValide() {
        try {
            return REDecisionsUtil.isPreparationDecisionAuthorise(getSession(), getNoDemandeRente());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return
     */
    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    /**
     * @return
     */
    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    /**
     * @return
     */

    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    /**
     * @return
     */
    public String getCsRelationAuRequerantLibelle() {
        return getSession().getCodeLibelle(getCsRelationAuRequerant());
    }

    public String getDateDecesTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);
        } else {
            return "";
        }
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

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {

        return PRNSSUtil.formatDetailRequerantListe(getNssTiersBeneficiaire(), getNomPrenomTiersBeneficiaire(),
                getDateNaissanceTiersBeneficiaire(), getLibelleCourtSexe(), getLibellePays());

    }

    public String getDetailRequerantDecede() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDecesTiersBeneficiaire())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNssTiersBeneficiaire(),
                    getNomPrenomTiersBeneficiaire(), getDateNaissanceTiersBeneficiaire(), getLibelleCourtSexe(),
                    getLibellePays(), getDateDecesTiersBeneficiaire());
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

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return RERenteLieeJointRenteAccordeeViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    /**
     * getter pour l'attribut methodes selection beneficiaire (pour les tiers)
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionBeneficiaire() {
        return RERenteLieeJointRenteAccordeeViewBean.METHODES_SEL_BENEFICIAIRE;
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

    public String getNoDemandeRente() throws Exception {

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(getSession());
        bc.setIdBasesCalcul(getIdBaseCalcul());
        bc.retrieve();

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(getSession());
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.setIdRenteCalculee(bc.getIdRenteCalculee());
        demande.retrieve();

        return demande.getIdDemandeRente();
    }

    public boolean isDecisionLotValide() throws Exception {

        REDecisionJointDemandeRenteManager mgr = new REDecisionJointDemandeRenteManager();
        mgr.setForNoDemandeRente(getNoDemandeRente());
        mgr.setForIdsRentesAccordeesIn(getIdPrestationAccordee());
        mgr.setSession(getSession());
        mgr.find();
        if (mgr.isEmpty()) {
            return true;
        }

        for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
            REDecisionJointDemandeRente entity = (REDecisionJointDemandeRente) iterator.next();
            RELotViewBean viewBean = new RELotViewBean();
            viewBean.setIdLot(entity.getIdLot());
            viewBean.setSession(getSession());
            viewBean.retrieve();
            if (viewBean.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE)) {
                return true;
            }

        }

        return false;

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

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            return "";
        }
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

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public boolean hasPostit() {
        return Integer.parseInt(nbPostit) > 0;
    }

    public void setPostit(String nbPostit) {
        this.nbPostit = nbPostit;
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

    public boolean isDemandeValidee() throws Exception {
        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(getSession());
        bc.setIdBasesCalcul(getIdBaseCalcul());
        bc.retrieve();

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(getSession());
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.setIdRenteCalculee(bc.getIdRenteCalculee());
        demande.retrieve();

        if (demande.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isModifiable() {
        return JadeStringUtil.isEmpty(getCsEtat()) || IREPrestationAccordee.CS_ETAT_CALCULE.equals(getCsEtat());
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
            return "false";
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
            return "false";
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
            return "false";
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
     * @param string
     */
    public void setCcpOuBanqueFormatte(String string) {
        ccpOuBanqueFormatte = string;
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

    public void setNumAffilieDepuisPyxis(String numAffilieDepuisPyxis) {
        this.numAffilieDepuisPyxis = numAffilieDepuisPyxis;
    }

}
