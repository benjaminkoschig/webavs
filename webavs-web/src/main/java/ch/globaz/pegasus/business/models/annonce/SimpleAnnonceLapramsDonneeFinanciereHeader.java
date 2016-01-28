/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author BSC Modele simple pour les ordres de versement
 */
public class SimpleAnnonceLapramsDonneeFinanciereHeader extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private String csRoleMembreFamille = null;
    private String idAnnonceLaprams = null;
    private String idAnnonceLAPRAMSDoFinH = null;
    private String idDonneeFinanciereHeader = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idAnnonceLAPRAMSDoFinH;
    }

    public String getIdAnnonceLaprams() {
        return idAnnonceLaprams;
    }

    public String getIdAnnonceLAPRAMSDoFinH() {
        return idAnnonceLAPRAMSDoFinH;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAnnonceLAPRAMSDoFinH = id;

    }

    public void setIdAnnonceLaprams(String idAnnonceLaprams) {
        this.idAnnonceLaprams = idAnnonceLaprams;
    }

    public void setIdAnnonceLAPRAMSDoFinH(String idAnnonceLAPRAMSDoFinH) {
        this.idAnnonceLAPRAMSDoFinH = idAnnonceLAPRAMSDoFinH;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

}
