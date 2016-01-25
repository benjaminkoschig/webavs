/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 *         Classe du modèle de données simple paramètres envoi >> table ALPARENV
 * 
 */
public class EnvoiParametresSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csTypeParametre = null;

    private String idParametreEnvoi = null;

    private String valeurParametre = null;

    /**
     * @return the csTypeParametre
     */
    public String getCsTypeParametre() {
        return csTypeParametre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdParametreEnvoi();
    }

    /**
     * @return the idParametreEnvoi
     */
    public String getIdParametreEnvoi() {
        return idParametreEnvoi;
    }

    /**
     * @return the valeurParametre
     */
    public String getValeurParametre() {
        return valeurParametre;
    }

    /**
     * @param csTypeParametre
     *            the csTypeParametre to set
     */
    public void setCsTypeParametre(String csTypeParametre) {
        this.csTypeParametre = csTypeParametre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        setIdParametreEnvoi(id);
    }

    /**
     * @param idParametreEnvoi
     *            the idParametreEnvoi to set
     */
    public void setIdParametreEnvoi(String idParametreEnvoi) {
        this.idParametreEnvoi = idParametreEnvoi;
    }

    /**
     * @param valeurParametre
     *            the valeurParametre to set
     */
    public void setValeurParametre(String valeurParametre) {
        this.valeurParametre = valeurParametre;
    }

}
