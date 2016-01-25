/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.vb.dossiers;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.api.dossiers.IRFDossiers;
import globaz.cygnus.db.dossiers.RFDossierJointDecisionJointTiersManager;
import globaz.cygnus.db.dossiers.RFDossierJointTiers;
import globaz.cygnus.db.dossiers.RFDossierJointTiersManager;
import globaz.cygnus.db.dossiers.RFPeriodeCAAIWrapper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * @author jje
 */
public class RFDossierJointTiersViewBean extends RFDossierJointTiers implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String LABEL_DROITS_TOUS = "DROITS_TOUS";

    private transient Vector etatsDossier = null;
    private boolean isAfficherDetail = false;
    private String likeNumeroAVS = "";
    private transient Vector orderBy = null;
    private String provenance = "";
    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;

    public Vector getCsEtatDossierData(boolean isdroitsTous) {
        if (etatsDossier == null) {
            etatsDossier = PRCodeSystem.getLibellesPourGroupe(IRFDossiers.CS_GROUPE_ETAT_DOSSIER, getSession());

            // ajout des options custom
            if (isdroitsTous) {
                etatsDossier.add(0, new String[] { RFDossierJointDecisionJointTiersManager.CLE_DROITS_TOUS,
                        getSession().getLabel(RFDossierJointTiersViewBean.LABEL_DROITS_TOUS) });
            }
        }

        return etatsDossier;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNss(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getLibelleCourtSexe(), getLibellePays(), getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                    getLibelleCourtSexe(), getLibellePays());
        }
    }

    public String getDetailGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getIdGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté sur une ligne
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerantSurUneLigne() throws Exception {

        return PRNSSUtil.formatDetailRequerantDetail(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public Vector getEtatsDossier() {
        return etatsDossier;
    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        return getVisaGestionnaire();

    }

    public boolean getIsAfficherDetail() {
        return isAfficherDetail;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        String code = getSession().getSystemCode("CIPAYORI", getCsNationalite());

        if ("999".equals(getSession().getCode(code))) {
            return "";
        } else {
            return getSession().getCodeLibelle(code);
        }

    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), this.isNNSS().equals(Boolean.TRUE.toString()) ? true : false);
    }

    public Vector getOrderBy() {
        return orderBy;
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector(2);
            orderBy.add(new String[] { RFDossierJointTiersManager.OrdreDeTri.NomPrenom.toString(),
                    getSession().getLabel("JSP_RF_DOS_R_TRIER_PAR_NOM") });
        }

        return orderBy;
    }

    public String getProvenance() {
        return provenance;
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public boolean isContributionAssistanceAIechue(String dateDernierPaiement) {
        if ((getPeriodesCAAI() == null) || getPeriodesCAAI().isEmpty()) {
            return false;
        }

        RFPeriodeCAAIWrapper dernierePeriode = getDernierePeriodeCAAI();
        if (dernierePeriode == null) {
            return false;
        }
        if (JadeDateUtil.isGlobazDate(dernierePeriode.getDateFinCAAI())) {
            JadePeriodWrapper periode = new JadePeriodWrapper(dernierePeriode.getDateDebutCAAI(),
                    dernierePeriode.getDateFinCAAI());
            return !periode.isDateDansLaPeriode("01." + dateDernierPaiement);
        }
        return false;
    }

    public boolean isContributionAssistanceAIenCours(String dateDernierPaiement) {
        if ((getPeriodesCAAI() == null) || getPeriodesCAAI().isEmpty()) {
            return false;
        }

        RFPeriodeCAAIWrapper dernierePeriode = getDernierePeriodeCAAI();
        if (dernierePeriode == null) {
            return false;
        }
        if (JadeDateUtil.isGlobazDate(dernierePeriode.getDateFinCAAI())) {
            JadePeriodWrapper periodeContribution = new JadePeriodWrapper(dernierePeriode.getDateDebutCAAI(),
                    dernierePeriode.getDateFinCAAI());
            JadePeriodWrapper periodeMoisComptable = new JadePeriodWrapper(dateDernierPaiement, dateDernierPaiement);

            switch (periodeContribution.comparerChevauchement(periodeMoisComptable)) {
                case LesPeriodesSeChevauchent:
                    return true;
                default:
                    return false;
            }
        }
        // si pas de date de fin sur la dernière période de contribution
        return true;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }

        if (getNss().length() > 14) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    public void setEtatsDossier(Vector etatsDossier) {
        this.etatsDossier = etatsDossier;
    }

    public void setIsAfficherDetail(boolean isAfficherDetail) {
        this.isAfficherDetail = isAfficherDetail;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setOrderBy(Vector orderBy) {
        this.orderBy = orderBy;
    }

    public void setProvenance(String string) {
        if (PRUtil.PROVENANCE_TIERS.equals(string)) {
            trouveDansCI = false;
            trouveDansTiers = true;
        } else if (PRUtil.PROVENANCE_CI.equals(string)) {
            trouveDansCI = true;
            trouveDansTiers = false;
        } else {
            trouveDansCI = false;
            trouveDansTiers = false;
        }
    }

    public void setTrouveDansCI(boolean trouveDansCI) {
        this.trouveDansCI = trouveDansCI;
    }

    public void setTrouveDansTiers(boolean trouveDansTiers) {
        this.trouveDansTiers = trouveDansTiers;
    }
}
