/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRente;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author bsc
 * 
 */
public class REPrestationsDuesJointDemandeRenteViewBean extends REPrestationsDuesJointDemandeRente implements
        FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String isWarningAffiche = "";
    private PRTiersWrapper tiersBeneficiaire = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    /**
     * @return
     */
    public String getCsTypeLibelle() {
        return getSession().getCodeLibelle(getCsType());
    }

    /**
     * @return
     */
    public String getCsTypePaiementLibelle() {
        return getSession().getCodeLibelle(getCsTypePaiement());
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
    public String getDetailBénéficiaire() {

        return PRNSSUtil.formatDetailRequerantListe(getNssTiersBeneficiaire(), getNomPrenomTiersBeneficiaire(),
                getDateNaissanceTiersBeneficiaire(), getLibelleCourtSexe(), getLibellePays());

    }

    public String getIsWarningAffiche() {

        if (isWarningAffiche.length() == 0) {
            isWarningAffiche = "false";
        }

        return isWarningAffiche;
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
        REPrestationDue pd = new REPrestationDue();
        pd.setSession(getSession());
        pd.setIdPrestationDue(getIdPrestationDue());
        try {
            pd.retrieve();
        } catch (Exception e) {
        }
        return pd.getSpy();
    }

    public boolean isModifiable(String idDemRente) {
        try {
            REDemandeRenteManager drManager = new REDemandeRenteManager();
            drManager.setSession(getSession());
            drManager.setForIdDemandeRente(idDemRente);
            drManager.find(1);

            if (drManager.getSize() > 0) {
                REDemandeRente dem = (REDemandeRente) drManager.getFirstEntity();

                // si la demande est dans l'état validé, aucunes modifications
                // n'est possible
                if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(dem.getCsEtat())
                        || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(dem.getCsEtat())
                        || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(dem.getCsEtat())
                        || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE.equals(dem.getCsEtat())
                        || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(dem.getCsEtat())) {
                    return false;
                } else {
                    return true;
                }

            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
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

    public void setIsWarningAffiche(String isWarningAffiche) {
        this.isWarningAffiche = isWarningAffiche;
    }
}
