/*
 * Créé le 26 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.prononces.IJMesureJointAgentExecutionManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJMesureJointAgentExecutionListViewBean extends IJMesureJointAgentExecutionManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeIJ = "";
    private String dateDebutPrononce = "";
    private String noAVS = "";
    private String prenomNom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJMesureJointAgentExecutionViewBean();
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
     * getter pour l'attribut date debut prononce
     * 
     * @return la valeur courante de l'attribut date debut prononce
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut prenom nom
     * 
     * @return la valeur courante de l'attribut prenom nom
     */
    public String getPrenomNom() {
        return prenomNom;
    }

    /**
     * setter pour l'attribut cs type IJ
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String string) {
        csTypeIJ = string;
    }

    /**
     * setter pour l'attribut date debut prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrononce(String string) {
        dateDebutPrononce = string;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * setter pour l'attribut prenom nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenomNom(String string) {
        prenomNom = string;
    }
}
