/**
 * 
 */
package ch.globaz.amal.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author cbu
 * 
 */
public class AnnoncesCaisse extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateAvisRip = null;
    private String noCaisseMaladie = null;
    private SimpleAnnonce simpleAnnonce = null;

    public AnnoncesCaisse() {
        super();
        simpleAnnonce = new SimpleAnnonce();
    }

    public String getDateAvisRip() {
        return dateAvisRip;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNoCaisseMaladie() {
        return noCaisseMaladie;
    }

    public SimpleAnnonce getSimpleAnnonce() {
        return simpleAnnonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setDateAvisRip(String dateAvisRip) {
        this.dateAvisRip = dateAvisRip;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setNoCaisseMaladie(String noCaisseMaladie) {
        this.noCaisseMaladie = noCaisseMaladie;
    }

    public void setSimpleAnnonce(SimpleAnnonce simpleAnnonce) {
        this.simpleAnnonce = simpleAnnonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
