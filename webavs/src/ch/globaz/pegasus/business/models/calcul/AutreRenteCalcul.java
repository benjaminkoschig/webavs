/**
 * 
 */
package ch.globaz.pegasus.business.models.calcul;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;

/**
 * @author ECO
 * 
 */
public class AutreRenteCalcul extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AutreRente autreRente = null;
    private String idDroit = null;
    private String noVersion = null;

    /**
	 * 
	 */
    public AutreRenteCalcul() {
        super();
        autreRente = new AutreRente();
    }

    /**
     * @return the autreRente
     */
    public AutreRente getAutreRente() {
        return autreRente;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #getId()
     */
    @Override
    public String getId() {
        return autreRente.getId();
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the noVersion
     */
    public String getNoVersion() {
        return noVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return autreRente.getSpy();
    }

    /**
     * @param autreRente
     *            the autreRente to set
     */
    public void setAutreRente(AutreRente autreRente) {
        this.autreRente = autreRente;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        autreRente.setId(id);
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param noVersion
     *            the noVersion to set
     */
    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        autreRente.setSpy(spy);
    }

}
