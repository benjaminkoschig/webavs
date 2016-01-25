package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

/**
 * @author FHA
 * 
 */
/**
 * @author FHA
 * 
 */
public class RevenusDepensesSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String FOR_ALL_VALABLE_LE = "forAllValableRevenusDepense";

    private String forDateValable = null;
    private String forIdDroit = null;
    private String forIdEntity = null;
    private String forIdMembre = null;
    private String forNumeroVersion = null;
    private List<String> inCsTypeDonneeFinancierer = null;

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdEntity() {
        return forIdEntity;
    }

    public String getForIdMembre() {
        return forIdMembre;
    }

    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    public List<String> getInCsTypeDonneeFinancierer() {
        return inCsTypeDonneeFinancierer;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    public void setForIdMembre(String forIdMembre) {
        this.forIdMembre = forIdMembre;
    }

    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    public void setInCsTypeDonneeFinancierer(List<String> inCsTypeDonneeFinancierer) {
        this.inCsTypeDonneeFinancierer = inCsTypeDonneeFinancierer;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    @Override
    public Class whichModelClass() {
        // TODO Auto-generated method stub
        return RevenusDepenses.class;
    }

}
