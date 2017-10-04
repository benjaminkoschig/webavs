/**
 * 
 */
package ch.globaz.corvus.business.models.ventilation;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Ventilation des rentes / prestations accordées.
 * 
 * Mis en place pour le déplafonnement loyer et l'apparitaion de la part cantonale dans les PC
 * 
 * @author est
 * 
 */
public class SimpleVentilation extends JadeSimpleModel {

    private static final long serialVersionUID = -8157501347781859603L;
    private String idVentilation = null;
    private String idPrestationAccordee = null;
    private String montantVentile = null;
    private String csTypeVentilation = null;

    public String getIdVentilation() {
        return idVentilation;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getMontantVentile() {
        return montantVentile;
    }

    public String getCsTypeVentilation() {
        return csTypeVentilation;
    }

    public void setIdVentilation(String idVentilation) {
        this.idVentilation = idVentilation;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setMontantVentile(String montantVentile) {
        this.montantVentile = montantVentile;
    }

    public void setCsTypeVentilation(String csTypeVentilation) {
        this.csTypeVentilation = csTypeVentilation;
    }

    @Override
    public String getId() {
        return idVentilation;
    }

    @Override
    public void setId(String id) {
        idVentilation = id;
    }

}
