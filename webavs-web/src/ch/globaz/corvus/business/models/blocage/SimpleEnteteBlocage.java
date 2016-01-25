package ch.globaz.corvus.business.models.blocage;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleEnteteBlocage extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEnteBlocage;
    private String montantBloque;
    private String montantDebloque;

    @Override
    public String getId() {
        return idEnteBlocage;
    }

    public String getIdEnteBlocage() {
        return idEnteBlocage;
    }

    public String getMontantBloque() {
        return montantBloque;
    }

    public String getMontantDebloque() {
        return montantDebloque;
    }

    @Override
    public void setId(String id) {
        idEnteBlocage = id;
    }

    public void setIdEnteBlocage(String idEnteBlocage) {
        this.idEnteBlocage = idEnteBlocage;
    }

    public void setMontantBloque(String montantBloque) {
        this.montantBloque = montantBloque;
    }

    public void setMontantDebloque(String montantDebloque) {
        this.montantDebloque = montantDebloque;
    }
}
