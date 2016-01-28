package ch.globaz.pegasus.business.models.blocage;

import globaz.jade.persistence.model.JadeComplexModel;

public class PcaBloqueConjoint extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPca;
    private String montantBloque;
    private String montantDebloque;
    private String montantPca;

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getMontantBloque() {
        return montantBloque;
    }

    public String getMontantDebloque() {
        return montantDebloque;
    }

    public String getMontantPca() {
        return montantPca;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setMontantBloque(String montantBloque) {
        this.montantBloque = montantBloque;
    }

    public void setMontantDebloque(String montantDebloque) {
        this.montantDebloque = montantDebloque;
    }

    public void setMontantPca(String montantPca) {
        this.montantPca = montantPca;
    }

    @Override
    public void setSpy(String spy) {

    }
}
