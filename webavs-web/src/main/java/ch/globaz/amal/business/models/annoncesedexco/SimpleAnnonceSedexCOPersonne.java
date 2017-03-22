/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonceSedexCOPersonne extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;
    private String idAnnonceSedexCOP = null;
    private String idAnnonceSedexCO = null;
    private String idContribuable = null;
    private String idFamille = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdAnnonceSedexCOP();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAnnonceSedexCOP = id;
    }

    public String getIdAnnonceSedexCOP() {
        return idAnnonceSedexCOP;
    }

    public void setIdAnnonceSedexCOP(String idAnnonceSedexCOP) {
        this.idAnnonceSedexCOP = idAnnonceSedexCOP;
    }

    public String getIdAnnonceSedexCO() {
        return idAnnonceSedexCO;
    }

    public void setIdAnnonceSedexCO(String idAnnonceSedexCO) {
        this.idAnnonceSedexCO = idAnnonceSedexCO;
    }

    public String getIdContribuable() {
        return idContribuable;
    }

    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    public String getIdFamille() {
        return idFamille;
    }

    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

}
