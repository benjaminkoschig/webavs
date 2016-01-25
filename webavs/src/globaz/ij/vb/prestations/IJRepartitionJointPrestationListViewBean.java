package globaz.ij.vb.prestations;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.prestations.IJRepartitionJointPrestationManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJRepartitionJointPrestationListViewBean extends IJRepartitionJointPrestationManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPrestation = "";
    private String idTiers;
    private String noAVS = "";

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
        return new IJRepartitionJointPrestationViewBean();
    }

    /**
     * getter pour l'attribut id prestation
     * 
     * @return la valeur courante de l'attribut id prestation
     */
    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNoAVS() {
        return noAVS;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut id prestation
     * 
     * @param idPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }
}
