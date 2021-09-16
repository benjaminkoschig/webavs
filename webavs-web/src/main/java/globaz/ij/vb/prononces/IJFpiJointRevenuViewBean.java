/*
 * Créé le 27 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.db.prononces.IJFpiJointRevenu;
import globaz.ij.regles.IJPrononceRegles;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author ebko
 */
public class IJFpiJointRevenuViewBean extends IJFpiJointRevenu implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean modifie = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return
     * @throws Exception
     */
    public String getDetailRequerant() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(),
                loadDemande(getSession().getCurrentThreadTransaction()).getIdTiers());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }

    }

    /**
     * getter pour l'attribut modifie.
     * 
     * @return la valeur courante de l'attribut modifie
     */
    public boolean isModifie() {
        return modifie;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return IJPrononceRegles.isModifierPermis(this);
    }

    /**
     * setter pour l'attribut modifie.
     * 
     * @param modifie
     *            une nouvelle valeur pour cet attribut
     */
    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

}
