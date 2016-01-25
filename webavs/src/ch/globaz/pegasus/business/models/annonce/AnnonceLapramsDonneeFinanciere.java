/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author eco
 * 
 */
public class AnnonceLapramsDonneeFinanciere extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AnnonceLaprams annonceLaprams = null;
    private SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;

    public AnnonceLapramsDonneeFinanciere() {
        super();
        annonceLaprams = new AnnonceLaprams();
        simpleAnnonceLapramsDonneeFinanciereHeader = new SimpleAnnonceLapramsDonneeFinanciereHeader();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
    }

    public AnnonceLaprams getAnnonceLaprams() {
        return annonceLaprams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAnnonceLapramsDonneeFinanciereHeader.getId();
    }

    public SimpleAnnonceLapramsDonneeFinanciereHeader getSimpleAnnonceLapramsDonneeFinanciereHeader() {
        return simpleAnnonceLapramsDonneeFinanciereHeader;
    }

    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAnnonceLapramsDonneeFinanciereHeader.getSpy();
    }

    public void setAnnonceLaprams(AnnonceLaprams annonceLaprams) {
        this.annonceLaprams = annonceLaprams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAnnonceLapramsDonneeFinanciereHeader.setId(id);
    }

    public void setSimpleAnnonceLapramsDonneeFinanciereHeader(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader) {
        this.simpleAnnonceLapramsDonneeFinanciereHeader = simpleAnnonceLapramsDonneeFinanciereHeader;
    }

    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAnnonceLapramsDonneeFinanciereHeader.setSpy(spy);
    }

}
