/**
 * 
 */
package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

/**
 * @author ECO
 * 
 */
public class RenteIjApiSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String FOR_ALL_VALABLE_LE = "forAllRenteIjApiValableLe";
    public final static String FOR_PC_ACCORDEE_AND_DATE_VALABLE = "forIdPCAccordeeAndDateValable";

    private String forDateValable = null;
    private String forIdDroit = null;
    private String forIdEntity = null;
    private String forIdMembre = null;
    private String forIdPCAccordee = null;
    private Boolean forIsSupprime = null;
    private String forNumeroVersion = null;

    private List<String> inCsTypeDonneeFinancierer = null;

    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forIdMembre
     */
    public String getForIdMembre() {
        return forIdMembre;
    }

    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public Boolean getForIsSupprime() {
        return forIsSupprime;
    }

    /**
     * @return the forNumeroVersion
     */
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

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdEntity
     *            the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forIdMembre
     *            the forIdMembre to set
     */
    public void setForIdMembre(String forIdMembre) {
        this.forIdMembre = forIdMembre;
    }

    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setForIsSupprime(Boolean forIsSupprime) {
        this.forIsSupprime = forIsSupprime;
    }

    /**
     * @param forNumeroVersion
     *            the forNumeroVersion to set
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<RenteIjApi> whichModelClass() {
        return RenteIjApi.class;
    }

}
