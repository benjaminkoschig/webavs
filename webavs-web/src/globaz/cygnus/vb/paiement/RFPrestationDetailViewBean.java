/*
 * Créé le 10 décembre 2010
 */

package globaz.cygnus.vb.paiement;

import globaz.cygnus.db.paiement.RFPrestation;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author FHA
 * @revision JJE
 * 
 */

public class RFPrestationDetailViewBean extends RFPrestation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private String referencePaiement = "";
    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    // private String dateMoisAnnee = "";
    private String dateNaissance = "";
    // private String idDecision = "";
    // private String idLot = "";
    // private String idPrestation = "";
    private String idTiers = "";
    // private String montantTotal = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";

    // ~ Constructor ------------------------------------------------------------------------------------------------
    public RFPrestationDetailViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNss(), getNom() + " " + getPrenom(),
                    getDateNaissance(), this.getLibelleCourtSexe(), this.getLibellePays(), getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                    this.getLibelleCourtSexe(), this.getLibellePays());
        }
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return (getSession()).getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return (getSession()).getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return (getSession()).getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return (getSession()).getLabel("JSP_LETTRE_SEXE_FEMME");
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

        if ("999".equals(((BSession) getISession()).getCode(((BSession) getISession()).getSystemCode("CIPAYORI",
                getCsNationalite())))) {
            return "";
        } else {
            return ((BSession) getISession()).getCodeLibelle(((BSession) getISession()).getSystemCode("CIPAYORI",
                    getCsNationalite()));
        }

    }

    public String getLibellePays(String csNationalite) {

        if ("999".equals(((BSession) getISession()).getCode(((BSession) getISession()).getSystemCode("CIPAYORI",
                csNationalite)))) {
            return "";
        } else {
            return ((BSession) getISession()).getCodeLibelle(((BSession) getISession()).getSystemCode("CIPAYORI",
                    csNationalite));
        }

    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}