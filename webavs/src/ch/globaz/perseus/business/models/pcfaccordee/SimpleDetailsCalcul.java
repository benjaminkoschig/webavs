package ch.globaz.perseus.business.models.pcfaccordee;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleDetailsCalcul extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDetailsCalcul = null;
    private String idPCFAccordee = null;
    private String montant = null;
    private String typeData = null;

    @Override
    public String getId() {
        return getIdDetailsCalcul();
    }

    @Override
    public void setId(String id) {
        setIdDetailsCalcul(id);
    }

    public void setIdPCFAccordee(String idPCFAccordee) {
        this.idPCFAccordee = idPCFAccordee;
    }

    public String getIdPCFAccordee() {
        return idPCFAccordee;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getMontant() {
        return montant;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setIdDetailsCalcul(String idDetailsCalcul) {
        this.idDetailsCalcul = idDetailsCalcul;
    }

    public String getIdDetailsCalcul() {
        return idDetailsCalcul;
    }

}
