package globaz.ij.vb.prestations;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.prestations.IJIJCalculeeJointIndemniteManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJIJCalculeeJointIndemniteListViewBean extends IJIJCalculeeJointIndemniteManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeIJ = "";
    private String datePrononce = "";
    // BZ 7366
    private String idTiers;
    private String noAVSAssure = "";

    private String nomPrenomAssure = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJIJCalculeeJointIndemniteViewBean();
    }

    /**
     * getter pour l'attribut cs type IJ
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date prononce
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return datePrononce;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSAssure());

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
                    getNoAVSAssure(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    public final String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut no AVSAssure
     * 
     * @return la valeur courante de l'attribut no AVSAssure
     */
    public String getNoAVSAssure() {
        return noAVSAssure;
    }

    /**
     * @return
     */
    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    /**
     * setter pour l'attribut cs type IJ
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date prononce
     * 
     * @param datePrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String datePrononce) {
        this.datePrononce = datePrononce;
    }

    public final void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut no AVSAssure
     * 
     * @param noAVSAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVSAssure(String noAVSAssure) {
        this.noAVSAssure = noAVSAssure;
    }

    /**
     * @param string
     */
    public void setNomPrenomAssure(String string) {
        nomPrenomAssure = string;
    }

}
