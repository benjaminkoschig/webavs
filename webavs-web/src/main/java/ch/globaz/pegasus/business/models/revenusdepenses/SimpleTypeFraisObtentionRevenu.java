package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleTypeFraisObtentionRevenu extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csFraisObtentionRevenu = null;
    private String idFraisObtentionRevenu = null;
    private String idRevenuActiviteLucrativeDependante = null;

    public String getCsFraisObtentionRevenu() {
        return csFraisObtentionRevenu;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idFraisObtentionRevenu;
    }

    public String getIdFraisObtentionRevenu() {
        return idFraisObtentionRevenu;
    }

    public String getIdRevenuActiviteLucrativeDependante() {
        return idRevenuActiviteLucrativeDependante;
    }

    public void setCsFraisObtentionRevenu(String csFraisObtentionRevenu) {
        this.csFraisObtentionRevenu = csFraisObtentionRevenu;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idFraisObtentionRevenu = id;
    }

    public void setIdFraisObtentionRevenu(String idFraisObtentionRevenu) {
        this.idFraisObtentionRevenu = idFraisObtentionRevenu;
    }

    public void setIdRevenuActiviteLucrativeDependante(String idRevenuActiviteLucrativeDependante) {
        this.idRevenuActiviteLucrativeDependante = idRevenuActiviteLucrativeDependante;
    }

}
