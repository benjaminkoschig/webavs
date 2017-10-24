/**
 * 
 */
package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author ECO
 * 
 */
public class CalculPlagesExistantesWithIdDemande extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String dateFin = null;
    private String idVersionDroit = null;
    private String idDemande = null;

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idVersionDroit;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getIdDemande() {
        return idDemande;
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

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idVersionDroit = id;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {

    }
}
