/*
 * Créé le 26 mai 2010
 */
package globaz.cygnus.vb.qds;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.dossiers.RFDossierJointDecisionJointTiersManager;
import globaz.cygnus.db.qds.RFQdJointDossierJointTiersJointDemande;
import globaz.framework.security.FWSecurityLoginException;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * 
 * @author jje
 */
public class RFQdJointDossierJointTiersJointDemandeViewBean extends RFQdJointDossierJointTiersJointDemande {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String LABEL_DROITS_TOUS = "DROITS_TOUS";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private String detailAssureGroupBy = "";

    private Vector<String[]> etatsQd = null;
    private Vector<String[]> genresQd = null;

    private String mntResiduelGroupBy = "";
    private transient Vector<String[]> orderBy = null;

    private String periodesGrandeQdGroupBy = "";

    private String provenance = "";

    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;
    private Vector<String[]> typesPc = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public Vector<String[]> getCsEtatQdData(boolean isdroitsTous) {
        if (etatsQd == null) {
            etatsQd = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_ETAT_QD, getSession());

            // ajout des options custom
            if (isdroitsTous) {
                etatsQd.add(0, new String[] { RFDossierJointDecisionJointTiersManager.CLE_DROITS_TOUS,
                        getSession().getLabel(RFQdJointDossierJointTiersJointDemandeViewBean.LABEL_DROITS_TOUS) });
            }
        }

        return etatsQd;
    }

    public Vector<String[]> getCsGenreQdData(boolean isdroitsTous) {
        if (genresQd == null) {
            genresQd = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_GENRE_QD, getSession());

            // ajout des options custom
            if (isdroitsTous) {
                genresQd.add(0, new String[] { RFDossierJointDecisionJointTiersManager.CLE_DROITS_TOUS,
                        getSession().getLabel(RFQdJointDossierJointTiersJointDemandeViewBean.LABEL_DROITS_TOUS) });
            }
        }

        return genresQd;
    }

    public Vector<String[]> getCsTypePcData(boolean isdroitsTous) {
        if (typesPc == null) {
            // TODO: typesPc =
            // PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_TYPE_PC,
            // getSession());
            typesPc = new Vector<String[]>();
            // ajout des options custom
            if (isdroitsTous) {
                typesPc.add(0, new String[] { RFDossierJointDecisionJointTiersManager.CLE_DROITS_TOUS,
                        getSession().getLabel(RFQdJointDossierJointTiersJointDemandeViewBean.LABEL_DROITS_TOUS) });
            }
        }

        return typesPc;
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

    public String getDetailAssureGroupBy() {
        return detailAssureGroupBy;
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

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            /*
             * JadeUser userName = this.getSession().getApplication()._getSecurityManager()
             * .getUserForVisa(this.getSession(), this.getIdGestionnaire()); return userName.getIdUser();
             */
            return getIdGestionnaire();
        }
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

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    public String getMntResiduelGroupBy() {
        return mntResiduelGroupBy;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), this.isNNSS().equals(Boolean.TRUE.toString()) ? true : false);
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector<String[]> getOrderByData() {
        /*
         * if (this.orderBy == null) { this.orderBy = new Vector<String[]>(2); this.orderBy.add(new String[] {
         * RFQd.FIELDNAME_ANNEE_QD + " DESC ," + RFQd.FIELDNAME_ID_QD + "," +
         * RFDemandeJointDossierJointTiers.FIELDNAME_NOM + "," + RFDemandeJointDossierJointTiers.FIELDNAME_PRENOM,"" });
         * }
         */

        return orderBy;
    }

    public String getPeriodesGrandeQdGroupBy() {
        return periodesGrandeQdGroupBy;
    }

    public String getProvenance() {
        return provenance;
    }

    public Vector<String[]> getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
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

    public void setDetailAssureGroupBy(String detailAssureGroupBy) {
        this.detailAssureGroupBy = detailAssureGroupBy;
    }

    public void setMntResiduelGroupBy(String mntResiduelGroupBy) {
        this.mntResiduelGroupBy = mntResiduelGroupBy;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setPeriodesGrandeQdGroupBy(String periodesGrandeQdGroupBy) {
        this.periodesGrandeQdGroupBy = periodesGrandeQdGroupBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setProvenance(java .lang.String)
     */
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
