/*
 * Créé le 14 juil. 08
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.api.basescalcul.IREFactureARestituer;
import globaz.corvus.db.rentesaccordees.REFactureARestituer;
import globaz.corvus.db.rentesaccordees.REFacturesARestituerManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * @author bsc
 * 
 */
public class REFactureARestituerViewBean extends REFactureARestituer implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String LABEL_NON_TRAITE = "JSP_NON_TRAITE";

    private Vector etatsFacture = null;

    private PRTiersWrapper tiersBeneficiaire = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCsCatSectionLibelle() {
        return getSession().getCodeLibelle(getCsCatSection());
    }

    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    public String getCsRoleLibelle() {
        return getSession().getCodeLibelle(getCsRole());
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
    public String getDetailBeneficiaire() {

        return PRNSSUtil.formatDetailRequerantListe(getNssTiersBeneficiaire(), getNomPrenomTiersBeneficiaire(),
                getDateNaissanceTiersBeneficiaire(), getLibelleCourtSexe(), getLibellePays());

    }

    /**
     * Retourne la liste des codes systèmes pour l'état des factures a restituer
     * 
     * @return un Vector de String[2]{codeSysteme, libelleCodeSysteme}.
     */
    public Vector getEtatFactureData() {
        if (etatsFacture == null) {
            etatsFacture = PRCodeSystem.getLibellesPourGroupe(IREFactureARestituer.CS_GROUPE_ETAT_FACTURE_A_RESTITUER,
                    getSession());

            // ajout des options custom
            etatsFacture
                    .add(0,
                            new String[] { REFacturesARestituerManager.CLE_NON_TRAITE,
                                    getSession().getLabel(LABEL_NON_TRAITE) });
        }

        return etatsFacture;
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

    /**
     * retrouve les propriétés du Beneficiaire
     */
    private void loadTiersBeneficiaire() {
        if (tiersBeneficiaire == null) {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiersBenefPrincipal())) {
                try {
                    tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), getIdTiersBenefPrincipal());
                } catch (Exception e) {
                    // on net fait rien
                }
            }
        }
    }
}
