/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleContribuableReprise extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idContribuable = null;
    private String idTiers = null;
    private String noContribuable = null;
    private String zoneCommuneDateNaissance = null;
    private String zoneCommuneNoContribuable = null;
    private String zoneCommuneNoContribuableFormate = null;

    /**
	 * 
	 */
    public SimpleContribuableReprise() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idContribuable;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the noContribuable
     */
    public String getNoContribuable() {
        return noContribuable;
    }

    /**
     * @return the zoneCommuneDateNaissance
     */
    public String getZoneCommuneDateNaissance() {
        return zoneCommuneDateNaissance;
    }

    /**
     * @return the zoneCommuneNoContribuable
     */
    public String getZoneCommuneNoContribuable() {
        return zoneCommuneNoContribuable;
    }

    /**
     * @return the zoneCommuneNoContribuableFormate
     */
    public String getZoneCommuneNoContribuableFormate() {
        return zoneCommuneNoContribuableFormate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idContribuable = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param noContribuable
     *            the noContribuable to set
     */
    public void setNoContribuable(String noContribuable) {
        this.noContribuable = noContribuable;
    }

    /**
     * @param zoneCommuneDateNaissance
     *            the zoneCommuneDateNaissance to set
     */
    public void setZoneCommuneDateNaissance(String zoneCommuneDateNaissance) {
        this.zoneCommuneDateNaissance = zoneCommuneDateNaissance;
    }

    /**
     * @param zoneCommuneNoContribuable
     *            the zoneCommuneNoContribuable to set
     */
    public void setZoneCommuneNoContribuable(String zoneCommuneNoContribuable) {
        this.zoneCommuneNoContribuable = zoneCommuneNoContribuable;
    }

    /**
     * @param zoneCommuneNoContribuableFormate
     *            the zoneCommuneNoContribuableFormate to set
     */
    public void setZoneCommuneNoContribuableFormate(String zoneCommuneNoContribuableFormate) {
        this.zoneCommuneNoContribuableFormate = zoneCommuneNoContribuableFormate;
    }

}
