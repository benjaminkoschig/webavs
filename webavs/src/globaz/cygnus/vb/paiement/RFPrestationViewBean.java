/*
 * Créé le 10 décembre 2010
 */

package globaz.cygnus.vb.paiement;

import globaz.cygnus.db.paiement.RFPrestationJointTiers;
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

public class RFPrestationViewBean extends RFPrestationJointTiers implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forOrderBy = null;

    // ~ Constructor ------------------------------------------------------------------------------------------------
    public RFPrestationViewBean() {
        super();
        // TODO Auto-generated constructor stub
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

    /**
     * 
     * Méthode qui retourne le détail du paiement
     * 
     * @return
     * @throws Exception
     */
    public String getDetailPaiement() throws Exception {

        // Domaine standard
        // if (!JadeStringUtil.isBlankOrZero(this.getNomPaiementDomaineStandard())) {

        return (!JadeStringUtil.isBlankOrZero(getNssPaiementDomaineStandard()) ? "<b>"
                + getNssPaiementDomaineStandard() + "</b><br />" : "")
                + getNomPaiementDomaineStandard()
                + (!JadeStringUtil.isBlankOrZero(getPrenomPaiementDomaineStandard()) ? " "
                        + getPrenomPaiementDomaineStandard() : "")
                + ((!JadeStringUtil.isBlankOrZero(getDateNaissancePaiementDomaineStandard()) && !JadeStringUtil
                        .isBlankOrZero(getNssPaiementDomaineStandard())) ? " / "
                        + getDateNaissancePaiementDomaineStandard() : "")
                + (!JadeStringUtil.isBlankOrZero(getCsSexePaiementDomaineStandard()) ? " / "
                        + this.getLibelleCourtSexe(getCsSexePaiementDomaineStandard()) : "")
                + (!JadeStringUtil.isBlankOrZero(getCsNationalitePaiementStandard()) ? " / "
                        + this.getLibellePays(getCsNationalitePaiementStandard()) : "");

        // Domaine rente
        /*
         * } else if (!JadeStringUtil.isBlankOrZero(this.getNomPaiementDomaineRente())) {
         * 
         * return (!JadeStringUtil.isBlankOrZero(this.getNssPaiementDomaineRente()) ? "<b>" +
         * this.getNssPaiementDomaineRente() + "</b><br />" : "") + this.getNomPaiementDomaineRente() +
         * (!JadeStringUtil.isBlankOrZero(this.getPrenomPaiementDomaineRente()) ? " " +
         * this.getPrenomPaiementDomaineRente() : "") +
         * (!JadeStringUtil.isBlankOrZero(this.getDateNaissancePaiementDomaineRente()) ? " / " +
         * this.getDateNaissancePaiementDomaineRente() : "") +
         * (!JadeStringUtil.isBlankOrZero(this.getCsSexePaiementDomaineRente()) ? " / " +
         * this.getLibelleCourtSexe(this.getCsSexePaiementDomaineRente()) : "") +
         * (!JadeStringUtil.isBlankOrZero(this.getCsNationalitePaiementRente()) ? " / " +
         * this.getLibellePays(this.getCsNationalitePaiementRente()) : "");
         * 
         * } else { return "-"; }
         */

    }

    public String getForOrderBy() {
        return forOrderBy;
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

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

}