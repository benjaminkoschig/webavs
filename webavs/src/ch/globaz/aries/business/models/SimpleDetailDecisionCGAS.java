package ch.globaz.aries.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDetailDecisionCGAS extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDecision = null;
    private String idDetailDecision = null;
    private String montant = null;
    private String nombre = null;
    private String total = null;
    private String type = null;

    @Override
    public String getId() {
        return idDetailDecision;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDetailDecision() {
        return idDetailDecision;
    }

    public String getMontant() {
        return montant;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTotal() {
        return total;
    }

    public String getType() {
        return type;
    }

    @Override
    public void setId(String idDetailDecision) {
        this.idDetailDecision = idDetailDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDetailDecision(String idDetailDecision) {
        this.idDetailDecision = idDetailDecision;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setType(String type) {
        this.type = type;
    }
}