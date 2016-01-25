/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

/**
 * @author SCE
 * 
 *         8 juil. 2010
 */
public class DonneeFinanciereHeaderSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutCheckPeriode = null;
    private String forDateFinCheckPeriode = null;
    private String forIdDonneeFinanciereHeader = null;
    private String forIdDroit = null;
    private String forIdEntity = null;
    private String forIdEntityGroup = null;
    private List<String> forIdEntityGroupIn = null;
    private String forIdVersionDroit = null;
    private Boolean forIsCopieFromPreviousVersion = null;
    private String forNumeroVersion = null;

    /**
     * @return the forDateDebutCheckPeriode
     */
    public String getForDateDebutCheckPeriode() {
        return forDateDebutCheckPeriode;
    }

    /**
     * @return the forDateFinCheckPeriode
     */
    public String getForDateFinCheckPeriode() {
        return forDateFinCheckPeriode;
    }

    /**
     * @return the forIdDonneeFinanciereHeader
     */
    public String getForIdDonneeFinanciereHeader() {
        return forIdDonneeFinanciereHeader;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forIdEntityGroup
     */
    public String getForIdEntityGroup() {
        return forIdEntityGroup;
    }

    public List<String> getForIdEntityGroupIn() {
        return forIdEntityGroupIn;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public Boolean getForIsCopieFromPreviousVersion() {
        return forIsCopieFromPreviousVersion;
    }

    /**
     * @return the forNumeroVersion
     */
    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    /**
     * @param forDateDebutCheckPeriode
     *            the forDateDebutCheckPeriode to set
     */
    public void setForDateDebutCheckPeriode(String forDateDebutCheckPeriode) {
        this.forDateDebutCheckPeriode = forDateDebutCheckPeriode;
    }

    /**
     * @param forDateFinCheckPeriode
     *            the forDateFinCheckPeriode to set
     */
    public void setForDateFinCheckPeriode(String forDateFinCheckPeriode) {
        this.forDateFinCheckPeriode = forDateFinCheckPeriode;
    }

    /**
     * @param forIdDonneeFinanciereHeader
     *            the forIdDonneeFinanciereHeader to set
     */
    public void setForIdDonneeFinanciereHeader(String forIdDonneeFinanciereHeader) {
        this.forIdDonneeFinanciereHeader = forIdDonneeFinanciereHeader;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forIdEntityGroup
     *            the forIdEntityGroup to set
     */
    public void setForIdEntityGroup(String forIdEntityGroup) {
        this.forIdEntityGroup = forIdEntityGroup;
    }

    public void setForIdEntityGroupIn(List<String> forIdEntityGroupIn) {
        this.forIdEntityGroupIn = forIdEntityGroupIn;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForIsCopieFromPreviousVersion(Boolean forIsCopieFromPreviousVersion) {
        this.forIsCopieFromPreviousVersion = forIsCopieFromPreviousVersion;
    }

    /**
     * @param forNumeroVersion
     *            the forNumeroVersion to set
     */
    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
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
    public Class whichModelClass() {
        return DonneeFinanciereHeader.class;
    }

}
