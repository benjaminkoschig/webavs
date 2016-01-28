package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author BSC
 * 
 */
public class SimpleInformationsComptabilite extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe = "";
    private String idInfoCompta = "";
    private String idTiersAdressePmt = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idInfoCompta;
    }

    /**
     * Getter pour l'attribut idCompteAnnexe
     * 
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Getter pour l'attribut idInfoCompta
     * 
     * @return
     */
    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    /**
     * Getter pour l'attribut idTiersAdressePmt
     * 
     * @return
     */
    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idInfoCompta = id;
    }

    /**
     * Setter pour l'attribut idCompteAnnexe
     * 
     * @param idCompteAnnexe
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * Setter pour l'attribut idInfoCompta
     * 
     * @param idInfoCompta
     */
    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    /**
     * Setter pour l'attribut idTiersAdressePmt
     * 
     * @param idTiersAdressePmt
     */
    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

}
